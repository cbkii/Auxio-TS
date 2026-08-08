#!/usr/bin/env python3
"""Strict defined-class and paired-target inspection for the Track-C bridge APK."""

from __future__ import annotations

import re
import struct
import sys
import zipfile
from pathlib import Path

if len(sys.argv) != 5:
    raise SystemExit("usage: check-lsposed-bridge-dex.py APK ENTRY TARGET_SIGNER TARGET_PACKAGE")

apk_path = Path(sys.argv[1])
entry_descriptor = "L" + sys.argv[2].replace(".", "/") + ";"
target_signer = sys.argv[3]
target_package = sys.argv[4]
approved_prefix = "Lorg/oxycblt/auxio/ts18bridge/"
forbidden_prefixes = (
    "Landroid/",
    "Landroidx/",
    "Lkotlin/",
    "Lorg/jetbrains/",
    "Lorg/intellij/",
    "Lcom/android/tools/",
    "Lio/github/libxposed/",
)


def u32(data: bytes, offset: int) -> int:
    if offset < 0 or offset + 4 > len(data):
        raise ValueError(f"u32 outside DEX at {offset}")
    return struct.unpack_from("<I", data, offset)[0]


def uleb128(data: bytes, offset: int) -> tuple[int, int]:
    value = 0
    shift = 0
    for _ in range(5):
        if offset >= len(data):
            raise ValueError("truncated ULEB128")
        byte = data[offset]
        offset += 1
        value |= (byte & 0x7F) << shift
        if byte & 0x80 == 0:
            return value, offset
        shift += 7
    raise ValueError("invalid ULEB128")


def decode_mutf8(value: bytes) -> str:
    """Decode DEX modified UTF-8 while preserving surrogate code units."""
    return value.replace(b"\xc0\x80", b"\x00").decode("utf-8", errors="surrogatepass")


def inspect_dex(data: bytes) -> tuple[list[str], set[str]]:
    if len(data) < 112 or data[:4] != b"dex\n":
        raise ValueError("invalid DEX header")
    string_count, string_off = u32(data, 56), u32(data, 60)
    type_count, type_off = u32(data, 64), u32(data, 68)
    class_count, class_off = u32(data, 96), u32(data, 100)
    if string_off + string_count * 4 > len(data):
        raise ValueError("string table outside DEX")
    if type_off + type_count * 4 > len(data):
        raise ValueError("type table outside DEX")
    if class_off + class_count * 32 > len(data):
        raise ValueError("class table outside DEX")

    strings: list[str] = []
    for index in range(string_count):
        cursor = u32(data, string_off + index * 4)
        _, cursor = uleb128(data, cursor)
        end = data.find(b"\x00", cursor)
        if end < 0:
            raise ValueError("unterminated DEX string")
        strings.append(decode_mutf8(data[cursor:end]))

    classes: set[str] = set()
    for index in range(class_count):
        class_idx = u32(data, class_off + index * 32)
        if class_idx >= type_count:
            raise ValueError("class index outside type table")
        descriptor_idx = u32(data, type_off + class_idx * 4)
        if descriptor_idx >= string_count:
            raise ValueError("descriptor outside string table")
        classes.add(strings[descriptor_idx])
    return strings, classes


try:
    if not re.fullmatch(r"[0-9A-F]{64}", target_signer):
        raise ValueError("paired target signer is malformed")

    with zipfile.ZipFile(apk_path) as apk:
        dex_names = sorted(
            name for name in apk.namelist() if re.fullmatch(r"classes(?:\d+)?\.dex", name)
        )
        if dex_names != ["classes.dex"]:
            raise ValueError(f"expected exactly classes.dex; found {dex_names}")
        strings, classes = inspect_dex(apk.read("classes.dex"))

    if entry_descriptor not in classes:
        raise ValueError(f"missing LSPosed entry class {entry_descriptor}")
    packaged_forbidden = sorted(
        item for item in classes if item.startswith(forbidden_prefixes)
    )
    if packaged_forbidden:
        raise ValueError("forbidden packaged classes: " + ", ".join(packaged_forbidden[:20]))
    unexpected = sorted(item for item in classes if not item.startswith(approved_prefix))
    if unexpected:
        raise ValueError("classes outside bridge package: " + ", ".join(unexpected[:20]))
    # The paired target signer is intentionally a build/release validation input only. It must not
    # be embedded into the bridge DEX or used as a runtime activation gate.
    if target_signer in strings:
        raise ValueError("paired target signer must not be embedded into bridge DEX")
    if target_package not in strings:
        raise ValueError("paired target package is not compiled into bridge DEX")

    print(f"[INFO] strict DEX contract passed: 1 DEX, {len(classes)} defined classes")
except (OSError, ValueError, zipfile.BadZipFile, UnicodeDecodeError, struct.error) as error:
    print(f"[ERROR] strict DEX inspection failed: {error}", file=sys.stderr)
    raise SystemExit(1)
