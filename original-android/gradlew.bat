@echo off
setlocal
set "GRADLE_VERSION=9.3.1"
if "%GRADLE_USER_HOME%"=="" set "GRADLE_USER_HOME=%USERPROFILE%\.gradle"
set "DIST=%GRADLE_USER_HOME%\trustcraft-wrapper\gradle-%GRADLE_VERSION%"
if not exist "%DIST%\bin\gradle.bat" (
  if not exist "%GRADLE_USER_HOME%\trustcraft-wrapper" mkdir "%GRADLE_USER_HOME%\trustcraft-wrapper"
  set "ARCHIVE=%GRADLE_USER_HOME%\trustcraft-wrapper\gradle-%GRADLE_VERSION%-bin.zip"
  if not exist "%GRADLE_USER_HOME%\trustcraft-wrapper\gradle-%GRADLE_VERSION%-bin.zip" powershell -NoProfile -Command "Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip' -OutFile '%GRADLE_USER_HOME%\trustcraft-wrapper\gradle-%GRADLE_VERSION%-bin.zip'"
  powershell -NoProfile -Command "Expand-Archive -Force '%GRADLE_USER_HOME%\trustcraft-wrapper\gradle-%GRADLE_VERSION%-bin.zip' '%GRADLE_USER_HOME%\trustcraft-wrapper'"
)
call "%DIST%\bin\gradle.bat" -p "%~dp0" %*
exit /b %ERRORLEVEL%
