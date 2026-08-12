#!/usr/bin/env python3
"""Deterministic, network-free Manual Release planner/dispatch contract matrix."""
from __future__ import annotations

import argparse
import importlib.util
import json
import os
import subprocess
import sys
import tempfile
from pathlib import Path
from types import ModuleType

REPO = Path.cwd()
ORCHESTRATOR = REPO / "scripts" / "release-orchestrator.py"
SELECT_SCRIPT = REPO / "scripts" / "manual-release" / "03-select-maintained-release-assets.sh"


class TestFailure(RuntimeError):
    pass


def check(condition: bool, message: str) -> None:
    if not condition:
        raise TestFailure(message)


def load_orchestrator() -> ModuleType:
    spec = importlib.util.spec_from_file_location("release_orchestrator", ORCHESTRATOR)
    if spec is None or spec.loader is None:
        raise TestFailure(f"Unable to load {ORCHESTRATOR}")
    module = importlib.util.module_from_spec(spec)
    sys.modules[spec.name] = module
    spec.loader.exec_module(module)
    return module


def expect_release_error(module: ModuleType, description: str, function, *args, **kwargs) -> None:
    try:
        function(*args, **kwargs)
    except module.ReleasePlanError:
        return
    raise TestFailure(f"Expected ReleasePlanError: {description}")


def write_lines(path: Path, values: list[str]) -> None:
    path.write_text("".join(f"{value}\n" for value in values), encoding="utf-8")


def run_asset_plan(module: ModuleType, root: Path, *, mode: str, tag: str, variants: list[str], debug_destination: str, existing: list[str], replace: str) -> dict:
    variants_file = root / "variants.txt"
    existing_file = root / "existing.txt"
    output = root / "asset-plan.json"
    write_lines(variants_file, variants)
    write_lines(existing_file, existing)
    args = argparse.Namespace(
        mode=mode,
        release_tag=tag,
        selected_variants_file=str(variants_file),
        debug_destination=debug_destination,
        existing_assets_file=str(existing_file),
        replace=replace,
        output=str(output),
    )
    module.command_plan_assets(args)
    return json.loads(output.read_text(encoding="utf-8"))


def variant_triplet(module: ModuleType, variant: str, tag: str) -> list[str]:
    return module.triplet(module.VARIANT_NAMES[variant].format(tag=tag))


def test_version_resolution(module: ModuleType) -> None:
    S = module.SemVer
    V = module.VersionResolution
    v647, v648, v650, v651, v655 = S(6, 4, 7), S(6, 4, 8), S(6, 5, 0), S(6, 5, 1), S(6, 5, 5)

    cases = [
        (
            "fresh repository reuses source version",
            dict(source_version=v647, git_versions=set(), release_versions=set(), draft_versions=set(), requested_mode="auto", requested_version=None),
            V(v647, "create_new_release", "reuse_source_version"),
        ),
        (
            "complete latest increments patch",
            dict(source_version=v647, git_versions={v647, v650}, release_versions={v647, v650}, draft_versions=set(), requested_mode="auto", requested_version=None),
            V(v651, "create_new_release", "increment_latest_complete_version"),
        ),
        (
            "latest tag-only resumes",
            dict(source_version=v647, git_versions={v647, v650}, release_versions={v647}, draft_versions=set(), requested_mode="auto", requested_version=None),
            V(v650, "repair_existing_release", "resume_latest_tag_without_release"),
        ),
        (
            "latest draft resumes",
            dict(source_version=v647, git_versions={v647, v650}, release_versions={v647, v650}, draft_versions={v650}, requested_mode="auto", requested_version=None),
            V(v650, "repair_existing_release", "resume_latest_draft_release"),
        ),
        (
            "explicit newer version outranks older interrupted tag",
            dict(source_version=v647, git_versions={v647, v650}, release_versions={v647}, draft_versions=set(), requested_mode="auto", requested_version=v655),
            V(v655, "create_new_release", "explicit_new_tag"),
        ),
        (
            "explicit existing tag repairs",
            dict(source_version=v647, git_versions={v647, v650}, release_versions={v647}, draft_versions=set(), requested_mode="auto", requested_version=v650),
            V(v650, "repair_existing_release", "explicit_existing_tag"),
        ),
        (
            "explicit repair mode keeps immutable tag",
            dict(source_version=v647, git_versions={v647, v648}, release_versions={v647}, draft_versions=set(), requested_mode="repair_existing_release", requested_version=v648),
            V(v648, "repair_existing_release", "explicit_repair"),
        ),
    ]
    for name, kwargs, expected in cases:
        actual = module.choose_version_resolution(**kwargs)
        check(actual == expected, f"{name}: expected {expected}, got {actual}")

    expect_release_error(
        module,
        "explicit regression",
        module.choose_version_resolution,
        source_version=v647,
        git_versions={v647, v650},
        release_versions={v647, v650},
        draft_versions=set(),
        requested_mode="auto",
        requested_version=v648,
    )
    expect_release_error(
        module,
        "release without immutable tag",
        module.validate_version_authority,
        {v647},
        {v647, v650},
        set(),
    )
    expect_release_error(
        module,
        "draft missing release index",
        module.validate_version_authority,
        {v647, v650},
        {v647},
        {v650},
    )
    expect_release_error(
        module,
        "repair without tag",
        module.choose_version_resolution,
        source_version=v647,
        git_versions={v647},
        release_versions={v647},
        draft_versions=set(),
        requested_mode="repair_existing_release",
        requested_version=v650,
    )
    expect_release_error(
        module,
        "unsupported mode",
        module.choose_version_resolution,
        source_version=v647,
        git_versions={v647},
        release_versions={v647},
        draft_versions=set(),
        requested_mode="surprise",
        requested_version=None,
    )


