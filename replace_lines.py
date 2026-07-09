import sys

file_path = "app/src/main/java/org/oxycblt/auxio/settings/categories/DiagnosticsRecoveryPreferenceFragment.kt"
with open(file_path, "r") as f:
    content = f.read()

content = content.replace(
    'getString(R.string.set_key_ts18_source_repair_status) ->\n                setupTs18SourceRepairStatus(preference)',
    'getString(R.string.set_key_ts18_source_repair_status) -> RootDiagnosticsHelper.setupTs18SourceRepairStatus(requireContext(), preference, viewLifecycleOwner.lifecycleScope)'
)

with open(file_path, "w") as f:
    f.write(content)
