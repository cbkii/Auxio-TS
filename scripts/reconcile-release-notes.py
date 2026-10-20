#!/usr/bin/env python3
"""Reconcile an Auxio draft release's anchored asset notes from uploaded APK sidecars.

This is a metadata-only operation: no build, signing, asset, tag or status writes.
Existing changelog and other release prose remain untouched.
"""
from __future__ import annotations

import argparse
import hashlib
import html
import json
import os
import re
import subprocess
import sys
from urllib.parse import quote

BEGIN = "<!-- auxio-release-assets:begin -->"
END = "<!-- auxio-release-assets:end -->"
SHA = re.compile(r"[0-9a-f]{40}\Z")
DIGEST = re.compile(r"[0-9a-f]{64}\Z")
TAG = re.compile(r"v[0-9]+\.[0-9]+\.[0-9]+(?:[.-][A-Za-z0-9]+)*\Z")
VARIANTS = {
    "app-release": ("com.tw.media", "Maintained signed app", "signed-apk"),
    "app-debug": (None, "Diagnostic debug app", "debug-diagnostics-apk"),
    "lsposed-api100-bridge": (None, "Optional Track-C bridge", "signed-lsposed-api100-addon"),
    "lsposed-api100-bridge-debug": (None, "Diagnostic Track-C bridge", "debug-lsposed-api100-addon"),
}


class ReconciliationError(ValueError):
    pass


def api(path: str, body: dict | None = None) -> dict:
    args = ["timeout", "60s", "gh", "api"]
    if body is not None:
        args += ["--method", "PATCH", "--input", "-"]
    args.append(path)
    result = subprocess.run(
        args, input=json.dumps(body) if body is not None else None,
        text=True, capture_output=True, check=True, timeout=70,
    )
    return json.loads(result.stdout)


def asset_bytes(repository: str, asset_id: int) -> bytes:
    token = os.environ["GH_TOKEN"]
    result = subprocess.run(
        ["curl", "--fail", "--silent", "--show-error", "--location",
         "--connect-timeout", "15", "--max-time", "60", "--max-filesize", "65536",
         "--header", "Accept: application/octet-stream",
         "--header", f"Authorization: Bearer {token}",
         "--header", "X-GitHub-Api-Version: 2022-11-28",
         f"https://api.github.com/repos/{repository}/releases/assets/{asset_id}"],
        capture_output=True, check=True, timeout=70,
    )
    if len(result.stdout) > 65536:
        raise ReconciliationError("Text sidecar exceeds size limit")
    return result.stdout


def header_fields(content: bytes) -> dict[str, str]:
    """Parse only the machine-readable header; cert reports follow a blank line."""
    output: dict[str, str] = {}
    for line in content.decode("utf-8").splitlines():
        if not line or line.startswith("[apksigner"):
            break
        if "=" not in line:
            raise ReconciliationError("Malformed metadata header line")
        key, value = line.split("=", 1)
        if not re.fullmatch(r"[a-z][a-z0-9_]*", key) or key in output:
            raise ReconciliationError(f"Duplicate/invalid metadata key: {key!r}")
        output[key] = value
    return output


def safe(value: str) -> str:
    return html.escape(value, quote=True).replace("|", "&#124;").replace("\x60", "&#96;")[:240]


def ensure_digest(asset: dict, content: bytes) -> None:
    expected = asset.get("digest")
    if expected is not None and expected != "sha256:" + hashlib.sha256(content).hexdigest():
        raise ReconciliationError(f"Remote SHA-256 mismatch for {asset['name']}")


