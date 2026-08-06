#!/usr/bin/env python3
"""Deterministic planning and validation for Auxio-TS Manual Release.

This helper is intentionally standard-library only so the workflow can validate the
release state before Gradle or signing setup. It never mutates GitHub state.
"""

from __future__ import annotations

import argparse
import json
import re
import sys
from dataclasses import dataclass
from pathlib import Path
from typing import Iterable, NoReturn, Sequence

SEMVER_RE = re.compile(r"^v?(0|[1-9][0-9]*)\.(0|[1-9][0-9]*)\.(0|[1-9][0-9]*)$")
VERSION_NAME_RE = re.compile(r'^(?P<indent>\s*)versionName\s+"(?P<value>[^"]+)"\s*$', re.MULTILINE)
VERSION_CODE_RE = re.compile(r"^(?P<indent>\s*)versionCode\s+(?P<value>[0-9]+)\s*$", re.MULTILINE)


class ReleasePlanError(RuntimeError):
    pass


@dataclass(frozen=True, order=True)
class SemVer:
    major: int
    minor: int
    patch: int

    @classmethod
    def parse(cls, value: str) -> SemVer | None:
        match = SEMVER_RE.fullmatch(value.strip())
        if not match:
            return None
        return cls(*(int(part) for part in match.groups()))

    @property
    def name(self) -> str:
        return f"{self.major}.{self.minor}.{self.patch}"

    @property
    def tag(self) -> str:
        return f"v{self.name}"

    @property
    def version_code(self) -> int:
        # Repository authority: major * 1,000,000 + minor * 10,000 + patch * 100.
        return self.major * 1_000_000 + self.minor * 10_000 + self.patch * 100

    def next_patch(self) -> SemVer:
        return SemVer(self.major, self.minor, self.patch + 1)


def fail(message: str) -> NoReturn:
    raise ReleasePlanError(message)


def read_lines(path: Path) -> list[str]:
    if not path.exists():
        return []
    return [
        line.strip()
        for line in path.read_text(encoding="utf-8").splitlines()
        if line.strip()
    ]


def parse_versions(values: Iterable[str]) -> set[SemVer]:
    parsed: set[SemVer] = set()
    for value in values:
        version = SemVer.parse(value)
        if version is not None:
            parsed.add(version)
    return parsed


def read_gradle_metadata(path: Path) -> tuple[SemVer, int]:
    text = path.read_text(encoding="utf-8")
    name_matches = list(VERSION_NAME_RE.finditer(text))
    code_matches = list(VERSION_CODE_RE.finditer(text))
    if len(name_matches) != 1 or len(code_matches) != 1:
        fail(
            f"Expected exactly one literal versionName and versionCode in {path}; "
            f"found {len(name_matches)} and {len(code_matches)}."
        )
    version = SemVer.parse(name_matches[0].group("value"))
    if version is None:
        fail(
            "Source versionName is not strict major.minor.patch: "
            f"{name_matches[0].group('value')}"
        )
    return version, int(code_matches[0].group("value"))


def load_target_release(path: Path) -> dict[str, object]:
    if not path.exists() or not path.read_text(encoding="utf-8").strip():
        return {}
    data = json.loads(path.read_text(encoding="utf-8"))
    if not isinstance(data, dict):
        fail("Target release response must be a JSON object.")
    return data


def highest_below(versions: Iterable[SemVer], target: SemVer) -> SemVer | None:
    eligible = [version for version in versions if version < target]
    return max(eligible) if eligible else None


@dataclass(frozen=True)
class VersionResolution:
    target: SemVer
    effective_mode: str
    reason: str


def validate_version_authority(
    git_versions: set[SemVer],
    release_versions: set[SemVer],
    draft_versions: set[SemVer],
) -> None:
    """Reject only ambiguous or impossible external authority states."""

    release_without_tag = release_versions - git_versions
    if release_without_tag:
        orphan = max(release_without_tag)
        fail(
            f"GitHub Release {orphan.tag} exists without a resolvable Git tag. "
            "STOP for human repair."
        )

    draft_without_release = draft_versions - release_versions
    if draft_without_release:
        orphan = max(draft_without_release)
        fail(
            f"Draft release index contains {orphan.tag} without a GitHub Release. "
            "STOP for human repair."
        )


