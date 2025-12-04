# 📋 Hướng Dẫn Fix CORS Error - Step by Step

## 🎯 Mục Tiêu

Fix CORS error để bạn có thể chạy Bài 1 thành công.

---

## ⚡ Quick Fix (5 phút)

### Bước 1: Tạo CorsFilter
**Copy toàn bộ code này vào file:**

**Path:** `src/main/java/com/thienloc/jakarta/lab58/filter/CorsFilter.java`

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

### Bước 2: Build Project
```bash
cd D:\Java4\Lab5_8\lab7_api
mvn clean package
```

**Wait for:**
```
BUILD SUCCESS
```

### Bước 3: Deploy WAR
```bash
# Windows Command Prompt
copy target\ROOT.war "C:\Program Files\Apache\Tomcat 10\webapps\"
```

**Note:** Thay đường dẫn Tomcat nếu khác

### Bước 4: Restart Tomcat
```bash
# Shutdown
"C:\Program Files\Apache\Tomcat 10\bin\shutdown.bat"

# Wait 5 seconds

# Startup
"C:\Program Files\Apache\Tomcat 10\bin\startup.bat"

# Wait 30 seconds for startup
```

### Bước 5: Test
**Open browser:**
```
http://localhost:8080/ajax-json-example.html
```

**Click button → F12 Console → See JSON ✅**

---

## 🔍 Detailed Explanation

### CorsFilter - Từng Dòng Code

**Annotation:**
```java
@WebFilter("/*")  // Apply to all URLs
```

**Method:**
```java
public void doFilter(ServletRequest request, ServletResponse response, 
        FilterChain chain) throws IOException, ServletException {
```

- `ServletRequest` - Client request
- `ServletResponse` - Server response
- `FilterChain` - Chain of filters

**Cast to HttpServletResponse:**
```java
HttpServletResponse httpResponse = (HttpServletResponse) response;
```

- Need to add headers
- HttpServletResponse has `setHeader()` method

**Add CORS Headers:**
```java
// Allow all origins
httpResponse.setHeader("Access-Control-Allow-Origin", "*");

// Allow these HTTP methods
httpResponse.setHeader("Access-Control-Allow-Methods", 
    "GET, POST, PUT, DELETE, OPTIONS");

// Allow these headers from client
httpResponse.setHeader("Access-Control-Allow-Headers", 
    "Content-Type, Authorization");
```

**Continue processing:**
```java
chain.doFilter(request, response);  // Let request continue
```

---

## 📊 Folder Structure

Create filter in correct location:

```
lab7_api/
├── src/
│   └── main/
│       └── java/
│           └── com/thienloc/jakarta/lab58/
│               ├── servlet/
│               │   ├── JsonResponseServlet.java ✅ (already exists)
│               │   └── ...
│               └── filter/
│                   └── CorsFilter.java ✅ (CREATE THIS)
```

---

## 🧪 Verification Steps

### Step 1: Check Filter Created

**Verify file exists:**
```bash
# Windows Command Prompt
type "src\main\java\com\thienloc\jakarta\lab58\filter\CorsFilter.java"
```

Expected: File content shown

---

### Step 2: Check Build

```bash
mvn clean package
```

Expected output:
```
[INFO] Building war: ...\target\ROOT.war
[INFO] BUILD SUCCESS
```

**If error:**
```
[ERROR] BUILD FAILURE
```

→ Check CorsFilter syntax (missing ; , { } etc.)

---

### Step 3: Check Deployment

```bash
# List Tomcat webapps
dir "C:\Program Files\Apache\Tomcat 10\webapps\"
```

Expected:
```
ROOT
ROOT.war
```

---

### Step 4: Check Tomcat Started

**Check log file:**
```bash
type "C:\Program Files\Apache\Tomcat 10\logs\catalina.out" | findstr "Server startup"
```

Expected:
```
Server startup in X ms
```

---

### Step 5: Test Servlet Direct Access

**Open browser:**
```
http://localhost:8080/api/employee-json
```

Expected: JSON shows
```json
{"manv":"TeoNV","hoTen":"Nguyễn Văn Tèo","gioiTinh":true,"luong":950.5}
```

**If 404:** Servlet not deployed - check Step 3

---

### Step 6: Test HTML File

**Open browser:**
```
http://localhost:8080/ajax-json-example.html
```

Expected: HTML page loads

**Click "Load Employee Data" button**

Expected: No error, F12 Console shows JSON ✅

