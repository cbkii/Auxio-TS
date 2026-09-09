#!/usr/bin/env python3
"""Deterministic three-variant release asset planning/manifest validation.

This helper is deliberately separate from release-orchestrator.py: version/tag transaction
semantics remain unchanged while the public artefact matrix can enforce variant identity and the
systemless-only exact-com.tw.music boundary.
"""
from __future__ import annotations

import argparse
import json
import re
import sys
from pathlib import Path
from typing import NoReturn, Sequence


class AssetPlanError(RuntimeError):
    pass


def fail(message: str) -> NoReturn:
    raise AssetPlanError(message)


def read_lines(path: Path) -> list[str]:
    if not path.exists():
        return []
    return [line.strip() for line in path.read_text(encoding="utf-8").splitlines() if line.strip()]

VARIANT_NAMES = {
    "standard": "Auxio-TS-{tag}-standard-release.apk",
    "standard_debug": "Auxio-TS-{tag}-standard-debug.apk",
    "topway_twmedia": "Auxio-TS-{tag}-topway-twmedia-release.apk",
    "topway_twmedia_debug": "Auxio-TS-{tag}-topway-twmedia-debug.apk",
    "topway_twmusic_magisk": "Auxio-TS-{tag}-topway-twmusic-magisk.zip",
    "lsposed_bridge": "Auxio-TS-{tag}-lsposed-api100-bridge.apk",
    "lsposed_bridge_debug": "Auxio-TS-{tag}-lsposed-api100-bridge-debug.apk",
}
EXPECTED_IDENTITY = {
    "standard": "org.oxycblt.auxio",
    "standard_debug": "org.oxycblt.auxio.debug",
    "topway_twmedia": "com.tw.media",
    "topway_twmedia_debug": "com.tw.media.debug",
    "topway_twmusic_magisk": "com.tw.music",
    "lsposed_bridge": "org.oxycblt.auxio.ts18bridge",
    "lsposed_bridge_debug": "org.oxycblt.auxio.ts18bridge.debug",
}


def triplet(base: str) -> list[str]:
    return [base, f"{base}.sha256", f"{base}.metadata.txt"]


def command_plan_assets(args: argparse.Namespace) -> None:
    selected = read_lines(Path(args.selected_variants_file))
    if len(selected) != len(set(selected)):
        fail("Selected release variants contain duplicates.")
    unknown = [variant for variant in selected if variant not in VARIANT_NAMES]
    if unknown:
        fail(f"Unknown selected variants: {', '.join(unknown)}")
    # Exact-package replacement may only appear as the Magisk logical variant. There is
    # intentionally no raw topwayTwMusic APK variant in this matrix.
    existing = set(read_lines(Path(args.existing_assets_file)))
    replace = args.replace == "true"
    build_variants: list[str] = []
    upload_names: list[str] = []
    replace_names: list[str] = []
    verify_names: list[str] = []
    debug_workflow_names: list[str] = []

    for variant in selected:
        is_debug = variant.endswith("_debug")
        names = triplet(VARIANT_NAMES[variant].format(tag=args.release_tag))
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
            continue
        elif any(present):
            build_variants.append(variant)
            upload_names.extend(names)
            replace_names.extend(name for name in names if name in existing)
        else:
            build_variants.append(variant)
            upload_names.extend(names)

    result = {
        "build_variants": build_variants,
        "build_asset_names": [VARIANT_NAMES[v].format(tag=args.release_tag) for v in build_variants],
        "needs_signing": any(not v.endswith("_debug") for v in build_variants),
        "upload_names": upload_names,
        "replace_names": replace_names,
        "verify_names": verify_names,
        "debug_workflow_names": debug_workflow_names,
    }
    Path(args.output).write_text(json.dumps(result, indent=2, sort_keys=True) + "\n", encoding="utf-8")