def choose_version_resolution(
    *,
    source_version: SemVer,
    git_versions: set[SemVer],
    release_versions: set[SemVer],
    draft_versions: set[SemVer],
    requested_mode: str,
    requested_version: SemVer | None,
) -> VersionResolution:
    """Choose an idempotent create or repair action from current release authority."""

    validate_version_authority(git_versions, release_versions, draft_versions)
    external_versions = git_versions | release_versions
    highest_external = max(external_versions) if external_versions else None

    if requested_mode == "repair_existing_release":
        if requested_version is None:
            fail("repair_existing_release requires an explicit version_tag.")
        if requested_version not in git_versions:
            fail(
                f"Repair requires existing immutable tag {requested_version.tag}; "
                "no matching Git tag was found."
            )
        return VersionResolution(
            requested_version,
            "repair_existing_release",
            "explicit_repair",
        )

    if requested_mode not in {"auto", "create_new_release"}:
        fail(f"Unsupported release mode: {requested_mode}")

    if requested_version is not None:
        if requested_version in git_versions:
            return VersionResolution(
                requested_version,
                "repair_existing_release",
                "explicit_existing_tag",
            )
        if highest_external is not None and requested_version <= highest_external:
            fail(
                f"Explicit new release {requested_version.tag} must be newer than existing "
                f"release/tag authority {highest_external.tag}."
            )
        return VersionResolution(
            requested_version,
            "create_new_release",
            "explicit_new_tag",
        )

    if highest_external is not None:
        if highest_external in git_versions and highest_external not in release_versions:
            return VersionResolution(
                highest_external,
                "repair_existing_release",
                "resume_latest_tag_without_release",
            )
        if highest_external in draft_versions:
            return VersionResolution(
                highest_external,
                "repair_existing_release",
                "resume_latest_draft_release",
            )

    if highest_external is None or source_version > highest_external:
        target = source_version
        reason = "reuse_source_version"
    else:
        target = max(source_version, highest_external).next_patch()
        reason = "increment_latest_complete_version"

    return VersionResolution(target, "create_new_release", reason)