def test_asset_planning(module: ModuleType, root: Path) -> None:
    tag = "v6.6.0"
    topway = variant_triplet(module, "app", tag)
    topway_debug = variant_triplet(module, "app_debug", tag)
    bridge = variant_triplet(module, "lsposed_bridge", tag)
    bridge_debug = variant_triplet(module, "lsposed_bridge_debug", tag)

    plan = run_asset_plan(
        module,
        root,
        mode="create_new_release",
        tag=tag,
        variants=["app", "app_debug"],
        debug_destination="workflow_artifacts",
        existing=[],
        replace="false",
    )
    check(plan["build_variants"] == ["app", "app_debug"], "default Topway selection did not build release+debug")
    check(plan["upload_names"] == topway, "workflow-only debug files leaked into release upload plan")
    check(plan["debug_workflow_names"] == topway_debug, "workflow debug triplet was not retained")
    check(plan["verify_names"] == topway, "release verification set is wrong for default Topway selection")
    check(plan["needs_signing"] is True, "release APK did not require signing")

    plan = run_asset_plan(
        module,
        root,
        mode="create_new_release",
        tag=tag,
        variants=["app", "app_debug", "lsposed_bridge", "lsposed_bridge_debug"],
        debug_destination="release_assets",
        existing=[],
        replace="false",
    )
    expected_all = topway + topway_debug + bridge + bridge_debug
    check(plan["upload_names"] == expected_all, "explicit debug publication did not upload every selected triplet")
    check(plan["verify_names"] == expected_all, "explicit debug publication verification set differs from upload set")
    check(plan["debug_workflow_names"] == [], "release debug assets were also planned as workflow artifacts")

    complete = topway.copy()
    plan = run_asset_plan(
        module,
        root,
        mode="repair_existing_release",
        tag=tag,
        variants=["app"],
        debug_destination="workflow_artifacts",
        existing=complete,
        replace="false",
    )
    check(plan["build_variants"] == [] and plan["upload_names"] == [], "complete repair triplet was rebuilt unnecessarily")
    check(plan["verify_names"] == topway, "complete repaired triplet was not verified")

    partial = topway[:2]
    plan = run_asset_plan(
        module,
        root,
        mode="repair_existing_release",
        tag=tag,
        variants=["app"],
        debug_destination="workflow_artifacts",
        existing=partial,
        replace="false",
    )
    check(plan["build_variants"] == ["app"], "partial interrupted triplet was not rebuilt")
    check(plan["upload_names"] == topway, "partial repair did not re-stage the full triplet")
    check(plan["replace_names"] == partial, "partial repair did not replace exactly the already-present files")

    plan = run_asset_plan(
        module,
        root,
        mode="repair_existing_release",
        tag=tag,
        variants=["app"],
        debug_destination="workflow_artifacts",
        existing=complete,
        replace="true",
    )
    check(plan["build_variants"] == ["app"], "explicit replacement did not rebuild a complete triplet")
    check(plan["replace_names"] == complete, "explicit replacement did not authorise all existing triplet files")

    unknown_file = root / "unknown-variants.txt"
    existing_file = root / "unknown-existing.txt"
    output = root / "unknown-plan.json"
    write_lines(unknown_file, ["unknown_variant"])
    write_lines(existing_file, [])
    expect_release_error(
        module,
        "unknown selected variant",
        module.command_plan_assets,
        argparse.Namespace(
            mode="create_new_release",
            release_tag=tag,
            selected_variants_file=str(unknown_file),
            debug_destination="workflow_artifacts",
            existing_assets_file=str(existing_file),
            replace="false",
            output=str(output),
        ),
    )


