# 🔧 FIX Bài 1: CORS Error + 404 Not Found

## 🔴 Vấn Đề Hiện Tại

### Error 1: CORS Policy Blocked
```
Access to fetch at 'http://localhost:8080/api/employee-json' 
from origin 'http://localhost:63342' 
has been blocked by CORS policy
```

**Nguyên nhân:**
- Bạn đang chạy HTML file từ **IDE (port 63342)**
- Servlet chạy trên **Tomcat (port 8080)**
- 2 ports khác nhau = CORS violation

### Error 2: 404 Not Found
```
GET http://localhost:8080/api/employee-json 404 (Not Found)
```

**Nguyên nhân:**
- HTML file chưa được deploy đúng
- Servlet có thể chưa được deploy

---

## ✅ Giải Pháp (3 cách)

### Cách 1: Deploy HTML File Đúng (RECOMMENDED) ✅

**Problem:** HTML file chỉ nằm trong IDE, chưa copy vào Tomcat

**Solution:**

#### Step 1: Build Project
```bash
cd D:\Java4\Lab5_8\lab7_api
mvn clean package
```

Expected output:
```
BUILD SUCCESS
target/ROOT.war created
```

#### Step 2: Deploy WAR File
```bash
copy target\ROOT.war %TOMCAT_HOME%\webapps\
```

**Note:** Thay `%TOMCAT_HOME%` bằng đường dẫn Tomcat của bạn
- Ví dụ: `C:\Program Files\Apache\Tomcat 10`

#### Step 3: Restart Tomcat
```bash
# Shutdown
%TOMCAT_HOME%\bin\shutdown.bat

# Wait 5 seconds

# Startup
%TOMCAT_HOME%\bin\startup.bat

# Wait 30 seconds
```

#### Step 4: Test URL
```
http://localhost:8080/ajax-json-example.html
```

**Kết quả:** ✅ CORS error biến mất, 404 biến mất

**Why it works:**
- HTML file được serve từ Tomcat (port 8080)
- Servlet cũng từ Tomcat (port 8080)
- Same origin = No CORS error

---

### Cách 2: Add CORS Filter (Quick Fix)

Nếu bạn muốn chạy HTML từ IDE ngay, add CORS header:

#### Step 1: Tạo CorsFilter
**File:** `src/main/java/com/thienloc/jakarta/lab58/filter/CorsFilter.java`

```java
package com.thienloc.jakarta.lab58.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
            FilterChain chain) throws IOException, ServletException {
        
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Add CORS headers
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", 
            "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", 
            "Content-Type, Authorization");
        
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
```

#### Step 2: Build & Deploy
```bash
mvn clean package
copy target\ROOT.war %TOMCAT_HOME%\webapps\
```

#### Step 3: Restart Tomcat
```bash
%TOMCAT_HOME%\bin\shutdown.bat
%TOMCAT_HOME%\bin\startup.bat
```

#### Step 4: Test từ IDE
```
http://localhost:63342/ajax-json-example.html
```

**Now CORS error gone!** ✅

**Why it works:**
- `Access-Control-Allow-Origin: *` = Allow all origins
- Bạn có thể chạy từ port 63342 hoặc 8080

---

### Cách 3: Change HTML URL to Direct Access

Nếu bạn chỉ muốn test servlet nhanh, access servlet trực tiếp:

**Open browser:**
```
http://localhost:8080/api/employee-json
```

**Expected result:**
```json
{"manv":"TeoNV","hoTen":"Nguyễn Văn Tèo","gioiTinh":true,"luong":950.5}
```

**This means servlet works!** ✅

---

## 🎯 Recommended Flow (Best Practice)

### For Development (Testing)
1. ✅ Create CorsFilter (Cách 2)
2. ✅ Build: `mvn clean package`
3. ✅ Deploy: Copy ROOT.war
4. ✅ Restart Tomcat
5. ✅ Open HTML from IDE (port 63342)
6. ✅ Test Fetch API

### For Production (Final)
1. ✅ Restrict CORS origins (not `*`)
2. ✅ Deploy HTML file to Tomcat
3. ✅ Access from `http://localhost:8080`

