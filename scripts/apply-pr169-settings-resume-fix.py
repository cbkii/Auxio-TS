from __future__ import annotations

import os
import tempfile
from pathlib import Path

ROOT = Path.cwd()
PATH = (
    "app/src/topwayCompat/java/org/oxycblt/auxio/settings/categories/"
    "CarPreferenceFragment.kt"
)


def atomic_write(content: str) -> None:
    target = ROOT / PATH
    fd, tmp_name = tempfile.mkstemp(prefix=f".{target.name}.", suffix=".tmp", dir=target.parent)
    tmp = Path(tmp_name)
    try:
        with os.fdopen(fd, "w", encoding="utf-8", newline="\n") as fh:
            fh.write(content)
            fh.flush()
            os.fsync(fh.fileno())
        os.replace(tmp, target)
    except BaseException:
        tmp.unlink(missing_ok=True)
        raise


text = (ROOT / PATH).read_text(encoding="utf-8")
old = '''    }

    private fun setupTs18FastResumeStatus(preference: Preference) {
'''
new = '''    }

    override fun onResume() {
        super.onResume()
        findPreference<androidx.preference.TwoStatePreference>(KEY_CAR_OVERLAY_ENABLED)
            ?.isChecked = CarOverlaySettings.isEnabled(requireContext())
        findPreference<Preference>(getString(R.string.set_key_autostart_floating_only))?.let {
            setupAutostartFloatingOnly(it)
        }
    }

    private fun setupTs18FastResumeStatus(preference: Preference) {
'''
if text.count(old) != 1:
    raise RuntimeError(f"{PATH}: expected one onResume insertion anchor, found {text.count(old)}")
text = text.replace(old, new, 1)
atomic_write(text)
if "override fun onResume()" not in (ROOT / PATH).read_text(encoding="utf-8"):
    raise RuntimeError(f"{PATH}: onResume refresh postcondition missing")
print("PR #169 overlay-settings resume refresh applied successfully")