def command_resolve(args: argparse.Namespace) -> None:
    source_version, source_code = read_gradle_metadata(Path(args.source_gradle))
    git_versions = parse_versions(read_lines(Path(args.git_tags_file)))
    release_versions = parse_versions(read_lines(Path(args.release_tags_file)))
    draft_versions = (
        parse_versions(read_lines(Path(args.draft_release_tags_file)))
        if args.draft_release_tags_file
        else set()
    )
    target_release = load_target_release(Path(args.target_release_json))

    requested = args.input_tag.strip()
    requested_version = SemVer.parse(requested) if requested else None
    if requested and requested_version is None:
        fail(
            f"Invalid version tag {requested!r}; expected "
            "vMAJOR.MINOR.PATCH or MAJOR.MINOR.PATCH."
        )

    resolution = choose_version_resolution(
        source_version=source_version,
        git_versions=git_versions,
        release_versions=release_versions,
        draft_versions=draft_versions,
        requested_mode=args.mode,
        requested_version=requested_version,
    )
    target = resolution.target
    effective_mode = resolution.effective_mode

    tag_exists = target in git_versions
    release_exists = target in release_versions or bool(target_release.get("id"))
    if release_exists and not tag_exists:
        fail(
            f"GitHub Release {target.tag} exists without a resolvable Git tag. "
            "STOP for human repair."
        )
    if effective_mode == "create_new_release" and (tag_exists or release_exists):
        fail(
            f"New release target {target.tag} unexpectedly already exists. "
            "Re-run so it can be resolved as an idempotent repair."
        )
    if effective_mode == "repair_existing_release" and not tag_exists:
        fail(f"Repair target {target.tag} has no immutable Git tag.")

    external_versions = git_versions | release_versions
    highest_external = max(external_versions) if external_versions else None
    if (
        effective_mode == "create_new_release"
        and highest_external is not None
        and target <= highest_external
    ):
        fail(
            f"New release {target.tag} must be newer than existing release/tag "
            f"authority {highest_external.tag}."
        )

    previous = highest_below(git_versions, target)
    target_code = target.version_code
    metadata_change_required = source_version != target or source_code != target_code
    if effective_mode == "create_new_release" and target_code < source_code:
        fail(
            f"Calculated versionCode {target_code} would regress below source "
            f"versionCode {source_code}."
        )
    if (
        effective_mode == "create_new_release"
        and target_code == source_code
        and source_version != target
    ):
        fail(
            f"Calculated versionCode {target_code} collides with source version "
            f"{source_version.name}."
        )

    unresolved_tag_only = sorted(git_versions - release_versions)
    apply_requested_status = (
        target not in release_versions or target in draft_versions
    )
    result = {
        "mode": effective_mode,
        "requested_mode": args.mode,
        "effective_mode": effective_mode,
        "resolution_reason": resolution.reason,
        "apply_requested_status": apply_requested_status,
        "release_tag": target.tag,
        "release_version_name": target.name,
        "release_version_code": target_code,
        "source_version_name": source_version.name,
        "source_version_code": source_code,
        "metadata_change_required": metadata_change_required,
        "tag_exists": tag_exists,
        "release_exists": release_exists,
        "previous_tag": previous.tag if previous else "",
        "highest_git_tag": max(git_versions).tag if git_versions else "",
        "highest_release_tag": max(release_versions).tag if release_versions else "",
        "unresolved_tag_only_versions": [version.tag for version in unresolved_tag_only],
        "target_release_draft": bool(target_release.get("draft", False)),
        "target_release_prerelease": bool(target_release.get("prerelease", False)),
        "target_release_url": str(target_release.get("html_url", "")),
    }
    Path(args.output).write_text(
        json.dumps(result, indent=2, sort_keys=True) + "\n",
        encoding="utf-8",
    )


def command_inspect_gradle(args: argparse.Namespace) -> None:
    version, code = read_gradle_metadata(Path(args.gradle))
    if args.expected_version and version.name != args.expected_version:
        fail(
            f"Tagged source versionName {version.name} does not match requested "
            f"{args.expected_version}."
        )
    if code != version.version_code:
        fail(
            f"Tagged source versionCode {code} does not match repository formula "
            f"{version.version_code} for {version.name}."
        )
    result = {"version_name": version.name, "version_code": code}
    Path(args.output).write_text(
        json.dumps(result, sort_keys=True) + "\n",
        encoding="utf-8",
    )


def command_update_gradle(args: argparse.Namespace) -> None:
    path = Path(args.gradle)
    version = SemVer.parse(args.version_name)
    if version is None or version.name != args.version_name:
        fail("--version-name must be strict MAJOR.MINOR.PATCH without a v prefix.")
    if args.version_code != version.version_code:
        fail(
            f"Version code {args.version_code} does not match repository formula "
            f"{version.version_code}."
        )
    text = path.read_text(encoding="utf-8")
    if (
        len(list(VERSION_NAME_RE.finditer(text))) != 1
        or len(list(VERSION_CODE_RE.finditer(text))) != 1
    ):
        fail("Refusing metadata update because version literals are not unique.")
    text = VERSION_NAME_RE.sub(
        lambda match: f'{match.group("indent")}versionName "{version.name}"',
        text,
        count=1,
    )
    text = VERSION_CODE_RE.sub(
        lambda match: f'{match.group("indent")}versionCode {args.version_code}',
        text,
        count=1,
    )
    path.write_text(text, encoding="utf-8")
    updated_version, updated_code = read_gradle_metadata(path)
    if updated_version != version or updated_code != args.version_code:
        fail("Version metadata update did not round-trip correctly.")


