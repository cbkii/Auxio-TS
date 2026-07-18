from pathlib import Path

path = Path("app/src/main/java/org/oxycblt/auxio/music/service/IndexingHolder.kt")
text = path.read_text()
old = '''                            // Skip this inaccessible update without stopping the tracker; keeping
                            // it alive lets later remount/accessibility events trigger a real scan.
                            return@collect
'''
new = '''                            // Keep the tracker alive and continue to the debounced planner so the
                            // source ledger records unavailability without publishing an empty generation.
'''
if old not in text:
    raise SystemExit("inaccessible-source observer block not found")
path.write_text(text.replace(old, new, 1))
