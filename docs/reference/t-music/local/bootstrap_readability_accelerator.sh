#!/usr/bin/env bash
#
# bootstrap_readability_accelerator.sh
#
# Repo bootstrap for accelerated readability closure in com.tw.music.
#
# This script is designed to be run from a local clone of the repository
# on Pixel 9a Termux, or from any normal Linux-like clone used to prepare
# a Codex/Copilot PR.
#
# The script creates the repo-side tools, Codex environment helper scripts,
# compact reports, documentation, Git ignore rules, and optional Git/GitHub
# workflow required to move readability work from slow manual mapping batches
# into bulk candidate generation, safe class promotion, high-impact method
# review, and tail classification.
#
# The script intentionally does NOT:
#   - rename smali descriptors;
#   - edit app/apktool smali code;
#   - alter package identity com.tw.music;
#   - alter sharedUserId=android.uid.system;
#   - alter vendor/runtime contract surfaces;
#   - commit APKs, binaries, raw JADX output, archives, or full decompiler dumps.
#
# Pinned optional JADX policy:
#   - Version: 1.5.5
#   - Source: official GitHub release asset
#   - URL: https://github.com/skylot/jadx/releases/download/v1.5.5/jadx-1.5.5.zip
#   - Java: Java 11+ required by JADX; Java 17 preferred for Termux/Codex.
#   - Default: do not install; only verify/report availability.
#   - Install: set INSTALL_PINNED_JADX=1 to download/install into ~/.local/opt/jadx-1.5.5.
#   - Integrity: if JADX_SHA256 is set, verify checksum before install.
#                If unset, validate only by pinned URL + extracted `jadx --version`.
#
# Intended local clone:
#   /data/data/com.termux/files/home/repos/twm/twmusic
#
# Branch policy for this workstream:
#   - Branch from: cx/create-readability-improvement-plan-for-repository
#   - Merge/PR back into: cx/create-readability-improvement-plan-for-repository
#   - Work branch default: cx/readability-accelerator-tooling
#
# Typical local usage from anywhere on the Pixel/Termux device:
#   bash /path/to/bootstrap_readability_accelerator.sh --branch --commit
#
# Typical local usage from inside the repo:
#   mkdir -p local
#   cp /path/to/bootstrap_readability_accelerator.sh local/
#   chmod +x local/bootstrap_readability_accelerator.sh
#   ./local/bootstrap_readability_accelerator.sh --branch --commit
#
# Optional push/PR usage, assuming gh auth is already logged in:
#   ./local/bootstrap_readability_accelerator.sh --branch --commit --push --pr
#
# Optional Termux package bootstrap:
#   INSTALL_OPTIONAL_PACKAGES=1 ./local/bootstrap_readability_accelerator.sh --branch
#
# Optional pinned JADX install test:
#   INSTALL_PINNED_JADX=1 ./local/bootstrap_readability_accelerator.sh --branch
#
# Environment variables:
#   LOCAL_REPO_DIR
#       Repo clone used when the script is run from outside a git repo.
#       Default: /data/data/com.termux/files/home/repos/twm/twmusic
#
#   BRANCH_NAME
#       Work branch created by --branch.
#       Default: cx/readability-accelerator-tooling
#
#   BASE_BRANCH
#       Branch to branch from and PR/merge back into.
#       Default: cx/create-readability-improvement-plan-for-repository
#
#   COMMIT_MESSAGE
#       Commit message used by --commit.
#       Default: readability: add accelerated candidate tooling
#
#   INSTALL_OPTIONAL_PACKAGES
#       If set to 1 and Termux `pkg` exists, install baseline CLI packages.
#
#   INSTALL_PINNED_JADX
#       If set to 1, install pinned JADX 1.5.5 into ~/.local/opt.
#
#   JADX_SHA256
#       Optional expected SHA-256 for the downloaded jadx-1.5.5.zip.
#
#   MAX_CHANGED_FILES / MAX_ADDED_DELETED
#       Override diff guard thresholds when running generated guard script.
#
set -euo pipefail

LOCAL_REPO_DIR="${LOCAL_REPO_DIR:-/data/data/com.termux/files/home/repos/twm/twmusic}"
BRANCH_NAME="${BRANCH_NAME:-cx/readability-accelerator-tooling}"
BASE_BRANCH="${BASE_BRANCH:-cx/create-readability-improvement-plan-for-repository}"
COMMIT_MESSAGE="${COMMIT_MESSAGE:-readability: add accelerated candidate tooling}"

JADX_VERSION="${JADX_VERSION:-1.5.5}"
JADX_URL="${JADX_URL:-https://github.com/skylot/jadx/releases/download/v${JADX_VERSION}/jadx-${JADX_VERSION}.zip}"
JADX_INSTALL_DIR="${JADX_INSTALL_DIR:-$HOME/.local/opt/jadx-${JADX_VERSION}}"
JADX_ZIP="${JADX_ZIP:-$HOME/.local/opt/jadx-${JADX_VERSION}.zip}"

DO_BRANCH=0
DO_COMMIT=0
DO_PUSH=0
DO_PR=0

for arg in "$@"; do
  case "$arg" in
    --branch) DO_BRANCH=1 ;;
    --commit) DO_COMMIT=1 ;;
    --push) DO_PUSH=1 ;;
    --pr) DO_PR=1 ;;
    --help|-h)
      sed -n '1,140p' "$0"
      exit 0
      ;;
    *)
      echo "Unknown argument: $arg" >&2
      exit 2
      ;;
  esac
done

log() { printf '\n[readability-bootstrap] %s\n' "$*"; }
warn() { printf '\n[readability-bootstrap][WARN] %s\n' "$*" >&2; }
die() { printf '\n[readability-bootstrap][ERROR] %s\n' "$*" >&2; exit 1; }

require_repo_root() {
  # Prefer the current working directory when already inside a git repository.
  # If the script is run from elsewhere on the Pixel/Termux device, fall back to
  # the user's known local clone path. This keeps the workflow reliable for both
  # interactive Termux runs and Codex/Copilot-driven instructions.
  if git rev-parse --show-toplevel >/dev/null 2>&1; then
    REPO_ROOT="$(git rev-parse --show-toplevel)"
  elif [[ -d "$LOCAL_REPO_DIR/.git" ]]; then
    REPO_ROOT="$LOCAL_REPO_DIR"
  else
    die "Run this from inside a git repo clone, or set LOCAL_REPO_DIR to a valid clone. Tried: $LOCAL_REPO_DIR"
  fi

  cd "$REPO_ROOT"
  log "Repo root: $REPO_ROOT"
  log "Base branch for branch/PR: $BASE_BRANCH"
  log "Work branch: $BRANCH_NAME"
}

ensure_not_dirty_for_branch_switch() {
  if [[ "$DO_BRANCH" -eq 1 ]] && [[ -n "$(git status --short)" ]]; then
    warn "Working tree already has changes. The script will not switch branches automatically."
    warn "Commit/stash/clean first, or rerun without --branch."
    exit 1
  fi
}

