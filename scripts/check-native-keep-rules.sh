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

# Derive the fully-qualified names of the type(s) that actually *enclose* an `external fun` in a
# file. A brace-depth stack tracks the current type nesting, so this correctly handles nested and
# `companion object` declarations and any declaration keyword (`class`, `enum class`, `data class`,
# `sealed`/`value`/`annotation`/`inner class`, `object`, `interface`). Nested names are joined with
# `$` to match the runtime/ProGuard binary name (e.g. `Outer$Inner`). Only types that contain a
# JNI method are emitted, so unrelated types in the same file are not forced to carry keep rules.
fqcns_for_file() {
  local file="$1"
  local pkg
  pkg="$(sed -n 's/^[[:space:]]*package[[:space:]]\+\([A-Za-z0-9_.]\+\).*/\1/p' "${file}" | head -n1)"
  [ -n "${pkg}" ] || return 0
  awk -v pkg="${pkg}" '
    BEGIN { sp = 0; depth = 0 }
    function emit(   i, s) {
      s = pkg
      for (i = 0; i < sp; i++) {
        s = s (i == 0 ? "." : "$") stack[i]
      }
      if (!(s in seen)) {
        seen[s] = 1
        print s
      }
    }
    {
      line = $0
      sub(/\/\/.*/, "", line)   # drop trailing line comments (approximate)
      preDepth = depth

      # Detect a type declaration and record its simple name. companion objects may be unnamed
      # (compiled to "Companion"); every other form carries an explicit name after the keyword.
      name = ""
      if (match(line, /(^|[^A-Za-z0-9_.])companion[ \t]+object([ \t]+[A-Za-z_][A-Za-z0-9_]*)?/)) {
        seg = substr(line, RSTART, RLENGTH)
        if (match(seg, /object[ \t]+[A-Za-z_][A-Za-z0-9_]*/)) {
          name = substr(seg, RSTART, RLENGTH); sub(/object[ \t]+/, "", name)
        } else {
          name = "Companion"
        }
      } else if (match(line, /(^|[^A-Za-z0-9_.])(class|interface|object)[ \t]+[A-Za-z_][A-Za-z0-9_]*/)) {
        seg = substr(line, RSTART, RLENGTH)
        name = seg; sub(/.*[ \t]+/, "", name)
      }
      if (name != "") {
        stack[sp] = name
        declDepth[sp] = preDepth
        sp++
      }

      if (line ~ /(^|[^A-Za-z0-9_.])external[ \t]+fun([^A-Za-z0-9_]|$)/ && sp > 0) {
        emit()
      }

      # Update running brace depth, then pop any type whose body has closed.
      n = gsub(/\{/, "{", line)
      m = gsub(/\}/, "}", line)
      depth += n - m
      while (sp > 0 && depth <= declDepth[sp - 1]) {
        sp--
      }
    }
  ' "${file}"
}

# Does any keep rule file contain `-keep ... class <fqcn>`?
keep_rule_exists() {
  local fqcn="$1"
  shift
  # Escape ERE metacharacters in the FQCN: '.' (any char) and '$' (end-of-line anchor, used as
  # the nested-class separator in binary names such as MetadataResult$Success).
  local esc="${fqcn//./\\.}"
  esc="${esc//\$/\\$}"
  local f
  for f in "$@"; do
    [ -f "${f}" ] || continue
    # Match the fqcn as a whole token on a -keep line.
    if grep -E "^[[:space:]]*-keep[a-z]*[[:space:]]+(class[[:space:]]+)?${esc}([[:space:]]|\{|$)" "${f}" >/dev/null 2>&1; then
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

# Sync check: the musikr consumer rules and proguard rules carry the same set of -keep rules, so
# the two duplicated files cannot silently drift in either direction. A rule added to one file but
# not the other (regardless of which) is a failure.
if [ -f "${musikr_consumer}" ] && [ -f "${musikr_proguard}" ]; then
  sync_failed=0
  # left=source file, right=file that must also contain each of left's -keep rules.
  check_keep_subset() {
    local left="$1" right="$2" rule
    while IFS= read -r rule; do
      [ -n "${rule}" ] || continue
      if ! grep -Fxq -- "${rule}" "${right}"; then
        echo "ERROR: keep rule present in ${left} but missing from ${right}: ${rule}" >&2
        fail=1
        sync_failed=1
      fi
    done < <(grep -E '^[[:space:]]*-keep' "${left}" | sed 's/[[:space:]]*$//')
  }
  check_keep_subset "${musikr_consumer}" "${musikr_proguard}"
  check_keep_subset "${musikr_proguard}" "${musikr_consumer}"
  if [ "${sync_failed}" -eq 0 ]; then
    echo "OK: musikr consumer/proguard keep rules are in sync"
  fi
fi

if [ "${fail}" -ne 0 ]; then
  echo "Result: FAIL" >&2
  exit 1
fi

echo "Result: PASS"