def required_output(values: str, key: str) -> str:
    prefix = f"{key}="
    value = next(
        (line.split("=", 1)[1] for line in values.splitlines() if line.startswith(prefix)),
        "",
    )
    if not value:
        raise TestFailure(f"selection output omitted required key: {key}")
    return value


def run_selection(root: Path, *, topway: str, bridge: str, debug: str, should_pass: bool) -> tuple[str, list[str]]:
    output = root / f"selection-{topway}-{bridge}-{debug}.out"
    output.write_text("", encoding="utf-8")
    env = os.environ.copy()
    env.update(
        {
            "RUNNER_TEMP": str(root),
            "GITHUB_OUTPUT": str(output),
            "INCLUDE_APP": topway,
            "INCLUDE_LSPOSED_BRIDGE": bridge,
            "PUBLISH_DEBUG_APKS": debug,
        }
    )
    completed = subprocess.run(
        ["bash", str(SELECT_SCRIPT)],
        cwd=REPO,
        env=env,
        text=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        timeout=15,
        check=False,
    )
    if should_pass and completed.returncode != 0:
        raise TestFailure(f"selection unexpectedly failed: {completed.stderr.strip()}")
    if not should_pass and completed.returncode == 0:
        raise TestFailure("selection unexpectedly accepted invalid inputs")
    if not should_pass:
        return "", []
    values = output.read_text(encoding="utf-8")
    destination = required_output(values, "debug_destination")
    selected_path = required_output(values, "selected_file")
    selected_file = Path(selected_path)
    if not selected_file.is_file():
        raise TestFailure(f"selection output points to a missing selected_file: {selected_path}")
    selected = selected_file.read_text(encoding="utf-8").splitlines()
    return destination, selected


def test_dispatch_selection(root: Path) -> None:
    destination, selected = run_selection(root, topway="true", bridge="false", debug="false", should_pass=True)
    check(destination == "workflow_artifacts", "debug-off selection did not stay in workflow artifacts")
    check(selected == ["app", "app_debug"], "default primary selection is not deterministic")

    destination, selected = run_selection(root, topway="false", bridge="true", debug="true", should_pass=True)
    check(destination == "release_assets", "debug-on selection did not resolve to release assets")
    check(selected == ["lsposed_bridge", "lsposed_bridge_debug"], "bridge-only selection is not deterministic")

    destination, selected = run_selection(root, topway="true", bridge="true", debug="false", should_pass=True)
    check(destination == "workflow_artifacts", "combined selection changed debug destination")
    check(
        selected == ["app", "app_debug", "lsposed_bridge", "lsposed_bridge_debug"],
        "combined selection order changed",
    )

    run_selection(root, topway="false", bridge="false", debug="false", should_pass=False)
    run_selection(root, topway="true", bridge="false", debug="maybe", should_pass=False)


def build_manifest_entry(module: ModuleType, variant: str, tag: str, version_name: str, version_code: int, source: str, destination: str) -> dict:
    filename = module.VARIANT_NAMES[variant].format(tag=tag)
    package = {
        "app": "com.tw.media",
        "app_debug": "com.tw.media.debug",
        "lsposed_bridge": "org.oxycblt.auxio.ts18bridge",
        "lsposed_bridge_debug": "org.oxycblt.auxio.ts18bridge.debug",
    }[variant]
    actual_name = f"{version_name}-DEBUG" if variant.endswith("_debug") else version_name
    return {
        "filename": filename,
        "variant": variant,
        "asset_kind": "test",
        "sha256": "a" * 64,
        "application_id": package,
        "version_name": actual_name,
        "version_code": version_code,
        "signer_sha256": "B" * 64,
        "source_commit": source,
        "release_tag": tag,
        "destination": destination,
    }


