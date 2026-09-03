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


def executable(path: Path, text: str) -> None:
    path.write_text(text, encoding="utf-8")
    path.chmod(0o755)


def parse_outputs(path: Path) -> dict[str, str]:
    result: dict[str, str] = {}
    for line in path.read_text(encoding="utf-8").splitlines():
        if "=" in line:
            key, value = line.split("=", 1)
            result[key] = value
    return result


def install_empty_release_gh(fakebin: Path) -> None:
    executable(
        fakebin / "gh",
        """#!/usr/bin/env bash
set -u
[[ "${1:-}" == api ]] || exit 2
exit 0
""",
    )


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
    run(["bash", str(RESOLVE)], cwd=repo, env=resolver_env(root, fakebin, output))
    values = parse_outputs(output)
    check(values.get("effective_mode") == "repair_existing_release", "current-head interrupted tag did not resume")
    check(values.get("selected_tag_relation") == "source_head", "current-head tag relation was not recognised")

    (repo / "README.md").write_text("new work\n", encoding="utf-8")
    git(repo, "add", "README.md")
    git(repo, "commit", "-m", "newer development work")
    check(git(repo, "rev-parse", "HEAD") != source, "test repository did not advance")

    stale_output = root / "stale-auto.out"
    stale = run(
        ["bash", str(RESOLVE)],
        cwd=repo,
        env=resolver_env(root, fakebin, stale_output),
        should_pass=False,
    )
    check("Refusing to publish stale source" in f"{stale.stdout}\n{stale.stderr}", "stale automatic repair did not fail with source-authority diagnostic")

    explicit_output = root / "explicit-repair.out"
    run(
        ["bash", str(RESOLVE)],
        cwd=repo,
        env=resolver_env(root, fakebin, explicit_output, input_tag="v6.5.0"),
    )
    explicit = parse_outputs(explicit_output)
    check(explicit.get("effective_mode") == "repair_existing_release", "explicit historical repair was blocked")
    check(explicit.get("selected_tag_relation") == "stale", "explicit historical repair did not report stale relation")

    git(repo, "checkout", "--detach", source)
    (repo / "release-metadata.txt").write_text("metadata\n", encoding="utf-8")
    git(repo, "add", "release-metadata.txt")
    git(repo, "commit", "-m", "release metadata")
    tagged = git(repo, "rev-parse", "HEAD")
    git(repo, "tag", "v6.5.1")
    git(repo, "checkout", "--detach", source)
    write_gradle(repo, version="6.5.1", code=6_050_100)
    parent_output = root / "parent-resume.out"
    run(["bash", str(RESOLVE)], cwd=repo, env=resolver_env(root, fakebin, parent_output))
    parent = parse_outputs(parent_output)
    check(parent.get("release_tag") == "v6.5.1", "interrupted metadata tag was not selected")
    check(parent.get("selected_tag_sha") == tagged, "selected interrupted tag SHA was not reported")
    check(parent.get("selected_tag_relation") == "source_parent", "one-commit release transaction did not resume safely")


def test_resolver_failed_api_read_stays_failed(root: Path) -> None:
    repo = root / "resolver-read-failure"
    repo.mkdir()
    fakebin = root / "fakebin-resolver-failure"
    fakebin.mkdir()
    executable(fakebin / "gh", "#!/usr/bin/env bash\nexit 42\n")
    executable(fakebin / "sleep", "#!/usr/bin/env bash\nexit 0\n")
    git(repo, "init")
    git(repo, "config", "user.name", "Release Smoke")
    git(repo, "config", "user.email", "release-smoke@example.invalid")
    write_gradle(repo)
    git(repo, "add", "app/build.gradle")
    git(repo, "commit", "-m", "release source")
    output = root / "resolver-read-failure.out"
    result = run(
        ["bash", str(RESOLVE)],
        cwd=repo,
        env=resolver_env(root, fakebin, output),
        should_pass=False,
    )
    check("GitHub read failed after 3 bounded attempts" in result.stderr, "resolver lost the failed gh status after retry exhaustion")


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
        json.dumps({"id": 101, "tag_name": "v6.6.0", "draft": draft, "assets": [{"name": item} for item in existing]}),
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
    partial_result = run_plan_case(root, draft=False, existing=partial, replace="false", should_pass=False)
    check("already published" in partial_result.stderr, "published partial repair was not rejected")
    replace_result = run_plan_case(root, draft=False, existing=complete, replace="true", should_pass=False)
    check("already published" in replace_result.stderr, "published complete replacement was not rejected")


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


def upload_fixture(case: Path) -> tuple[Path, Path]:
    staged = case / "artifact.apk"
    staged.write_bytes(b"policy-test")
    uploads = case / "upload.tsv"
    uploads.write_text(f"artifact.apk\t{staged}\n", encoding="utf-8")
    replace = case / "replace.txt"
    replace.write_text("", encoding="utf-8")
    return uploads, replace


