# ⚡ Quick Cheat Sheet - Fix & Run Lab 7 Bài 1

## 🚨 Problem
```
CORS Error: Access to fetch blocked
404: api/employee-json not found
```

---

## ✅ Solution (Copy-Paste Commands)

### Step 1: Create CorsFilter
**File path:** `src/main/java/com/thienloc/jakarta/lab58/filter/CorsFilter.java`

**Copy this code:**
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

---

### Step 2: Build
```bash
cd D:\Java4\Lab5_8\lab7_api
mvn clean package
```

Wait for `BUILD SUCCESS` ✅

---

### Step 3: Deploy
```bash
copy target\ROOT.war "C:\Program Files\Apache\Tomcat 10\webapps\"
```

**Note:** Update Tomcat path if different

---

### Step 4: Restart Tomcat
```bash
# Kill Tomcat
taskkill /F /IM java.exe

# Wait 5 seconds, then start
"C:\Program Files\Apache\Tomcat 10\bin\startup.bat"

# Wait 30 seconds
```

---

### Step 5: Test
**Open browser:**
```
http://localhost:8080/ajax-json-example.html
```

**Click button → F12 Console → JSON shows ✅**

---

## 🔍 Verify Each Step

### Verify 1: Filter Created
```bash
type "src\main\java\com\thienloc\jakarta\lab58\filter\CorsFilter.java"
```
→ Should show file content

---

### Verify 2: Build Success
```bash
mvn clean package 2>&1 | findstr "SUCCESS"
```
→ Should show: `BUILD SUCCESS`

---

### Verify 3: WAR Deployed
```bash
dir "C:\Program Files\Apache\Tomcat 10\webapps\ROOT.war"
```
→ Should show file exists

---

### Verify 4: Servlet Access
```
http://localhost:8080/api/employee-json
```
→ Should show JSON (not 404)

---

### Verify 5: HTML Access
```
http://localhost:8080/ajax-json-example.html
```
→ Should load page

---

## 🧪 Test Flow

| Step | Do This | Expected |
|------|---------|----------|
| 1 | Click "Load Employee Data" | No error |
| 2 | F12 → Console | JSON shows |
| 3 | No CORS message | ✅ |
| 4 | No 404 error | ✅ |

---

## 🐛 Quick Troubleshooting

| Problem | Fix |
|---------|-----|
| 404 Not Found | Redeploy ROOT.war, restart Tomcat |
| CORS Error | Create CorsFilter, rebuild |
| Port in use | `taskkill /F /IM java.exe` |
| Build fails | Check CorsFilter syntax |
| Tomcat won't start | Check port 8080 free |

---

## 📍 Paths Reference

**Change these if different on your machine:**

```
Tomcat: C:\Program Files\Apache\Tomcat 10
Project: D:\Java4\Lab5_8\lab7_api
```

---

## ⏱️ Total Time: 5 minutes

1. Create filter (1 min)
2. Build (2 min)
3. Deploy & restart (2 min)
4. Test (1 min)

---

**Done! Bài 1 now works! 🎉**
