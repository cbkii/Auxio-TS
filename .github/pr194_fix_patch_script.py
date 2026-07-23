#!/usr/bin/env python3

from pathlib import Path

path = Path('.github/pr194_patch.py')
text = path.read_text(encoding='utf-8')

text = text.replace(
    '    count = text.count(old)\n',
    '    print(f"PATCH {path}: {old.splitlines()[0]!r}", flush=True)\n'
    '    count = text.count(old)\n',
    1,
)

start = text.index('def patch_direct_fs()')
end = text.index('\ndef patch_root_listing()', start)
section = text[start:end]
section = section.replace('old = dedent(', 'old = "    " + dedent(', 1)
section = section.replace('new = dedent(', 'new = "    " + dedent(', 1)
section = section.replace('.replace("\\n", "\\n    ", 1)', '.replace("\\n", "\\n    ")', 2)
section = section.replace(
    '    replace_once(path, old, new)\n',
    '''    file = Path(path)\n'''
    '''    source = file.read_text(encoding="utf-8")\n'''
    '''    block_start = source.index("    private suspend fun exploreBounded(files: Channel<File>)")\n'''
    '''    block_end = source.index("    private fun combineRootFingerprints", block_start)\n'''
    '''    file.write_text(source[:block_start] + new + "\\n" + source[block_end:], encoding="utf-8", newline="\\n")\n''',
    1,
)
text = text[:start] + section + text[end:]

start = text.index('def patch_cover_provider()')
end = text.index('\ndef patch_visualizer_metrics()', start)
section = text[start:end]
section = section.replace('old = dedent(', 'old = "        " + dedent(', 1)
section = section.replace('new = dedent(', 'new = "        " + dedent(', 1)
section = section.replace('.replace("\\n", "\\n        ", 1)', '.replace("\\n", "\\n        ")', 2)
section = section.replace(
    '    replace_once(path, old, new)\n',
    '''    file = Path(path)\n'''
    '''    source = file.read_text(encoding="utf-8")\n'''
    '''    block_start = source.index("        return try {\\n            writerExecutor.execute")\n'''
    '''    block_end = source.index("    override fun shutdown()", block_start)\n'''
    '''    file.write_text(source[:block_start] + new + "\\n" + source[block_end:], encoding="utf-8", newline="\\n")\n''',
    1,
)
text = text[:start] + section + text[end:]

path.write_text(text, encoding='utf-8', newline='\n')