def command_validate_manifest(args: argparse.Namespace) -> None:
    data = json.loads(Path(args.manifest).read_text(encoding="utf-8"))
    if not isinstance(data, list):
        fail("Release manifest must be a JSON array.")
    expected_built = set(read_lines(Path(args.expected_built_names_file)))
    required = {
        "filename", "variant", "asset_kind", "sha256", "application_id", "version_name",
        "version_code", "signer_sha256", "source_commit", "release_tag", "destination",
    }
    names: set[str] = set()
    for entry in data:
        if not isinstance(entry, dict) or not required.issubset(entry):
            fail("Manifest entry is missing publication-critical fields.")
        variant = str(entry["variant"])
        if variant not in EXPECTED_IDENTITY:
            fail(f"Unexpected manifest variant: {variant}")
        filename = str(entry["filename"])
        if filename in names:
            fail(f"Duplicate staged asset name: {filename}")
        names.add(filename)
        expected_filename = VARIANT_NAMES[variant].format(tag=args.release_tag)
        if filename != expected_filename:
            fail(f"{variant} filename mismatch: {filename}")
        if variant == "topway_twmusic_magisk" and not filename.endswith("-magisk.zip"):
            fail("Exact com.tw.music publication is not a Magisk ZIP.")
        if variant != "topway_twmusic_magisk" and "topway-twmusic" in filename.lower():
            fail("Raw topwayTwMusic publication is forbidden.")
        if entry["application_id"] != EXPECTED_IDENTITY[variant]:
            fail(f"{variant} package mismatch: {entry['application_id']}")
        expected_name = f"{args.version_name}-DEBUG" if variant.endswith("_debug") else args.version_name
        if entry["version_name"] != expected_name:
            fail(f"{variant} versionName mismatch: {entry['version_name']}")
        if int(entry["version_code"]) != int(args.version_code):
            fail(f"{variant} versionCode mismatch: {entry['version_code']}")
        if entry["source_commit"] != args.source_commit or entry["release_tag"] != args.release_tag:
            fail(f"{variant} source/tag provenance mismatch.")
        expected_destination = args.debug_destination if variant.endswith("_debug") else "release"
        if entry["destination"] != expected_destination:
            fail(f"{variant} destination mismatch: {entry['destination']}")
        if not re.fullmatch(r"[0-9a-f]{64}", str(entry["sha256"])):
            fail(f"{variant} asset SHA-256 is malformed.")
        if not re.fullmatch(r"[0-9A-F]{64}", str(entry["signer_sha256"])):
            fail(f"{variant} signer SHA-256 is malformed.")

    if names != expected_built:
        fail(f"Manifest asset set does not match plan: expected {sorted(expected_built)}, got {sorted(names)}")


def command_self_test(_: argparse.Namespace) -> None:
    assert "topway_twmusic" not in VARIANT_NAMES
    assert VARIANT_NAMES["topway_twmusic_magisk"].endswith("-magisk.zip")
    assert EXPECTED_IDENTITY["standard"] == "org.oxycblt.auxio"
    assert EXPECTED_IDENTITY["topway_twmedia"] == "com.tw.media"
    assert EXPECTED_IDENTITY["topway_twmusic_magisk"] == "com.tw.music"
    print("release asset matrix self-tests: PASS")


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser()
    sub = parser.add_subparsers(dest="command", required=True)
    plan = sub.add_parser("plan-assets")
    plan.add_argument("--mode", choices=["create_new_release", "repair_existing_release"], required=True)
    plan.add_argument("--release-tag", required=True)
    plan.add_argument("--selected-variants-file", required=True)
    plan.add_argument("--debug-destination", choices=["workflow_artifacts", "release_assets"], required=True)
    plan.add_argument("--existing-assets-file", required=True)
    plan.add_argument("--replace", choices=["true", "false"], required=True)
    plan.add_argument("--output", required=True)
    plan.set_defaults(func=command_plan_assets)
    validate = sub.add_parser("validate-manifest")
    validate.add_argument("--manifest", required=True)
    validate.add_argument("--expected-built-names-file", required=True)
    validate.add_argument("--version-name", required=True)
    validate.add_argument("--version-code", required=True)
    validate.add_argument("--release-tag", required=True)
    validate.add_argument("--source-commit", required=True)
    validate.add_argument("--debug-destination", choices=["workflow_artifacts", "release_assets"], required=True)
    validate.set_defaults(func=command_validate_manifest)
    self_test = sub.add_parser("self-test")
    self_test.set_defaults(func=command_self_test)
    return parser


def main(argv: Sequence[str] | None = None) -> int:
    args = build_parser().parse_args(argv)
    try:
        args.func(args)
    except (AssetPlanError, OSError, json.JSONDecodeError, ValueError) as exc:
        print(f"::error::{exc}", file=sys.stderr)
        return 1
    return 0

if __name__ == "__main__":
    raise SystemExit(main())
