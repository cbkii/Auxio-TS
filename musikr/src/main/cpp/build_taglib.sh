#!/usr/bin/env bash

# Build the pinned TagLib dependency only for explicitly requested Android ABIs.
# Default maintained lanes are the physical arm64 TS18 and hosted x86_64 emulator.

WORKING_DIR=${1:-}
NDK_PATH=${2:-}
REQUESTED_ABIS=${3:-arm64-v8a,x86_64}

fail() {
  printf 'FAILED: %s\n' "$1" >&2
  exit 1
}

[ -n "$WORKING_DIR" ] || fail 'missing working-directory argument'
[ -n "$NDK_PATH" ] || fail 'missing Android NDK path argument'
[ -d "$WORKING_DIR" ] || fail "working directory does not exist: $WORKING_DIR"
[ -d "$NDK_PATH" ] || fail "Android NDK directory does not exist: $NDK_PATH"
command -v cmake >/dev/null 2>&1 || fail 'cmake is required'

TAGLIB_SRC_DIR="$WORKING_DIR/taglib"
TAGLIB_DST_DIR="$TAGLIB_SRC_DIR/build"
TAGLIB_PKG_DIR="$TAGLIB_SRC_DIR/pkg"
NDK_TOOLCHAIN="$WORKING_DIR/android.toolchain.cmake"

[ -d "$TAGLIB_SRC_DIR" ] || fail "TagLib source directory does not exist: $TAGLIB_SRC_DIR"
[ -f "$NDK_TOOLCHAIN" ] || fail "Android toolchain file does not exist: $NDK_TOOLCHAIN"

printf 'Working directory: %s\n' "$WORKING_DIR" >&2
printf 'TagLib source: %s\n' "$TAGLIB_SRC_DIR" >&2
printf 'Requested ABIs: %s\n' "$REQUESTED_ABIS" >&2

if command -v nproc >/dev/null 2>&1; then
  BUILD_JOBS=$(nproc)
else
  BUILD_JOBS=1
fi
case "$BUILD_JOBS" in
  ''|*[!0-9]*) BUILD_JOBS=1 ;;
  0) BUILD_JOBS=1 ;;
esac

build_for_arch() {
  local arch=$1
  local dst_dir="$TAGLIB_DST_DIR/$arch"
  local pkg_dir="$TAGLIB_PKG_DIR/$arch"

  case "$arch" in
    arm64-v8a|x86_64|armeabi-v7a|x86) ;;
    *) fail "unsupported Android ABI requested: $arch" ;;
  esac

  if [ -f "$pkg_dir/lib/libtag.a" ] && [ -d "$pkg_dir/include/taglib" ]; then
    printf 'TagLib %s already prepared; reusing validated package.\n' "$arch" >&2
    return 0
  fi

  printf 'Configuring TagLib for %s...\n' "$arch" >&2
  if ! cmake -S "$TAGLIB_SRC_DIR" -B "$dst_dir" \
    -DANDROID_NDK_PATH="$NDK_PATH" \
    -DCMAKE_TOOLCHAIN_FILE="$NDK_TOOLCHAIN" \
    -DANDROID_ABI="$arch" \
    -DBUILD_SHARED_LIBS=OFF \
    -DVISIBILITY_HIDDEN=ON \
    -DBUILD_TESTING=OFF \
    -DBUILD_EXAMPLES=OFF \
    -DBUILD_BINDINGS=OFF \
    -DWITH_ZLIB=OFF \
    -DCMAKE_BUILD_TYPE=Release \
    -DWITH_APE=OFF \
    -DWITH_ASF=OFF \
    -DWITH_MOD=OFF \
    -DWITH_SHORTEN=OFF \
    -DWITH_TRUEAUDIO=OFF \
    -DCMAKE_CXX_FLAGS=-fPIC; then
    fail "TagLib configure failed for $arch"
  fi

  printf 'Building TagLib for %s with %s job(s)...\n' "$arch" "$BUILD_JOBS" >&2
  if ! cmake --build "$dst_dir" --config Release --parallel "$BUILD_JOBS"; then
    fail "TagLib build failed for $arch"
  fi

  if ! cmake --install "$dst_dir" --config Release --prefix "$pkg_dir" --strip; then
    fail "TagLib install failed for $arch"
  fi

  [ -f "$pkg_dir/lib/libtag.a" ] || fail "TagLib package missing libtag.a for $arch"
  [ -d "$pkg_dir/include/taglib" ] || fail "TagLib package missing headers for $arch"
  printf 'Prepared TagLib for %s.\n' "$arch" >&2
}

OLD_IFS=$IFS
IFS=','
read -r -a ABI_LIST <<< "$REQUESTED_ABIS"
IFS=$OLD_IFS

[ "${#ABI_LIST[@]}" -gt 0 ] || fail 'no Android ABIs requested'
for abi in "${ABI_LIST[@]}"; do
  [ -n "$abi" ] || fail 'empty ABI entry in requested ABI list'
  build_for_arch "$abi"
done

printf 'SUCCESS: TagLib prepared for %s\n' "$REQUESTED_ABIS" >&2
