#!/usr/bin/env python3
"""Network-free execution tests for Manual Release transaction shell steps."""
from __future__ import annotations

import json
import os
import shutil
import subprocess
import sys
import tempfile
from pathlib import Path
from typing import Any

REPO = Path.cwd()
SCRIPTS = REPO / "scripts" / "manual-release"


class TestFailure(RuntimeError):
    pass


def check(condition: bool, message: str) -> None:
    if not condition:
        raise TestFailure(message)


def write_json(path: Path, data: Any) -> None:
    path.write_text(json.dumps(data, indent=2, sort_keys=True) + "\n", encoding="utf-8")


def read_json(path: Path) -> Any:
    return json.loads(path.read_text(encoding="utf-8"))


def base_state(*, release: dict[str, Any] | None = None, fail_once: str = "") -> dict[str, Any]:
    return {
        "next_release_id": 101,
        "next_asset_id": 501,
        "fail_once": fail_once,
        "failure_consumed": False,
        "releases": [] if release is None else [release],
    }


def make_release(tag: str = "v6.6.0", release_id: int = 101, *, draft: bool = True, prerelease: bool = False, assets: list[dict[str, Any]] | None = None) -> dict[str, Any]:
    return {
        "id": release_id,
        "tag_name": tag,
        "name": tag,
        "html_url": f"https://github.test/cbkii/Auxio-TS/releases/tag/{tag}",
        "draft": draft,
        "prerelease": prerelease,
        "target_commitish": "1" * 40,
        "upload_url": f"https://uploads.github.test/repos/cbkii/Auxio-TS/releases/{release_id}/assets{{?name,label}}",
        "assets": [] if assets is None else assets,
    }