---

## 🐛 Troubleshooting

### Problem 1: Build Fails
```
[ERROR] BUILD FAILURE
```

**Solution:**
1. Check CorsFilter syntax
2. Check imports correct
3. Check @WebFilter("/*") annotation
4. Try: `mvn clean` then `mvn package`

---

### Problem 2: 404 Not Found
```
GET http://localhost:8080/api/employee-json 404
```

**Solution:**
1. Check WAR deployed: `dir %TOMCAT_HOME%\webapps\`
2. Check Tomcat restarted
3. Wait 30 seconds
4. Check servlet exists: `JsonResponseServlet.java`
5. Check @WebServlet("/api/employee-json")

---

### Problem 3: Port 8080 Already in Use
```
Address already in use
```

**Solution:**
```bash
# Find process using port 8080
netstat -ano | findstr :8080

# Kill process (replace PID with actual number)
taskkill /PID <PID> /F

# Then start Tomcat again
```

---

### Problem 4: Still Getting CORS Error
```
Access to fetch ... blocked by CORS policy
```

**Solution:**
1. Verify CorsFilter created correctly
2. Rebuild: `mvn clean package`
3. Redeploy WAR file
4. Restart Tomcat
5. Wait full 30 seconds
6. Clear browser cache (Ctrl+Shift+Delete)
7. Try different browser

---

### Problem 5: Can't Find Tomcat
```bash
The system cannot find the path specified
```

**Solution:**
1. Find where Tomcat installed
2. Search in: `C:\Program Files\`, `D:\`, etc.
3. Replace path in commands

**Or ask in IDE:**
```
View → Tool Windows → Services
```

Check if Tomcat running there

---

## 📍 Common Tomcat Paths

**Windows:**
```
C:\Program Files\Apache\Tomcat 10
C:\Program Files (x86)\Apache Software Foundation\Tomcat 10.0
D:\Tomcat
```

**If using IDE (IntelliJ/Eclipse):**
```
IDE embedded Tomcat location
```

---

## ✅ Final Verification

After all steps:

### ✔️ Check 1: Browser Access
```
http://localhost:8080/ajax-json-example.html
```
→ Page loads ✅

### ✔️ Check 2: Click Button
→ No CORS error ✅

### ✔️ Check 3: F12 Console
→ JSON appears ✅

### ✔️ Check 4: Console Message
```
Employee Data: {manv: "TeoNV", ...}
```
→ Displays ✅

---

## 🎓 Understanding CORS

### Why CORS Error Happens?

**Browser security rule:**
- Prevent malicious scripts
- If HTML from `origin A` calls API at `origin B`
- Browser blocks unless server says OK

### Different Origins:

```
http://localhost:63342/ajax-json-example.html
                ↑
              PORT 63342 (IDE)

http://localhost:8080/api/employee-json
                ↑
              PORT 8080 (Tomcat)

Ports different → Different origins
→ CORS error
```

### Solution:

**Add headers from server:**
```
Access-Control-Allow-Origin: *
```

Means: "Allow requests from any origin"

Then browser allows ✅

---

## 🚀 Next Steps

After CORS fixed:

1. ✅ Verify Bài 1 works
2. ✅ Move to Bài 2 (File Upload)
3. ✅ Then Bài 3 (REST API)
4. ✅ Finally Bài 4 (Web Client)

---

## 💡 Pro Tips

### Tip 1: Keep Tomcat Running
- Once started, leave running
- Don't restart unless deploy new WAR

### Tip 2: Browser Cache
- If still seeing old error
- Clear cache: `Ctrl+Shift+Delete`
- Or use incognito mode

### Tip 3: Check Logs
```bash
type "%TOMCAT_HOME%\logs\catalina.out" | tail -50
```
- Shows recent errors
- Helps debugging

### Tip 4: One WAR at a Time
- Don't have multiple WAR files
- Delete old: `del %TOMCAT_HOME%\webapps\ROOT.war`
- Deploy new only

---

## 📞 If Still Stuck

1. **Check error message carefully**
   - CORS error? → Cách 2 solution
   - 404 error? → Deployment issue
   - Port error? → Kill process

2. **Verify each step**
   - CorsFilter created?
   - Build successful?
   - WAR deployed?
   - Tomcat restarted?
   - 30 seconds waited?

3. **Ask for help with:**
   - Full error message
   - Browser console output
   - What step failed

---

**Ready? Create CorsFilter & rebuild! 💪**
