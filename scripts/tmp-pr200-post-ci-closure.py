#!/usr/bin/env python3
import base64
from pathlib import Path
import sys

if len(sys.argv) != 2:
    raise SystemExit("usage: tmp-pr200-post-ci-closure.py <repo-root>")

repo = Path(sys.argv[1]).resolve()
replacements = [
    (
        "scripts/check-headunit-compat-safety.sh",
        "dmVuZG9yX2hpdHM9IiQoc2VhcmNoX2FkZGVkX21hdGNoZXMgJ2NvbVwudHdcLltBLVphLXowLTlfLl0rfGNvbVwuYW5kcm9pZFwubGF1bmNoZXJcLndpZGdldF9tdXNpY19wcm9ncmVzcycgIiR7cHJvZHVjdF9jb2RlX3NvdXJjZXNbQF19IikiCg==",
        "cm9vdF9wcm9iZV92ZW5kb3JfaWRzPSIkKAogIHB5dGhvbjMgLSAiJHtyb290X3Byb2JlX2FsbG93bGlzdF90ZXN0fSIgPDwnUFknCmltcG9ydCByZQppbXBvcnQgc3lzCmZyb20gcGF0aGxpYiBpbXBvcnQgUGF0aAoKdGV4dCA9IFBhdGgoc3lzLmFyZ3ZbMV0pLnJlYWRfdGV4dChlbmNvZGluZz0idXRmLTgiKS5yZXBsYWNlKCJcXFxcLiIsICIuIikKZm9yIHZhbHVlIGluIHNvcnRlZChzZXQocmUuZmluZGFsbChyIlxiY29tXC50d1wuW0EtWmEtejAtOV8uXStcYiIsIHRleHQpKSk6CiAgICBwcmludCh2YWx1ZSkKUFkKKSIgfHwgZmFpbCAnY2Fubm90IHZhbGlkYXRlIHRoZSByb290LXByb2JlIHZlbmRvciBpZGVudGlmaWVyIGFsbG93bGlzdCcKZXhwZWN0ZWRfcm9vdF9wcm9iZV92ZW5kb3JfaWRzPSIkKHByaW50ZiAnJXNcbicgICAnY29tLnR3LmVxJyAgICdjb20udHcubWVkaWEnICAgJ2NvbS50dy5tdXNpYycgICAnY29tLnR3Lm11c2ljLk11c2ljQWN0aXZpdHknKSIKaWYgWyAiJHtyb290X3Byb2JlX3ZlbmRvcl9pZHN9IiAhPSAiJHtleHBlY3RlZF9yb290X3Byb2JlX3ZlbmRvcl9pZHN9IiBdOyB0aGVuCiAgcHJpbnRmICdPYnNlcnZlZCByb290LXByb2JlIHZlbmRvciBpZGVudGlmaWVyczpcbiVzXG4nICIke3Jvb3RfcHJvYmVfdmVuZG9yX2lkc30iID4mMgogIGZhaWwgJ3Jvb3QtcHJvYmUgdGVzdCB2ZW5kb3IgaWRlbnRpZmllcnMgZGlmZmVyIGZyb20gdGhlIGFwcHJvdmVkIHJlYWQtb25seSBzZXQnCmZpCgp2ZW5kb3JfaGl0cz0iJChzZWFyY2hfYWRkZWRfbWF0Y2hlcyAnY29tXC50d1wuW0EtWmEtejAtOV8uXSt8Y29tXC5hbmRyb2lkXC5sYXVuY2hlclwud2lkZ2V0X211c2ljX3Byb2dyZXNzJyAiJHtwcm9kdWN0X2NvZGVfc291cmNlc1tAXX0iKSIK",
    ),
    (
        "app/src/main/java/org/oxycblt/auxio/headunit/root/storage/PreparedVolumeIndexStore.kt",
        "ICAgIHByaXZhdGUgZnVuIHJlYWRDYWNoZWRSZWNvcmRzKCk6IExpc3Q8UHJlcGFyZWRWb2x1bWVSZWNvcmQ+ID0KICAgICAgICBydW5DYXRjaGluZyB7IFByZXBhcmVkVm9sdW1lTWFuaWZlc3RDb2RlYy5wYXJzZShjYWNoZUZpbGUucmVhZFRleHQoKSkgfQogICAgICAgICAgICAuZ2V0T3JOdWxsKCkKICAgICAgICAgICAgLm9yRW1wdHkoKQo=",
        "ICAgIHByaXZhdGUgZnVuIHJlYWRDYWNoZWRSZWNvcmRzKCk6IExpc3Q8UHJlcGFyZWRWb2x1bWVSZWNvcmQ+ID0KICAgICAgICBydW5DYXRjaGluZyB7CiAgICAgICAgICAgICAgICB2YWwgdGV4dCA9IGF0b21pY0NhY2hlRmlsZS5yZWFkRnVsbHkoKS50b1N0cmluZyhDaGFyc2V0cy5VVEZfOCkKICAgICAgICAgICAgICAgIFByZXBhcmVkVm9sdW1lTWFuaWZlc3RDb2RlYy5wYXJzZSh0ZXh0KQogICAgICAgICAgICB9CiAgICAgICAgICAgIC5nZXRPck51bGwoKQogICAgICAgICAgICAub3JFbXB0eSgpCg==",
    ),
    (
        "app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt",
        "ICAgICAgICAgICAgaWYgKGxvY2F0aW9uTW9kZSAhPSBpbml0aWF0aW5nTW9kZSkgewogICAgICAgICAgICAgICAgTC5kKAogICAgICAgICAgICAgICAgICAgICJJZ25vcmluZyBhY2NlcHRlZCBzb3VyY2UgYWZ0ZXIgbW9kZSBjaGFuZ2VkIGZyb20gJGluaXRpYXRpbmdNb2RlICIgKwogICAgICAgICAgICAgICAgICAgICAgICAidG8gJGxvY2F0aW9uTW9kZSIKICAgICAgICAgICAgICAgICkKICAgICAgICAgICAgICAgIGNsZWFyUGVuZGluZ0xvY2F0aW9uQ2FsbGJhY2soY2FsbGJhY2spCiAgICAgICAgICAgICAgICByZXR1cm5AbGF1bmNoCiAgICAgICAgICAgIH0K",
        "",
    ),
]

changed = []
for relative, old_b64, new_b64 in replacements:
    path = repo / relative
    text = path.read_text(encoding="utf-8")
    old = base64.b64decode(old_b64).decode("utf-8")
    new = base64.b64decode(new_b64).decode("utf-8")
    if new and new in text and old not in text:
        print(f"already applied: {relative}")
        continue
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"expected one match in {relative}, found {count}")
    path.write_text(text.replace(old, new), encoding="utf-8", newline="\n")
    changed.append(relative)
    print(f"patched: {relative}")

if not changed:
    raise SystemExit("no post-CI closure changes were applied")
print(f"patched {len(changed)} files")