def install_fakes(root: Path) -> Path:
    fakebin = root / "fakebin"
    fakebin.mkdir()
    gh = fakebin / "gh"
    gh.write_text(
        r'''#!/usr/bin/env python3
import json, os, sys
from pathlib import Path

state_path = Path(os.environ["FAKE_GH_STATE"])
state = json.loads(state_path.read_text(encoding="utf-8"))
args = sys.argv[1:]

def save():
    tmp = state_path.with_suffix(".tmp")
    tmp.write_text(json.dumps(state, sort_keys=True) + "\n", encoding="utf-8")
    tmp.replace(state_path)

def consume_failure(name):
    if state.get("fail_once") == name and not state.get("failure_consumed", False):
        state["failure_consumed"] = True
        save()
        return True
    return False

def release_by_id(value):
    rid = int(value)
    for release in state["releases"]:
        if release["id"] == rid:
            return release
    raise SystemExit(44)

def output(value):
    sys.stdout.write(json.dumps(value) + "\n")

if args[:2] == ["auth", "setup-git"]:
    raise SystemExit(0)
if not args or args[0] != "api":
    print("unsupported fake gh invocation", args, file=sys.stderr)
    raise SystemExit(2)

method = "GET"
endpoint = ""
fields = {}
want_tsv = False
i = 1
while i < len(args):
    arg = args[i]
    if arg == "--method":
        method = args[i + 1]
        i += 2
    elif arg in ("-f", "-F"):
        key, value = args[i + 1].split("=", 1)
        fields[key] = value
        i += 2
    elif arg == "--jq":
        want_tsv = True
        i += 2
    elif arg == "--paginate":
        i += 1
    elif arg.startswith("repos/"):
        endpoint = arg
        i += 1
    else:
        i += 1

if not endpoint:
    print("fake gh did not receive endpoint", args, file=sys.stderr)
    raise SystemExit(2)

base_endpoint = endpoint.split("?", 1)[0]
if method == "GET" and base_endpoint.endswith("/releases"):
    if want_tsv:
        for release in state["releases"]:
            sys.stdout.write(f'{release["id"]}\t{release["tag_name"]}\n')
    else:
        output(state["releases"])
    raise SystemExit(0)

parts = base_endpoint.split("/")
if method == "GET" and len(parts) >= 5 and parts[-2] == "releases" and parts[-1].isdigit():
    output(release_by_id(parts[-1]))
    raise SystemExit(0)

if method == "POST" and base_endpoint.endswith("/releases"):
    release = {
        "id": int(state["next_release_id"]),
        "tag_name": fields.get("tag_name", ""),
        "name": fields.get("name", ""),
        "html_url": f'https://github.test/cbkii/Auxio-TS/releases/tag/{fields.get("tag_name", "")}',
        "draft": fields.get("draft", "false").lower() == "true",
        "prerelease": fields.get("prerelease", "false").lower() == "true",
        "target_commitish": fields.get("target_commitish", ""),
        "upload_url": f'https://uploads.github.test/repos/cbkii/Auxio-TS/releases/{state["next_release_id"]}/assets{{?name,label}}',
        "assets": [],
    }
    state["next_release_id"] += 1
    state["releases"].append(release)
    save()
    if consume_failure("create_after_commit"):
        raise SystemExit(1)
    output(release)
    raise SystemExit(0)

if method == "PATCH" and len(parts) >= 5 and parts[-2] == "releases" and parts[-1].isdigit():
    release = release_by_id(parts[-1])
    if "draft" in fields:
        release["draft"] = fields["draft"].lower() == "true"
    if "prerelease" in fields:
        release["prerelease"] = fields["prerelease"].lower() == "true"
    save()
    if consume_failure("patch_after_commit"):
        raise SystemExit(1)
    output(release)
    raise SystemExit(0)

if method == "DELETE" and len(parts) >= 6 and parts[-2] == "assets" and parts[-1].isdigit():
    asset_id = int(parts[-1])
    found = False
    for release in state["releases"]:
        kept = []
        for asset in release.get("assets", []):
            if asset.get("id") == asset_id:
                found = True
            else:
                kept.append(asset)
        release["assets"] = kept
    if not found:
        raise SystemExit(44)
    save()
    raise SystemExit(0)

print("unsupported fake gh api", method, endpoint, fields, file=sys.stderr)
raise SystemExit(2)
''',
        encoding="utf-8",
    )
    gh.chmod(0o755)

    curl = fakebin / "curl"
    curl.write_text(
        r'''#!/usr/bin/env python3
import json, os, sys, urllib.parse
from pathlib import Path

state_path = Path(os.environ["FAKE_GH_STATE"])
state = json.loads(state_path.read_text(encoding="utf-8"))
args = sys.argv[1:]
url = next((arg for arg in reversed(args) if arg.startswith("https://")), "")
if not url:
    print("fake curl missing URL", file=sys.stderr)
    raise SystemExit(2)
parsed = urllib.parse.urlparse(url)
query = urllib.parse.parse_qs(parsed.query)
name = query.get("name", [""])[0]
parts = parsed.path.rstrip("/").split("/")
try:
    release_id = int(parts[-2])
except (ValueError, IndexError):
    print("fake curl invalid release upload URL", url, file=sys.stderr)
    raise SystemExit(2)
release = next((item for item in state["releases"] if item["id"] == release_id), None)
if release is None:
    raise SystemExit(44)
asset = {
    "id": int(state["next_asset_id"]),
    "name": name,
    "state": "uploaded",
}
state["next_asset_id"] += 1
release.setdefault("assets", []).append(asset)
fail = state.get("fail_once") == "upload_after_commit" and not state.get("failure_consumed", False)
if fail:
    state["failure_consumed"] = True
tmp = state_path.with_suffix(".tmp")
tmp.write_text(json.dumps(state, sort_keys=True) + "\n", encoding="utf-8")
tmp.replace(state_path)
if fail:
    print("simulated transport failure after committed upload", file=sys.stderr)
    raise SystemExit(22)
print(json.dumps(asset))
''',
        encoding="utf-8",
    )
    curl.chmod(0o755)

    sleep = fakebin / "sleep"
    sleep.write_text("#!/usr/bin/env bash\nexit 0\n", encoding="utf-8")
    sleep.chmod(0o755)
    return fakebin


