# Lab 7 - Bài 2: AJAX File Upload (2 điểm)

## 🎯 Mục Tiêu

Xây dựng chức năng upload file từ browser lên server sử dụng **Fetch API** + **FormData**, rồi server gửi lại thông tin file dưới dạng JSON.

---

## 📝 Yêu Cầu

### 1. Tạo Servlet Xử Lý Upload

**File cần tạo**: `src/main/java/com/thienloc/jakarta/lab58/servlet/FileUploadServlet.java`

**Requirements:**
- URL mapping: `/api/upload`
- HTTP method: `POST`
- Accept `multipart/form-data` (file upload)
- Lưu file vào thư mục `uploads` (tạo nếu chưa tồn tại)
- Response JSON với cấu trúc:
  ```json
  {
    "name": "filename.txt",
    "type": "text/plain",
    "size": 1234
  }
  ```

**Hints:**
- Sử dụng `@WebServlet("/api/upload")` annotation
- Thêm `@MultipartConfig` annotation để handle file upload
- Sử dụng `req.getPart("file")` để lấy file
- Các method cần: `getSubmittedFileName()`, `getContentType()`, `getSize()`
- Lưu file bằng `part.write(filePath)`
- Tạo `/uploads` folder bằng `getServletContext().getRealPath()`

---

### 2. Tạo HTML File Client

**File cần tạo**: `src/main/webapp/file-upload-ajax.html`

**Requirements:**
- Có `<input type="file">` để chọn file
- Có button để trigger upload
- Sử dụng **FormData** để gửi file
- Sử dụng **Fetch API** POST request
- Xuất response JSON ra **console**
- Structure cơ bản:
  ```html
  <!DOCTYPE html>
  <html>
  <head>
    <title>File Upload AJAX - Bài 2</title>
  </head>
  <body>
    <h1>File Upload with AJAX</h1>
    <input type="file" id="fileInput">
    <button onclick="...">Upload File</button>
    
    <script>
      function uploadFile() {
        // TODO: Viết Fetch API + FormData code ở đây
      }
    </script>
  </body>
  </html>
  ```

**Hints:**
- Get file từ: `document.getElementById("fileInput").files[0]`
- Tạo FormData: `new FormData()`
- Append file: `formData.append("file", file)`
- Fetch POST: `fetch(url, {method: "POST", body: formData})`
- URL: `http://localhost:8080/api/upload`

---

## 🔧 Step-by-Step Hướng Dẫn

### Bước 1: Tạo Servlet Xử Lý Upload

1. **Tạo file** `FileUploadServlet.java`

2. **Khai báo annotations:**
   ```java
   @WebServlet("/api/upload")
   @MultipartConfig(
       fileSizeThreshold = 1024 * 1024,      // 1 MB
       maxFileSize = 1024 * 1024 * 10,       // 10 MB
       maxRequestSize = 1024 * 1024 * 50     // 50 MB
   )
   public class FileUploadServlet extends HttpServlet {
   ```

3. **Override doPost() method:**
   - Lấy file part: `req.getPart("file")`
   - Lấy tên file: `part.getSubmittedFileName()`
   - Lấy MIME type: `part.getContentType()`
   - Lấy kích thước: `part.getSize()`

4. **Tạo thư mục uploads:**
   ```java
   String uploadDir = getServletContext().getRealPath("/uploads");
   File uploadDirFile = new File(uploadDir);
   if (!uploadDirFile.exists()) {
       uploadDirFile.mkdirs();
   }
   ```

5. **Lưu file:**
   ```java
   String filePath = uploadDir + File.separator + fileName;
   part.write(filePath);
   ```

6. **Tạo response JSON:**
   ```java
   Map<String, Object> result = new LinkedHashMap<>();
   result.put("name", fileName);
   result.put("type", contentType);
   result.put("size", fileSize);
   // Convert to JSON và gửi
   ```

---

### Bước 2: Tạo HTML File Client

1. **Tạo file** `file-upload-ajax.html`

2. **Viết HTML:**
   - `<input type="file" id="fileInput">`
   - `<button onclick="uploadFile()">Upload File</button>`

3. **Viết JavaScript function `uploadFile()`:**
   - Get file từ input element
   - Tạo FormData object
   - Append file vào FormData
   - Gọi fetch POST
   - Parse JSON response
   - In ra console

