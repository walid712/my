#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")/.."
JAR="gradle/wrapper/gradle-wrapper.jar"
EXPECTED_FILE="gradle/wrapper/gradle-wrapper.jar.sha256"
[ -f "$JAR" ] || { echo "gradle-wrapper.jar is missing. Generate it with: gradle wrapper --gradle-version 9.3.1 --distribution-type bin" >&2; exit 2; }
EXPECTED="$(awk '{print $1}' "$EXPECTED_FILE")"
if command -v sha256sum >/dev/null 2>&1; then
  ACTUAL="$(sha256sum "$JAR" | awk '{print $1}')"
else
  ACTUAL="$(shasum -a 256 "$JAR" | awk '{print $1}')"
fi
[ "$EXPECTED" = "$ACTUAL" ] || { echo "Wrapper JAR checksum mismatch" >&2; exit 7; }
echo "Gradle Wrapper JAR checksum OK"