def run_script(path: Path, env: dict[str, str], *, cwd: Path | None = None, should_pass: bool = True, timeout: int = 30) -> subprocess.CompletedProcess[str]:
    completed = subprocess.run(
        ["bash", str(path)],
        cwd=cwd or REPO,
        env=env,
        text=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        timeout=timeout,
        check=False,
    )
    if should_pass and completed.returncode != 0:
        raise TestFailure(
            f"{path.name} failed unexpectedly ({completed.returncode})\nstdout:\n{completed.stdout}\nstderr:\n{completed.stderr}"
        )
    if not should_pass and completed.returncode == 0:
        raise TestFailure(f"{path.name} unexpectedly succeeded\nstdout:\n{completed.stdout}\nstderr:\n{completed.stderr}")
    return completed


def env_for(root: Path, fakebin: Path, state_path: Path, output_name: str) -> tuple[dict[str, str], Path]:
    output = root / output_name
    output.write_text("", encoding="utf-8")
    env = os.environ.copy()
    env.update(
        {
            "PATH": f"{fakebin}:{env.get('PATH', '')}",
            "FAKE_GH_STATE": str(state_path),
            "RUNNER_TEMP": str(root),
            "GITHUB_REPOSITORY": "cbkii/Auxio-TS",
            "GH_TOKEN": "local-mock-token",
            "GITHUB_OUTPUT": str(output),
        }
    )
    return env, output


def parse_outputs(path: Path) -> dict[str, str]:
    result: dict[str, str] = {}
    for line in path.read_text(encoding="utf-8").splitlines():
        if "=" in line:
            key, value = line.split("=", 1)
            result[key] = value
    return result


def test_release_create(root: Path, fakebin: Path) -> None:
    state_path = root / "create-state.json"
    write_json(state_path, base_state())
    env, output = env_for(root, fakebin, state_path, "create.out")
    env.update(
        {
            "RELEASE_TAG": "v6.6.0",
            "RELEASE_SHA": "1" * 40,
            "PREVIOUS_TAG": "",
            "RELEASE_EXISTS": "false",
            "DEBUG_DESTINATION": "workflow_artifacts",
            "TARGET_RELEASE_FILE": str(root / "unused-target.json"),
        }
    )
    run_script(SCRIPTS / "15-ensure-draft-release-transaction-exists.sh", env)
    state = read_json(state_path)
    check(len(state["releases"]) == 1, "release create did not create exactly one transaction")
    release = state["releases"][0]
    check(release["target_commitish"] == "1" * 40, "release create did not bind target_commitish to immutable release SHA")
    check(release["draft"] is True and release["prerelease"] is False, "release create was not draft-first")
    values = parse_outputs(output)
    check(values.get("release_id") == "101" and values.get("created") == "true", "release create outputs are wrong")

    state_path2 = root / "create-ambiguous-state.json"
    write_json(state_path2, base_state(fail_once="create_after_commit"))
    env2, output2 = env_for(root, fakebin, state_path2, "create-ambiguous.out")
    env2.update(env)
    env2["FAKE_GH_STATE"] = str(state_path2)
    env2["GITHUB_OUTPUT"] = str(output2)
    run_script(SCRIPTS / "15-ensure-draft-release-transaction-exists.sh", env2)
    state2 = read_json(state_path2)
    check(len(state2["releases"]) == 1, "ambiguous create retry duplicated the GitHub Release")
    check(parse_outputs(output2).get("release_id") == "101", "ambiguous create did not recover the committed release ID")

    existing = make_release()
    target = root / "existing-target.json"
    write_json(target, existing)
    state_path3 = root / "existing-state.json"
    write_json(state_path3, base_state(release=existing))
    env3, output3 = env_for(root, fakebin, state_path3, "existing.out")
    env3.update(
        {
            "RELEASE_TAG": "v6.6.0",
            "RELEASE_SHA": "1" * 40,
            "PREVIOUS_TAG": "v6.5.8",
            "RELEASE_EXISTS": "true",
            "DEBUG_DESTINATION": "workflow_artifacts",
            "TARGET_RELEASE_FILE": str(target),
        }
    )
    run_script(SCRIPTS / "15-ensure-draft-release-transaction-exists.sh", env3)
    check(parse_outputs(output3).get("created") == "false", "existing release transaction was recreated")


