from pathlib import Path

path = Path("app/build.gradle")
text = path.read_text(encoding="utf-8")
replacements = {
    '    id "androidx.baselineprofile"\n': "",
    '    baselineProfile project(":startup-benchmark")\n': "",
    '''\nbaselineProfile {\n    automaticGenerationDuringBuild = false\n    mergeIntoMain = true\n    saveInSrc = true\n}\n''': "\n",
}
for old, new in replacements.items():
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"Expected one occurrence of {old!r}, found {count}")
    text = text.replace(old, new)
path.write_text(text, encoding="utf-8", newline="\n")
