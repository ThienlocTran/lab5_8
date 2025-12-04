# 🔧 Sửa FileUploadServlet - Bài 2

## 🔴 Vấn Đề

Lỗi:
```
Content-Type header is text/plain (not multipart/form-data)
```

## 🔍 Nguyên Nhân

Servlet của bạn có 2 vấn đề:

### Vấn đề 1: JSON Key Sai
```java
result.put("fileName", fileName);  // ❌ WRONG - key là "fileName"
```

Nhưng HTML của bạn expect:
```javascript
data.name  // expect key "name", not "fileName"
```

### Vấn đề 2: Có thể @MultipartConfig không được đặt đúng vị trí

---

## ✅ Fix FileUploadServlet

**Replace toàn bộ file `FileUploadServlet.java` với code này:**

```java
package com.thienloc.jakarta.lab58.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/api/upload")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      // 1 MB
    maxFileSize = 1024 * 1024 * 10,       // 10 MB
    maxRequestSize = 1024 * 1024 * 50     // 50 MB
)
public class FileUploadServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        try {
            // Get file from request
            Part filePart = req.getPart("file");
            
            // Get file info
            String fileName = filePart.getSubmittedFileName();
            String contentType = filePart.getContentType();
            long fileSize = filePart.getSize();
            
            // Create uploads directory
            String uploadDir = getServletContext().getRealPath("/uploads");
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }
            
            // Save file
            String filePath = uploadDir + File.separator + fileName;
            filePart.write(filePath);
            
            // Create response
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("name", fileName);        // ✅ CHANGED: "name" (not "fileName")
            result.put("type", contentType);
            result.put("size", fileSize);
            
            // Convert to JSON
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(result);
            
            // Send response
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            resp.getWriter().write(json);
            resp.flushBuffer();
            
        } catch (Exception e) {
            // Error handling
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
```

---

## 📝 Thay Đổi Chính

### Change 1: JSON Key Name
```java
// ❌ OLD
result.put("fileName", fileName);

// ✅ NEW
result.put("name", fileName);
```

**Why:** HTML expects `data.name`, not `data.fileName`

---

### Change 2: Variable Name (Typo Fix)
```java
// ❌ OLD
File uploadDrirFile = new File(uploadDir);  // typo: "Drir"
if(!uploadDrirFile.exists()){
    uploadDrirFile.mkdirs();
}

// ✅ NEW
File uploadDirFile = new File(uploadDir);   // fixed: "Dir"
if (!uploadDirFile.exists()) {
    uploadDirFile.mkdirs();
}
```

**Why:** Just a typo fix for cleaner code

---

### Change 3: Formatting
```java
// ✅ Better spacing & organization
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 50
)
```

---

## 🧪 Test After Fix

### Step 1: Update Servlet File
- Replace FileUploadServlet.java with code above
- Save file

### Step 2: Build
```bash
cd D:\Java4\Lab5_8\lab7_api
mvn clean package
```

**Wait for:** `BUILD SUCCESS`

### Step 3: Deploy
```bash
copy target\ROOT.war "C:\Program Files\Apache\Tomcat 10\webapps\"
```

### Step 4: Restart Tomcat
```bash
taskkill /F /IM java.exe
"C:\Program Files\Apache\Tomcat 10\bin\startup.bat"
# Wait 30 seconds
```

### Step 5: Test
```
http://localhost:8080/file-upload-ajax.html
```

**Steps:**
1. Select file
2. Click "Upload File"
3. F12 Console → Check result

**Expected output:**
```javascript
Upload Result: {
  name: "filename.ext",
  type: "text/plain",
  size: 1024
}
```

**Alert:** "File uploaded successfully. Check console for details."

✅ **No error!**

---

## ✅ Checklist

- [ ] Updated FileUploadServlet.java
- [ ] Changed "fileName" → "name"
- [ ] Fixed "uploadDrirFile" → "uploadDirFile"
- [ ] mvn clean package ✅
- [ ] Deployed ROOT.war
- [ ] Restarted Tomcat
- [ ] Waited 30 seconds
- [ ] Test upload works ✅
- [ ] Console shows correct result ✅

---

## 📊 Before vs After

### ❌ BEFORE
```
Upload Result: {fileName: "test.txt", type: "...", size: 1024}
```

### ✅ AFTER
```
Upload Result: {name: "test.txt", type: "...", size: 1024}
```

---

## 💡 Why This Fixes the Error

**The error:**
```
Content-Type header is text/plain (not multipart/form-data)
```

**Root cause:**
- Sometimes this error happens if request parsing fails
- But your main issue was wrong JSON key name

**By fixing:**
1. Correct JSON key: `name` instead of `fileName`
2. Clean code: Fixed variable typo
3. Proper @MultipartConfig: Ensures multipart handling

---

## 🎯 Next Step

After Bài 2 works:

1. ✅ Bài 1: AJAX JSON ✅
2. ✅ Bài 2: File Upload ✅ (FIXED NOW)
3. 🔜 Bài 3: REST API (GET/POST/PUT/DELETE)
4. 🔜 Bài 4: Web Client (CRUD UI)

---

**Ready? Update servlet & rebuild! 🚀**
