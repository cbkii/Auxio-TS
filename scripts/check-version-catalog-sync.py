#!/usr/bin/env python3
"""Verify each curated catalogue version against its authoritative Groovy declaration(s)."""

from __future__ import annotations

import re
import sys
import tomllib
from pathlib import Path
from typing import Iterable

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "gradle" / "libs.versions.toml"
FILES = {
    "root": ROOT / "build.gradle",
    "app": ROOT / "app" / "build.gradle",
    "musikr": ROOT / "musikr" / "build.gradle",
}


class SyncError(RuntimeError):
    pass


def fail(message: str) -> None:
    raise SyncError(message)


def read(name: str) -> str:
    path = FILES[name]
    if not path.is_file():
        fail(f"required authoritative build file is missing: {path.relative_to(ROOT)}")
    return path.read_text(encoding="utf-8")


TEXT = {name: read(name) for name in FILES}


def one(pattern: str, text: str, label: str) -> str:
    matches = re.findall(pattern, text, flags=re.MULTILINE)
    if len(matches) != 1:
        fail(f"{label}: expected exactly one authoritative version, found {len(matches)}")
    value = matches[0]
    if isinstance(value, str):
        if not value:
            fail(f"{label}: authoritative version capture is empty")
        return value
    captures = [part for part in value if part]
    if len(captures) != 1:
        fail(f"{label}: expected exactly one non-empty version capture, found {len(captures)}")
    return captures[0]


def variable(file_name: str, name: str) -> str:
    return one(
        rf"^\s*(?:def\s+)?{re.escape(name)}\s*=\s*['\"]([^'\"]+)['\"]\s*$",
        TEXT[file_name],
        f"{file_name}:{name}",
    )


def coordinate(file_name: str, group_artifact: str) -> str:
    return one(
        rf"['\"]{re.escape(group_artifact)}:([^'\"$]+)['\"]",
        TEXT[file_name],
        f"{file_name}:{group_artifact}",
    )


def coordinate_consistent(file_names: Iterable[str], group_artifact: str) -> str:
    declarations = {
        file_name: coordinate(file_name, group_artifact) for file_name in file_names
    }
    versions = set(declarations.values())
    if len(versions) != 1:
        detail = ", ".join(
            f"{file_name}={version}" for file_name, version in declarations.items()
        )
        fail(f"{group_artifact}: authoritative module declarations disagree: {detail}")
    return next(iter(versions))


def plugin(plugin_id: str) -> str:
    return one(
        rf'id\s+["\']{re.escape(plugin_id)}["\']\s+version\s+["\']([^"\']+)["\']',
        TEXT["root"],
        f"root plugin:{plugin_id}",
    )


def classpath(group_artifact: str) -> str:
    return one(
        rf'classpath\s+["\']{re.escape(group_artifact)}:([^"\'$]+)["\']',
        TEXT["root"],
        f"root classpath:{group_artifact}",
    )


def authoritative_versions() -> dict[str, str]:
    return {
        "agp": variable("root", "agp_version"),
        "kotlin": variable("root", "kotlin_version"),
        "ksp": plugin("com.google.devtools.ksp"),
        "kotlinCoroutines": variable("root", "kotlin_coroutines_version"),
        "navigation": variable("root", "navigation_version"),
        "hilt": variable("root", "hilt_version"),
        "room": variable("root", "room_version"),
        "androidxCore": variable("root", "core_version"),
        "desugarJdkLibs": variable("root", "desugaring_version"),
        "dokka": classpath("org.jetbrains.dokka:dokka-gradle-plugin"),
        "spotless": plugin("com.diffplug.spotless"),
        "roborazzi": plugin("io.github.takahirom.roborazzi"),
        "baselineProfile": plugin("androidx.baselineprofile"),
        "appcompat": coordinate("app", "androidx.appcompat:appcompat"),
        "activity": coordinate("app", "androidx.activity:activity-ktx"),
        "fragment": coordinate("app", "androidx.fragment:fragment-ktx"),
        "recyclerview": variable("app", "recyclerViewVersion"),
        "constraintlayout": coordinate("app", "androidx.constraintlayout:constraintlayout"),
        "viewpager2": variable("app", "viewPager2Version"),
        "lifecycle": variable("app", "lifecycle_version"),
        "androidxMedia": coordinate("app", "androidx.media:media"),
        "carApp": coordinate("app", "androidx.car.app:app"),
        "preference": coordinate("app", "androidx.preference:preference-ktx"),
        "coil": coordinate("app", "io.coil-kt.coil3:coil-core"),
        "material": coordinate("app", "com.google.android.material:material"),
        "timber": coordinate("app", "com.jakewharton.timber:timber"),
        "speedDial": coordinate("app", "com.leinardi.android:speed-dial"),
        "taskerPluginLibrary": coordinate("app", "com.joaomgcd:taskerpluginlibrary"),
        "commonsText": coordinate("app", "org.apache.commons:commons-text"),
        "junit4": coordinate_consistent(("app", "musikr"), "junit:junit"),
        "robolectricApp": coordinate("app", "org.robolectric:robolectric"),
        "robolectricMusikr": coordinate("musikr", "org.robolectric:robolectric"),
        "androidxTestCore": coordinate("app", "androidx.test:core"),
        "androidxTestCoreKtx": coordinate_consistent(
            ("app", "musikr"), "androidx.test:core-ktx"
        ),
        "mockk": coordinate("musikr", "io.mockk:mockk"),
        "androidxTestJunit": coordinate_consistent(
            ("app", "musikr"), "androidx.test.ext:junit"
        ),
        "espresso": coordinate("musikr", "androidx.test.espresso:espresso-core"),
    }


def main() -> int:
    if not CATALOG.is_file():
        fail(f"version catalogue is missing: {CATALOG.relative_to(ROOT)}")
    data = tomllib.loads(CATALOG.read_text(encoding="utf-8"))
    catalog = data.get("versions")
    if not isinstance(catalog, dict):
        fail("gradle/libs.versions.toml has no [versions] table")

    expected = authoritative_versions()
    catalog_keys = set(catalog)
    expected_keys = set(expected)
    if catalog_keys != expected_keys:
        missing = sorted(expected_keys - catalog_keys)
        unmanaged = sorted(catalog_keys - expected_keys)
        fail(
            "catalogue inventory coverage mismatch: "
            f"missing={missing or 'none'} unmanaged={unmanaged or 'none'}"
        )

    errors: list[str] = []
    for key in sorted(expected):
        actual = str(catalog[key])
        authoritative = expected[key]
        if actual != authoritative:
            errors.append(
                f"{key}: catalogue={actual} authoritative={authoritative}"
            )
        else:
            print(f"OK {key}: {actual}")

    if errors:
        for error in errors:
            print(f"::error::CATALOG_SYNC: {error}", file=sys.stderr)
        return 1

    print(
        f"READY: all {len(expected)} curated catalogue versions match their maintained Groovy declarations."
    )
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except (OSError, tomllib.TOMLDecodeError, SyncError) as exc:
        print(f"::error::CATALOG_SYNC: {exc}", file=sys.stderr)
        raise SystemExit(1)
