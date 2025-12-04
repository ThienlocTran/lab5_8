# Lab 7 - Bài 2: AJAX File Upload (Chi Tiết)

## 📖 Khái Niệm Cơ Bản

### File Upload là gì?

**File Upload** = Gửi file từ browser lên server

**Cách hoạt động:**
1. User chọn file từ computer
2. Click "Upload" button
3. File được gửi lên server (qua HTTP)
4. Server nhận file, lưu vào disk
5. Server trả response (JSON chứa file info)
6. Browser hiển thị kết quả

**Ví dụ thực tế:**
- Upload ảnh lên Facebook
- Upload document lên Google Drive
- Upload CV lên trang tuyển dụng

### FormData là gì?

**FormData** = Công cụ JavaScript để gửi file + data

Cách tạo:
```javascript
const formData = new FormData();
formData.append("file", fileObject);
formData.append("description", "My file");
```

**Tại sao cần FormData?**
- File object không thể convert thành JSON
- FormData có format đặc biệt cho files
- Browser tự động set headers

### Multipart Form Data

**Format multipart/form-data:**
```
------boundary
Content-Disposition: form-data; name="file"; filename="test.txt"
Content-Type: text/plain

[File content bytes here]
------boundary--
```

**Giải thích:**
- `boundary` - Dấu phân cách giữa các parts
- `Content-Disposition` - Metadata về field
- `filename` - Tên file
- `Content-Type` - MIME type của file
- Browser tự động tạo format này từ FormData

### @MultipartConfig Annotation

```java
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      // 1 MB
    maxFileSize = 1024 * 1024 * 10,       // 10 MB
    maxRequestSize = 1024 * 1024 * 50     // 50 MB
)
```

**Giải thích:**
- `fileSizeThreshold` - File < này sẽ lưu memory, >= sẽ lưu disk
- `maxFileSize` - Max size của 1 file
- `maxRequestSize` - Max size toàn bộ request

---

## 🔄 Quy Trình Upload

### Flow Diagram

```
[BROWSER]                          [SERVER]
    ↓
1. User chọn file
    ↓
2. Click "Upload"
    ↓
3. FormData + Fetch POST
    ↓ (HTTP POST + file data)      →
                                   4. Servlet @MultipartConfig
                                      nhận request
                                   ↓
                                   5. getPart("file")
                                      lấy file
                                   ↓
                                   6. Lưu file vào
                                      /uploads folder
                                   ↓
                                   7. Tạo JSON response:
                                      {name, type, size}
    ↓ (HTTP Response + JSON)       ←
8. Browser nhận response
    ↓
9. Parse JSON
    ↓
10. console.log(result)
```

### Ví Dụ Thực Tế

**User Actions:**
1. Select file: `test.txt` (100 bytes, `text/plain`)
2. Click "Upload" button

**What Happens:**
```javascript
// 1. Get file from input
const file = document.getElementById("fileInput").files[0];
// file = {name: "test.txt", type: "text/plain", size: 100}

// 2. Create FormData
const formData = new FormData();
formData.append("file", file);

// 3. Fetch POST
fetch("http://localhost:8080/api/upload", {
    method: "POST",
    body: formData  // FormData tự động set headers
})

// 4. Parse response
.then(resp => resp.json())
.then(data => console.log(data))
// data = {name: "test.txt", type: "text/plain", size: 100}
```

---

## 🛠️ PHẦN 1: TẠO SERVLET

### Servlet Overview

```java
@WebServlet("/api/upload")
@MultipartConfig(...)
public class FileUploadServlet extends HttpServlet {
    protected void doPost(...) {
        // 1. Lấy file từ request
        Part filePart = req.getPart("file");
        
        // 2. Lấy file info
        String fileName = filePart.getSubmittedFileName();
        String contentType = filePart.getContentType();
        long fileSize = filePart.getSize();
        
        // 3. Tạo /uploads folder
        String uploadDir = getServletContext().getRealPath("/uploads");
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();
        
        // 4. Lưu file
        String filePath = uploadDir + File.separator + fileName;
        filePart.write(filePath);
        
        // 5. Tạo response JSON
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", fileName);
        result.put("type", contentType);
        result.put("size", fileSize);
        
        // 6. Gửi response
        String json = mapper.writeValueAsString(result);
        resp.setContentType("application/json");
        resp.getWriter().print(json);
    }
}
```

### Method `getPart()`

```java
Part filePart = req.getPart("file");
```

**Giải thích:**
- `req.getPart("file")` - Lấy part named "file" từ FormData
- `"file"` - Tên field trong FormData (phải match HTML `name="file"`)
- Return: Part object chứa file