VARIANT_NAMES = {
    "topway_twmedia": "Auxio-TS-{tag}-topway-twmedia-release.apk",
    "topway_twmedia_debug": "Auxio-TS-{tag}-topway-twmedia-debug.apk",
    "lsposed_bridge": "Auxio-TS-{tag}-lsposed-api100-bridge.apk",
    "lsposed_bridge_debug": "Auxio-TS-{tag}-lsposed-api100-bridge-debug.apk",
}


def triplet(base: str) -> list[str]:
    return [base, f"{base}.sha256", f"{base}.metadata.txt"]


def command_plan_assets(args: argparse.Namespace) -> None:
    selected = read_lines(Path(args.selected_variants_file))
    unknown = [variant for variant in selected if variant not in VARIANT_NAMES]
    if unknown:
        fail(f"Unknown selected variants: {', '.join(unknown)}")
    existing = set(read_lines(Path(args.existing_assets_file)))
    replace = args.replace == "true"

    build_variants: list[str] = []
    upload_names: list[str] = []
    replace_names: list[str] = []
    verify_names: list[str] = []
    debug_workflow_names: list[str] = []

    for variant in selected:
        is_debug = variant.endswith("_debug")
        base = VARIANT_NAMES[variant].format(tag=args.release_tag)
        names = triplet(base)
        publish = not is_debug or args.debug_destination == "release_assets"

        if not publish:
            build_variants.append(variant)
            debug_workflow_names.extend(names)
            continue

        verify_names.extend(names)
        present = [name in existing for name in names]
        if args.mode == "create_new_release" or not existing:
            build_variants.append(variant)
            upload_names.extend(names)
        elif replace:
            build_variants.append(variant)
            upload_names.extend(names)
            replace_names.extend(name for name in names if name in existing)
        elif all(present):
            # Complete existing triplet: no rebuild or duplicate upload needed.
            continue
        elif any(present):
            # Interrupted uploads are expected resumable state. Rebuild the
            # complete triplet and replace only the pieces already present.
            build_variants.append(variant)
            upload_names.extend(names)
            replace_names.extend(name for name in names if name in existing)
        else:
            build_variants.append(variant)
            upload_names.extend(names)

    build_apk_names = [
        VARIANT_NAMES[variant].format(tag=args.release_tag)
        for variant in build_variants
    ]
    result = {
        "build_variants": build_variants,
        "build_apk_names": build_apk_names,
        "needs_signing": any(
            not variant.endswith("_debug") for variant in build_variants
        ),
        "upload_names": upload_names,
        "replace_names": replace_names,
        "verify_names": verify_names,
        "debug_workflow_names": debug_workflow_names,
    }
    Path(args.output).write_text(
        json.dumps(result, indent=2, sort_keys=True) + "\n",
        encoding="utf-8",
    )


