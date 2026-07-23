#!/usr/bin/env python3
"""Fix the Android string-resource apostrophe in PR #195 exactly once."""

from pathlib import Path

path = Path("app/src/main/res/values/strings.xml")
text = path.read_text(encoding="utf-8")
old = "Review Auxio's playback notification channel. Device-specific controller behaviour, including TS18/DoFun compatibility, requires runtime validation."
new = "Review Auxio’s playback notification channel. Device-specific controller behaviour, including TS18/DoFun compatibility, requires runtime validation."
if text.count(old) != 1:
    raise SystemExit("Expected exactly one unescaped Auxio apostrophe in the playback-channel description")
path.write_text(text.replace(old, new, 1), encoding="utf-8", newline="\n")
