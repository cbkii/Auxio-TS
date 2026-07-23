#!/usr/bin/env python3
"""Temporarily patch the API 29 lane to preserve complete Gradle failure output."""

from pathlib import Path

path = Path('.github/workflows/android.yml')
text = path.read_text(encoding='utf-8')
old = '''      - name: Run API 29 compatibility and Room migration tests
        shell: bash
        run: |
          bash ./scripts/ci-gradle.sh \\
            :app:connectedStandardDebugAndroidTest \\
            :musikr:connectedDebugAndroidTest
'''
new = '''      - name: Run API 29 compatibility and Room migration tests
        shell: bash
        run: |
          set -o pipefail
          bash ./scripts/ci-gradle.sh \\
            :app:connectedStandardDebugAndroidTest \\
            :musikr:connectedDebugAndroidTest \\
            2>&1 | tee "${RUNNER_TEMP}/api29-gradle.log"
'''
if text.count(old) != 1:
    raise SystemExit(f'expected one API29 test step, found {text.count(old)}')
text = text.replace(old, new)
old_upload = '''            ${{ runner.temp }}/avdmanager-create.log
            ${{ runner.temp }}/api29-emulator.log
'''
new_upload = '''            ${{ runner.temp }}/avdmanager-create.log
            ${{ runner.temp }}/api29-emulator.log
            ${{ runner.temp }}/api29-gradle.log
'''
if text.count(old_upload) != 1:
    raise SystemExit(f'expected one API29 upload block, found {text.count(old_upload)}')
path.write_text(text.replace(old_upload, new_upload), encoding='utf-8', newline='\n')
print('patched .github/workflows/android.yml')