def make_asset(asset_id: int, name: str) -> dict[str, Any]:
    return {"id": asset_id, "name": name, "state": "uploaded"}


def test_asset_upload(root: Path, fakebin: Path) -> None:
    tag = "v6.6.0"
    release = make_release(tag=tag)
    state_path = root / "upload-state.json"
    write_json(state_path, base_state(release=release))
    staged = root / "staged.bin"
    staged.write_bytes(b"payload")
    upload_tsv = root / "upload.tsv"
    upload_tsv.write_text(f"artifact.apk\t{staged}\n", encoding="utf-8")
    replace_names = root / "replace.txt"
    replace_names.write_text("", encoding="utf-8")
    env, _ = env_for(root, fakebin, state_path, "upload.out")
    env.update(
        {
            "RELEASE_TAG": tag,
            "RELEASE_ID": "101",
            "UPLOAD_TSV": str(upload_tsv),
            "REPLACE_NAMES_FILE": str(replace_names),
            "REPLACE": "false",
        }
    )
    run_script(SCRIPTS / "16-upload-or-replace-planned-release-assets.sh", env)
    state = read_json(state_path)
    assets = state["releases"][0]["assets"]
    check([item["name"] for item in assets] == ["artifact.apk"], "asset upload did not create exactly one uploaded asset")

    release2 = make_release(tag=tag)
    state_path2 = root / "upload-ambiguous-state.json"
    state2 = base_state(release=release2, fail_once="upload_after_commit")
    write_json(state_path2, state2)
    env2, _ = env_for(root, fakebin, state_path2, "upload-ambiguous.out")
    env2.update({key: value for key, value in env.items() if key in {"RELEASE_TAG", "RELEASE_ID", "UPLOAD_TSV", "REPLACE_NAMES_FILE", "REPLACE"}})
    run_script(SCRIPTS / "16-upload-or-replace-planned-release-assets.sh", env2)
    assets2 = read_json(state_path2)["releases"][0]["assets"]
    check(len(assets2) == 1 and assets2[0]["name"] == "artifact.apk", "ambiguous upload retry created duplicate assets")

    existing = make_asset(777, "artifact.apk")
    release3 = make_release(tag=tag, assets=[existing])
    state_path3 = root / "upload-unauthorised-state.json"
    write_json(state_path3, base_state(release=release3))
    env3, _ = env_for(root, fakebin, state_path3, "upload-unauthorised.out")
    env3.update({key: value for key, value in env.items() if key in {"RELEASE_TAG", "RELEASE_ID", "UPLOAD_TSV", "REPLACE_NAMES_FILE", "REPLACE"}})
    run_script(SCRIPTS / "16-upload-or-replace-planned-release-assets.sh", env3, should_pass=False)
    check(read_json(state_path3)["releases"][0]["assets"][0]["id"] == 777, "unauthorised replacement modified remote state")

    replace_names.write_text("artifact.apk\n", encoding="utf-8")
    env4, _ = env_for(root, fakebin, state_path3, "upload-authorised.out")
    env4.update({key: value for key, value in env.items() if key in {"RELEASE_TAG", "RELEASE_ID", "UPLOAD_TSV", "REPLACE_NAMES_FILE", "REPLACE"}})
    run_script(SCRIPTS / "16-upload-or-replace-planned-release-assets.sh", env4)
    replaced_assets = read_json(state_path3)["releases"][0]["assets"]
    check(len(replaced_assets) == 1 and replaced_assets[0]["id"] != 777, "authorised repair did not replace exactly one existing asset")

    duplicate_tsv = root / "duplicate-upload.tsv"
    duplicate_tsv.write_text(f"dup.apk\t{staged}\ndup.apk\t{staged}\n", encoding="utf-8")
    state_path4 = root / "duplicate-plan-state.json"
    write_json(state_path4, base_state(release=make_release(tag=tag)))
    env5, _ = env_for(root, fakebin, state_path4, "duplicate-plan.out")
    env5.update(
        {
            "RELEASE_TAG": tag,
            "RELEASE_ID": "101",
            "UPLOAD_TSV": str(duplicate_tsv),
            "REPLACE_NAMES_FILE": str(replace_names),
            "REPLACE": "false",
        }
    )
    run_script(SCRIPTS / "16-upload-or-replace-planned-release-assets.sh", env5, should_pass=False)
    check(read_json(state_path4)["releases"][0]["assets"] == [], "duplicate upload plan mutated remote state")


