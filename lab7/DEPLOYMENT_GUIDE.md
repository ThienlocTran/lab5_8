# Lab 7 - Deployment & Testing Guide

## 📋 Pre-requisites

- ✅ Java JDK 17 hoặc cao hơn
- ✅ Maven (hoặc mvnw.cmd trong project)
- ✅ Tomcat 10.1+ (hoặc đang dùng)
- ✅ Postman (optional, cho testing)
- ✅ cURL (Windows built-in hoặc git bash)

---

## 🔧 Step 1: Build Project

### Option A: Using Maven (Global)
```bash
cd D:\Java4\Lab5_8\lab7
mvn clean package
```

### Option B: Using Maven Wrapper
```bash
cd D:\Java4\Lab5_8\lab7
mvnw.cmd clean package
```

### Option C: Using Build Script
```bash
cd D:\Java4\Lab5_8\lab7
build.bat
```

### Expected Output
```
BUILD SUCCESS
Total time: XX seconds
Final Name: ROOT.war
```

### Output Location
```
D:\Java4\Lab5_8\lab7\target\ROOT.war
```

---

## 📦 Step 2: Deploy to Tomcat

### Find Tomcat Directory
```bash
# If using IDE (IntelliJ):
C:\Program Files\Apache\Tomcat 10

# Or custom location
$TOMCAT_HOME
```

### Manual Deployment

#### Option A: Copy WAR File (Recommended)
```bash
# Windows PowerShell
Copy-Item "D:\Java4\Lab5_8\lab7\target\ROOT.war" "$TOMCAT_HOME\webapps\"

# Windows Command Prompt
copy "D:\Java4\Lab5_8\lab7\target\ROOT.war" "%TOMCAT_HOME%\webapps\"
```

#### Option B: Using IDE
1. Right-click project → Configuration
2. Select Tomcat Server
3. Deploy on Server

---

## 🚀 Step 3: Start Tomcat

### Option A: Startup Script
```bash
# Windows
$TOMCAT_HOME\bin\startup.bat

# Linux/Mac
$TOMCAT_HOME/bin/startup.sh
```

### Option B: IDE
- IntelliJ: Run → Run on Server
- Eclipse: Run → Run on Server

### Option C: Docker (if using)
```bash
docker run -p 8080:8080 -v "$PWD/target:/usr/local/tomcat/webapps" tomcat:10.1
```

### Wait for Startup
```
[Catalina] Server startup in X ms
```

---

## ✅ Step 4: Verify Deployment

### Check if application is running
```bash
curl http://localhost:8080/
```

Expected: HTML content or welcome page

### Check specific endpoints
```bash
# Check if REST API is available
curl http://localhost:8080/employees

# Expected Response (JSON array):
# [
#   {"id":"NV01","name":"Nhân viên 01","gender":true,"salary":500.0},
#   ...
# ]
```

---

## 🧪 Step 5: Test Each Lesson

### BƯỚC 5.1: Test Bài 1 - AJAX JSON

**Browser Test:**
```
1. Open: http://localhost:8080/ajax-json-example.html
2. Click: "Load Employee Data" button
3. Press: F12 → Console tab
4. Verify: JSON data appears in console
   {"manv":"TeoNV","hoTen":"Nguyễn Văn Tèo","gioiTinh":true,"luong":950.5}
```

**cURL Test:**
```bash
curl http://localhost:8080/api/employee-json
```

---

### BƯỚC 5.2: Test Bài 2 - File Upload

**Browser Test:**
```
1. Open: http://localhost:8080/file-upload-ajax.html
2. Select: Any file (txt, pdf, image, etc.)
3. Click: "Upload File" button
4. Press: F12 → Console tab
5. Verify: Upload result appears
   {"name":"filename.ext","type":"file/mime-type","size":12345}
6. Check: File saved to webapps/ROOT/uploads/
```

**Batch Test:**
```bash
# Create a test file first
echo "Test file content" > test.txt

# Upload using cURL
curl -X POST -F "file=@test.txt" http://localhost:8080/api/upload
```

---

### BƯỚC 5.3: Test Bài 3 - REST API

#### Using test_api.bat Script
```bash
cd D:\Java4\Lab5_8\lab7
test_api.bat
```

This will automatically test:
1. GET /employees - Get all
2. GET /employees/NV03 - Get specific
3. POST /employees - Create new
4. PUT /employees/NV06 - Update
5. DELETE /employees/NV06 - Delete
6. GET /employees - Verify delete