---

## 📋 Checklist - Fix CORS Error

- [ ] Created CorsFilter.java
- [ ] Added @WebFilter("/*")
- [ ] Set `Access-Control-Allow-Origin: *`
- [ ] Set `Access-Control-Allow-Methods: GET, POST, PUT, DELETE`
- [ ] Build: `mvn clean package` ✅
- [ ] Deploy: Copy ROOT.war ✅
- [ ] Restart Tomcat
- [ ] Wait 30 seconds
- [ ] Open browser: `http://localhost:8080/ajax-json-example.html`
- [ ] Click "Load Employee Data" button
- [ ] F12 → Console → See JSON data ✅

---

## 🧪 Test After Fix

### Test 1: Direct Access Servlet
```
URL: http://localhost:8080/api/employee-json
Expected: JSON shows (no error)
Status: 200 OK
```

### Test 2: Access HTML from Tomcat
```
URL: http://localhost:8080/ajax-json-example.html
Click button
Expected: F12 Console shows JSON ✅
```

### Test 3: Access HTML from IDE (Optional)
```
URL: http://localhost:63342/ajax-json-example.html
Click button
Expected: Works (CORS allowed) ✅
```

---

## 📖 Giải Thích Chi Tiết

### CORS Error là gì?

**CORS** = Cross-Origin Resource Sharing

**Khi nào xảy ra?**
- Browser request từ `Origin A`
- Server ở `Origin B`
- Browser blocks nếu không có CORS header

**Origins khác nhau:**
- `http://localhost:63342` (IDE)
- `http://localhost:8080` (Tomcat)
- Port khác nhau = Different origin

### Solution: Same Origin

**Cách 1: Same port (Tomcat serve HTML)**
```
HTML: http://localhost:8080/ajax-json-example.html
API: http://localhost:8080/api/employee-json
Same origin ✅ No CORS error
```

**Cách 2: Add CORS Headers**
```
Browser: http://localhost:63342
Server respond với: Access-Control-Allow-Origin: *
Browser allow ✅ No CORS error
```

---

## 🔍 Verification

### Cách verify servlet deployed đúng:

**Step 1:** Restart Tomcat, wait 30 seconds

**Step 2:** Check Tomcat folder
```bash
dir %TOMCAT_HOME%\webapps\
```

Expected:
```
ROOT/
```

**Step 3:** Check if file exists
```bash
dir %TOMCAT_HOME%\webapps\ROOT\
```

Expected:
```
api
index.html
ajax-json-example.html
```

**Step 4:** Direct access servlet
```
http://localhost:8080/api/employee-json
```

Expected: JSON displayed (no 404)

---

## 🐛 If Still Not Working

### Check 1: Tomcat Log
```bash
type %TOMCAT_HOME%\logs\catalina.out
```

Search for errors:
```
Exception
ERROR
FAILED
```

### Check 2: Servlet Created?
- File exists? `src/main/java/com/thienloc/jakarta/lab58/servlet/JsonResponseServlet.java`
- @WebServlet("/api/employee-json") present?
- doGet() method exists?

### Check 3: Build Success?
```bash
mvn clean package 2>&1 | findstr "BUILD"
```

Expected: `BUILD SUCCESS`

### Check 4: WAR File Copied?
```bash
dir %TOMCAT_HOME%\webapps\ROOT.war
```

Expected: File exists

---

## ✅ Final Checklist

After fixing:

- [ ] CorsFilter created & deployed
- [ ] Build successful
- [ ] WAR deployed
- [ ] Tomcat restarted
- [ ] Can access: `http://localhost:8080/ajax-json-example.html` ✅
- [ ] Click button works ✅
- [ ] F12 Console shows JSON ✅
- [ ] No CORS error ✅
- [ ] No 404 error ✅

---

## 🎯 Next Steps

Once CORS fixed:

1. ✅ Verify Bài 1 works
2. ✅ Move to Bài 2 (File Upload)
3. ✅ Then Bài 3 (REST API)
4. ✅ Finally Bài 4 (Web Client)

---

**Ready to fix? Create CorsFilter & rebuild! 💪**
