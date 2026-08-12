#!/usr/bin/env python3
"""Validate current documentation links, authority, inventory and duplicate policy."""

from __future__ import annotations

import hashlib
import re
import sys
from pathlib import Path
from urllib.parse import unquote

ROOT = Path(__file__).resolve().parents[1]
INVENTORY = ROOT / "docs/DOCUMENTATION_INVENTORY.md"
CURRENT_CLASSES = {"authoritative/current", "current topic guide", "runbook", "architecture decision"}
CANONICAL = {
    "README.md", "AGENTS.md", ".github/copilot-instructions.md", "docs/README.md",
    "docs/PRODUCT_SCOPE.md", "docs/ARCHITECTURE.md", "docs/DEVELOPMENT.md",
    "docs/RELEASE_WORKFLOW.md",
}
STALE_SURFACES = (
    "docs/reference/t-music", "docs/prompts", "docs/suggestionfix-support",
    "docs/evidence/topway-dofun-navradio-static-analysis",
    "TS18_PR117_PR118_RELEASE_GATE.md", "TS18_PR117_SELECTED_PATH_STATUS.md",
    "TS18_FAST_RESUME_SOURCE_REPAIR_STACK.md", "TS18_COMPATIBILITY_AUDIT.md",
)
LINK_RE = re.compile(r"!?\[[^\]]*\]\(([^)]+)\)")
ROW_RE = re.compile(r"^\| `([^`]+)` \| ([^|]+?) \| ([^|]+?) \|$")
RETIRED_TASK_RE = re.compile(
    r":(?:app|startup-benchmark):(?:assemble|bundle|test|lint|connected|record|verify|compare)"
    r"[A-Za-z]*(?:TopwayTwMedia|TopwayTwMusic|Standard)"
)


def inventory_rows() -> dict[str, tuple[str, str]]:
    if not INVENTORY.is_file():
        raise SystemExit("documentation policy: inventory is missing")
    rows: dict[str, tuple[str, str]] = {}
    for line in INVENTORY.read_text(encoding="utf-8").splitlines():
        match = ROW_RE.match(line)
        if match:
            rows[match.group(1)] = (match.group(2).strip(), match.group(3).strip())
    return rows


def local_target(source: Path, raw: str) -> Path | None:
    raw = raw.strip()
    if raw.startswith("<") and ">" in raw:
        raw = raw[1 : raw.index(">")]
    else:
        raw = raw.split(maxsplit=1)[0]
    raw = unquote(raw.split("#", 1)[0])
    if not raw or raw.startswith(("http://", "https://", "mailto:", "tel:", "data:")):
        return None
    return (source.parent / raw).resolve()


def main() -> int:
    failures: list[str] = []
    rows = inventory_rows()
    markdown = sorted(path for path in ROOT.rglob("*.md") if ".git" not in path.parts)
    relative = {path.relative_to(ROOT).as_posix() for path in markdown}

    for path in sorted(relative - rows.keys()):
        failures.append(f"unclassified Markdown file: {path}")

    current = {
        path for path, (classification, disposition) in rows.items()
        if classification in CURRENT_CLASSES and disposition.startswith("Keep")
    }
    for path in sorted(current):
        source = ROOT / path
        if not source.is_file():
            failures.append(f"current document is missing: {path}")
            continue
        text = source.read_text(encoding="utf-8")
        for raw in LINK_RE.findall(text):
            target = local_target(source, raw)
            if target is None:
                continue
            try:
                target.relative_to(ROOT)
            except ValueError:
                failures.append(f"{path}: local link escapes repository: {raw}")
                continue
            if not target.exists():
                failures.append(f"{path}: missing local link target: {raw}")
        if path != "docs/DOCUMENTATION_INVENTORY.md":
            for stale in STALE_SURFACES:
                if stale in text:
                    failures.append(f"{path}: stale policy/reference surface: {stale}")
            retired_task = RETIRED_TASK_RE.search(text)
            if retired_task:
                failures.append(f"{path}: retired Gradle task reference: {retired_task.group(0)}")

    for path in CANONICAL:
        if path not in current:
            failures.append(f"canonical document is not classified current: {path}")

    for directory in (
        ROOT / "docs/reference/t-music", ROOT / "docs/prompts",
        ROOT / "docs/suggestionfix-support",
        ROOT / "docs/evidence/topway-dofun-navradio-static-analysis",
    ):
        if directory.exists():
            failures.append(f"removed documentation surface returned: {directory.relative_to(ROOT)}")

    agents = sorted(path.relative_to(ROOT).as_posix() for path in ROOT.rglob("AGENTS.md"))
    if agents != ["AGENTS.md", "lsposed-bridge/AGENTS.md"]:
        failures.append(f"unexpected live AGENTS.md files: {agents}")
    copilots = sorted(path.relative_to(ROOT).as_posix() for path in ROOT.rglob("copilot-instructions.md"))
    if copilots != [".github/copilot-instructions.md"]:
        failures.append(f"unexpected live Copilot instruction files: {copilots}")

    by_digest: dict[str, list[str]] = {}
    for path in markdown:
        digest = hashlib.sha256(path.read_bytes()).hexdigest()
        by_digest.setdefault(digest, []).append(path.relative_to(ROOT).as_posix())
    for duplicates in by_digest.values():
        if len(duplicates) > 1 and any(
            path in CANONICAL or "prompt" in Path(path).name.lower() for path in duplicates
        ):
            failures.append(f"duplicate canonical/prompt documents: {duplicates}")

    prompt_paths = sorted(
        path for path in relative if "prompt" in Path(path).name.lower() or "/prompts/" in path
    )
    if prompt_paths:
        failures.append(f"temporary prompt documents remain active: {prompt_paths}")

    if failures:
        for message in failures:
            print(f"documentation policy: {message}", file=sys.stderr)
        return 1
    print(f"Documentation policy: PASS ({len(current)} current documents, {len(relative)} Markdown files)")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