#### Using Postman

**Setup Collections:**

1. **Create → GET All Employees**
   ```
   Method: GET
   URL: http://localhost:8080/employees
   ```

2. **Create → GET Single**
   ```
   Method: GET
   URL: http://localhost:8080/employees/NV03
   ```

3. **Create → POST Create**
   ```
   Method: POST
   URL: http://localhost:8080/employees
   Headers: Content-Type: application/json
   Body (raw):
   {
     "id": "NV06",
     "name": "Nhân viên 06",
     "gender": false,
     "salary": 9500.0
   }
   ```

4. **Create → PUT Update**
   ```
   Method: PUT
   URL: http://localhost:8080/employees/NV06
   Headers: Content-Type: application/json
   Body (raw):
   {
     "id": "NV06",
     "name": "Nguyễn Văn Tèo",
     "gender": true,
     "salary": 10000.0
   }
   ```

5. **Create → DELETE**
   ```
   Method: DELETE
   URL: http://localhost:8080/employees/NV06
   ```

**Run Tests:**
- Select each request and click "Send"
- Check response status and body
- Expected: 200 OK with appropriate response

#### Using Manual cURL

```bash
# 1. Get all employees
echo "[1] GET /employees"
curl http://localhost:8080/employees
echo.

# 2. Get specific employee
echo "[2] GET /employees/NV03"
curl http://localhost:8080/employees/NV03
echo.

# 3. Create new employee
echo "[3] POST /employees"
curl -X POST http://localhost:8080/employees ^
  -H "Content-Type: application/json" ^
  -d "{\"id\":\"NV06\",\"name\":\"Nhân viên 06\",\"gender\":false,\"salary\":9500.0}"
echo.

# 4. Update employee
echo "[4] PUT /employees/NV06"
curl -X PUT http://localhost:8080/employees/NV06 ^
  -H "Content-Type: application/json" ^
  -d "{\"id\":\"NV06\",\"name\":\"Nguyễn Văn Tèo\",\"gender\":true,\"salary\":10000.0}"
echo.

# 5. Delete employee
echo "[5] DELETE /employees/NV06"
curl -X DELETE http://localhost:8080/employees/NV06
echo.

# 6. Verify delete
echo "[6] GET /employees (after delete)"
curl http://localhost:8080/employees
echo.
```

---

### BƯỚC 5.4: Test Bài 4 - Web Client

**Browser Test:**
```
1. Open: http://localhost:8080/employee-rest-client.html

2. Initial Load:
   - Table should show 5 employees (NV01-NV05)
   - Form should be empty

3. Test Edit:
   - Click "Edit" on any row
   - Form fills with employee data
   - Verify all fields populated

4. Test Create:
   - Click "Reset"
   - Fill form: ID=NV06, Name=Test, Gender=Male, Salary=5000
   - Click "Create"
   - New employee appears in table
   - Form clears

5. Test Update:
   - Click "Edit" on NV06
   - Change name to "Updated Name"
   - Click "Update"
   - Table updates with new name
   - Form clears

6. Test Delete:
   - Click "Edit" on NV06
   - Click "Delete"
   - Employee removed from table
   - Form clears
   - List back to 5 employees

7. Verify Console:
   - F12 → Console tab
   - Should see network requests logs
   - No errors
```

---

## 📊 Test Result Summary

### Expected Results

| Test | URL | Method | Expected Status |
|------|-----|--------|-----------------|
| Bài 1 | /ajax-json-example.html | Browser | 200 OK |
| Bài 1 | /api/employee-json | GET | 200 OK, JSON response |
| Bài 2 | /file-upload-ajax.html | Browser | 200 OK |
| Bài 2 | /api/upload | POST | 200 OK, JSON response |
| Bài 3 | /employees | GET | 200 OK, array |
| Bài 3 | /employees/NV03 | GET | 200 OK, object |
| Bài 3 | /employees | POST | 200 OK, created object |
| Bài 3 | /employees/NV06 | PUT | 200 OK, {} |
| Bài 3 | /employees/NV06 | DELETE | 200 OK, {} |
| Bài 4 | /employee-rest-client.html | Browser | 200 OK |
| Bài 4 | CRUD ops | All | All working |

---

## 🐛 Troubleshooting

### ❌ Connection Refused (Port 8080)

**Problem:**
```
curl: (7) Failed to connect to localhost port 8080: Connection refused
```

