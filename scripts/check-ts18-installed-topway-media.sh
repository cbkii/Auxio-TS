#!/usr/bin/env bash
# Read-only preflight for the exact Auxio-TS package identity used by DoFun validation.

check_installed_package() {
    local adb_bin=${ADB_BIN:-adb}
    local packages
    local debug_installed=0
    local release_installed=0

    printf '%s\n' 'Checking installed packages for DoFun integration...'

    if ! command -v "$adb_bin" >/dev/null 2>&1; then
        printf 'ERROR: adb command not found: %s\n' "$adb_bin" >&2
        return 1
    fi

    if ! "$adb_bin" shell "echo 1" >/dev/null 2>&1; then
        printf '%s\n' 'ERROR: no reachable/authorised adb device.' >&2
        return 1
    fi

    if ! packages=$("$adb_bin" shell pm list packages 2>/dev/null); then
        printf '%s\n' 'ERROR: package-manager query failed.' >&2
        return 1
    fi

    if printf '%s\n' "$packages" | grep -Fxq 'package:com.tw.media.debug'; then
        debug_installed=1
    fi
    if printf '%s\n' "$packages" | grep -Fxq 'package:com.tw.media'; then
        release_installed=1
    fi

    if (( debug_installed )); then
        printf '%s\n' 'WARNING: com.tw.media.debug is installed.'
        printf '%s\n' 'DoFun fixed-identity matching requires the package to be exactly com.tw.media.'
        printf '%s\n' 'The .debug suffix is not a valid fixed-identity qualification target.'
        printf '%s\n' 'Uninstall com.tw.media.debug and install the signed com.tw.media release APK.'
        if (( release_installed )); then
            printf '%s\n' 'WARNING: both debug and release packages are installed; remove the debug package before qualification.'
        fi
        return 1
    fi

    if (( release_installed )); then
        printf '%s\n' 'SUCCESS: com.tw.media is installed and ready for DoFun integration testing.'
        return 0
    fi

    printf '%s\n' 'WARNING: neither com.tw.media nor com.tw.media.debug is installed.'
    printf '%s\n' 'Install the signed com.tw.media release APK before DoFun integration testing.'
    return 1
}

self_test() {
    local temp_dir
    local fake_adb
    local failures=0

    if ! temp_dir=$(mktemp -d "${TMPDIR:-/tmp}/auxio-ts-package-check.XXXXXX"); then
        printf '%s\n' 'self-test: unable to create temporary directory' >&2
        return 1
    fi
    fake_adb="$temp_dir/adb"

    cat > "$fake_adb" <<'EOF'
#!/usr/bin/env bash
mode=${FAKE_ADB_MODE:-missing}

if [[ ${1:-} == shell && ${2:-} == 'echo 1' ]]; then
    [[ $mode != no-device ]]
    exit $?
fi

if [[ ${1:-} == shell && ${2:-} == pm && ${3:-} == list && ${4:-} == packages ]]; then
    case "$mode" in
        release) printf '%s\n' 'package:com.tw.media' ;;
        debug) printf '%s\n' 'package:com.tw.media.debug' ;;
        both) printf '%s\n' 'package:com.tw.media' 'package:com.tw.media.debug' ;;
        missing) printf '%s\n' 'package:org.example.other' ;;
        query-fail) exit 3 ;;
        *) printf 'fake adb: unsupported mode: %s\n' "$mode" >&2; exit 64 ;;
    esac
    exit 0
fi

printf 'fake adb: unexpected arguments: %s\n' "$*" >&2
exit 64
EOF
    chmod 700 "$fake_adb"

    run_case() {
        local name=$1
        local mode=$2
        local expected=$3
        local actual

        if FAKE_ADB_MODE="$mode" ADB_BIN="$fake_adb" check_installed_package >/dev/null 2>&1; then
            actual=0
        else
            actual=$?
        fi
        if [[ $actual -ne $expected ]]; then
            printf 'self-test: %s expected rc=%s, got rc=%s\n' "$name" "$expected" "$actual" >&2
            failures=$((failures + 1))
        fi
    }

    run_case release release 0
    run_case debug debug 1
    run_case both both 1
    run_case missing missing 1
    run_case no-device no-device 1
    run_case query-fail query-fail 1

    rm -rf -- "$temp_dir"
    if (( failures )); then
        printf 'TS18 package preflight self-test: FAIL (%d case(s))\n' "$failures" >&2
        return 1
    fi
    printf '%s\n' 'TS18 package preflight self-test: PASS'
    return 0
}

usage() {
    printf 'Usage: %s [--self-test|--help]\n' "${0##*/}"
}

main() {
    case ${1:-} in
        '') check_installed_package ;;
        --self-test) self_test ;;
        --help|-h) usage; return 0 ;;
        *)
            usage >&2
            return 2
            ;;
    esac
}

main "$@"
exit $?