**Part object methods:**
- `getSubmittedFileName()` - Lấy tên file
- `getContentType()` - Lấy MIME type (e.g., "text/plain")
- `getSize()` - Lấy kích thước bytes
- `write(path)` - Lưu file vào path

### Method `getRealPath()`

```java
String uploadDir = getServletContext().getRealPath("/uploads");
```

**Giải thích:**
- `getServletContext()` - Lấy ServletContext (application scope)
- `getRealPath("/uploads")` - Lấy physical path của `/uploads`
- Ví dụ result:
  ```
  C:\Program Files\Tomcat\webapps\ROOT\uploads
  ```

### Creating Directory

```java
File dir = new File(uploadDir);
if (!dir.exists()) {
    dir.mkdirs();  // Create folder (+ parent folders)
}
```

**Giải thích:**
- `new File(path)` - Create File object (không create physical file)
- `exists()` - Check folder tồn tại
- `mkdirs()` - Create folder + all parent folders

### Writing File

```java
String filePath = uploadDir + File.separator + fileName;
filePart.write(filePath);
```

**Giải thích:**
- `File.separator` - Platform-specific separator (`\` on Windows, `/` on Linux)
- `filePath` - Full path to save file
- `write(filePath)` - Save file to disk

---

## 💻 PHẦN 2: VIẾT SERVLET CODE

### File Path

**File:** `src/main/java/com/thienloc/jakarta/lab58/servlet/FileUploadServlet.java`

### Code Template

```java
package com.thienloc.jakarta.lab58.servlet;

// TODO 1: Imports - copy nguyên
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

// TODO 2: @WebServlet annotation - URL mapping
@WebServlet("/api/upload")

// TODO 3: @MultipartConfig annotation - File upload config
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,           // 1 MB
    maxFileSize = 1024 * 1024 * 10,            // 10 MB
    maxRequestSize = 1024 * 1024 * 50          // 50 MB
)

// TODO 4: Class declaration
public class FileUploadServlet extends HttpServlet {
    
    // TODO 5: Override doPost() method
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        try {
            // TODO 6: Get file from request
            // "file" must match HTML <input name="file">
            Part filePart = req.getPart("file");
            
            // TODO 7: Get file information
            String fileName = filePart.getSubmittedFileName();
            String contentType = filePart.getContentType();
            long fileSize = filePart.getSize();
            
            // TODO 8: Get upload directory path
            // getRealPath("/uploads") returns: C:\Tomcat\webapps\ROOT\uploads
            String uploadDir = getServletContext().getRealPath("/uploads");
            
            // TODO 9: Create uploads folder if not exists
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();  // Create folder
            }
            
            // TODO 10: Create full file path
            // Example: C:\Tomcat\webapps\ROOT\uploads\test.txt
            String filePath = uploadDir + File.separator + fileName;
            
            // TODO 11: Write file to disk
            filePart.write(filePath);
            
            // TODO 12: Create response JSON
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("name", fileName);
            result.put("type", contentType);
            result.put("size", fileSize);
            
            // TODO 13: Convert to JSON
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(result);
            
            // TODO 14: Set response headers
            resp.setCharacterEncoding("utf-8");
            resp.setContentType("application/json");
            
            // TODO 15: Send response
            resp.getWriter().print(json);
            resp.flushBuffer();
            
        } catch (Exception e) {
            // TODO 16: Error handling
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
```

### Line-by-Line Giải Thích

**Imports:**
```java
import jakarta.servlet.annotation.MultipartConfig;  // For file upload
import jakarta.servlet.http.Part;                   // For file part
import java.io.File;                                // For file operations
```

**Annotations:**
```java
@WebServlet("/api/upload")  // URL: http://localhost:8080/api/upload

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,        // < 1MB sẽ keep in memory
    maxFileSize = 1024 * 1024 * 10,         // Max 10MB per file
    maxRequestSize = 1024 * 1024 * 50       // Max 50MB per request
)
```

**doPost() Method:**
```java
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    // Khi browser gửi POST request (form submit hoặc Fetch POST)
}
```

**Get File:**
```java
Part filePart = req.getPart("file");
```
- "file" = name attribute trong HTML input

**Get File Info:**
```java
String fileName = filePart.getSubmittedFileName();  // "test.txt"
String contentType = filePart.getContentType();     // "text/plain"
long fileSize = filePart.getSize();                 // 1024 (bytes)
```

**Create Directory:**
```java
String uploadDir = getServletContext().getRealPath("/uploads");
// Result: "C:\Program Files\Tomcat\webapps\ROOT\uploads"

File dir = new File(uploadDir);
if (!dir.exists()) {
    dir.mkdirs();  // Create if not exist
}
```

**Save File:**
```java
String filePath = uploadDir + File.separator + fileName;
// Result: "C:\...\uploads\test.txt"