def test_published_upload_guard(root: Path) -> None:
    case = root / "upload-guard"
    case.mkdir()
    fakebin = case / "fakebin"
    fakebin.mkdir()
    marker = case / "curl-called"
    release = {"id": 101, "tag_name": "v6.6.0", "draft": False, "upload_url": "https://uploads.github.test/repos/cbkii/Auxio-TS/releases/101/assets{?name,label}", "assets": []}
    executable(fakebin / "gh", "#!/usr/bin/env python3\nimport json\n" + f"print(json.dumps({release!r}))\n")
    executable(fakebin / "curl", f"#!/usr/bin/env bash\nprintf called > {marker!s}\nexit 99\n")
    uploads, replace = upload_fixture(case)
    result = run(["bash", str(UPLOAD)], cwd=REPO, env=upload_env(case, fakebin, uploads, replace), should_pass=False)
    check("Refusing to add, delete or replace release assets" in result.stderr, "published uploader guard did not explain rejection")
    check(not marker.exists(), "published release guard executed curl before rejecting mutation")


def install_phase_release_gh(fakebin: Path, *, publish_phase: str, assets: str) -> None:
    executable(
        fakebin / "gh",
        "#!/usr/bin/env python3\n"
        "import json, os, sys\n"
        "phase = os.environ.get('RELEASE_REFRESH_PHASE', '')\n"
        f"publish_phase = {publish_phase!r}\n"
        "if '--method' in sys.argv and 'DELETE' in sys.argv:\n"
        "    marker = os.environ.get('DELETE_MARKER')\n"
        "    if marker:\n"
        "        with open(marker, 'w', encoding='utf-8') as handle:\n"
        "            handle.write('called')\n"
        "    raise SystemExit(91)\n"
        "draft = phase != publish_phase\n"
        "release = {'id': 101, 'tag_name': 'v6.6.0', 'draft': draft, "
        "'upload_url': 'https://uploads.github.test/repos/cbkii/Auxio-TS/releases/101/assets{?name,label}', "
        f"'assets': {assets}}}\n"
        "print(json.dumps(release))\n",
    )


def test_publication_transition_upload_guard(root: Path) -> None:
    case = root / "upload-transition-guard"
    case.mkdir()
    fakebin = case / "fakebin"
    fakebin.mkdir()
    marker = case / "curl-called"
    install_phase_release_gh(fakebin, publish_phase="uploading artifact.apk", assets="[]")
    executable(fakebin / "curl", f"#!/usr/bin/env bash\nprintf called > {marker!s}\nexit 99\n")
    uploads, replace = upload_fixture(case)
    result = run(["bash", str(UPLOAD)], cwd=REPO, env=upload_env(case, fakebin, uploads, replace), should_pass=False)
    check("already published before uploading artifact.apk" in result.stderr, "publication transition before upload was not rejected at the semantic mutation boundary")
    check(not marker.exists(), "publication transition guard executed curl after the release became published")


def test_publication_transition_delete_guard(root: Path) -> None:
    case = root / "delete-transition-guard"
    case.mkdir()
    fakebin = case / "fakebin"
    fakebin.mkdir()
    delete_marker = case / "delete-called"
    install_phase_release_gh(
        fakebin,
        publish_phase="deleting artifact.apk",
        assets="[{'id': 909, 'name': 'artifact.apk', 'state': 'uploaded'}]",
    )
    executable(fakebin / "curl", "#!/usr/bin/env bash\nexit 99\n")
    uploads, replace = upload_fixture(case)
    replace.write_text("artifact.apk\n", encoding="utf-8")
    env = upload_env(case, fakebin, uploads, replace)
    env["REPLACE"] = "true"
    env["DELETE_MARKER"] = str(delete_marker)
    result = run(["bash", str(UPLOAD)], cwd=REPO, env=env, should_pass=False)
    check("already published before deleting artifact.apk" in result.stderr, "publication transition before delete was not rejected at the semantic mutation boundary")
    check(not delete_marker.exists(), "publication transition guard issued DELETE after the release became published")


def test_upload_failed_api_read_stays_failed(root: Path) -> None:
    case = root / "upload-read-failure"
    case.mkdir()
    fakebin = case / "fakebin"
    fakebin.mkdir()
    curl_marker = case / "curl-called"
    executable(fakebin / "gh", "#!/usr/bin/env bash\nexit 43\n")
    executable(fakebin / "sleep", "#!/usr/bin/env bash\nexit 0\n")
    executable(fakebin / "curl", f"#!/usr/bin/env bash\nprintf called > {curl_marker!s}\nexit 99\n")
    uploads, replace = upload_fixture(case)
    result = run(["bash", str(UPLOAD)], cwd=REPO, env=upload_env(case, fakebin, uploads, replace), should_pass=False)
    check("GitHub release-state read failed before asset mutation" in result.stderr, "uploader lost the failed gh status after retry exhaustion")
    check(not curl_marker.exists(), "uploader mutated assets after release-state read failure")


def main() -> int:
    for tool in ("git", "jq", "timeout"):
        if subprocess.run(["sh", "-c", f"command -v {tool} >/dev/null 2>&1"], check=False).returncode != 0:
            raise TestFailure(f"required CI tool is unavailable: {tool}")

    with tempfile.TemporaryDirectory(prefix="auxio-release-hardening-") as tmp:
        root = Path(tmp)
        test_release_source_authority(root)
        test_resolver_failed_api_read_stays_failed(root)
        test_published_asset_planning(root)
        test_published_upload_guard(root)
        test_publication_transition_upload_guard(root)
        test_publication_transition_delete_guard(root)
        test_upload_failed_api_read_stays_failed(root)

    print("release-readiness hardening contracts: PASS")
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except (OSError, subprocess.TimeoutExpired, TestFailure) as exc:
        print(f"ERROR: {exc}", file=os.sys.stderr)
        raise SystemExit(1)