4. **Error handling:**
   - Kiểm tra xem user có select file không
   - Catch network errors
   - Show alert nếu fail

---

## 🧪 Cách Test

1. **Build project**:
   ```bash
   mvn clean package
   ```

2. **Deploy** `ROOT.war` lên Tomcat

3. **Start Tomcat**

4. **Mở browser** vào: `http://localhost:8080/file-upload-ajax.html`

5. **Chọn file** bằng input element

6. **Click button** "Upload File"

7. **Mở Developer Tools** (F12):
   - Console tab → Kiểm tra JSON response
   - Network tab → Kiểm tra request/response details

8. **Kiểm tra file được lưu:**
   ```bash
   # Windows:
   dir %TOMCAT_HOME%\webapps\ROOT\uploads\
   
   # Linux/Mac:
   ls -la $TOMCAT_HOME/webapps/ROOT/uploads/
   ```

**Expected console output:**
```javascript
{name: "myfile.txt", type: "text/plain", size: 1234}
```

---

## 📚 Kiến Thức Cần Biết

### FormData API
```javascript
const formData = new FormData();
formData.append("file", fileObject);
formData.append("description", "My file");
```

### Multipart Form Data
- Format để gửi file từ browser
- Tự động set Content-Type header
- Không cần set header thủ công khi dùng FormData

### File Object Properties
```javascript
const file = document.getElementById("input").files[0];
file.name              // Tên file
file.type              // MIME type (e.g., "text/plain")
file.size              // Kích thước bytes
file.lastModified      // Timestamp
```

### @MultipartConfig Annotation
```java
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,     // Kích thước trước khi lưu vào disk
    maxFileSize = 1024 * 1024 * 10,      // Max size 1 file
    maxRequestSize = 1024 * 1024 * 50    // Max size toàn bộ request
)
```

---

## 💡 Tips & Tricks

1. **Check File Size trước khi upload:**
   ```javascript
   const file = document.getElementById("fileInput").files[0];
   if (file.size > 10 * 1024 * 1024) {
       alert("File too large!");
       return;
   }
   ```

2. **Progress tracking** (Advanced):
   ```javascript
   xhr.upload.onprogress = (e) => {
       console.log(e.loaded / e.total * 100 + "%");
   };
   ```

3. **Multiple files:**
   - Change: `getSelectedFile().files[0]` → `getSelectedFile().files`
   - Loop qua files array

4. **File validation:**
   - Check MIME type
   - Check extension
   - Check file size

---

## ❌ Lỗi Thường Gặp

| Lỗi | Nguyên Nhân | Giải Pháp |
|-----|-------------|----------|
| 405 Method Not Allowed | Servlet không override doPost() | Add @Override doPost() method |
| File not saved | uploads folder không được tạo | Verify folder creation code |
| null file | Input element ID sai | Check getElementById ID matches HTML |
| Empty response | Response JSON format sai | Validate JSON structure |
| CORS error | Request from different origin | Check browser URL matches deployment |

---

## 🔍 Debugging Tips

1. **Kiểm tra Network Tab:**
   - Go to F12 → Network
   - Upload file
   - Click request → Response tab
   - Kiểm tra JSON response

2. **Kiểm tra Console:**
   - Errors sẽ show here
   - Print dữ liệu bằng console.log()

3. **Kiểm tra File System:**
   - Navigate to uploads folder
   - Verify file tồn tại
   - Check file content

---

## 🎓 Learning Outcomes

Sau bài này, bạn sẽ biết:
- ✅ Tạo servlet xử lý file upload
- ✅ Sử dụng @MultipartConfig annotation
- ✅ Xử lý FormData từ client
- ✅ Lưu file vào server file system
- ✅ Sử dụng FormData API trong JavaScript
- ✅ Parse multipart requests trong servlet

---

## ✨ Bonus (Optional)

Sau khi hoàn thành, bạn có thể thử:
- Upload multiple files cùng lúc
- Validate file type (chỉ allow *.txt, *.pdf, etc.)
- Display uploaded files list
- Progress bar cho upload
- Drag & drop file handling
- Resize images trước khi upload

---

**Status**: Ready for Implementation
**Points**: 2 điểm
**Time Estimate**: 20-25 phút
