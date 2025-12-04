# 🔧 FIX Bài 2: FormData Content-Type Error

## 🔴 Lỗi Hiện Tại

```
"error": "org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException: 
the request doesn't contain a multipart/form-data or multipart/mixed stream, 
content type header is text/plain"
```

**Nguyên nhân:**
- Content-Type gửi là `text/plain` thay vì `multipart/form-data`
- FormData bị gửi sai format

---

## ✅ Giải Pháp

### Lỗi: Bạn Gửi Cái Gì?

**Sai ❌ - Current Code:**
```javascript
fetch(url, {
    method: "POST",
    headers: {"Content-Type": "application/json"},  // ❌ WRONG!
    body: formData
})
```

**Problem:**
- Báo Content-Type là JSON
- Nhưng gửi FormData
- Tomcat nhận được text/plain thay vì multipart/form-data

---

### Đúng ✅ - Correct Code

```javascript
fetch(url, {
    method: "POST",
    // ❌ KHÔNG set Content-Type header
    body: formData  // FormData sẽ tự set đúng header
})
```

**Why:**
- Browser automatically set: `Content-Type: multipart/form-data; boundary=...`
- Nếu bạn set header, nó override
- Dẫn đến error

---

## 📝 Sửa HTML File

### File: `src/main/webapp/file-upload-ajax.html`

**Tìm section JavaScript:**

```javascript
function uploadFile() {
    const fileInput = document.getElementById("fileInput");
    const file = fileInput.files[0];
    
    if (!file) {
        alert("Please select a file first");
        return;
    }
    
    const formData = new FormData();
    formData.append("file", file);
    
    const url = "http://localhost:8080/api/upload";
    
    fetch(url, {
        method: "POST",
        // ❌ DELETE THIS LINE:
        // headers: {"Content-Type": "application/json"},
        
        // ✅ KEEP ONLY THIS:
        body: formData
    })
    .then(resp => resp.json())
    .then(data => {
        console.log("Upload Result:", data);
        alert("File uploaded successfully!");
    })
    .catch(error => console.error("Error:", error));
}
```

---

## 🔍 Chi Tiết Sửa Lỗi

### Sai ❌
```javascript
fetch(url, {
    method: "POST",
    headers: {"Content-Type": "application/json"},  // ❌ WRONG
    body: formData
})
```

**Why wrong:**
1. Bạn báo: "Tôi gửi JSON"
2. Nhưng thực tế: FormData (khác hẳn)
3. Tomcat confused → Error

---

### Đúng ✅
```javascript
fetch(url, {
    method: "POST",
    body: formData  // ✅ CORRECT
})
```

**Why correct:**
1. Không set header
2. Browser auto-detect FormData
3. Browser set: `Content-Type: multipart/form-data`
4. Tomcat nhận đúng format
5. Success ✅

---

## 📋 Full Correct Code

**Replace `uploadFile()` function với code này:**

```javascript
function uploadFile() {
    // Get file from input
    const fileInput = document.getElementById("fileInput");
    const file = fileInput.files[0];
    
    // Validate file selected
    if (!file) {
        alert("Please select a file first");
        return;
    }
    
    // Create FormData
    const formData = new FormData();
    formData.append("file", file);
    
    // URL of servlet
    const url = "http://localhost:8080/api/upload";
    
    // Fetch POST - IMPORTANT: NO Content-Type header!
    fetch(url, {
        method: "POST",
        body: formData  // Browser will auto-set multipart/form-data
    })
    
    // Parse response
    .then(resp => resp.json())
    
    // Handle success
    .then(data => {
        console.log("Upload Result:", data);
        console.log("File name:", data.name);
        console.log("File type:", data.type);
        console.log("File size:", data.size);
        
        alert("File uploaded successfully!");
        
        // Clear input
        fileInput.value = "";
    })
    
    // Handle error
    .catch(error => {
        console.error("Upload error:", error);
        alert("Upload failed: " + error.message);
    });
}
```

---

## 🎓 Giải Thích FormData

### FormData Auto Headers

**Khi bạn gửi FormData:**

```javascript
const formData = new FormData();
formData.append("file", fileObject);

fetch(url, {
    method: "POST",
    body: formData  // Browser automatically generates:
                    // Content-Type: multipart/form-data; boundary=----WebKitFormBoundary...
})
```

**Browser tự động:**
1. Tính `boundary` (dấu phân cách)
2. Set `Content-Type: multipart/form-data; boundary=...`
3. Encode file đúng format
4. Gửi đi

---

### Nếu Bạn Ghi Đè Header

```javascript
fetch(url, {
    method: "POST",
    headers: {"Content-Type": "application/json"},  // ❌ Override auto header
    body: formData
})
```

**Kết quả:**
1. Browser set multipart/form-data
2. Bạn override thành application/json
3. Mismatch ❌
4. Server error

---

## 🧪 Test Lại

### Step 1: Update HTML File
- Open `file-upload-ajax.html`
- Fix `uploadFile()` function
- Save file

### Step 2: Rebuild
```bash
mvn clean package
```

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
3. Check console
4. Should see: `Upload Result: {name: "...", type: "...", size: ...}`

**No error ✅**

---

## ✅ Checklist

- [ ] Remove `headers: {"Content-Type": "application/json"}` line
- [ ] Keep only `body: formData`
- [ ] Save HTML file
- [ ] `mvn clean package` ✅
- [ ] Deploy & restart Tomcat
- [ ] Test upload
- [ ] No error in console ✅
- [ ] File uploaded successfully ✅

---

## 📊 Comparison: Before vs After

### ❌ BEFORE (Wrong)
```javascript
fetch(url, {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: formData
})
```
→ Error: `text/plain` received

---

### ✅ AFTER (Correct)
```javascript
fetch(url, {
    method: "POST",
    body: formData
})
```
→ Success: `multipart/form-data` received ✅

---

## 💡 Key Takeaway

**FormData Rule:**
```
NEVER set Content-Type header when using FormData

❌ Wrong:
headers: {"Content-Type": "application/json"}

✅ Correct:
// Just omit headers completely
// Browser will auto-set multipart/form-data
```

---

## 🔍 Verify It Works

### Test Request-Response

**You send:**
```
POST /api/upload
Content-Type: multipart/form-data; boundary=----WebKitFormBoundary...

------WebKitFormBoundary...
Content-Disposition: form-data; name="file"; filename="test.txt"
Content-Type: text/plain

[FILE CONTENT]
------WebKitFormBoundary...--
```

**Server responds:**
```json
{
  "name": "test.txt",
  "type": "text/plain",
  "size": 1024
}
```

**Console shows:**
```
Upload Result: {name: "test.txt", type: "text/plain", size: 1024}
```

---

## 🎯 Next Step

Once Bài 2 works:

1. ✅ Bài 1: AJAX JSON ✅
2. ✅ Bài 2: File Upload ✅
3. 🔜 Bài 3: REST API (GET/POST/PUT/DELETE)
4. 🔜 Bài 4: Web Client (CRUD UI)

---

**Fixed? Test upload now! 🚀**
