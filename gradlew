#!/usr/bin/env sh
set -eu

GRADLE_VERSION="9.3.1"
if [ -n "${GRADLE_HOME:-}" ] && [ -x "$GRADLE_HOME/bin/gradle" ]; then
  exec "$GRADLE_HOME/bin/gradle" "$@"
fi
if command -v gradle >/dev/null 2>&1; then
  exec gradle "$@"
fi

CACHE_ROOT="${GRADLE_USER_HOME:-$HOME/.gradle}/mizan-bootstrap"
GRADLE_DIR="$CACHE_ROOT/gradle-$GRADLE_VERSION"
GRADLE_BIN="$GRADLE_DIR/bin/gradle"
if [ ! -x "$GRADLE_BIN" ]; then
  mkdir -p "$CACHE_ROOT"
  ZIP="$CACHE_ROOT/gradle-$GRADLE_VERSION-bin.zip"
  URL="https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip"
  echo "Gradle $GRADLE_VERSION is not installed; downloading official distribution..."
  if command -v curl >/dev/null 2>&1; then
    curl --fail --location --retry 3 "$URL" --output "$ZIP"
    : # SHA-256 is pinned below from Gradle's official release checksum list
  elif command -v wget >/dev/null 2>&1; then
    wget -O "$ZIP" "$URL"
    : # SHA-256 is pinned below from Gradle's official release checksum list
  else
    echo "ERROR: install Gradle $GRADLE_VERSION or curl/wget to bootstrap it." >&2
    exit 2
  fi
  EXPECTED="b266d5ff6b90eada6dc3b20cb090e3731302e553a27c5d3e4df1f0d76beaff06"
  if command -v sha256sum >/dev/null 2>&1; then
    ACTUAL="$(sha256sum "$ZIP" | awk '{print $1}')"
  else
    ACTUAL="$(shasum -a 256 "$ZIP" | awk '{print $1}')"
  fi
  [ "$EXPECTED" = "$ACTUAL" ] || { echo "ERROR: Gradle distribution SHA-256 mismatch." >&2; rm -f "$ZIP"; exit 7; }
  command -v unzip >/dev/null 2>&1 || { echo "ERROR: unzip is required." >&2; exit 2; }
  unzip -q -o "$ZIP" -d "$CACHE_ROOT"
fi
exec "$GRADLE_BIN" "$@"
