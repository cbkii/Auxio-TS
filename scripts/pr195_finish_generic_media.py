#!/usr/bin/env python3
"""Temporary compatibility entry point for the stored PR #195 verification workflow."""

from pathlib import Path
import runpy

runpy.run_path(
    str(Path(__file__).with_name("pr195_harden_launcher_media.py")),
    run_name="__main__",
)
