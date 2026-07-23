#!/usr/bin/env python3
"""Update legacy startup-policy test harness calls after authority separation."""

from pathlib import Path

path = Path("app/src/test/java/org/oxycblt/auxio/music/StartupLibraryPolicyTest.kt")
text = path.read_text(encoding="utf-8")
replacements = [
    ("isTopwayCompat = true", "automaticScanAllowed = false"),
    (
        "private val isTopwayCompat: Boolean = false",
        "private val automaticScanAllowed: Boolean = true",
    ),
    (
        "isTopwayCompat = isTopwayCompat",
        "automaticScanAllowed = automaticScanAllowed",
    ),
]
for old, new in replacements:
    count = text.count(old)
    if count < 1:
        raise SystemExit(f"Expected at least one legacy test reference: {old!r}")
    text = text.replace(old, new)
path.write_text(text, encoding="utf-8", newline="\n")