def validate_manifest(module: ModuleType, root: Path, entries: list[dict], expected_names: list[str], *, tag: str = "v6.6.0", version_name: str = "6.6.0", version_code: int = 6060000, source: str = "1" * 40, debug_destination: str = "workflow_artifacts") -> None:
    manifest = root / "manifest.json"
    expected = root / "expected-names.txt"
    manifest.write_text(json.dumps(entries), encoding="utf-8")
    write_lines(expected, expected_names)
    module.command_validate_manifest(
        argparse.Namespace(
            manifest=str(manifest),
            expected_built_names_file=str(expected),
            version_name=version_name,
            version_code=str(version_code),
            source_commit=source,
            release_tag=tag,
            debug_destination=debug_destination,
        )
    )


def test_manifest_validation(module: ModuleType, root: Path) -> None:
    tag = "v6.6.0"
    version = "6.6.0"
    code = 6060000
    source = "1" * 40
    variants = [
        ("app", "release"),
        ("app_debug", "workflow_artifacts"),
        ("lsposed_bridge", "release"),
        ("lsposed_bridge_debug", "workflow_artifacts"),
    ]
    entries = [
        build_manifest_entry(module, variant, tag, version, code, source, destination)
        for variant, destination in variants
    ]
    names = [entry["filename"] for entry in entries]
    validate_manifest(module, root, entries, names, tag=tag, version_name=version, version_code=code, source=source)

    mutations = [
        ("wrong package", "application_id", "wrong.package"),
        ("wrong version", "version_name", "0.0.0"),
        ("wrong versionCode", "version_code", 1),
        ("wrong source", "source_commit", "2" * 40),
        ("wrong tag", "release_tag", "v9.9.9"),
        ("wrong release destination", "destination", "workflow_artifacts"),
        ("bad APK digest", "sha256", "bad"),
        ("bad signer digest", "signer_sha256", "bad"),
    ]
    for description, field, value in mutations:
        broken = [dict(entry) for entry in entries]
        broken[0][field] = value
        try:
            validate_manifest(module, root, broken, names, tag=tag, version_name=version, version_code=code, source=source)
        except module.ReleasePlanError:
            continue
        raise TestFailure(f"manifest validator accepted {description}")

    debug_index = next(index for index, entry in enumerate(entries) if entry["variant"] == "app_debug")
    debug_mutations = [
        ("debug APK without -DEBUG version suffix", "version_name", version),
        ("debug APK outside configured debug destination", "destination", "release"),
    ]
    for description, field, value in debug_mutations:
        broken = [dict(entry) for entry in entries]
        broken[debug_index][field] = value
        try:
            validate_manifest(module, root, broken, names, tag=tag, version_name=version, version_code=code, source=source)
        except module.ReleasePlanError:
            continue
        raise TestFailure(f"manifest validator accepted {description}")

    duplicate = [dict(entries[0]), dict(entries[0])]
    try:
        validate_manifest(module, root, duplicate, [entries[0]["filename"]], tag=tag, version_name=version, version_code=code, source=source)
    except module.ReleasePlanError:
        pass
    else:
        raise TestFailure("manifest validator accepted duplicate staged asset names")


def main() -> int:
    if not ORCHESTRATOR.is_file() or not SELECT_SCRIPT.is_file():
        print("FAILED: run from the Auxio-TS repository root", file=sys.stderr)
        return 2
    try:
        module = load_orchestrator()
        test_version_resolution(module)
        with tempfile.TemporaryDirectory(prefix="auxio-release-matrix-") as temp:
            root = Path(temp)
            test_asset_planning(module, root)
            test_dispatch_selection(root)
            test_manifest_validation(module, root)
    except (TestFailure, OSError, subprocess.SubprocessError, ValueError, json.JSONDecodeError) as error:
        print(f"FAILED: Manual Release matrix: {error}", file=sys.stderr)
        return 1
    print("SUCCESS: Manual Release planner, dispatch, asset and manifest matrix passed")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

