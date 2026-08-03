#!/usr/bin/env python3
from pathlib import Path
import runpy

patcher = Path('scripts/tmp-apply-draft-release-fix.py')
text = patcher.read_text(encoding='utf-8')
old = '# Do not query the published-only /releases/tags/{tag} endpoint afterwards.'
new = '# Do not query the published-only tag lookup endpoint afterwards.'
if text.count(old) != 1:
    raise SystemExit(f'expected one published-only endpoint comment, found {text.count(old)}')
patcher.write_text(text.replace(old, new, 1), encoding='utf-8')
runpy.run_path(str(patcher), run_name='__main__')