def command_validate_manifest(args: argparse.Namespace) -> None:
    manifest_path = Path(args.manifest)
    data = json.loads(manifest_path.read_text(encoding="utf-8"))
    if not isinstance(data, list):
        fail("Release manifest must be a JSON array.")
    names: set[str] = set()
    expected_version_code = int(args.version_code)
    required_fields = {
        "filename",
        "variant",
        "asset_kind",
        "sha256",
        "application_id",
        "version_name",
        "version_code",
        "signer_sha256",
        "source_commit",
        "release_tag",
        "destination",
    }
    expected_identity = {
        "topway_twmedia": ("com.tw.media", args.version_name, "release"),
        "topway_twmedia_debug": (
            "com.tw.media.debug",
            f"{args.version_name}-DEBUG",
            args.debug_destination,
        ),
        "lsposed_bridge": (
            "org.oxycblt.auxio.ts18bridge",
            args.version_name,
            "release",
        ),
        "lsposed_bridge_debug": (
            "org.oxycblt.auxio.ts18bridge.debug",
            f"{args.version_name}-DEBUG",
            args.debug_destination,
        ),
    }
    for entry in data:
        if not isinstance(entry, dict) or not required_fields.issubset(entry):
            fail("Manifest entry is missing publication-critical fields.")
        name = str(entry["filename"])
        if name in names:
            fail(f"Duplicate staged asset name: {name}")
        names.add(name)
        variant = str(entry["variant"])
        if variant not in expected_identity:
            fail(f"Unexpected manifest variant: {variant}")
        expected_package, expected_name, expected_destination = expected_identity[variant]
        if entry["application_id"] != expected_package:
            fail(f"{variant} package mismatch: {entry['application_id']}")
        if entry["version_name"] != expected_name:
            fail(f"{variant} versionName mismatch: {entry['version_name']}")
        if int(entry["version_code"]) != expected_version_code:
            fail(f"{variant} versionCode mismatch: {entry['version_code']}")
        if (
            entry["source_commit"] != args.source_commit
            or entry["release_tag"] != args.release_tag
        ):
            fail(f"{variant} source/tag provenance mismatch.")
        if entry["destination"] != expected_destination:
            fail(f"{variant} destination mismatch: {entry['destination']}")
        if not re.fullmatch(r"[0-9a-f]{64}", str(entry["sha256"])):
            fail(f"{variant} APK SHA-256 is malformed.")
        if not re.fullmatch(r"[0-9A-F]{64}", str(entry["signer_sha256"])):
            fail(f"{variant} signer SHA-256 is malformed.")

    expected_built = set(read_lines(Path(args.expected_built_names_file)))
    actual_apks = {entry["filename"] for entry in data}
    if actual_apks != expected_built:
        fail(
            "Manifest APK set does not match the asset plan: "
            f"expected {sorted(expected_built)}, got {sorted(actual_apks)}"
        )