maybe_create_branch() {
  [[ "$DO_BRANCH" -eq 1 ]] || return 0

  local current
  current="$(git branch --show-current)"

  if [[ "$current" == "$BRANCH_NAME" ]]; then
    log "Already on work branch $BRANCH_NAME"
    return 0
  fi

  # Make sure the requested base exists locally. If an origin remote is present,
  # fetch the specific base branch first so the new work branch starts from the
  # branch the user intends to merge back into, not from main by accident.
  if git remote get-url origin >/dev/null 2>&1; then
    log "Fetching base branch origin/$BASE_BRANCH"
    git fetch origin "$BASE_BRANCH":"refs/remotes/origin/$BASE_BRANCH" || {
      warn "Could not fetch origin/$BASE_BRANCH; will try local branch resolution."
    }
  fi

  if git rev-parse --verify "$BRANCH_NAME" >/dev/null 2>&1; then
    log "Checking out existing work branch $BRANCH_NAME"
    git checkout "$BRANCH_NAME"
    return 0
  fi

  if git rev-parse --verify "$BASE_BRANCH" >/dev/null 2>&1; then
    log "Creating $BRANCH_NAME from local $BASE_BRANCH"
    git checkout "$BASE_BRANCH"
    if git remote get-url origin >/dev/null 2>&1; then
      git pull --ff-only origin "$BASE_BRANCH" || warn "Could not fast-forward local $BASE_BRANCH; continuing from local state."
    fi
    git checkout -b "$BRANCH_NAME"
  elif git rev-parse --verify "origin/$BASE_BRANCH" >/dev/null 2>&1; then
    log "Creating $BRANCH_NAME from origin/$BASE_BRANCH"
    git checkout -b "$BRANCH_NAME" "origin/$BASE_BRANCH"
  else
    die "Base branch not found locally or on origin: $BASE_BRANCH"
  fi
}

safe_mkdirs() {
  mkdir -p \
    local \
    scripts/codex \
    tools/readability \
    docs/reports \
    docs/prompts \
    tmp/readability \
    tmp/jadx \
    "$HOME/.local/bin" \
    "$HOME/.local/opt"
}

install_optional_termux_packages() {
  if [[ "${INSTALL_OPTIONAL_PACKAGES:-0}" != "1" ]]; then
    log "Skipping optional Termux package install. Set INSTALL_OPTIONAL_PACKAGES=1 to enable."
    return 0
  fi

  if command -v pkg >/dev/null 2>&1; then
    log "Termux pkg detected; installing baseline optional packages."
    pkg update -y
    pkg install -y git gh python openjdk-17 unzip curl ripgrep || {
      warn "Optional Termux package install failed. Continue if tools already exist."
    }
  else
    warn "pkg not found; optional package install skipped."
  fi
}

install_pinned_jadx_if_requested() {
  if [[ "${INSTALL_PINNED_JADX:-0}" != "1" ]]; then
    log "Pinned JADX install disabled. Set INSTALL_PINNED_JADX=1 to enable."
    return 0
  fi

  command -v java >/dev/null 2>&1 || die "Java is required for JADX. Install Java 11+ first; Java 17 is preferred."

  mkdir -p "$HOME/.local/bin" "$HOME/.local/opt"

  log "Installing pinned JADX ${JADX_VERSION} from ${JADX_URL}"
  if [[ ! -f "$JADX_ZIP" ]]; then
    if command -v curl >/dev/null 2>&1; then
      curl -L --fail --retry 3 -o "$JADX_ZIP" "$JADX_URL"
    elif command -v wget >/dev/null 2>&1; then
      wget -O "$JADX_ZIP" "$JADX_URL"
    else
      die "Need curl or wget to download JADX."
    fi
  else
    log "Using existing $JADX_ZIP"
  fi

  if [[ -n "${JADX_SHA256:-}" ]]; then
    log "Verifying JADX_SHA256"
    printf '%s  %s\n' "$JADX_SHA256" "$JADX_ZIP" | sha256sum -c -
  else
    warn "JADX_SHA256 not set; validating by pinned URL + extracted version only."
  fi

  rm -rf "$JADX_INSTALL_DIR"
  mkdir -p "$JADX_INSTALL_DIR"

  command -v unzip >/dev/null 2>&1 || die "unzip is required to install JADX zip."
  unzip -q "$JADX_ZIP" -d "$JADX_INSTALL_DIR"

  local nested_bin
  nested_bin="$(find "$JADX_INSTALL_DIR" -type f -path '*/bin/jadx' | head -n 1 || true)"
  [[ -n "$nested_bin" ]] || die "Could not find bin/jadx after unzip."

  ln -sf "$nested_bin" "$HOME/.local/bin/jadx"
  local gui
  gui="$(find "$JADX_INSTALL_DIR" -type f -path '*/bin/jadx-gui' | head -n 1 || true)"
  [[ -n "$gui" ]] && ln -sf "$gui" "$HOME/.local/bin/jadx-gui"

  export PATH="$HOME/.local/bin:$PATH"
  if ! grep -qxF 'export PATH="$HOME/.local/bin:$PATH"' "$HOME/.bashrc" 2>/dev/null; then
    echo 'export PATH="$HOME/.local/bin:$PATH"' >> "$HOME/.bashrc"
  fi

  local actual
  actual="$(jadx --version | tr -d '\r' || true)"
  echo "[readability-bootstrap] jadx --version => $actual"
  echo "$actual" | grep -F "$JADX_VERSION" >/dev/null || die "Installed JADX version did not report ${JADX_VERSION}."
}

write_file() {
  local path="$1"
  mkdir -p "$(dirname "$path")"
  cat > "$path"
}

append_gitignore_once() {
  local line="$1"
  touch .gitignore
  grep -qxF "$line" .gitignore || printf '%s\n' "$line" >> .gitignore
}

update_gitignore() {
  log "Updating .gitignore"
  append_gitignore_once ""
  append_gitignore_once "# Local-only bootstrap entrypoint"
  append_gitignore_once "local/bootstrap_readability_accelerator.sh"

  append_gitignore_once ""
  append_gitignore_once "# Readability / reverse-engineering scratch outputs"
  append_gitignore_once "tmp/readability/"
  append_gitignore_once "tmp/jadx/"
  append_gitignore_once "out/jadx/"
  append_gitignore_once "out/readability/"
  append_gitignore_once "jadx-output/"
  append_gitignore_once "jadx-export/"
  append_gitignore_once "*.jobf"
  append_gitignore_once "*.jadx.kts.cache"

  append_gitignore_once ""
  append_gitignore_once "# Android/decompiler artifacts that must not enter readability PRs"
  append_gitignore_once "*.apk"
  append_gitignore_once "*.apks"
  append_gitignore_once "*.aab"
  append_gitignore_once "*.dex"
  append_gitignore_once "*.jar"
  append_gitignore_once "*.zip"
  append_gitignore_once "*.tar"
  append_gitignore_once "*.tar.gz"
}