**Solutions:**
```bash
# 1. Check Tomcat is running
# Windows Task Manager → Search "Catalina" or "Tomcat"

# 2. Start Tomcat
$TOMCAT_HOME\bin\startup.bat

# 3. Wait 30 seconds for startup

# 4. Check port
netstat -ano | findstr :8080

# 5. If port in use, kill process or change port
taskkill /PID <PID> /F

# 6. Check firewall
# Allow java.exe on port 8080
```

---

### ❌ 404 Not Found

**Problem:**
```
{"status":404,"message":"Not Found"}
```

**Solutions:**
```bash
# 1. Verify URL is correct
http://localhost:8080/employees        ✅ Correct
http://localhost:8080/api/employees    ❌ Wrong (no /api for REST API)

# 2. Check deployment
# - ROOT.war copied to webapps?
# - Tomcat auto-deployed?
# - Check webapps folder

# 3. Restart Tomcat
$TOMCAT_HOME\bin\shutdown.bat
$TOMCAT_HOME\bin\startup.bat

# 4. Check Tomcat logs
# File: $TOMCAT_HOME\logs\catalina.out
cat $TOMCAT_HOME/logs/catalina.out | tail -50
```

---

### ❌ 500 Internal Server Error

**Problem:**
```
HTTP/1.1 500 Internal Server Error
```

**Solutions:**
```bash
# 1. Check server logs
tail -100 $TOMCAT_HOME/logs/catalina.out

# 2. Check console output in IDE

# 3. Common causes:
# - Missing dependency (Jackson, FileUpload)
# - Compilation error
# - NullPointerException in code

# 4. Rebuild
mvn clean package

# 5. Redeploy
# Delete webapps/ROOT folder
# Copy new ROOT.war
# Restart Tomcat
```

---

### ❌ CORS Error

**Problem:**
```
Access to XMLHttpRequest from origin 'http://localhost:8080' 
has been blocked by CORS policy
```

**Solution:**
```
✅ Already fixed in this project!
- CorsFilter.java is included
- Sets Access-Control-Allow-* headers
- Allows all origins (for dev only)
```

---

### ❌ File Upload Not Working

**Problem:**
```
Upload fails or file not saved
```

**Solutions:**
```bash
# 1. Check /uploads directory exists
dir %TOMCAT_HOME%\webapps\ROOT\uploads\

# 2. Check permissions
# Tomcat user must have write access

# 3. Check Tomcat logs for errors
tail $TOMCAT_HOME/logs/catalina.out

# 4. Verify servlet is deployed
# Check if FileUploadServlet appears in logs
```

---

### ❌ JSON Parse Error

**Problem:**
```
SyntaxError: Unexpected token < in JSON at position 0
```

**Solutions:**
```
1. Response is not JSON (maybe HTML error page)
2. Check server returned valid JSON
3. Validate JSON format
4. Check Content-Type header

cURL test:
curl -i http://localhost:8080/api/employee-json
# Look for: Content-Type: application/json
```

---

## 📝 Logs Location

### Tomcat Logs
```
Windows: %TOMCAT_HOME%\logs\catalina.out
Linux:   $TOMCAT_HOME/logs/catalina.out
```

### IDE Logs
- **IntelliJ**: Services → Tomcat → Console tab
- **Eclipse**: Console → Check Eclipse console

### Browser Logs
- **F12**: Developer Tools
- **Console tab**: JavaScript errors & logs
- **Network tab**: HTTP requests/responses

---

## ✨ Complete Testing Workflow

```
1. Build:           mvn clean package ✅
2. Deploy:          Copy ROOT.war to webapps/ ✅
3. Start Tomcat:    startup.bat ✅
4. Wait:            30 seconds ✅
5. Verify:          curl localhost:8080 ✅
6. Test Bài 1:      Browser + console ✅
7. Test Bài 2:      Browser + console ✅
8. Test Bài 3:      Postman / cURL / test_api.bat ✅
9. Test Bài 4:      Browser + CRUD ✅
10. Check logs:     No errors ✅
11. Submit:         Ready for grading ✅
```

---

## 🎓 Summary

After following this guide, you will have:
- ✅ Built Lab 7 project successfully
- ✅ Deployed to Tomcat
- ✅ Verified all 4 lessons working
- ✅ Tested each feature
- ✅ Generated logs for verification

All 4 main lessons (Bài 1-4) are complete and ready for grading!

---

**Last Updated**: 2025-12-04
**Version**: 1.0
**Status**: Ready for Deployment
