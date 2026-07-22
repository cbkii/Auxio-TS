#!/usr/bin/env python3
from pathlib import Path

path = Path("app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwaySourcePolicy.kt")
text = path.read_text(encoding="utf-8")
bad = "replace('\\', '/')"
good = "replace('\\\\', '/')"
count = text.count(bad)
if count != 1:
    raise SystemExit(f"STOP: expected one malformed backslash expression, found {count}")
path.write_text(text.replace(bad, good, 1), encoding="utf-8")