create_codex_scripts() {
  log "Creating Codex helper scripts"

  write_file scripts/codex/setup_readability_env.sh <<'EOF'
#!/usr/bin/env bash
#
# setup_readability_env.sh
#
# Codex Cloud setup helper for com.tw.music readability work.
#
# Purpose:
#   Verify the toolchain needed by repo-side readability scripts and optionally
#   install pinned JADX 1.5.5 into ~/.local/opt. This script is designed for
#   Codex setup scripts, which run before the agent phase.
#
# Internet policy:
#   - Setup may download pinned JADX only when INSTALL_PINNED_JADX=1.
#   - Agent phase should normally keep internet disabled.
#
# JADX policy:
#   - Version: 1.5.5
#   - URL: https://github.com/skylot/jadx/releases/download/v1.5.5/jadx-1.5.5.zip
#   - Java: 11+ required; Java 17 preferred
#   - Optional checksum: set JADX_SHA256 to verify zip
#
set -euo pipefail

JADX_VERSION="${JADX_VERSION:-1.5.5}"
JADX_URL="${JADX_URL:-https://github.com/skylot/jadx/releases/download/v${JADX_VERSION}/jadx-${JADX_VERSION}.zip}"
JADX_INSTALL_DIR="${JADX_INSTALL_DIR:-$HOME/.local/opt/jadx-${JADX_VERSION}}"
JADX_ZIP="${JADX_ZIP:-$HOME/.local/opt/jadx-${JADX_VERSION}.zip}"

mkdir -p "$HOME/.local/bin" "$HOME/.local/opt" tmp/readability tmp/jadx

if ! grep -qxF 'export PATH="$HOME/.local/bin:$PATH"' "$HOME/.bashrc" 2>/dev/null; then
  echo 'export PATH="$HOME/.local/bin:$PATH"' >> "$HOME/.bashrc"
fi
export PATH="$HOME/.local/bin:$PATH"

echo "[setup] Python"
python3 --version

echo "[setup] Git"
git --version

echo "[setup] Java"
if command -v java >/dev/null 2>&1; then
  java -version
else
  echo "[setup][WARN] java not found; jadx/apktool may be unavailable."
fi

if [[ "${INSTALL_PINNED_JADX:-0}" == "1" ]]; then
  command -v java >/dev/null 2>&1 || {
    echo "[setup][ERROR] Java required for JADX. Install Java 11+ / configure Codex environment."
    exit 1
  }
  command -v unzip >/dev/null 2>&1 || {
    echo "[setup][ERROR] unzip required for pinned JADX install."
    exit 1
  }

  echo "[setup] Installing pinned JADX ${JADX_VERSION}"
  if [[ ! -f "$JADX_ZIP" ]]; then
    if command -v curl >/dev/null 2>&1; then
      curl -L --fail --retry 3 -o "$JADX_ZIP" "$JADX_URL"
    elif command -v wget >/dev/null 2>&1; then
      wget -O "$JADX_ZIP" "$JADX_URL"
    else
      echo "[setup][ERROR] curl or wget required to download JADX."
      exit 1
    fi
  fi

  if [[ -n "${JADX_SHA256:-}" ]]; then
    printf '%s  %s\n' "$JADX_SHA256" "$JADX_ZIP" | sha256sum -c -
  else
    echo "[setup][WARN] JADX_SHA256 unset; validating by pinned URL + version output only."
  fi

  rm -rf "$JADX_INSTALL_DIR"
  mkdir -p "$JADX_INSTALL_DIR"
  unzip -q "$JADX_ZIP" -d "$JADX_INSTALL_DIR"

  nested_bin="$(find "$JADX_INSTALL_DIR" -type f -path '*/bin/jadx' | head -n 1 || true)"
  if [[ -z "$nested_bin" ]]; then
    echo "[setup][ERROR] Could not find bin/jadx after unzip."
    exit 1
  fi

  ln -sf "$nested_bin" "$HOME/.local/bin/jadx"
  nested_gui="$(find "$JADX_INSTALL_DIR" -type f -path '*/bin/jadx-gui' | head -n 1 || true)"
  [[ -n "$nested_gui" ]] && ln -sf "$nested_gui" "$HOME/.local/bin/jadx-gui"

  actual="$(jadx --version | tr -d '\r' || true)"
  echo "[setup] jadx --version => $actual"
  echo "$actual" | grep -F "$JADX_VERSION" >/dev/null || {
    echo "[setup][ERROR] Installed JADX version mismatch."
    exit 1
  }
fi

echo "[setup] JADX"
command -v jadx >/dev/null 2>&1 && jadx --version || echo "[setup][WARN] jadx unavailable; smali-only readability tools still work."

echo "[setup] apktool"
command -v apktool >/dev/null 2>&1 && apktool --version || echo "[setup][WARN] apktool unavailable; repo scripts may provide build coverage."

echo "[setup] GitHub CLI"
command -v gh >/dev/null 2>&1 && gh --version | head -n 1 || echo "[setup][WARN] gh unavailable."

echo "[setup] Readability setup complete."
EOF
  chmod +x scripts/codex/setup_readability_env.sh

  write_file scripts/codex/maintain_readability_env.sh <<'EOF'
#!/usr/bin/env bash
#
# maintain_readability_env.sh
#
# Codex Cloud maintenance helper for cached readability environments.
#
# Purpose:
#   Recreate scratch directories and print tool versions when Codex resumes a
#   cached environment. Does not install or download tools.
#
set -euo pipefail

mkdir -p tmp/readability tmp/jadx
export PATH="$HOME/.local/bin:$PATH"

echo "[maintenance] Python"
python3 --version

echo "[maintenance] Git"
git --version

echo "[maintenance] Java"
command -v java >/dev/null 2>&1 && java -version || echo "[maintenance][WARN] java unavailable"

echo "[maintenance] JADX"
command -v jadx >/dev/null 2>&1 && jadx --version || echo "[maintenance][WARN] jadx unavailable"

echo "[maintenance] apktool"
command -v apktool >/dev/null 2>&1 && apktool --version || echo "[maintenance][WARN] apktool unavailable"

echo "[maintenance] Readability environment checked."
EOF
  chmod +x scripts/codex/maintain_readability_env.sh
}

