# Lab 7 - Getting Started (5 phút)

## ⚡ Quick Start

### 1. Build (1 phút)
```bash
cd D:\Java4\Lab5_8\lab7
build.bat
# hoặc: mvnw.cmd clean package
```

✅ Output: `target/ROOT.war`

### 2. Deploy (1 phút)
```bash
# Copy ROOT.war to Tomcat
copy target\ROOT.war %TOMCAT_HOME%\webapps\
```

### 3. Start Tomcat (1 phút)
```bash
# Start server
%TOMCAT_HOME%\bin\startup.bat
# Wait ~30 seconds
```

### 4. Open Browser (1 phút)
```
http://localhost:8080
```

### 5. Test (1 phút)
- Click links để test từng bài
- Bài 1 & 2: Browser test
- Bài 3: Use Postman or `test_api.bat`
- Bài 4: Full CRUD test

---

## 📖 Files Overview

| File | Purpose |
|------|---------|
| **index.html** | Home page - navigate giữa các bài |
| **ajax-json-example.html** | Bài 1: Load JSON data |
| **file-upload-ajax.html** | Bài 2: Upload file |
| **/employees API** | Bài 3: REST CRUD (use Postman) |
| **employee-rest-client.html** | Bài 4: Web UI quản lý nhân viên |

---

## 🧪 Quick Test

### Bài 1: JSON Response
```
1. Go to: http://localhost:8080/ajax-json-example.html
2. Click "Load Employee Data"
3. Open F12 → Console
4. See: Employee data in console
✅ Done
```

### Bài 2: File Upload
```
1. Go to: http://localhost:8080/file-upload-ajax.html
2. Select any file
3. Click "Upload File"
4. Open F12 → Console
5. See: File info in console
✅ Done
```

### Bài 3: REST API
```
1. Run: test_api.bat
   OR
2. Open Postman
3. Test each endpoint (GET, POST, PUT, DELETE)
✅ Done
```

### Bài 4: Web Client
```
1. Go to: http://localhost:8080/employee-rest-client.html
2. Create: Add new employee
3. Edit: Click Edit and modify
4. Delete: Delete employee
5. Table updates automatically
✅ Done
```

---

## 🚨 If Something Goes Wrong

### Port Already in Use
```bash
# Kill process on 8080
netstat -ano | findstr :8080
taskkill /PID <number> /F
```

### 404 Error
```bash
# Stop Tomcat
%TOMCAT_HOME%\bin\shutdown.bat
# Delete old deployment
rmdir /s %TOMCAT_HOME%\webapps\ROOT
# Redeploy
copy target\ROOT.war %TOMCAT_HOME%\webapps\
# Restart
%TOMCAT_HOME%\bin\startup.bat
```

### JSON Error
```bash
# Check endpoint responding
curl http://localhost:8080/employees
# Should return JSON array
```

---

## 📚 Documentation

For more details, see:
- `README.md` - Full overview
- `LAB7_IMPLEMENTATION_GUIDE.md` - Detailed guide
- `TEST_URLS.md` - All test URLs & commands
- `DEPLOYMENT_GUIDE.md` - Step-by-step deployment
- `COMPLETION_SUMMARY.md` - What was implemented

---

## ✅ Checklist

- [ ] Build successful (no errors)
- [ ] ROOT.war copied to webapps
- [ ] Tomcat started
- [ ] http://localhost:8080 loads
- [ ] Bài 1 works (JSON appears in console)
- [ ] Bài 2 works (File uploads and shows info)
- [ ] Bài 3 works (REST API responds to requests)
- [ ] Bài 4 works (Web UI CRUD operations work)
- [ ] No console errors (F12 → Console)
- [ ] Ready to submit

---

## 💡 Key URLs

```
Home:           http://localhost:8080/
Bài 1:          http://localhost:8080/ajax-json-example.html
Bài 2:          http://localhost:8080/file-upload-ajax.html
Bài 3 API:      http://localhost:8080/employees (REST)
Bài 4:          http://localhost:8080/employee-rest-client.html
```

---

## 🎯 Total Points

| Bài | Points | Status |
|-----|--------|--------|
| 1   | 2      | ✅ Done |
| 2   | 2      | ✅ Done |
| 3   | 2      | ✅ Done |
| 4   | 2      | ✅ Done |
| 5   | 2      | ⏳ Pending |
| **Total** | **10** | **8/10** |

---

**Time Estimate**: 5-10 minutes total
**Difficulty**: Intermediate
**Status**: Ready to Deploy

Next step: Run `build.bat` and deploy! 🚀
