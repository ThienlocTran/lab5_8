@echo off
REM Lab 7 API Testing Script

setlocal enabledelayedexpansion

echo ============================================
echo Lab 7 - REST API Testing
echo ============================================
echo.

set SERVER=http://localhost:8080

echo.
echo [1/6] GET - All Employees
echo URL: %SERVER%/employees
echo.
curl -X GET %SERVER%/employees
echo.
pause

echo.
echo [2/6] GET - Single Employee (NV03)
echo URL: %SERVER%/employees/NV03
echo.
curl -X GET %SERVER%/employees/NV03
echo.
pause

echo.
echo [3/6] POST - Create New Employee (NV06)
echo URL: %SERVER%/employees
echo.
curl -X POST %SERVER%/employees ^
  -H "Content-Type: application/json" ^
  -d "{\"id\":\"NV06\",\"name\":\"Nhân viên 06\",\"gender\":false,\"salary\":9500.0}"
echo.
pause

echo.
echo [4/6] PUT - Update Employee (NV06)
echo URL: %SERVER%/employees/NV06
echo.
curl -X PUT %SERVER%/employees/NV06 ^
  -H "Content-Type: application/json" ^
  -d "{\"id\":\"NV06\",\"name\":\"Nguyễn Văn Tèo\",\"gender\":true,\"salary\":9500.0}"
echo.
pause

echo.
echo [5/6] DELETE - Delete Employee (NV06)
echo URL: %SERVER%/employees/NV06
echo.
curl -X DELETE %SERVER%/employees/NV06
echo.
pause

echo.
echo [6/6] GET - All Employees (After Delete)
echo URL: %SERVER%/employees
echo.
curl -X GET %SERVER%/employees
echo.

echo.
echo ============================================
echo Testing Complete
echo ============================================
pause