def test_remote_verification(root: Path, fakebin: Path) -> None:
    tag = "v6.6.0"
    names = ["app.apk", "app.apk.sha256", "app.apk.metadata.txt"]
    release = make_release(tag=tag, assets=[make_asset(700 + index, name) for index, name in enumerate(names)])
    state_path = root / "verify-state.json"
    write_json(state_path, base_state(release=release))
    verify = root / "verify.txt"
    verify.write_text("\n".join(names) + "\n", encoding="utf-8")
    env, output = env_for(root, fakebin, state_path, "verify.out")
    env.update(
        {
            "RELEASE_TAG": tag,
            "RELEASE_ID": "101",
            "RELEASE_MODE": "create_new_release",
            "VERIFY_NAMES_FILE": str(verify),
            "DEBUG_DESTINATION": "workflow_artifacts",
        }
    )
    run_script(SCRIPTS / "17-verify-remote-release-asset-manifest.sh", env)
    check(parse_outputs(output).get("verified") == "true", "exact new-release manifest did not verify")

    release_extra = make_release(tag=tag, assets=release["assets"] + [make_asset(999, "unexpected.bin")])
    state_path2 = root / "verify-extra-state.json"
    write_json(state_path2, base_state(release=release_extra))
    env2, _ = env_for(root, fakebin, state_path2, "verify-extra.out")
    env2.update({key: value for key, value in env.items() if key in {"RELEASE_TAG", "RELEASE_ID", "RELEASE_MODE", "VERIFY_NAMES_FILE", "DEBUG_DESTINATION"}})
    run_script(SCRIPTS / "17-verify-remote-release-asset-manifest.sh", env2, should_pass=False)

    env3, output3 = env_for(root, fakebin, state_path2, "verify-repair.out")
    env3.update({key: value for key, value in env.items() if key in {"RELEASE_TAG", "RELEASE_ID", "VERIFY_NAMES_FILE", "DEBUG_DESTINATION"}})
    env3["RELEASE_MODE"] = "repair_existing_release"
    run_script(SCRIPTS / "17-verify-remote-release-asset-manifest.sh", env3)
    check(parse_outputs(output3).get("verified") == "true", "repair verification incorrectly rejected unrelated historical assets")

    debug_name = f"Auxio-TS-{tag}-topway-twmedia-debug.apk"
    debug_release = make_release(tag=tag, assets=[make_asset(801, debug_name)])
    state_path3 = root / "verify-debug-state.json"
    write_json(state_path3, base_state(release=debug_release))
    debug_verify = root / "verify-debug.txt"
    debug_verify.write_text(debug_name + "\n", encoding="utf-8")
    env4, _ = env_for(root, fakebin, state_path3, "verify-debug.out")
    env4.update(
        {
            "RELEASE_TAG": tag,
            "RELEASE_ID": "101",
            "RELEASE_MODE": "create_new_release",
            "VERIFY_NAMES_FILE": str(debug_verify),
            "DEBUG_DESTINATION": "workflow_artifacts",
        }
    )
    run_script(SCRIPTS / "17-verify-remote-release-asset-manifest.sh", env4, should_pass=False)