create_candidate_generator() {
  log "Creating candidate generator"

  write_file tools/readability/02_generate_mapping_candidates.py <<'PYEOF'
#!/usr/bin/env python3
"""
Generate compact readability mapping candidates for com.tw.music.

This tool accelerates readability closure by scanning canonical apktool/smali
sources and producing a compact, reviewable candidate map.

It is intentionally conservative:
- no smali edits;
- no descriptor renames;
- no automatic mapping promotion;
- no large raw dumps;
- no decompiler output committed.

Candidate buckets:
- AUTO_CONFIRM_CLASS_SOURCE
- AUTO_CONFIRM_NAMED_INNER
- REVIEW_HIGH_IMPACT
- CLASSIFY_VENDOR_EXTERNAL
- CLASSIFY_SYNTHETIC_LOW_VALUE
- CLASSIFY_SUPPORT_THIRD_PARTY
- UNSAFE_REFLECTION_SERIALIZATION
- UNKNOWN_NEEDS_EVIDENCE
"""

from __future__ import annotations

import csv
import datetime as dt
import re
from dataclasses import dataclass
from pathlib import Path
from typing import Iterable

ROOT = Path(".")
SMALI_ROOTS = sorted(p for p in (ROOT / "app" / "apktool").glob("smali*") if p.is_dir())

MAPPING_FILE = ROOT / "mappings" / "manual-enigma" / "music-core.mapping"
OUT_MD = ROOT / "docs" / "reports" / "readability-candidate-map.md"
OUT_TSV = ROOT / "docs" / "reports" / "readability-candidate-map.tsv"

MAX_MD_ROWS_PER_BUCKET = 80

SUPPORT_PREFIXES = (
    "Landroid/support/",
    "Landroidx/",
    "Lcom/google/",
    "Lcom/squareup/",
    "Lokhttp3/",
    "Lokio/",
    "Lorg/apache/",
    "Lorg/json/",
    "Lorg/antlr/",
    "Linfo/monitorenter/",
)

VENDOR_TOKEN_PATTERNS = (
    "com.tw.music.action",
    "persist.tw.",
    "persist.media.",
    "com.tw.eq",
    "com.tw.radio",
    "com/tw/service/xt/aidl",
)

UNSAFE_PATTERNS = (
    "Ljava/lang/Class;->forName",
    "getDeclaredMethod",
    "getDeclaredField",
    "Ljava/lang/reflect/",
    "Ljava/io/Serializable;",
    "Parcelable",
    "Lorg/json/JSONObject;",
    "Lorg/json/JSONArray;",
    ".method public native",
    ".method private native",
    ".method protected native",
)

MINIFIED_NAME_RE = re.compile(r"^(?:[a-z]|[a-z]{1,2}|[A-Z]|[A-Z]{1,2}|C\d+[a-zA-Z]?|[a-z]\$[a-z0-9]+)$")


@dataclass(frozen=True)
class SmaliClass:
    descriptor: str
    path: Path
    source: str | None
    super_descriptor: str | None
    implements: tuple[str, ...]
    inner_name: str | None
    outer_descriptor: str | None
    body_sample: str

    @property
    def package_path(self) -> str:
        desc = self.descriptor.removeprefix("L").removesuffix(";")
        return "/".join(desc.split("/")[:-1])

    @property
    def simple_name(self) -> str:
        desc = self.descriptor.removeprefix("L").removesuffix(";")
        return desc.split("/")[-1]

    @property
    def source_stem(self) -> str | None:
        if not self.source:
            return None
        return self.source.removesuffix(".java").removesuffix(".kt")


@dataclass(frozen=True)
class Candidate:
    bucket: str
    descriptor: str
    proposed_name: str
    source: str
    evidence: str
    risk: str
    path: str


def read_text(path: Path) -> str:
    try:
        return path.read_text(encoding="utf-8", errors="replace")
    except FileNotFoundError:
        return ""


def existing_mapped_descriptors() -> set[str]:
    text = read_text(MAPPING_FILE)
    mapped: set[str] = set()
    for line in text.splitlines():
        if line.strip().startswith("#"):
            continue
        for match in re.findall(r"L[\w/$]+;", line):
            mapped.add(match)
        if "->" in line:
            left = line.split("->", 1)[0].strip()
            if "/" in left:
                mapped.add("L" + left.strip("L;") + ";")
    return mapped


def parse_smali_file(path: Path) -> SmaliClass | None:
    text = read_text(path)
    class_match = re.search(r"^\.class\b.*?(L[\w/$]+;)", text, re.M)
    if not class_match:
        return None

    source_match = re.search(r'^\.source\s+"([^"]+)"', text, re.M)
    super_match = re.search(r"^\.super\s+(L[\w/$]+;)", text, re.M)
    implements = tuple(re.findall(r"^\.implements\s+(L[\w/$]+;)", text, re.M))

    inner_name = None
    inner_match = re.search(r'name\s*=\s*"([^"]+)"', text)
    if inner_match and inner_match.group(1) != "null":
        inner_name = inner_match.group(1)

    outer_descriptor = None
    outer_match = re.search(r"EnclosingClass;.*?value\s*=\s*(L[\w/$]+;)", text, re.S)
    if outer_match:
        outer_descriptor = outer_match.group(1)

    return SmaliClass(
        descriptor=class_match.group(1),
        path=path,
        source=source_match.group(1) if source_match else None,
        super_descriptor=super_match.group(1) if super_match else None,
        implements=implements,
        inner_name=inner_name,
        outer_descriptor=outer_descriptor,
        body_sample=text[:16000],
    )


def iter_smali_classes() -> Iterable[SmaliClass]:
    for root in SMALI_ROOTS:
        for path in sorted(root.rglob("*.smali")):
            item = parse_smali_file(path)
            if item:
                yield item


def is_support_or_third_party(cls: SmaliClass) -> bool:
    return cls.descriptor.startswith(SUPPORT_PREFIXES)


def is_generated_resource_holder(cls: SmaliClass) -> bool:
    return cls.simple_name == "R" or cls.simple_name.startswith("R$") or cls.simple_name == "BuildConfig"


def is_vendor_sensitive(cls: SmaliClass) -> bool:
    text = cls.descriptor + "\n" + cls.body_sample
    return any(pattern in text for pattern in VENDOR_TOKEN_PATTERNS)


def has_unsafe_coupling(cls: SmaliClass) -> bool:
    return any(pattern in cls.body_sample for pattern in UNSAFE_PATTERNS)


def meaningful_source_name(cls: SmaliClass) -> bool:
    stem = cls.source_stem
    if not stem:
        return False
    if stem in {"R", "BuildConfig"} or stem.startswith("R$"):
        return False
    if MINIFIED_NAME_RE.match(stem):
        return False
    return True


def proposed_name_from_source(cls: SmaliClass) -> str:
    stem = cls.source_stem or cls.simple_name
    if "$" in cls.simple_name and cls.outer_descriptor:
        outer = cls.outer_descriptor.removeprefix("L").removesuffix(";").split("/")[-1]
        inner = cls.inner_name or stem
        return f"{outer}${inner}"
    return stem


def classify(cls: SmaliClass, mapped: set[str]) -> Candidate | None:
    if cls.descriptor in mapped:
        return None

    if is_generated_resource_holder(cls):
        return Candidate("CLASSIFY_SYNTHETIC_LOW_VALUE", cls.descriptor, cls.simple_name, cls.source or "", "generated Android resource holder", "ignore", str(cls.path))

    if is_support_or_third_party(cls):
        return Candidate("CLASSIFY_SUPPORT_THIRD_PARTY", cls.descriptor, cls.simple_name, cls.source or "", "support/third-party prefix", "low app-maintainer value", str(cls.path))

    if is_vendor_sensitive(cls):
        return Candidate("CLASSIFY_VENDOR_EXTERNAL", cls.descriptor, cls.simple_name, cls.source or "", "vendor/runtime token pattern present", "do not rename casually", str(cls.path))

    if has_unsafe_coupling(cls):
        return Candidate("UNSAFE_REFLECTION_SERIALIZATION", cls.descriptor, cls.simple_name, cls.source or "", "reflection/serialization/native coupling pattern present", "manual review or mapping-only", str(cls.path))

    if cls.inner_name:
        return Candidate("AUTO_CONFIRM_NAMED_INNER", cls.descriptor, proposed_name_from_source(cls), cls.source or "", f"non-null InnerClass name={cls.inner_name}; owner={cls.outer_descriptor or 'unknown'}", "class-level mapping-only appears safe; verify owner context", str(cls.path))

    if meaningful_source_name(cls):
        return Candidate("AUTO_CONFIRM_CLASS_SOURCE", cls.descriptor, proposed_name_from_source(cls), cls.source or "", ".source provides meaningful class-level name", "class-level mapping-only appears safe", str(cls.path))

    if "$" in cls.simple_name:
        return Candidate("CLASSIFY_SYNTHETIC_LOW_VALUE", cls.descriptor, cls.simple_name, cls.source or "", "anonymous/synthetic-looking inner without stable name", "defer unless high-impact", str(cls.path))

    if cls.package_path.startswith("com/eckom/xtlibrary/") or cls.package_path.startswith("com/tw/music/"):
        return Candidate("REVIEW_HIGH_IMPACT", cls.descriptor, cls.simple_name, cls.source or "", "app-owned unreadable class without automatic evidence", "manual review required", str(cls.path))

    return Candidate("UNKNOWN_NEEDS_EVIDENCE", cls.descriptor, cls.simple_name, cls.source or "", "unclassified unreadable symbol", "needs evidence", str(cls.path))


def write_reports(candidates: list[Candidate]) -> None:
    OUT_MD.parent.mkdir(parents=True, exist_ok=True)

    with OUT_TSV.open("w", encoding="utf-8", newline="") as f:
        writer = csv.writer(f, delimiter="\t")
        writer.writerow(["bucket", "descriptor", "proposed_name", "source", "evidence", "risk", "path"])
        for c in candidates:
            writer.writerow([c.bucket, c.descriptor, c.proposed_name, c.source, c.evidence, c.risk, c.path])

    buckets: dict[str, list[Candidate]] = {}
    for c in candidates:
        buckets.setdefault(c.bucket, []).append(c)

    now = dt.datetime.utcnow().strftime("%Y-%m-%d %H:%M:%S UTC")
    lines = [
        "# Readability Candidate Map",
        "",
        f"Generated: {now}",
        "",
        "Generated by `tools/readability/02_generate_mapping_candidates.py`.",
        "This is a compact review queue, not ground truth.",
        "",
        "## Bucket counts",
        "",
        "| Bucket | Count |",
        "|---|---:|",
    ]
    for bucket in sorted(buckets):
        lines.append(f"| `{bucket}` | {len(buckets[bucket])} |")

    for bucket in sorted(buckets):
        rows = buckets[bucket]
        lines.extend(["", f"## {bucket}", "", "| Descriptor | Proposed | Source | Evidence | Risk | Path |", "|---|---|---|---|---|---|"])
        for c in rows[:MAX_MD_ROWS_PER_BUCKET]:
            lines.append(f"| `{c.descriptor}` | `{c.proposed_name}` | `{c.source}` | {c.evidence} | {c.risk} | `{c.path}` |")
        if len(rows) > MAX_MD_ROWS_PER_BUCKET:
            lines.append(f"| … | … | … | {len(rows) - MAX_MD_ROWS_PER_BUCKET} more rows in TSV | … | … |")

    OUT_MD.write_text("\n".join(lines) + "\n", encoding="utf-8")


def main() -> int:
    mapped = existing_mapped_descriptors()
    candidates = [c for cls in iter_smali_classes() if (c := classify(cls, mapped))]
    candidates.sort(key=lambda c: (c.bucket, c.path, c.descriptor))
    write_reports(candidates)

    print(f"wrote {OUT_MD}")
    print(f"wrote {OUT_TSV}")
    counts: dict[str, int] = {}
    for c in candidates:
        counts[c.bucket] = counts.get(c.bucket, 0) + 1
    for bucket in sorted(counts):
        print(f"{bucket}: {counts[bucket]}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
PYEOF
  chmod +x tools/readability/02_generate_mapping_candidates.py
}

create_promoter() {
  log "Creating safe mapping promoter"

  write_file tools/readability/03_promote_safe_class_mappings.py <<'PYEOF'
#!/usr/bin/env python3
"""
Promote strict safe class/interface candidates into manual mapping docs.

Default mode is dry-run. Applying requires --apply-safe-class-mappings.

This tool never:
- edits smali;
- renames descriptors;
- touches resources;
- changes vendor/runtime contracts;
- promotes methods/fields.
"""

from __future__ import annotations

import argparse
import csv
import datetime as dt
from pathlib import Path

ROOT = Path(".")
CANDIDATE_TSV = ROOT / "docs" / "reports" / "readability-candidate-map.tsv"
MAPPING_FILE = ROOT / "mappings" / "manual-enigma" / "music-core.mapping"
EVIDENCE_FILE = ROOT / "mappings" / "manual-enigma" / "mapping-evidence.md"
NOTES_FILE = ROOT / "docs" / "deobf" / "enigma-notes.md"

SAFE_BUCKETS = {"AUTO_CONFIRM_CLASS_SOURCE", "AUTO_CONFIRM_NAMED_INNER"}


def read_text(path: Path) -> str:
    try:
        return path.read_text(encoding="utf-8", errors="replace")
    except FileNotFoundError:
        return ""


def descriptor_to_slash(desc: str) -> str:
    return desc.removeprefix("L").removesuffix(";")


def mapped_path(desc: str, proposed: str) -> str:
    original = descriptor_to_slash(desc)
    package = "/".join(original.split("/")[:-1])
    return f"{package}/{proposed}" if package else proposed


def existing_text() -> str:
    return "\n".join(read_text(p) for p in (MAPPING_FILE, EVIDENCE_FILE, NOTES_FILE))


def load_candidates(limit: int) -> list[dict[str, str]]:
    if not CANDIDATE_TSV.exists():
        raise SystemExit(f"missing {CANDIDATE_TSV}; run 02_generate_mapping_candidates.py first")

    existing = existing_text()
    selected: list[dict[str, str]] = []

    with CANDIDATE_TSV.open("r", encoding="utf-8", newline="") as f:
        reader = csv.DictReader(f, delimiter="\t")
        for row in reader:
            if row["bucket"] not in SAFE_BUCKETS:
                continue
            if row["descriptor"] in existing:
                continue
            new_path = mapped_path(row["descriptor"], row["proposed_name"])
            if new_path in existing:
                continue
            selected.append(row)
            if len(selected) >= limit:
                break
    return selected


def append_mapping(rows: list[dict[str, str]]) -> None:
    MAPPING_FILE.parent.mkdir(parents=True, exist_ok=True)
    with MAPPING_FILE.open("a", encoding="utf-8") as f:
        f.write("\n# Accelerated readability safe class mappings\n")
        for row in rows:
            f.write(f"{descriptor_to_slash(row['descriptor'])} -> {mapped_path(row['descriptor'], row['proposed_name'])}\n")


def append_evidence(rows: list[dict[str, str]]) -> None:
    EVIDENCE_FILE.parent.mkdir(parents=True, exist_ok=True)
    now = dt.datetime.utcnow().strftime("%Y-%m-%d UTC")
    with EVIDENCE_FILE.open("a", encoding="utf-8") as f:
        f.write("\n## Accelerated safe class mapping evidence\n\n")
        f.write(f"Generated: {now}\n\n")
        f.write("| Original | Mapping | Type | Confidence | Evidence | Files inspected | Risk/runtime note |\n")
        f.write("|---|---|---|---|---|---|---|\n")
        for row in rows:
            f.write(
                f"| `{row['descriptor']}` | `{mapped_path(row['descriptor'], row['proposed_name'])}` | "
                f"class | high | {row['evidence']}; source `{row['source']}` | `{row['path']}` | "
                f"{row['risk']}; runtime evidence not required for class-level mapping-only |\n"
            )


def append_notes(rows: list[dict[str, str]]) -> None:
    NOTES_FILE.parent.mkdir(parents=True, exist_ok=True)
    now = dt.datetime.utcnow().strftime("%Y-%m-%d UTC")
    with NOTES_FILE.open("a", encoding="utf-8") as f:
        f.write("\n## Accelerated readability mapping batch\n\n")
        f.write(f"Generated: {now}\n\n")
        for row in rows:
            f.write(f"- `{row['descriptor']}` → `{mapped_path(row['descriptor'], row['proposed_name'])}` ({row['evidence']}).\n")


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--dry-run", action="store_true", help="Print proposed mappings only.")
    parser.add_argument("--apply-safe-class-mappings", action="store_true", help="Append safe class mappings/docs.")
    parser.add_argument("--limit", type=int, default=80, help="Maximum candidates to promote.")
    args = parser.parse_args()

    if args.apply_safe_class_mappings and args.dry_run:
        raise SystemExit("choose dry-run or apply, not both")

    rows = load_candidates(args.limit)
    print(f"selected {len(rows)} safe candidates")
    for row in rows:
        print(f"{row['descriptor']} -> {mapped_path(row['descriptor'], row['proposed_name'])} [{row['bucket']}]")

    if not args.apply_safe_class_mappings:
        print("dry-run only; pass --apply-safe-class-mappings to update files")
        return 0

    append_mapping(rows)
    append_evidence(rows)
    append_notes(rows)
    print("applied safe class mappings")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
PYEOF
  chmod +x tools/readability/03_promote_safe_class_mappings.py
}

create_method_candidates() {
  log "Creating high-impact method candidate reporter"

  write_file tools/readability/04_high_impact_method_candidates.py <<'PYEOF'
#!/usr/bin/env python3
"""
Report high-impact method/interface/callback candidates.

This tool intentionally does not rename methods. It produces a compact review
queue for Codex/Copilot to inspect manually.

Targets include obvious boundary methods such as:
- run
- handleMessage
- doInBackground
- onPostExecute
- compare
- accept
- onClick
- Android lifecycle/interface overrides
- getter/setter-like DTO methods
"""

from __future__ import annotations

import datetime as dt
import re
from pathlib import Path

ROOT = Path(".")
SMALI_ROOTS = sorted(p for p in (ROOT / "app" / "apktool").glob("smali*") if p.is_dir())
OUT_MD = ROOT / "docs" / "reports" / "readability-high-impact-methods.md"

METHOD_RE = re.compile(r"^\.method\b(?P<decl>.+)$", re.M)
CLASS_RE = re.compile(r"^\.class\b.*?(L[\w/$]+;)", re.M)
SOURCE_RE = re.compile(r'^\.source\s+"([^"]+)"', re.M)

INTERESTING = (
    " run(",
    " handleMessage(",
    " doInBackground(",
    " onPostExecute(",
    " onProgressUpdate(",
    " compare(",
    " accept(",
    " onClick(",
    " onCreate(",
    " onStartCommand(",
    " onDestroy(",
    " onReceive(",
)


def read_text(path: Path) -> str:
    return path.read_text(encoding="utf-8", errors="replace")


def main() -> int:
    rows: list[tuple[str, str, str, str]] = []
    for root in SMALI_ROOTS:
        for path in sorted(root.rglob("*.smali")):
            text = read_text(path)
            cls = CLASS_RE.search(text)
            if not cls:
                continue
            source = SOURCE_RE.search(text)
            for m in METHOD_RE.finditer(text):
                decl = m.group("decl").strip()
                normalized = " " + decl.replace("->", " ")
                if any(x in normalized for x in INTERESTING):
                    rows.append((cls.group(1), source.group(1) if source else "", decl, str(path)))

    OUT_MD.parent.mkdir(parents=True, exist_ok=True)
    now = dt.datetime.utcnow().strftime("%Y-%m-%d %H:%M:%S UTC")
    lines = [
        "# Readability High-Impact Method Candidates",
        "",
        f"Generated: {now}",
        "",
        "This is a review queue only. Do not bulk-rename methods from this report.",
        "",
        "| Class | Source | Method declaration | Path |",
        "|---|---|---|---|",
    ]
    for cls, source, decl, path in rows[:200]:
        lines.append(f"| `{cls}` | `{source}` | `{decl}` | `{path}` |")
    if len(rows) > 200:
        lines.append(f"| … | … | {len(rows) - 200} more rows not shown | … |")

    OUT_MD.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(f"wrote {OUT_MD}")
    print(f"method candidates: {len(rows)}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
PYEOF
  chmod +x tools/readability/04_high_impact_method_candidates.py
}

create_tail_classifier() {
  log "Creating tail classifier"

  write_file tools/readability/05_tail_classification.py <<'PYEOF'
#!/usr/bin/env python3
"""
Classify remaining readability tail.

This is the mechanism that makes practical readability closure finite:
remaining symbols are intentionally classified instead of chased forever.
"""

from __future__ import annotations

import csv
import datetime as dt
from pathlib import Path

ROOT = Path(".")
CANDIDATE_TSV = ROOT / "docs" / "reports" / "readability-candidate-map.tsv"
OUT_CLASS = ROOT / "docs" / "reports" / "readability-tail-classification.md"
OUT_UNRESOLVED = ROOT / "docs" / "reports" / "readability-unresolved-queue.md"

TAIL_MAP = {
    "CLASSIFY_VENDOR_EXTERNAL": "vendor/external contract",
    "CLASSIFY_SUPPORT_THIRD_PARTY": "support/third-party",
    "CLASSIFY_SYNTHETIC_LOW_VALUE": "generated/synthetic or anonymous low-value",
    "UNSAFE_REFLECTION_SERIALIZATION": "unsafe reflection/JNI/serialization/resource coupling",
    "UNKNOWN_NEEDS_EVIDENCE": "unknown needs evidence",
    "REVIEW_HIGH_IMPACT": "high-impact unresolved",
}


def load_rows() -> list[dict[str, str]]:
    if not CANDIDATE_TSV.exists():
        raise SystemExit(f"missing {CANDIDATE_TSV}; run 02_generate_mapping_candidates.py first")
    with CANDIDATE_TSV.open("r", encoding="utf-8", newline="") as f:
        return list(csv.DictReader(f, delimiter="\t"))


def main() -> int:
    rows = load_rows()
    now = dt.datetime.utcnow().strftime("%Y-%m-%d %H:%M:%S UTC")

    buckets: dict[str, list[dict[str, str]]] = {}
    for row in rows:
        category = TAIL_MAP.get(row["bucket"])
        if category:
            buckets.setdefault(category, []).append(row)

    OUT_CLASS.parent.mkdir(parents=True, exist_ok=True)

    lines = [
        "# Readability Tail Classification",
        "",
        f"Generated: {now}",
        "",
        "This report classifies remaining unreadable symbols for practical readability closure.",
        "",
        "| Category | Count |",
        "|---|---:|",
    ]
    for category in sorted(buckets):
        lines.append(f"| {category} | {len(buckets[category])} |")

    for category in sorted(buckets):
        lines.extend(["", f"## {category}", "", "| Descriptor | Source | Evidence | Risk | Path |", "|---|---|---|---|---|"])
        for row in buckets[category][:100]:
            lines.append(f"| `{row['descriptor']}` | `{row['source']}` | {row['evidence']} | {row['risk']} | `{row['path']}` |")
        if len(buckets[category]) > 100:
            lines.append(f"| … | … | {len(buckets[category]) - 100} more not shown | … | … |")

    OUT_CLASS.write_text("\n".join(lines) + "\n", encoding="utf-8")

    unresolved = buckets.get("high-impact unresolved", []) + buckets.get("unknown needs evidence", [])
    u_lines = [
        "# Readability Unresolved Queue",
        "",
        f"Generated: {now}",
        "",
        "These are symbols requiring evidence, deferral, or manual review.",
        "",
        "| Descriptor | Bucket | Source | Evidence | Risk | Path |",
        "|---|---|---|---|---|---|",
    ]
    for row in unresolved[:150]:
        u_lines.append(f"| `{row['descriptor']}` | `{row['bucket']}` | `{row['source']}` | {row['evidence']} | {row['risk']} | `{row['path']}` |")
    if len(unresolved) > 150:
        u_lines.append(f"| … | … | … | {len(unresolved) - 150} more not shown | … | … |")

    OUT_UNRESOLVED.write_text("\n".join(u_lines) + "\n", encoding="utf-8")

    print(f"wrote {OUT_CLASS}")
    print(f"wrote {OUT_UNRESOLVED}")
    for category in sorted(buckets):
        print(f"{category}: {len(buckets[category])}")
    print(f"unresolved queue: {len(unresolved)}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
PYEOF
  chmod +x tools/readability/05_tail_classification.py
}

create_diff_guard() {
  log "Creating diff size guard"

  write_file tools/readability/06_diff_size_guard.sh <<'EOF'
#!/usr/bin/env bash
#
# Prevent oversized Codex/GitHub diffs during readability work.
#
set -euo pipefail

MAX_CHANGED_FILES="${MAX_CHANGED_FILES:-25}"
MAX_ADDED_DELETED="${MAX_ADDED_DELETED:-2500}"

echo "[diff-size] git diff --stat"
git diff --stat || true

echo "[diff-size] git diff --numstat"
git diff --numstat || true

changed_files="$(git diff --name-only | wc -l | tr -d ' ')"
added_deleted="$(git diff --numstat | awk '{if ($1 ~ /^[0-9]+$/) a+=$1; if ($2 ~ /^[0-9]+$/) d+=$2} END {print a+d+0}')"

echo "[diff-size] changed_files=$changed_files added_deleted=$added_deleted"
echo "[diff-size] limits changed_files<=$MAX_CHANGED_FILES added_deleted<=$MAX_ADDED_DELETED"

if [ "$changed_files" -gt "$MAX_CHANGED_FILES" ]; then
  echo "STOP: too many changed files for one readability pass. Split the work."
  exit 1
fi

if [ "$added_deleted" -gt "$MAX_ADDED_DELETED" ]; then
  echo "STOP: diff too large. Split the work."
  exit 1
fi

if git diff --name-only | grep -E '\.(apk|apks|aab|dex|jar|zip|tar|gz|png|jpg|jpeg|webp)$' >/dev/null 2>&1; then
  echo "STOP: binary/archive/generated artifact detected in diff."
  git diff --name-only | grep -E '\.(apk|apks|aab|dex|jar|zip|tar|gz|png|jpg|jpeg|webp)$' || true
  exit 1
fi

echo "[diff-size] OK"
EOF
  chmod +x tools/readability/06_diff_size_guard.sh
}

create_prompts_and_docs() {
  log "Creating prompt/instruction docs"

  write_file docs/prompts/readability-accelerator-bulk-promotion.md <<'EOF'
# Codex prompt — accelerated bulk safe class promotion

Continue accelerated readability closure in repo `com.tw.music`.

Run the candidate tooling and promote only strict safe class/interface candidates. Do not edit smali, rename descriptors, rename resources, promote methods/fields, or alter vendor/runtime contract surfaces.

Commands:

```bash
python3 tools/readability/01_inventory_symbols.py
python3 tools/readability/02_generate_mapping_candidates.py
python3 tools/readability/03_promote_safe_class_mappings.py --dry-run --limit 150
python3 tools/readability/03_promote_safe_class_mappings.py --apply-safe-class-mappings --limit 80
python3 tools/readability/01_inventory_symbols.py
python3 tools/readability/02_generate_mapping_candidates.py
python3 tools/readability/05_tail_classification.py
python3 tools/readability/04_high_impact_method_candidates.py
bash scripts/08_verify_vendor_tokens.sh
bash tools/readability/06_diff_size_guard.sh
git status --short
git diff --stat
git diff --numstat
```

Keep reports compact. Do not commit APKs, binaries, raw JADX output, archives, or oversized dumps.

Update:
- `docs/readability-closure-plan-2026-05.md`
- `docs/deobf/enigma-notes.md`
- `mappings/manual-enigma/music-core.mapping`
- `mappings/manual-enigma/mapping-evidence.md`
- compact reports under `docs/reports/`

Commit the slice if checks pass.

Final response must be a triple-tilde unbroken markdown codeblock including:
1. candidate counts by bucket;
2. mappings promoted;
3. tail classification summary;
4. commands run and outcomes;
5. diff-size guard result;
6. confirmation no large artifacts were committed;
7. next recommended accelerated pass.
EOF

  write_file docs/prompts/readability-accelerator-high-impact-methods.md <<'EOF'
# Codex prompt — high-impact method/interface review

Continue accelerated readability closure in repo `com.tw.music`.

Use `docs/reports/readability-high-impact-methods.md` as a review queue. Promote only high-confidence method/interface/callback labels with strong evidence. Do not run a broad method rename sweep.

Allowed targets:
- obvious getters/setters;
- Android/framework override methods;
- presenter/model/view interface boundary methods;
- callback methods with strongly typed arguments and clear call paths;
- `run`, `handleMessage`, `doInBackground`, `onPostExecute`, `compare`, `accept`, `onClick` when behaviour is explicit.

Do not edit smali descriptors. Prefer mapping/docs only unless separately justified.

Run:
```bash
python3 tools/readability/04_high_impact_method_candidates.py
bash scripts/08_verify_vendor_tokens.sh
bash tools/readability/06_diff_size_guard.sh
git status --short
git diff --stat
git diff --numstat
```

Final response must be a triple-tilde unbroken markdown codeblock.
EOF

  write_file docs/readability-accelerator-setup.md <<'EOF'
# Readability accelerator setup

This repo uses an accelerated readability closure workflow:

1. Generate compact candidates.
2. Promote strict safe class/interface mappings in bulk.
3. Review high-impact method/interface boundaries.
4. Classify the remaining tail.
5. Run vendor-token and diff-size guards.

## Codex Cloud setup

Set setup command:

```bash
INSTALL_PINNED_JADX=1 bash scripts/codex/setup_readability_env.sh
```

Set maintenance command:

```bash
bash scripts/codex/maintain_readability_env.sh
```

Keep agent internet off for normal tasks. The setup script installs pinned JADX 1.5.5 only if `INSTALL_PINNED_JADX=1`.

## Local / Termux setup

Optional baseline packages:

```bash
INSTALL_OPTIONAL_PACKAGES=1 ./local/bootstrap_readability_accelerator.sh
```

Optional pinned JADX install:

```bash
INSTALL_PINNED_JADX=1 ./local/bootstrap_readability_accelerator.sh
```

## Bulk safe class promotion

```bash
python3 tools/readability/01_inventory_symbols.py
python3 tools/readability/02_generate_mapping_candidates.py
python3 tools/readability/03_promote_safe_class_mappings.py --dry-run --limit 150
python3 tools/readability/03_promote_safe_class_mappings.py --apply-safe-class-mappings --limit 80
python3 tools/readability/01_inventory_symbols.py
python3 tools/readability/02_generate_mapping_candidates.py
python3 tools/readability/05_tail_classification.py
bash scripts/08_verify_vendor_tokens.sh
bash tools/readability/06_diff_size_guard.sh
```

## Guardrails

- No smali descriptor rename by default.
- No broad method/field rename sweep.
- No vendor/runtime surface mutation.
- No raw JADX exports, APKs, DEX files, archives, or oversized reports committed.
EOF
}

update_readability_plan() {
  local plan="docs/readability-closure-plan-2026-05.md"
  if [[ ! -f "$plan" ]]; then
    warn "$plan not found; skipping plan update."
    return 0
  fi

  if grep -q "Accelerated closure mode" "$plan"; then
    log "Accelerated closure mode already present in $plan."
    return 0
  fi

  log "Appending accelerated closure mode to $plan"
  cat >> "$plan" <<'EOF'

## Accelerated closure mode

This section records the strategy shift from slow manual package-by-package
mapping toward tool-assisted candidate generation, bulk safe class promotion,
high-impact method review, tail classification, and targeted closure.

### Purpose

Finish practical readability closure in a smaller number of larger but still
reviewable passes.

Practical closure means:
- high-impact app-owned symbols are readable via confirmed mapping/name or
  explicitly blocked by runtime/device evidence;
- remaining unreadable symbols are classified as vendor/external,
  support/third-party, generated/synthetic, unsafe, runtime-only unresolved,
  unknown-low-impact, or high-impact unresolved;
- vendor/runtime contract surfaces remain unchanged;
- no speculative descriptor/method/field rename is merged.

### Tooling

- `tools/readability/02_generate_mapping_candidates.py`
  generates compact candidate reports from canonical smali and existing mappings.

- `tools/readability/03_promote_safe_class_mappings.py`
  dry-runs or applies strict safe class-level mappings only. It never edits smali.

- `tools/readability/04_high_impact_method_candidates.py`
  generates a review queue for method/interface boundary naming. It never renames.

- `tools/readability/05_tail_classification.py`
  classifies remaining unreadable symbols so low-value/synthetic/vendor/unsafe
  tail items can be closed deliberately.

- `tools/readability/06_diff_size_guard.sh`
  prevents oversized diffs and accidental binary/generated artifact commits.

### Accelerated pass sequence

1. Candidate generation and tail classification.
2. Bulk safe class-level promotion from strict `AUTO_CONFIRM_*` buckets.
3. High-impact method/interface/callback review only where evidence is strong.
4. Notification/widget/foreground targeted closure or runtime-blocked classification.
5. Final tail classification and verification.

### Guardrails

- No smali descriptor rename unless separately scoped and proven safe.
- No broad method/field sweep.
- No vendor token/runtime surface mutation.
- No raw JADX exports, APKs, DEX files, archives, or oversized generated reports.
- Reports must remain compact and reviewable.
EOF
}

run_initial_tools() {
  log "Running initial tools"

  if [[ -x tools/readability/01_inventory_symbols.py || -f tools/readability/01_inventory_symbols.py ]]; then
    python3 tools/readability/01_inventory_symbols.py || warn "01_inventory_symbols.py failed; continue if repo state explains it."
  else
    warn "tools/readability/01_inventory_symbols.py not found."
  fi

  python3 tools/readability/02_generate_mapping_candidates.py
  python3 tools/readability/05_tail_classification.py
  python3 tools/readability/04_high_impact_method_candidates.py

  if [[ -x scripts/08_verify_vendor_tokens.sh || -f scripts/08_verify_vendor_tokens.sh ]]; then
    bash scripts/08_verify_vendor_tokens.sh
  else
    warn "scripts/08_verify_vendor_tokens.sh not found."
  fi

  bash tools/readability/06_diff_size_guard.sh
}

maybe_commit() {
  [[ "$DO_COMMIT" -eq 1 ]] || return 0

  log "Preparing filtered commit"
  git status --short
  git diff --stat
  git diff --numstat
  bash tools/readability/06_diff_size_guard.sh

  if [[ -z "$(git status --short)" ]]; then
    log "No changes to commit."
    return 0
  fi

  git add \
    .gitignore \
    scripts/codex/setup_readability_env.sh \
    scripts/codex/maintain_readability_env.sh \
    tools/readability/02_generate_mapping_candidates.py \
    tools/readability/03_promote_safe_class_mappings.py \
    tools/readability/04_high_impact_method_candidates.py \
    tools/readability/05_tail_classification.py \
    tools/readability/06_diff_size_guard.sh \
    docs/prompts/readability-accelerator-bulk-promotion.md \
    docs/prompts/readability-accelerator-high-impact-methods.md \
    docs/readability-accelerator-setup.md \
    docs/reports/readability-candidate-map.md \
    docs/reports/readability-candidate-map.tsv \
    docs/reports/readability-tail-classification.md \
    docs/reports/readability-unresolved-queue.md \
    docs/reports/readability-high-impact-methods.md \
    docs/readability-closure-plan-2026-05.md 2>/dev/null || true

  log "Staged files"
  git diff --cached --name-only
  git diff --cached --stat
  git diff --cached --numstat

  if git diff --cached --name-only | grep -E '\.(apk|apks|aab|dex|jar|zip|tar|gz|png|jpg|jpeg|webp)$' >/dev/null 2>&1; then
    die "Refusing to commit binary/archive/generated artifact."
  fi

  if [[ -z "$(git diff --cached --name-only)" ]]; then
    log "No staged changes after filtering."
    return 0
  fi

  git commit -m "$COMMIT_MESSAGE"
}

maybe_push() {
  [[ "$DO_PUSH" -eq 1 ]] || return 0
  log "Pushing branch"
  git push -u origin "$(git branch --show-current)"
}

maybe_pr() {
  [[ "$DO_PR" -eq 1 ]] || return 0
  command -v gh >/dev/null 2>&1 || die "gh not found; cannot create PR."

  log "Creating PR"
  gh pr create \
    --base "$BASE_BRANCH" \
    --head "$(git branch --show-current)" \
    --title "readability: add accelerated candidate tooling" \
    --body "$(cat <<'EOF'
Adds repo-side tooling and Codex environment helpers for accelerated readability closure.

Branch policy: this work branches from and targets `cx/create-readability-improvement-plan-for-repository`, not `main`.

This PR prepares the project to move from small manual mapping batches to:
- compact mapping candidate generation;
- strict safe class-level promotion;
- high-impact method review queue;
- tail classification;
- diff-size guarding;
- Codex/Copilot-friendly setup scripts.

No smali descriptor renames are performed.
No vendor/runtime contract surfaces are changed.
No APKs, binaries, raw JADX exports, or oversized artifacts are committed.
EOF
)"
}

main() {
  require_repo_root
  ensure_not_dirty_for_branch_switch
  maybe_create_branch
  safe_mkdirs
  install_optional_termux_packages
  install_pinned_jadx_if_requested

  update_gitignore
  create_codex_scripts
  create_candidate_generator
  create_promoter
  create_method_candidates
  create_tail_classifier
  create_diff_guard
  create_prompts_and_docs
  update_readability_plan
  run_initial_tools

  log "Final diff"
  git status --short
  git diff --stat
  git diff --numstat

  maybe_commit
  maybe_push
  maybe_pr

  log "Bootstrap complete."
  log "Next Codex pass: use docs/prompts/readability-accelerator-bulk-promotion.md"
}

main "$@"