def verified_snapshots(repository: str, release: dict) -> list[dict]:
    tag = release["name"]
    if not TAG.fullmatch(tag):
        raise ReconciliationError(f"Unsupported release version: {tag!r}")
    prefix = f"Auxio-TS-{tag}-"
    assets = release.get("assets", [])
    by_name = {}
    for asset in assets:
        name = asset["name"]
        if name in by_name:
            raise ReconciliationError(f"Duplicate asset: {name}")
        if asset.get("state") != "uploaded" or asset.get("size", 0) <= 0:
            raise ReconciliationError(f"Incomplete asset: {name}")
        expected_url = (
            f"https://github.com/{repository}/releases/download/"
            f"{quote(release['tag_name'], safe='')}/{quote(name, safe='')}"
        )
        if asset.get("browser_download_url") != expected_url:
            raise ReconciliationError(f"Asset URL does not match release identity: {name}")
        by_name[name] = asset

    if not by_name:
        raise ReconciliationError("Release has no uploaded APK triplets")
    allowed = set()
    entries = []
    commits = set()
    versions = set()
    for variant, (required_package, label, required_kind) in VARIANTS.items():
        apk = prefix + variant + ".apk"
        names = [apk, apk + ".metadata.txt", apk + ".sha256"]
        if not any(name in by_name for name in names):
            continue
        if not all(name in by_name for name in names):
            raise ReconciliationError(f"Incomplete APK/metadata/checksum triplet: {apk}")
        allowed.update(names)
        metadata_raw = asset_bytes(repository, by_name[names[1]]["id"])
        checksums_raw = asset_bytes(repository, by_name[names[2]]["id"])
        ensure_digest(by_name[names[1]], metadata_raw)
        ensure_digest(by_name[names[2]], checksums_raw)
        meta = header_fields(metadata_raw)
        for key in ("asset", "asset_kind", "asset_sha256", "source_commit",
                    "release_tag", "application_id", "version_code", "version_name"):
            if not meta.get(key):
                raise ReconciliationError(f"{apk}: missing {key}")
        if (meta["asset"] != apk or meta["release_tag"] != tag or
                meta["asset_kind"] != required_kind):
            raise ReconciliationError(f"{apk}: metadata identity/kind mismatch")
        if not SHA.fullmatch(meta["source_commit"]) or not DIGEST.fullmatch(meta["asset_sha256"]):
            raise ReconciliationError(f"{apk}: malformed provenance or SHA-256")
        if required_package and meta["application_id"] != required_package:
            raise ReconciliationError(f"{apk}: expected package {required_package}")
        if not meta["version_code"].isdigit():
            raise ReconciliationError(f"{apk}: invalid version code")
        checksum_text = checksums_raw.decode("utf-8").strip()
        if checksum_text != f"{meta['asset_sha256']}  {apk}":
            raise ReconciliationError(f"{apk}: SHA256SUMS and metadata disagree")
        remote_hash = by_name[apk].get("digest")
        if remote_hash and remote_hash != "sha256:" + meta["asset_sha256"]:
            raise ReconciliationError(f"{apk}: remote APK hash differs from sidecars")
        commits.add(meta["source_commit"])
        versions.add((meta["version_name"], meta["version_code"]))
        entries.append({
            "name": apk, "label": label, "digest": meta["asset_sha256"],
            "source": meta["source_commit"], "package": meta["application_id"],
            "version_name": meta["version_name"], "version_code": meta["version_code"],
            "apk_url": by_name[apk]["browser_download_url"],
            "metadata_url": by_name[names[1]]["browser_download_url"],
            "checksums_url": by_name[names[2]]["browser_download_url"],
            "hash_checked": bool(remote_hash),
        })
    extra = set(by_name) - allowed
    if extra:
        raise ReconciliationError(f"Unexpected or unpaired assets: {sorted(extra)}")
    if len(commits) != 1:
        raise ReconciliationError("Release APKs do not share one recorded source commit")
    if len(versions) != 1:
        raise ReconciliationError("Release APKs have inconsistent version identity")
    return entries


def render(entries: list[dict], repository: str, tag: str) -> str:
    rows = [
        "## Verified release assets", "",
        "Generated from uploaded APK metadata TXT and SHA-256 sidecars.",
        "Only the APKs actually attached to this release are listed.", "",
        "| Variant | APK | Metadata | SHA-256 | Package |",
        "| --- | --- | --- | --- | --- |",
    ]
    for item in entries:
        rows.append(
            f"| {safe(item['label'])} | [Download]({item['apk_url']}) "
            f"| [TXT]({item['metadata_url']}) "
            f"| [SHA-256]({item['checksums_url']}) "
            f"| \x60{safe(item['package'])}\x60 |"
        )
    rows += ["", "### APK provenance", ""]
    for item in entries:
        rows += [
            f"- **{safe(item['name'])}** — SHA-256 \x60{item['digest']}\x60; "
            f"version \x60{safe(item['version_name'])}\x60 "
            f"(\x60{safe(item['version_code'])}\x60); source "
            f"[\x60{item['source'][:7]}\x60](https://github.com/{repository}/commit/{item['source']})."
        ]
    if any(not item["hash_checked"] for item in entries):
        rows += [
            "",
            "Note: GitHub did not expose the remote APK SHA-256 for every asset. "
            "The TXT/checksum agreement was verified; remote APK digest verification was unavailable."
        ]
    rows += ["", "Release notes and CI verification do not constitute physical TS18 validation."]
    return "\n".join(rows) + "\n"