def command_self_test(_: argparse.Namespace) -> None:
    assert SemVer.parse("v6.10.0") > SemVer.parse("6.9.9")  # type: ignore[operator]
    assert SemVer.parse("6.4.8").version_code == 6_040_800  # type: ignore[union-attr]
    assert SemVer.parse("v01.2.3") is None
    assert SemVer.parse("v6.4") is None
    versions = parse_versions(["v6.4.7", "6.4.8", "bad", "v6.5.0"])
    assert max(versions).tag == "v6.5.0"

    v647 = SemVer(6, 4, 7)
    v648 = SemVer(6, 4, 8)
    v650 = SemVer(6, 5, 0)
    v655 = SemVer(6, 5, 5)

    resumed_tag = choose_version_resolution(
        source_version=v647,
        git_versions={v647, v650},
        release_versions={v647},
        draft_versions=set(),
        requested_mode="auto",
        requested_version=None,
    )
    assert resumed_tag == VersionResolution(
        v650,
        "repair_existing_release",
        "resume_latest_tag_without_release",
    )

    explicit_new = choose_version_resolution(
        source_version=v647,
        git_versions={v647, v650},
        release_versions={v647},
        draft_versions=set(),
        requested_mode="auto",
        requested_version=v655,
    )
    assert explicit_new == VersionResolution(
        v655,
        "create_new_release",
        "explicit_new_tag",
    )

    explicit_existing = choose_version_resolution(
        source_version=v647,
        git_versions={v647, v650},
        release_versions={v647},
        draft_versions=set(),
        requested_mode="auto",
        requested_version=v650,
    )
    assert explicit_existing == VersionResolution(
        v650,
        "repair_existing_release",
        "explicit_existing_tag",
    )

    resumed_draft = choose_version_resolution(
        source_version=v647,
        git_versions={v647, v650},
        release_versions={v647, v650},
        draft_versions={v650},
        requested_mode="auto",
        requested_version=None,
    )
    assert resumed_draft == VersionResolution(
        v650,
        "repair_existing_release",
        "resume_latest_draft_release",
    )

    incremented = choose_version_resolution(
        source_version=v647,
        git_versions={v647, v650},
        release_versions={v647, v650},
        draft_versions=set(),
        requested_mode="auto",
        requested_version=None,
    )
    assert incremented.target == SemVer(6, 5, 1)
    assert incremented.effective_mode == "create_new_release"

    explicit_repair = choose_version_resolution(
        source_version=v647,
        git_versions={v647, v648},
        release_versions={v647},
        draft_versions=set(),
        requested_mode="repair_existing_release",
        requested_version=v648,
    )
    assert explicit_repair.effective_mode == "repair_existing_release"

    try:
        validate_version_authority({v647}, {v647, v648}, set())
    except ReleasePlanError:
        pass
    else:
        raise AssertionError("release-without-tag authority was accepted")

    print("release-orchestrator self-tests: PASS")


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser()
    subparsers = parser.add_subparsers(dest="command", required=True)

    resolve = subparsers.add_parser("resolve")
    resolve.add_argument(
        "--mode",
        choices=["auto", "create_new_release", "repair_existing_release"],
        required=True,
    )
    resolve.add_argument("--input-tag", default="")
    resolve.add_argument("--source-gradle", required=True)
    resolve.add_argument("--git-tags-file", required=True)
    resolve.add_argument("--release-tags-file", required=True)
    resolve.add_argument("--draft-release-tags-file", default="")
    resolve.add_argument("--target-release-json", required=True)
    resolve.add_argument("--output", required=True)
    resolve.set_defaults(func=command_resolve)

    inspect_gradle = subparsers.add_parser("inspect-gradle")
    inspect_gradle.add_argument("--gradle", required=True)
    inspect_gradle.add_argument("--expected-version", default="")
    inspect_gradle.add_argument("--output", required=True)
    inspect_gradle.set_defaults(func=command_inspect_gradle)

    update_gradle = subparsers.add_parser("update-gradle")
    update_gradle.add_argument("--gradle", required=True)
    update_gradle.add_argument("--version-name", required=True)
    update_gradle.add_argument("--version-code", type=int, required=True)
    update_gradle.set_defaults(func=command_update_gradle)

    plan_assets = subparsers.add_parser("plan-assets")
    plan_assets.add_argument(
        "--mode",
        choices=["create_new_release", "repair_existing_release"],
        required=True,
    )
    plan_assets.add_argument("--release-tag", required=True)
    plan_assets.add_argument("--selected-variants-file", required=True)
    plan_assets.add_argument(
        "--debug-destination",
        choices=["workflow_artifacts", "release_assets"],
        required=True,
    )
    plan_assets.add_argument("--existing-assets-file", required=True)
    plan_assets.add_argument("--replace", choices=["true", "false"], required=True)
    plan_assets.add_argument("--output", required=True)
    plan_assets.set_defaults(func=command_plan_assets)

    validate_manifest = subparsers.add_parser("validate-manifest")
    validate_manifest.add_argument("--manifest", required=True)
    validate_manifest.add_argument("--expected-built-names-file", required=True)
    validate_manifest.add_argument("--version-name", required=True)
    validate_manifest.add_argument("--version-code", required=True)
    validate_manifest.add_argument("--release-tag", required=True)
    validate_manifest.add_argument("--source-commit", required=True)
    validate_manifest.add_argument(
        "--debug-destination",
        choices=["workflow_artifacts", "release_assets"],
        required=True,
    )
    validate_manifest.set_defaults(func=command_validate_manifest)

    self_test = subparsers.add_parser("self-test")
    self_test.set_defaults(func=command_self_test)
    return parser


def main(argv: Sequence[str] | None = None) -> int:
    parser = build_parser()
    args = parser.parse_args(argv)
    try:
        args.func(args)
    except (ReleasePlanError, OSError, json.JSONDecodeError, ValueError) as exc:
        print(f"::error::{exc}", file=sys.stderr)
        return 1
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