filePart.write(filePath);  // Write file to disk
```

**Create Response:**
```java
Map<String, Object> result = new LinkedHashMap<>();
result.put("name", fileName);      // "test.txt"
result.put("type", contentType);   // "text/plain"
result.put("size", fileSize);      // 1024

// Convert to JSON:
// {"name":"test.txt","type":"text/plain","size":1024}
```

**Error Handling:**
```java
try {
    // File upload logic
} catch (Exception e) {
    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // 400 error
    resp.setContentType("application/json");
    resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");
}
```

---

## 🌐 PHẦN 3: TẠO HTML CLIENT

### HTML File Input

```html
<input type="file" id="fileInput">
```

**Attributes:**
- `type="file"` - File picker
- `id="fileInput"` - To select via JavaScript
- File object: `document.getElementById("fileInput").files[0]`

### Getting File Object

```javascript
const fileInput = document.getElementById("fileInput");
const file = fileInput.files[0];  // Get first (only) file
```

**File Object Properties:**
- `file.name` - "test.txt"
- `file.type` - "text/plain"
- `file.size` - 1024 (bytes)
- `file.lastModified` - Timestamp

### FormData API

```javascript
const formData = new FormData();
formData.append("file", file);
```

**Giải thích:**
- `new FormData()` - Create FormData object
- `append(key, value)` - Add field to FormData
- `"file"` - Key (must match servlet `req.getPart("file")`)
- `file` - Value (File object)

**Browser tự động set:**
- Content-Type: `multipart/form-data; boundary=...`
- Encoding file bytes correctly

### Fetch with FormData

```javascript
fetch(url, {
    method: "POST",
    body: formData  // FormData (not JSON.stringify)
})
```

**Important:**
- `method: "POST"` - Must be POST for file upload
- `body: formData` - Pass FormData directly (not JSON)
- Do NOT set `Content-Type` header (browser sets automatically)

### Full HTML Code

**Copy toàn bộ:**

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>File Upload AJAX - Bài 2</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        .container {
            max-width: 500px;
        }
        input[type="file"] {
            margin: 10px 0;
            padding: 5px;
        }
        button {
            padding: 10px 20px;
            font-size: 16px;
            cursor: pointer;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 5px;
        }
        button:hover {
            background-color: #218838;
        }
        .info {
            margin-top: 20px;
            padding: 10px;
            background-color: #e7f3ff;
            border-radius: 5px;
            display: none;
        }
        .info.show {
            display: block;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>File Upload with AJAX</h1>
        <p>Select a file and upload to server</p>
        
        <!-- File input -->
        <input type="file" id="fileInput">
        
        <!-- Upload button -->
        <button onclick="uploadFile()">Upload File</button>
        
        <!-- Result info -->
        <div id="info" class="info">
            <h3>Upload Result:</h3>
            <p>File Name: <span id="name"></span></p>
            <p>File Type: <span id="type"></span></p>
            <p>File Size: <span id="size"></span> bytes</p>
        </div>
    </div>
    
    <script>
        function uploadFile() {
            // Step 1: Get file from input
            const fileInput = document.getElementById("fileInput");
            const file = fileInput.files[0];
            
            // Validation: check if file selected
            if (!file) {
                alert("Please select a file first");
                return;
            }
            
            // Step 2: Create FormData
            const formData = new FormData();
            formData.append("file", file);
            
            // Step 3: Get URL
            const url = "http://localhost:8080/api/upload";
            
            // Step 4: Fetch POST
            fetch(url, {
                method: "POST",
                body: formData  // Important: Pass FormData, not JSON
            })
            
            // Step 5: Parse response
            .then(resp => resp.json())
            
            // Step 6: Handle response
            .then(data => {
                console.log("Upload Result:", data);
                
                // Display result
                document.getElementById("name").textContent = data.name;
                document.getElementById("type").textContent = data.type;
                document.getElementById("size").textContent = data.size;
                document.getElementById("info").classList.add("show");
                
                // Clear input
                fileInput.value = "";
                
                alert("File uploaded successfully!");
            })
            
            // Step 7: Handle error
            .catch(error => {
                console.error("Upload error:", error);
                alert("Upload failed: " + error.message);
            });
        }
    </script>
</body>
</html>
```

### Giải Thích HTML

**File Input:**
```html
<input type="file" id="fileInput">
```
- User click → File picker dialog
- Select file → Input shows filename

**Upload Button:**
```html
<button onclick="uploadFile()">Upload File</button>
```
- Click → Call JavaScript `uploadFile()` function