def test_status_update(root: Path, fakebin: Path) -> None:
    tag = "v6.6.0"
    release = make_release(tag=tag, draft=True, prerelease=False)
    state_path = root / "status-state.json"
    write_json(state_path, base_state(release=release))
    env, output = env_for(root, fakebin, state_path, "status.out")
    env.update({"RELEASE_ID": "101", "DRAFT": "false", "PRERELEASE": "true"})
    run_script(SCRIPTS / "18-apply-requested-status-after-verified-create-transaction.sh", env)
    state = read_json(state_path)
    remote = state["releases"][0]
    check(remote["draft"] is False and remote["prerelease"] is True, "requested release status was not applied")
    values = parse_outputs(output)
    check(values.get("draft") == "false" and values.get("prerelease") == "true", "release status outputs are wrong")

    release2 = make_release(tag=tag, draft=True, prerelease=False)
    state_path2 = root / "status-ambiguous-state.json"
    write_json(state_path2, base_state(release=release2, fail_once="patch_after_commit"))
    env2, output2 = env_for(root, fakebin, state_path2, "status-ambiguous.out")
    env2.update({"RELEASE_ID": "101", "DRAFT": "false", "PRERELEASE": "true"})
    run_script(SCRIPTS / "18-apply-requested-status-after-verified-create-transaction.sh", env2)
    check(read_json(state_path2)["releases"][0]["draft"] is False, "ambiguous status PATCH did not accept committed postcondition")
    check(parse_outputs(output2).get("draft") == "false", "ambiguous status PATCH outputs are wrong")


def git(args: list[str], cwd: Path) -> str:
    completed = subprocess.run(["git", *args], cwd=cwd, text=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE, timeout=20, check=False)
    if completed.returncode != 0:
        raise TestFailure(f"git {' '.join(args)} failed: {completed.stderr}")
    return completed.stdout.strip()


def init_git_pair(root: Path) -> tuple[Path, Path, str]:
    bare = root / "origin.git"
    work = root / "work"
    git(["init", "--bare", str(bare)], root)
    git(["init", str(work)], root)
    git(["switch", "-c", "dev"], work)
    git(["config", "user.name", "CI Smoke"], work)
    git(["config", "user.email", "ci-smoke@example.invalid"], work)
    (work / "file.txt").write_text("base\n", encoding="utf-8")
    git(["add", "file.txt"], work)
    git(["commit", "-m", "base"], work)
    git(["remote", "add", "origin", str(bare)], work)
    git(["push", "-u", "origin", "HEAD:refs/heads/dev"], work)
    git(["symbolic-ref", "HEAD", "refs/heads/dev"], bare)
    base = git(["rev-parse", "HEAD"], work)
    return bare, work, base


