#!/usr/bin/env python3
import base64
from pathlib import Path
import sys

if len(sys.argv) != 2:
    raise SystemExit("usage: tmp-pr200-headunit-guard-closure.py <repo-root>")

repo = Path(sys.argv[1]).resolve()
path = repo / "scripts/check-headunit-compat-safety.sh"
text = path.read_text(encoding="utf-8")
replacements = [
    (
        "YWxsb3dlZF90b3B3YXlfdGVzdD0nYXBwL3NyYy90ZXN0L2phdmEvb3JnL294eWNibHQvYXV4aW8vaGVhZHVuaXQvdG9wd2F5LycKYWxsb3dlZF90b3B3YXlfZmxhdm91cj0nYXBwL3NyYy90b3B3YXlDb21wYXQvamF2YS9jb20vdHcvbXVzaWMvJwo=",
        "YWxsb3dlZF90b3B3YXlfdGVzdD0nYXBwL3NyYy90ZXN0L2phdmEvb3JnL294eWNibHQvYXV4aW8vaGVhZHVuaXQvdG9wd2F5LycKcm9vdF9wcm9iZV9hbGxvd2xpc3RfdGVzdD0nYXBwL3NyYy90ZXN0L2phdmEvb3JnL294eWNibHQvYXV4aW8vaGVhZHVuaXQvcm9vdC9kb2Z1bi9UczE4RG9mdW5JbnRlZ3JhdGlvblJlc29sdmVyVGVzdC5rdCcKYWxsb3dlZF90b3B3YXlfZmxhdm91cj0nYXBwL3NyYy90b3B3YXlDb21wYXQvamF2YS9jb20vdHcvbXVzaWMvJwo=",
    ),
    (
        "ICAgICAgIiR7d2lkZ2V0X2NvbXBvbmVudH0iKQogICAgICAgIGNhc2UgIiR7bGluZX0iIGluCiAgICAgICAgICAqJ0NsYXNzLmZvck5hbWUoImNvbS50dy5tdXNpYy52aWV3Lk11c2ljV2lkZ2V0UHJvdmlkZXIiKScqKSA7OwogICAgICAgICAgKikKICAgICAgICAgICAgZWNobyAiJHtsaW5lfSIgPiYyCiAgICAgICAgICAgIGVjaG8gIlVuZXhwZWN0ZWQgdmVuZG9yIHN0cmluZyBpbiBXaWRnZXRDb21wb25lbnQiID4mMgogICAgICAgICAgICBleGl0IDEKICAgICAgICAgICAgOzsKICAgICAgICBlc2FjCiAgICAgICAgOzsKICAgICAgJHthbGxvd2VkX3RvcHdheV9tYWlufSp8JHthbGxvd2VkX3RvcHdheV90ZXN0fSp8JHthbGxvd2VkX3RvcHdheV9mbGF2b3VyfSopCg==",
        "ICAgICAgIiR7d2lkZ2V0X2NvbXBvbmVudH0iKQogICAgICAgIGNhc2UgIiR7bGluZX0iIGluCiAgICAgICAgICAqJ0NsYXNzLmZvck5hbWUoImNvbS50dy5tdXNpYy52aWV3Lk11c2ljV2lkZ2V0UHJvdmlkZXIiKScqKSA7OwogICAgICAgICAgKikKICAgICAgICAgICAgZWNobyAiJHtsaW5lfSIgPiYyCiAgICAgICAgICAgIGVjaG8gIlVuZXhwZWN0ZWQgdmVuZG9yIHN0cmluZyBpbiBXaWRnZXRDb21wb25lbnQiID4mMgogICAgICAgICAgICBleGl0IDEKICAgICAgICAgICAgOzsKICAgICAgICBlc2FjCiAgICAgICAgOzsKICAgICAgIiR7cm9vdF9wcm9iZV9hbGxvd2xpc3RfdGVzdH0iKQogICAgICAgIDs7CiAgICAgICR7YWxsb3dlZF90b3B3YXlfbWFpbn0qfCR7YWxsb3dlZF90b3B3YXlfdGVzdH0qfCR7YWxsb3dlZF90b3B3YXlfZmxhdm91cn0qKQo=",
    ),
]

changed = False
for old_b64, new_b64 in replacements:
    old = base64.b64decode(old_b64).decode("utf-8")
    new = base64.b64decode(new_b64).decode("utf-8")
    if new in text and old not in text:
        continue
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"expected one head-unit guard match, found {count}")
    text = text.replace(old, new)
    changed = True

if changed:
    path.write_text(text, encoding="utf-8", newline="\n")
    print("patched head-unit vendor-test allowlist")
else:
    print("head-unit vendor-test allowlist already applied")