**Result Display:**
```html
<div id="info" class="info">
    <p>File Name: <span id="name"></span></p>
    <p>File Type: <span id="type"></span></p>
    <p>File Size: <span id="size"></span></p>
</div>
```
- Display upload result
- Initially hidden (display: none)
- Show after successful upload

### JavaScript Function Breakdown

**Step 1: Get File**
```javascript
const fileInput = document.getElementById("fileInput");
const file = fileInput.files[0];
```
- `files[0]` - First selected file (usually only 1)
- `file` = File object

**Step 2: Validate**
```javascript
if (!file) {
    alert("Please select a file first");
    return;
}
```
- Check if file selected
- Prevent error if no file

**Step 3: FormData**
```javascript
const formData = new FormData();
formData.append("file", file);
```
- Create FormData
- Add file as "file" field

**Step 4: Fetch**
```javascript
fetch(url, {
    method: "POST",
    body: formData
})
```
- POST request
- Pass FormData as body
- Browser sets Content-Type automatically

**Step 5-6: Handle Response**
```javascript
.then(resp => resp.json())
.then(data => {
    console.log("Upload Result:", data);
    // data = {name: "test.txt", type: "text/plain", size: 1024}
    
    // Display result in UI
    document.getElementById("name").textContent = data.name;
    // ... etc
})
```

**Step 7: Error Handling**
```javascript
.catch(error => {
    console.error("Upload error:", error);
    alert("Upload failed: " + error.message);
});
```

---

## 🧪 PHẦN 4: TEST & DEBUG

### Build & Deploy

**Build:**
```bash
mvn clean package
```

**Deploy:**
```bash
copy target\ROOT.war %TOMCAT_HOME%\webapps\
```

**Start Tomcat:**
```bash
%TOMCAT_HOME%\bin\startup.bat
```

**Wait ~30 seconds**

### Test File Upload

**URL:**
```
http://localhost:8080/file-upload-ajax.html
```

**Test Flow:**
1. Mở URL
2. Click "Choose File"
3. Select any file từ computer
4. Click "Upload File" button
5. Wait for response
6. See result on page + console (F12)

### Verify File Saved

**Windows:**
```bash
dir %TOMCAT_HOME%\webapps\ROOT\uploads\
```

**Expected:**
```
test.txt
myfile.pdf
image.jpg
...
```

### Console Output

**Expected console log:**
```javascript
Upload Result: {
  name: "test.txt",
  type: "text/plain",
  size: 1024
}
```

---

## 🐛 Troubleshooting

### Error 1: 404 Not Found

**Symptom:**
```
POST http://localhost:8080/api/upload 404
```

**Solution:**
- Check `@WebServlet("/api/upload")` annotation
- Rebuild project
- Restart Tomcat

---

### Error 2: 405 Method Not Allowed

**Symptom:**
```
POST http://localhost:8080/api/upload 405
```

**Solution:**
- Check `@Override protected void doPost(...)` exists
- Check imports correct

---

### Error 3: Part not found

**Symptom:**
```
java.lang.IllegalArgumentException: Part not found
```

**Solution:**
- Check HTML `<input name="file">` ← Must be "file"
- Check `req.getPart("file")` ← Must match

---

### Error 4: File not uploading

**Symptom:**
- Upload button clicked but nothing happens
- No error in console

**Solution:**
- Check file selected (validation at start of function)
- Check browser console (F12 → Console)
- Check network tab (F12 → Network)
- Check FormData correct: `new FormData()` + `append()`

---

### Error 5: Permission denied when writing

**Symptom:**
```
java.io.FileNotFoundException: uploads\test.txt
```

**Solution:**
- Check folder permissions
- Check folder path exists
- Try different file type

---

## ✅ CHECKLIST

- [ ] `FileUploadServlet.java` tạo xong
- [ ] `file-upload-ajax.html` tạo xong
- [ ] Both imports correct
- [ ] @MultipartConfig annotation present
- [ ] `mvn clean package` - build success
- [ ] Deploy ROOT.war
- [ ] Tomcat started
- [ ] Test URL accessible
- [ ] Can select file
- [ ] Can upload file
- [ ] Console shows result JSON
- [ ] File exists in uploads folder

---

## 📊 SUMMARY

### What Happens on Upload

```
1. User select file
2. Click "Upload"
3. uploadFile() called
4. FormData created with file
5. Fetch POST to /api/upload
6. Tomcat receives POST request
7. doPost() method called
8. @MultipartConfig handles multipart
9. getPart("file") retrieves file
10. File info extracted (name, type, size)
11. /uploads folder created if not exist
12. File written to disk
13. Response JSON created
14. Sent to browser
15. Browser parses JSON
16. Display result on page & console
```

---

**Congrats! Bài 2 xong! 🎉**

Next: Bài 3 - REST API (phức tạp hơn!)
