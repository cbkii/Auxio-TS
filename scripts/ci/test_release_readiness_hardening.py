#!/usr/bin/env python3
"""Network-free regression tests for release-readiness hardening boundaries."""

from __future__ import annotations

import json
import os
import subprocess
import tempfile
from pathlib import Path

REPO = Path.cwd()
ORCHESTRATOR = REPO / "scripts" / "release-orchestrator.py"
RESOLVE = REPO / "scripts" / "manual-release" / "05-resolve-version-and-repository-release-state.sh"
PLAN = REPO / "scripts" / "manual-release" / "06-plan-efficient-asset-work.sh"
UPLOAD = REPO / "scripts" / "manual-release" / "16-upload-or-replace-planned-release-assets.sh"


class TestFailure(RuntimeError):
    pass


def check(condition: bool, message: str) -> None:
    if not condition:
        raise TestFailure(message)


def run(
    args: list[str],
    *,
    cwd: Path,
    env: dict[str, str] | None = None,
    should_pass: bool = True,
    timeout: int = 30,
) -> subprocess.CompletedProcess[str]:
    completed = subprocess.run(
        args,
        cwd=cwd,
        env=env,
        text=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        timeout=timeout,
        check=False,
    )
    if should_pass and completed.returncode != 0:
        raise TestFailure(
            f"{' '.join(args)} failed unexpectedly ({completed.returncode})\n"
            f"stdout:\n{completed.stdout}\nstderr:\n{completed.stderr}"
        )
    if not should_pass and completed.returncode == 0:
        raise TestFailure(
            f"{' '.join(args)} unexpectedly succeeded\n"
            f"stdout:\n{completed.stdout}\nstderr:\n{completed.stderr}"
        )
    return completed


def git(root: Path, *args: str) -> str:
    return run(["git", *args], cwd=root).stdout.strip()


def parse_outputs(path: Path) -> dict[str, str]:
    result: dict[str, str] = {}
    for line in path.read_text(encoding="utf-8").splitlines():
        if "=" in line:
            key, value = line.split("=", 1)
            result[key] = value
    return result


def install_empty_release_gh(fakebin: Path) -> None:
    gh = fakebin / "gh"
    gh.write_text(
        """#!/usr/bin/env bash
set -u
if [[ "${1:-}" != api ]]; then
  echo "unsupported fake gh call" >&2
  exit 2
fi
# The resolver only needs an empty paginated release index in these cases.
exit 0
""",
        encoding="utf-8",
    )
    gh.chmod(0o755)


def write_gradle(root: Path, version: str = "6.5.0", code: int = 6_050_000) -> None:
    app = root / "app"
    app.mkdir(exist_ok=True)
    (app / "build.gradle").write_text(
        "android {\n"
        "    defaultConfig {\n"
        f'        versionName "{version}"\n'
        f"        versionCode {code}\n"
        "    }\n"
        "}\n",
        encoding="utf-8",
    )


def resolver_env(root: Path, fakebin: Path, output: Path, *, input_tag: str = "") -> dict[str, str]:
    env = os.environ.copy()
    env.update(
        {
            "PATH": f"{fakebin}:{env.get('PATH', '')}",
            "RUNNER_TEMP": str(root / "runner"),
            "GITHUB_REPOSITORY": "cbkii/Auxio-TS",
            "GITHUB_OUTPUT": str(output),
            "GH_TOKEN": "network-free-test-token",
            "RELEASE_MODE": "auto",
            "INPUT_TAG": input_tag,
            "TOOL": str(ORCHESTRATOR),
        }
    )
    Path(env["RUNNER_TEMP"]).mkdir(exist_ok=True)
    output.write_text("", encoding="utf-8")
    return env