def test_tag_and_sync(root: Path, fakebin: Path) -> None:
    git_root = root / "git-tests"
    git_root.mkdir()
    bare, work, _base = init_git_pair(git_root)
    (work / "file.txt").write_text("release\n", encoding="utf-8")
    git(["add", "file.txt"], work)
    git(["commit", "-m", "release metadata"], work)
    release_sha = git(["rev-parse", "HEAD"], work)

    state_path = root / "git-state.json"
    write_json(state_path, base_state())
    env, tag_output = env_for(root, fakebin, state_path, "tag.out")
    env.update({"RELEASE_TAG": "v6.6.0", "RELEASE_SHA": release_sha})
    run_script(SCRIPTS / "14-push-immutable-release-tag.sh", env, cwd=work)
    remote_tag = git(["ls-remote", "--tags", "origin", "refs/tags/v6.6.0"], work).split()[0]
    check(remote_tag == release_sha, "immutable tag did not resolve to validated release SHA")
    check(parse_outputs(tag_output).get("tag_pushed") == "true", "tag step did not report successful immutable publication")

    env2, sync_output = env_for(root, fakebin, state_path, "sync.out")
    env2.update({"RELEASE_SHA": release_sha})
    run_script(SCRIPTS / "19-synchronise-released-source-metadata-to-dev.sh", env2, cwd=work)
    check(parse_outputs(sync_output).get("status") == "fast_forwarded", "metadata sync did not fast-forward remote dev from release parent")
    check(git(["ls-remote", "origin", "refs/heads/dev"], work).split()[0] == release_sha, "remote dev was not advanced to release metadata commit")

    env3, sync_output2 = env_for(root, fakebin, state_path, "sync-again.out")
    env3.update({"RELEASE_SHA": release_sha})
    run_script(SCRIPTS / "19-synchronise-released-source-metadata-to-dev.sh", env3, cwd=work)
    check(parse_outputs(sync_output2).get("status") == "already_synced", "metadata sync was not idempotent on rerun")

    git(["checkout", "dev"], work)
    git(["reset", "--hard", release_sha], work)
    (work / "file.txt").write_text("candidate\n", encoding="utf-8")
    git(["add", "file.txt"], work)
    git(["commit", "-m", "candidate metadata"], work)
    candidate = git(["rev-parse", "HEAD"], work)
    other = git_root / "other"
    git(["clone", str(bare), str(other)], git_root)
    git(["config", "user.name", "CI Smoke"], other)
    git(["config", "user.email", "ci-smoke@example.invalid"], other)
    (other / "other.txt").write_text("moved\n", encoding="utf-8")
    git(["add", "other.txt"], other)
    git(["commit", "-m", "dev moved"], other)
    git(["push", "origin", "HEAD:refs/heads/dev"], other)

    env4, sync_output3 = env_for(root, fakebin, state_path, "sync-moved.out")
    env4.update({"RELEASE_SHA": candidate})
    run_script(SCRIPTS / "19-synchronise-released-source-metadata-to-dev.sh", env4, cwd=work)
    check(parse_outputs(sync_output3).get("status") == "dev_moved", "diverged dev was not detected without destructive push")


def main() -> int:
    required = [
        "14-push-immutable-release-tag.sh",
        "15-ensure-draft-release-transaction-exists.sh",
        "16-upload-or-replace-planned-release-assets.sh",
        "17-verify-remote-release-asset-manifest.sh",
        "18-apply-requested-status-after-verified-create-transaction.sh",
        "19-synchronise-released-source-metadata-to-dev.sh",
    ]
    if any(not (SCRIPTS / name).is_file() for name in required):
        print("FAILED: run from the Auxio-TS repository root", file=sys.stderr)
        return 2
    for command in ("bash", "git", "jq", "timeout"):
        if shutil.which(command) is None:
            print(f"FAILED: required host command missing: {command}", file=sys.stderr)
            return 2
    try:
        with tempfile.TemporaryDirectory(prefix="auxio-release-transaction-") as temp:
            root = Path(temp)
            fakebin = install_fakes(root)
            test_release_create(root, fakebin)
            test_asset_upload(root, fakebin)
            test_remote_verification(root, fakebin)
            test_status_update(root, fakebin)
            test_tag_and_sync(root, fakebin)
    except (TestFailure, OSError, subprocess.SubprocessError, ValueError, json.JSONDecodeError) as error:
        print(f"FAILED: Manual Release transaction mocks: {error}", file=sys.stderr)
        return 1
    print("SUCCESS: Manual Release tag/create/upload/verify/status/sync transaction mocks passed")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
