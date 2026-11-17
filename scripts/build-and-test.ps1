$ErrorActionPreference = 'Stop'
Set-Location (Join-Path $PSScriptRoot '..')

if (-not ($env:ANDROID_HOME -or $env:ANDROID_SDK_ROOT -or (Test-Path '.\local.properties'))) {
    throw 'Android SDK path not found. Set ANDROID_HOME/ANDROID_SDK_ROOT or create local.properties with sdk.dir=...'
}

& .\gradlew.bat --version
if ($LASTEXITCODE -ne 0) { throw 'Gradle bootstrap/version check failed.' }
if (-not (Test-Path '.\gradle\wrapper\gradle-wrapper.jar')) {
    & .\gradlew.bat wrapper --gradle-version 9.3.1 --distribution-type bin
    if ($LASTEXITCODE -ne 0) { throw 'Gradle Wrapper generation failed.' }
    & .\gradlew.bat wrapper --gradle-version 9.3.1 --distribution-type bin
    if ($LASTEXITCODE -ne 0) { throw 'Gradle Wrapper refresh failed.' }
}
& .\scripts\verify-wrapper.ps1
& .\gradlew.bat clean lintDebug testDebugUnitTest assembleDebug bundleDebug
if ($LASTEXITCODE -ne 0) { throw 'Unit tests or debug APK/AAB build failed.' }

$apk = Join-Path (Get-Location) 'app\build\outputs\apk\debug\app-debug.apk'
$aab = Join-Path (Get-Location) 'app\build\outputs\bundle\debug\app-debug.aab'
if (-not (Test-Path $apk)) { throw "Debug APK was not produced: $apk" }
if (-not (Test-Path $aab)) { throw "Debug AAB was not produced: $aab" }
New-Item -ItemType Directory -Force -Path '.\artifacts' | Out-Null
Copy-Item $apk '.\artifacts\mizan-maroc-debug.apk' -Force
Copy-Item $aab '.\artifacts\mizan-maroc-debug.aab' -Force
Write-Host 'DEBUG APK: artifacts\mizan-maroc-debug.apk'
Write-Host 'DEBUG AAB: artifacts\mizan-maroc-debug.aab'

if ($env:KEYSTORE_PATH -and $env:STORE_PASSWORD -and $env:KEY_PASSWORD) {
    & .\gradlew.bat assembleRelease bundleRelease
    if ($LASTEXITCODE -ne 0) { throw 'Signed release APK/AAB build failed.' }
    $releaseApk = '.\app\build\outputs\apk\release\app-release.apk'
    $releaseAab = '.\app\build\outputs\bundle\release\app-release.aab'
    if (-not (Test-Path $releaseApk) -or -not (Test-Path $releaseAab)) { throw 'Release APK or AAB missing.' }
    Copy-Item $releaseApk '.\artifacts\mizan-maroc-release.apk' -Force
    Copy-Item $releaseAab '.\artifacts\mizan-maroc-release.aab' -Force
    Write-Host 'SIGNED RELEASE APK/AAB copied to artifacts.'
} else {
    Write-Host 'Release signing skipped. Set KEYSTORE_PATH, STORE_PASSWORD, KEY_PASSWORD and KEY_ALIAS for signed release files.'
}

if ($env:MIZAN_RUN_DEVICE_TESTS -eq '1') {
    $adbCommand = Get-Command adb -ErrorAction SilentlyContinue
    $sdkRoot = if ($env:ANDROID_HOME) { $env:ANDROID_HOME } else { $env:ANDROID_SDK_ROOT }
    $adbPath = if ($adbCommand) { $adbCommand.Source } else { $null }
    if (-not $adbPath -and $sdkRoot) {
        $candidate = Join-Path $sdkRoot 'platform-tools\adb.exe'
        if (Test-Path $candidate) { $adbPath = $candidate }
    }
    if (-not $adbPath) { throw 'adb not found. Install Android SDK Platform-Tools and add platform-tools to PATH.' }
    & $adbPath devices -l
    if ($LASTEXITCODE -ne 0) { throw 'adb devices failed.' }
    $devices = (& $adbPath devices | Select-Object -Skip 1 | Where-Object { $_ -match '\sdevice$' }).Count
    if ($devices -lt 1) { throw 'No authorized Android device/emulator. Enable USB debugging and accept the RSA prompt.' }
    & $adbPath install -r $apk
    if ($LASTEXITCODE -ne 0) { throw 'APK installation on the connected device failed.' }
    & $adbPath shell monkey -p ma.mizanmaroc.app 1
    Start-Sleep -Seconds 3
    & $adbPath shell am force-stop ma.mizanmaroc.app
    & .\gradlew.bat installDebug connectedDebugAndroidTest
    if ($LASTEXITCODE -ne 0) { throw 'Device installation or instrumented tests failed.' }
    Write-Host 'DEVICE TESTS OK'
} else {
    Write-Host 'Device tests skipped. Set MIZAN_RUN_DEVICE_TESTS=1 after connecting and authorizing your phone.'
}