def test_release_source_authority(root: Path) -> None:
    repo = root / "source-authority"
    repo.mkdir()
    fakebin = root / "fakebin-resolve"
    fakebin.mkdir()
    install_empty_release_gh(fakebin)

    git(repo, "init")
    git(repo, "config", "user.name", "Release Smoke")
    git(repo, "config", "user.email", "release-smoke@example.invalid")
    write_gradle(repo)
    git(repo, "add", "app/build.gradle")
    git(repo, "commit", "-m", "release source")
    source = git(repo, "rev-parse", "HEAD")
    git(repo, "tag", "v6.5.0")

    output = root / "same-head.out"
    run(
        ["bash", str(RESOLVE)],
        cwd=repo,
        env=resolver_env(root, fakebin, output),
    )
    values = parse_outputs(output)
    check(values.get("effective_mode") == "repair_existing_release", "current-head interrupted tag did not resume")
    check(values.get("selected_tag_relation") == "source_head", "current-head tag relation was not recognised")

    (repo / "README.md").write_text("new work\n", encoding="utf-8")
    git(repo, "add", "README.md")
    git(repo, "commit", "-m", "newer development work")
    advanced = git(repo, "rev-parse", "HEAD")
    check(advanced != source, "test repository did not advance")

    stale_output = root / "stale-auto.out"
    stale = run(
        ["bash", str(RESOLVE)],
        cwd=repo,
        env=resolver_env(root, fakebin, stale_output),
        should_pass=False,
    )
    combined = f"{stale.stdout}\n{stale.stderr}"
    check("Refusing to publish stale source" in combined, "stale automatic repair did not fail with source-authority diagnostic")

    explicit_output = root / "explicit-repair.out"
    run(
        ["bash", str(RESOLVE)],
        cwd=repo,
        env=resolver_env(root, fakebin, explicit_output, input_tag="v6.5.0"),
    )
    explicit = parse_outputs(explicit_output)
    check(explicit.get("effective_mode") == "repair_existing_release", "explicit historical repair was blocked")
    check(explicit.get("selected_tag_relation") == "stale", "explicit historical repair did not report stale relation")

    # Genuine interrupted create: the immutable tag points at one metadata commit whose parent is
    # still current dev because the final dev fast-forward did not run.
    git(repo, "checkout", "--detach", source)
    (repo / "release-metadata.txt").write_text("metadata\n", encoding="utf-8")
    git(repo, "add", "release-metadata.txt")
    git(repo, "commit", "-m", "release metadata")
    tagged = git(repo, "rev-parse", "HEAD")
    git(repo, "tag", "v6.5.1")
    git(repo, "checkout", "--detach", source)
    write_gradle(repo, version="6.5.1", code=6_050_100)
    # Keep source metadata aligned in the working tree for the pure planner without creating a
    # second commit; this test only discriminates tag ancestry.
    parent_output = root / "parent-resume.out"
    run(
        ["bash", str(RESOLVE)],
        cwd=repo,
        env=resolver_env(root, fakebin, parent_output),
    )
    parent = parse_outputs(parent_output)
    check(parent.get("release_tag") == "v6.5.1", "interrupted metadata tag was not selected")
    check(parent.get("selected_tag_sha") == tagged, "selected interrupted tag SHA was not reported")
    check(parent.get("selected_tag_relation") == "source_parent", "one-commit release transaction did not resume safely")


def run_plan_case(
    root: Path,
    *,
    draft: bool,
    existing: list[str],
    replace: str,
    should_pass: bool,
) -> subprocess.CompletedProcess[str]:
    case = root / f"plan-{draft}-{replace}-{len(existing)}"
    case.mkdir()
    variants = case / "variants.txt"
    variants.write_text("app\n", encoding="utf-8")
    existing_file = case / "existing.txt"
    existing_file.write_text("".join(f"{item}\n" for item in existing), encoding="utf-8")
    target = case / "target.json"
    target.write_text(
        json.dumps(
            {
                "id": 101,
                "tag_name": "v6.6.0",
                "draft": draft,
                "assets": [{"name": item} for item in existing],
            }
        ),
        encoding="utf-8",
    )
    output = case / "outputs.txt"
    output.write_text("", encoding="utf-8")
    env = os.environ.copy()
    env.update(
        {
            "RUNNER_TEMP": str(case),
            "GITHUB_OUTPUT": str(output),
            "TOOL": str(ORCHESTRATOR),
            "RELEASE_MODE": "repair_existing_release",
            "RELEASE_TAG": "v6.6.0",
            "SELECTED_FILE": str(variants),
            "DEBUG_DESTINATION": "workflow_artifacts",
            "EXISTING_ASSETS_FILE": str(existing_file),
            "TARGET_RELEASE_FILE": str(target),
            "REPLACE": replace,
        }
    )
    return run(["bash", str(PLAN)], cwd=REPO, env=env, should_pass=should_pass)


