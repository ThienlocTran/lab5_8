# Lab 7 Build Script (PowerShell)
# Usage: .\build.ps1

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "Lab 7 - AJAX & REST API" -ForegroundColor Cyan
Write-Host "Build Script (PowerShell)" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

$projectPath = Get-Location
Write-Host "Project Path: $projectPath" -ForegroundColor Green
Write-Host ""

# Check if mvnw.cmd exists
if (Test-Path "mvnw.cmd") {
    Write-Host "Using Maven Wrapper (mvnw.cmd)" -ForegroundColor Yellow
    & .\mvnw.cmd clean package
} else {
    Write-Host "Using Global Maven" -ForegroundColor Yellow
    mvn clean package
}

Write-Host ""

if ($LASTEXITCODE -eq 0) {
    Write-Host "============================================" -ForegroundColor Green
    Write-Host "✅ Build Successful!" -ForegroundColor Green
    Write-Host "============================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Output: $projectPath\target\ROOT.war" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Next steps:" -ForegroundColor Yellow
    Write-Host "1. Copy ROOT.war to Tomcat webapps directory" -ForegroundColor White
    Write-Host "   Example: C:\Program Files\Apache\Tomcat 10\webapps\" -ForegroundColor Gray
    Write-Host ""
    Write-Host "2. Start Tomcat" -ForegroundColor White
    Write-Host "   Command: start %TOMCAT_HOME%\bin\startup.bat" -ForegroundColor Gray
    Write-Host ""
    Write-Host "3. Access application" -ForegroundColor White
    Write-Host "   Browser: http://localhost:8080" -ForegroundColor Gray
    Write-Host ""
    Write-Host "4. Run tests" -ForegroundColor White
    Write-Host "   Command: .\test_api.bat" -ForegroundColor Gray
    Write-Host ""
    
    # Ask if user wants to run test script
    $response = Read-Host "Run test_api.bat? (y/n)"
    if ($response -eq "y" -or $response -eq "Y") {
        if (Test-Path "test_api.bat") {
            & .\test_api.bat
        } else {
            Write-Host "test_api.bat not found" -ForegroundColor Red
        }
    }
} else {
    Write-Host "============================================" -ForegroundColor Red
    Write-Host "❌ Build Failed!" -ForegroundColor Red
    Write-Host "============================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Check the output above for errors" -ForegroundColor Yellow
    Write-Host ""
}

Write-Host ""
Read-Host "Press Enter to exit"
