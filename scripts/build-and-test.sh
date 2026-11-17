#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

if [[ -z "${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}" && ! -f "$ROOT/local.properties" ]]; then
  echo "ERROR: Android SDK path not found. Set ANDROID_HOME/ANDROID_SDK_ROOT or create local.properties." >&2
  exit 2
fi

./gradlew --version
if [[ ! -f "$ROOT/gradle/wrapper/gradle-wrapper.jar" ]]; then
  ./gradlew wrapper --gradle-version 9.3.1 --distribution-type bin
  ./gradlew wrapper --gradle-version 9.3.1 --distribution-type bin
fi
./scripts/verify-wrapper.sh
./gradlew clean lintDebug testDebugUnitTest assembleDebug bundleDebug

APK="$ROOT/app/build/outputs/apk/debug/app-debug.apk"
AAB="$ROOT/app/build/outputs/bundle/debug/app-debug.aab"
[[ -f "$APK" ]] || { echo "ERROR: Debug APK missing: $APK" >&2; exit 3; }
[[ -f "$AAB" ]] || { echo "ERROR: Debug AAB missing: $AAB" >&2; exit 3; }

mkdir -p "$ROOT/artifacts"
cp "$APK" "$ROOT/artifacts/mizan-maroc-debug.apk"
cp "$AAB" "$ROOT/artifacts/mizan-maroc-debug.aab"
echo "DEBUG APK: $ROOT/artifacts/mizan-maroc-debug.apk"
echo "DEBUG AAB: $ROOT/artifacts/mizan-maroc-debug.aab"

if [[ -n "${KEYSTORE_PATH:-}" && -n "${STORE_PASSWORD:-}" && -n "${KEY_PASSWORD:-}" ]]; then
  ./gradlew assembleRelease bundleRelease
  RELEASE_APK="$ROOT/app/build/outputs/apk/release/app-release.apk"
  RELEASE_AAB="$ROOT/app/build/outputs/bundle/release/app-release.aab"
  [[ -f "$RELEASE_APK" ]] || { echo "ERROR: Release APK missing." >&2; exit 6; }
  [[ -f "$RELEASE_AAB" ]] || { echo "ERROR: Release AAB missing." >&2; exit 6; }
  cp "$RELEASE_APK" "$ROOT/artifacts/mizan-maroc-release.apk"
  cp "$RELEASE_AAB" "$ROOT/artifacts/mizan-maroc-release.aab"
  echo "SIGNED RELEASE APK/AAB copied to artifacts/."
else
  echo "Release signing skipped: set KEYSTORE_PATH, STORE_PASSWORD, KEY_PASSWORD and KEY_ALIAS to build signed release files."
fi

if [[ "${MIZAN_RUN_DEVICE_TESTS:-0}" == "1" ]]; then
  ADB="$(command -v adb || true)"
  SDK_ROOT="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}"
  if [[ -z "$ADB" && -n "$SDK_ROOT" && -x "$SDK_ROOT/platform-tools/adb" ]]; then ADB="$SDK_ROOT/platform-tools/adb"; fi
  [[ -n "$ADB" ]] || { echo "ERROR: adb not found. Install Android SDK Platform-Tools." >&2; exit 4; }
  "$ADB" devices -l
  DEVICE_COUNT="$("$ADB" devices | awk 'NR>1 && $2=="device" {n++} END {print n+0}')"
  [[ "$DEVICE_COUNT" -ge 1 ]] || { echo "ERROR: No authorized Android device/emulator. Enable USB debugging and accept the RSA prompt." >&2; exit 5; }
  "$ADB" install -r "$APK"
  "$ADB" shell monkey -p ma.mizanmaroc.app 1 >/dev/null
  sleep 3
  "$ADB" shell am force-stop ma.mizanmaroc.app
  ./gradlew installDebug connectedDebugAndroidTest
  echo "DEVICE TESTS OK"
else
  echo "Device tests skipped. Connect an Android phone and run with MIZAN_RUN_DEVICE_TESTS=1."
fi