def test_published_asset_planning(root: Path) -> None:
    base = "Auxio-TS-v6.6.0-app-release.apk"
    complete = [base, f"{base}.sha256", f"{base}.metadata.txt"]
    partial = complete[:2]

    run_plan_case(root, draft=True, existing=partial, replace="false", should_pass=True)
    run_plan_case(root, draft=False, existing=complete, replace="false", should_pass=True)

    published_partial = run_plan_case(
        root,
        draft=False,
        existing=partial,
        replace="false",
        should_pass=False,
    )
    check("already published" in published_partial.stderr, "published partial repair was not rejected")

    published_replace = run_plan_case(
        root,
        draft=False,
        existing=complete,
        replace="true",
        should_pass=False,
    )
    check("already published" in published_replace.stderr, "published complete replacement was not rejected")


def upload_env(case: Path, fakebin: Path, uploads: Path, replace: Path) -> dict[str, str]:
    env = os.environ.copy()
    env.update(
        {
            "PATH": f"{fakebin}:{env.get('PATH', '')}",
            "RUNNER_TEMP": str(case),
            "GITHUB_REPOSITORY": "cbkii/Auxio-TS",
            "GH_TOKEN": "network-free-test-token",
            "RELEASE_TAG": "v6.6.0",
            "RELEASE_ID": "101",
            "UPLOAD_TSV": str(uploads),
            "REPLACE_NAMES_FILE": str(replace),
            "REPLACE": "false",
        }
    )
    return env


def test_published_upload_guard(root: Path) -> None:
    case = root / "upload-guard"
    case.mkdir()
    fakebin = case / "fakebin"
    fakebin.mkdir()
    marker = case / "curl-called"

    release = {
        "id": 101,
        "tag_name": "v6.6.0",
        "draft": False,
        "upload_url": "https://uploads.github.test/repos/cbkii/Auxio-TS/releases/101/assets{?name,label}",
        "assets": [],
    }
    gh = fakebin / "gh"
    gh.write_text(
        "#!/usr/bin/env python3\n"
        "import json\n"
        f"print(json.dumps({release!r}))\n",
        encoding="utf-8",
    )
    gh.chmod(0o755)
    curl = fakebin / "curl"
    curl.write_text(
        f"#!/usr/bin/env bash\nprintf called > {marker!s}\nexit 99\n",
        encoding="utf-8",
    )
    curl.chmod(0o755)

    staged = case / "artifact.apk"
    staged.write_bytes(b"not-an-apk-needed-for-policy-test")
    uploads = case / "upload.tsv"
    uploads.write_text(f"artifact.apk\t{staged}\n", encoding="utf-8")
    replace = case / "replace.txt"
    replace.write_text("", encoding="utf-8")

    result = run(
        ["bash", str(UPLOAD)],
        cwd=REPO,
        env=upload_env(case, fakebin, uploads, replace),
        should_pass=False,
    )
    check("Refusing to add, delete or replace release assets" in result.stderr, "published uploader guard did not explain rejection")
    check(not marker.exists(), "published release guard executed curl before rejecting mutation")


