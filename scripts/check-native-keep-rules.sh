#!/usr/bin/env bash
set -euo pipefail

# Release native-crash guardrail.
#
# Classes that declare a Kotlin `external fun` are bound to a native (JNI) method that the
# corresponding `.so` resolves by the class's fully-qualified name. In a minified release build
# R8 may strip or rename such a class if it lacks an explicit `-keep`, producing a release-only
# `UnsatisfiedLinkError`/`NoSuchMethodError` that never reproduces in debug. This check fails
# closed when any JNI-bearing class is missing a keep rule, and when the duplicated musikr
# consumer/proguard rule sets drift apart.

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "${repo_root}"

# Module source root -> proguard rule files that must keep its JNI classes.
# musikr ships consumer rules (applied to the app that consumes the library) and its own
# proguard rules; both must keep the class.
musikr_src='musikr/src/main/java'
app_src='app/src/main/java'
musikr_proguard='musikr/proguard-rules.pro'
musikr_consumer='musikr/consumer-rules.pro'
app_proguard='app/proguard-rules.pro'

fail=0

# Collect files containing an `external fun` declaration.
list_jni_files() {
  local root="$1"
  [ -d "${root}" ] || return 0
  if command -v rg >/dev/null 2>&1; then
    rg -l --no-messages '\bexternal\s+fun\b' "${root}" -g '*.kt' || true
  else
    grep -RIl --include='*.kt' -E '\bexternal[[:space:]]+fun\b' "${root}" 2>/dev/null || true
  fi
}

# Derive the fully-qualified names of the enclosing object/class declarations in a file.
fqcns_for_file() {
  local file="$1"
  local pkg
  pkg="$(sed -n 's/^[[:space:]]*package[[:space:]]\+\([A-Za-z0-9_.]\+\).*/\1/p' "${file}" | head -n1)"
  [ -n "${pkg}" ] || return 0
  sed -n -E 's/^[[:space:]]*(internal |private |public |abstract |sealed |open |data )*(object|class)[[:space:]]+([A-Za-z0-9_]+).*/\3/p' "${file}" |
    while IFS= read -r name; do
      [ -n "${name}" ] && echo "${pkg}.${name}"
    done
}

# Does any keep rule file contain `-keep ... class <fqcn>`?
keep_rule_exists() {
  local fqcn="$1"
  shift
  local f
  for f in "$@"; do
    [ -f "${f}" ] || continue
    # Match the fqcn as a whole token (escape dots) on a -keep line.
    if grep -E "^[[:space:]]*-keep[a-z]*[[:space:]]+(class[[:space:]]+)?${fqcn//./\\.}([[:space:]]|\{|$)" "${f}" >/dev/null 2>&1; then
      return 0
    fi
  done
  return 1
}

check_module() {
  local label="$1"
  local root="$2"
  shift 2
  local rule_files=("$@")
  local file fqcn had_external
  while IFS= read -r file; do
    [ -n "${file}" ] || continue
    # Only consider files that genuinely declare external fun (rg -l already ensures this).
    had_external=0
    while IFS= read -r fqcn; do
      [ -n "${fqcn}" ] || continue
      had_external=1
      if keep_rule_exists "${fqcn}" "${rule_files[@]}"; then
        echo "OK: ${label} JNI class kept: ${fqcn}"
      else
        echo "ERROR: ${label} JNI class '${fqcn}' (declares 'external fun' in ${file}) has no -keep rule in: ${rule_files[*]}" >&2
        fail=1
      fi
    done < <(fqcns_for_file "${file}")
    if [ "${had_external}" -eq 0 ]; then
      echo "WARN: could not derive class name for JNI file ${file}" >&2
    fi
  done < <(list_jni_files "${root}")
}

echo "--- Native (JNI) keep-rule guardrail ---"

check_module "musikr" "${musikr_src}" "${musikr_proguard}" "${musikr_consumer}"
check_module "app" "${app_src}" "${app_proguard}"

# Sync check: every -keep line in the musikr consumer rules must also appear in the musikr
# proguard rules so the two duplicated files cannot silently drift.
if [ -f "${musikr_consumer}" ] && [ -f "${musikr_proguard}" ]; then
  sync_failed=0
  while IFS= read -r rule; do
    [ -n "${rule}" ] || continue
    if ! grep -Fxq -- "${rule}" "${musikr_proguard}"; then
      echo "ERROR: keep rule present in ${musikr_consumer} but missing from ${musikr_proguard}: ${rule}" >&2
      fail=1
      sync_failed=1
    fi
  done < <(grep -E '^[[:space:]]*-keep' "${musikr_consumer}" | sed 's/[[:space:]]*$//')
  if [ "${sync_failed}" -eq 0 ]; then
    echo "OK: musikr consumer/proguard keep rules are in sync"
  fi
fi

if [ "${fail}" -ne 0 ]; then
  echo "Result: FAIL" >&2
  exit 1
fi

echo "Result: PASS"
