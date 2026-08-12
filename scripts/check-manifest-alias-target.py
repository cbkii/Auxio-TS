#!/usr/bin/env python3
"""Require one exact Android activity-alias to target one exact activity."""

import sys
import xml.etree.ElementTree as ET


ANDROID = "{http://schemas.android.com/apk/res/android}"


def main() -> int:
    if len(sys.argv) != 4:
        print(
            "usage: check-manifest-alias-target.py MANIFEST ALIAS TARGET",
            file=sys.stderr,
        )
        return 2

    manifest_path, required_alias, expected_target = sys.argv[1:]
    try:
        application = ET.parse(manifest_path).getroot().find("application")
    except (OSError, ET.ParseError) as exc:
        print(f"unable to parse manifest {manifest_path}: {exc}", file=sys.stderr)
        return 1

    if application is None:
        print(f"manifest {manifest_path} has no application element", file=sys.stderr)
        return 1

    aliases = [
        element
        for element in application.findall("activity-alias")
        if element.attrib.get(ANDROID + "name") == required_alias
    ]
    if len(aliases) != 1:
        print(
            f"expected one {required_alias} activity-alias, found {len(aliases)}",
            file=sys.stderr,
        )
        return 1

    actual_target = aliases[0].attrib.get(ANDROID + "targetActivity")
    if actual_target != expected_target:
        print(
            f"{required_alias} targetActivity expected {expected_target}, got {actual_target!r}",
            file=sys.stderr,
        )
        return 1

    return 0


if __name__ == "__main__":
    raise SystemExit(main())

