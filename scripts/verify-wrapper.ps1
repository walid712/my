$ErrorActionPreference = 'Stop'
Set-Location (Join-Path $PSScriptRoot '..')
$jar = '.\gradle\wrapper\gradle-wrapper.jar'
if (-not (Test-Path $jar)) { throw 'gradle-wrapper.jar is missing. Run: gradle wrapper --gradle-version 9.3.1 --distribution-type bin' }
$expected = (Get-Content '.\gradle\wrapper\gradle-wrapper.jar.sha256' -Raw).Trim().Split(' ')[0].ToLowerInvariant()
$actual = (Get-FileHash $jar -Algorithm SHA256).Hash.ToLowerInvariant()
if ($actual -ne $expected) { throw "Gradle Wrapper JAR checksum mismatch: expected $expected, got $actual" }
Write-Host 'Gradle Wrapper JAR checksum OK'