def test_publication_transition_upload_guard(root: Path) -> None:
    case = root / "upload-transition-guard"
    case.mkdir()
    fakebin = case / "fakebin"
    fakebin.mkdir()
    counter = case / "gh-count"
    marker = case / "curl-called"

    gh = fakebin / "gh"
    gh.write_text(
        "#!/usr/bin/env python3\n"
        "import json, os\n"
        "from pathlib import Path\n"
        f"counter = Path({str(counter)!r})\n"
        "count = int(counter.read_text() if counter.exists() else '0')\n"
        "counter.write_text(str(count + 1))\n"
        "draft = count < 2\n"
        "release = {'id': 101, 'tag_name': 'v6.6.0', 'draft': draft, "
        "'upload_url': 'https://uploads.github.test/repos/cbkii/Auxio-TS/releases/101/assets{?name,label}', 'assets': []}\n"
        "print(json.dumps(release))\n",
        encoding="utf-8",
    )
    gh.chmod(0o755)
    curl = fakebin / "curl"
    curl.write_text(
        f"#!/usr/bin/env bash\nprintf called > {marker!s}\nexit 99\n",
        encoding="utf-8",
    )
    curl.chmod(0o755)

    staged = case / "artifact.apk"
    staged.write_bytes(b"policy-test")
    uploads = case / "upload.tsv"
    uploads.write_text(f"artifact.apk\t{staged}\n", encoding="utf-8")
    replace = case / "replace.txt"
    replace.write_text("", encoding="utf-8")

    result = run(
        ["bash", str(UPLOAD)],
        cwd=REPO,
        env=upload_env(case, fakebin, uploads, replace),
        should_pass=False,
    )
    check("already published before uploading artifact.apk" in result.stderr, "publication transition before upload was not rejected at the mutation boundary")
    check(not marker.exists(), "publication transition guard executed curl after the release became published")


def test_publication_transition_delete_guard(root: Path) -> None:
    case = root / "delete-transition-guard"
    case.mkdir()
    fakebin = case / "fakebin"
    fakebin.mkdir()
    counter = case / "gh-count"
    delete_marker = case / "delete-called"

    gh = fakebin / "gh"
    gh.write_text(
        "#!/usr/bin/env python3\n"
        "import json, sys\n"
        "from pathlib import Path\n"
        f"counter = Path({str(counter)!r})\n"
        f"delete_marker = Path({str(delete_marker)!r})\n"
        "if '--method' in sys.argv and 'DELETE' in sys.argv:\n"
        "    delete_marker.write_text('called')\n"
        "    raise SystemExit(91)\n"
        "count = int(counter.read_text() if counter.exists() else '0')\n"
        "counter.write_text(str(count + 1))\n"
        "draft = count < 2\n"
        "release = {'id': 101, 'tag_name': 'v6.6.0', 'draft': draft, "
        "'upload_url': 'https://uploads.github.test/repos/cbkii/Auxio-TS/releases/101/assets{?name,label}', "
        "'assets': [{'id': 909, 'name': 'artifact.apk', 'state': 'uploaded'}]}\n"
        "print(json.dumps(release))\n",
        encoding="utf-8",
    )
    gh.chmod(0o755)
    curl = fakebin / "curl"
    curl.write_text("#!/usr/bin/env bash\nexit 99\n", encoding="utf-8")
    curl.chmod(0o755)

    staged = case / "artifact.apk"
    staged.write_bytes(b"policy-test")
    uploads = case / "upload.tsv"
    uploads.write_text(f"artifact.apk\t{staged}\n", encoding="utf-8")
    replace = case / "replace.txt"
    replace.write_text("artifact.apk\n", encoding="utf-8")
    env = upload_env(case, fakebin, uploads, replace)
    env["REPLACE"] = "true"

    result = run(["bash", str(UPLOAD)], cwd=REPO, env=env, should_pass=False)
    check("already published before deleting artifact.apk" in result.stderr, "publication transition before delete was not rejected at the mutation boundary")
    check(not delete_marker.exists(), "publication transition guard issued DELETE after the release became published")


def main() -> int:
    for tool in ("git", "jq", "timeout"):
        if subprocess.run(
            ["sh", "-c", f"command -v {tool} >/dev/null 2>&1"],
            check=False,
        ).returncode != 0:
            raise TestFailure(f"required CI tool is unavailable: {tool}")

    with tempfile.TemporaryDirectory(prefix="auxio-release-hardening-") as tmp:
        root = Path(tmp)
        test_release_source_authority(root)
        test_published_asset_planning(root)
        test_published_upload_guard(root)
        test_publication_transition_upload_guard(root)
        test_publication_transition_delete_guard(root)

    print("release-readiness hardening contracts: PASS")
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except (OSError, subprocess.TimeoutExpired, TestFailure) as exc:
        print(f"ERROR: {exc}", file=os.sys.stderr)
        raise SystemExit(1)
