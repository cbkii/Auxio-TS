#!/usr/bin/env python3

from pathlib import Path

path = Path('.github/pr194_patch.py')
text = path.read_text(encoding='utf-8')

start = text.index('def patch_direct_fs()')
end = text.index('\ndef patch_root_listing()', start)
section = text[start:end]
section = section.replace('old = dedent(', 'old = "    " + dedent(', 1)
section = section.replace('new = dedent(', 'new = "    " + dedent(', 1)
section = section.replace('.replace("\\n", "\\n    ", 1)', '.replace("\\n", "\\n    ")', 2)
text = text[:start] + section + text[end:]

start = text.index('def patch_cover_provider()')
end = text.index('\ndef patch_visualizer_metrics()', start)
section = text[start:end]
section = section.replace('old = dedent(', 'old = "        " + dedent(', 1)
section = section.replace('new = dedent(', 'new = "        " + dedent(', 1)
section = section.replace('.replace("\\n", "\\n        ", 1)', '.replace("\\n", "\\n        ")', 2)
text = text[:start] + section + text[end:]

path.write_text(text, encoding='utf-8', newline='\n')
