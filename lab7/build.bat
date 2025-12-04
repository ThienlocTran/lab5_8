@echo off
REM Lab 7 Build Script

echo ============================================
echo Lab 7 - AJAX & REST API
echo Build Script
echo ============================================
echo.

echo Building project...
cd /d "%~dp0"

if exist mvnw.cmd (
    echo Using mvnw...
    call mvnw.cmd clean package
) else (
    echo Using global Maven...
    call mvn clean package
)

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ============================================
    echo Build Successful!
    echo ============================================
    echo.
    echo Output: %~dp0target\ROOT.war
    echo.
    echo Next steps:
    echo 1. Copy ROOT.war to Tomcat webapps directory
    echo 2. Start Tomcat
    echo 3. Access: http://localhost:8080
    echo.
    pause
) else (
    echo.
    echo ============================================
    echo Build Failed!
    echo ============================================
    pause
)
