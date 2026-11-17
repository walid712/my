param(
    [Parameter(ValueFromRemainingArguments = $true)]
    [string[]] $GradleArgs
)
$ErrorActionPreference = 'Stop'
$GradleVersion = '9.3.1'

if ($env:GRADLE_HOME -and (Test-Path (Join-Path $env:GRADLE_HOME 'bin\gradle.bat'))) {
    & (Join-Path $env:GRADLE_HOME 'bin\gradle.bat') @GradleArgs
    exit $LASTEXITCODE
}
$installed = Get-Command gradle -ErrorAction SilentlyContinue
if ($installed) {
    & $installed.Source @GradleArgs
    exit $LASTEXITCODE
}

$cacheRoot = if ($env:GRADLE_USER_HOME) { Join-Path $env:GRADLE_USER_HOME 'mizan-bootstrap' } else { Join-Path $HOME '.gradle\mizan-bootstrap' }
$gradleDir = Join-Path $cacheRoot "gradle-$GradleVersion"
$gradleBat = Join-Path $gradleDir 'bin\gradle.bat'
if (-not (Test-Path $gradleBat)) {
    New-Item -ItemType Directory -Force -Path $cacheRoot | Out-Null
    $zip = Join-Path $cacheRoot "gradle-$GradleVersion-bin.zip"
    $url = "https://services.gradle.org/distributions/gradle-$GradleVersion-bin.zip"
    Write-Host "Gradle $GradleVersion is not installed; downloading official distribution..."
    Invoke-WebRequest -Uri $url -OutFile $zip
    $expected = 'b266d5ff6b90eada6dc3b20cb090e3731302e553a27c5d3e4df1f0d76beaff06'
    $actual = (Get-FileHash -Path $zip -Algorithm SHA256).Hash.ToLowerInvariant()
    if ($actual -ne $expected) { Remove-Item $zip -Force; throw 'Gradle distribution SHA-256 mismatch.' }
    Expand-Archive -Path $zip -DestinationPath $cacheRoot -Force
}
& $gradleBat @GradleArgs
exit $LASTEXITCODE