def anchored(current: str, section: str) -> str:
    begins, ends = current.count(BEGIN), current.count(END)
    if begins == ends == 0:
        # Preserve existing commit-based changelog and static release guidance.
        return current.rstrip() + "\n\n" + BEGIN + "\n\n" + section + "\n" + END + "\n"
    if begins != 1 or ends != 1 or current.index(BEGIN) > current.index(END):
        raise ReconciliationError("Ambiguous or damaged release-note anchors")
    before, after_begin = current.split(BEGIN, 1)
    _, after = after_begin.split(END, 1)
    return before + BEGIN + "\n\n" + section + "\n" + END + after


def reconcile(repository: str, release_id: int, expected_tag: str, expected_sha: str = "") -> bool:
    path = f"repos/{repository}/releases/{release_id}"
    release = api(path)
    if release.get("id") != release_id or release.get("name") != expected_tag:
        raise ReconciliationError("Release ID/version mismatch")
    if release.get("draft") is not True:
        raise ReconciliationError("Published releases are immutable to this action")
    entries = verified_snapshots(repository, release)
    if expected_sha and any(e["source"] != expected_sha for e in entries):
        raise ReconciliationError("Published metadata source SHA does not match release build")
    original = release.get("body") or ""
    desired = anchored(original, render(entries, repository, expected_tag))
    if desired != original:
        # Refuse a race against a release promoted to published status.
        before_patch = api(path)
        if before_patch.get("draft") is not True or before_patch.get("body") != original:
            raise ReconciliationError("Release changed after inspection; refusing stale write")
        try:
            api(path, {"body": desired})
        except (subprocess.CalledProcessError, subprocess.TimeoutExpired):
            # The write may have been committed despite a network failure.
            if api(path).get("body") != desired:
                raise
    final = api(path)
    if (final.get("body") != desired or final.get("id") != release_id or
            final.get("name") != expected_tag or final.get("draft") is not True):
        raise ReconciliationError("Release-body PATCH/read-back or draft identity mismatch")
    # Ensure no concurrent asset replacement invalidated the rendered manifest.
    if sorted(
        (a["id"], a["name"], a.get("digest") or "") for a in final.get("assets", [])
    ) != sorted(
        (a["id"], a["name"], a.get("digest") or "") for a in release.get("assets", [])
    ):
        raise ReconciliationError("Release assets changed during reconciliation")
    print(f"Verified {len(entries)} APK triplet(s) for {expected_tag}; body changed={desired != original}")
    return desired != original


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--release-id", type=int, required=True)
    parser.add_argument("--version-tag", required=True)
    parser.add_argument("--expected-source-sha", default="")
    args = parser.parse_args()
    repository = os.environ["GITHUB_REPOSITORY"]
    if repository != "cbkii/Auxio-TS":
        raise ReconciliationError("Unexpected GitHub repository")
    if not TAG.fullmatch(args.version_tag):
        raise ReconciliationError("Invalid Auxio release version")
    if args.expected_source_sha and not SHA.fullmatch(args.expected_source_sha):
        raise ReconciliationError("Invalid expected source commit")
    reconcile(repository, args.release_id, args.version_tag, args.expected_source_sha)


if __name__ == "__main__":
    try:
        main()
    except (ValueError, KeyError, UnicodeError, OSError, subprocess.SubprocessError) as exc:
        print(f"::error::Auxio release-note reconciliation failed: {exc}", file=sys.stderr)
        sys.exit(1)
