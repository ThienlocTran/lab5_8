# ⚡ Quick Fix Bài 2 (30 giây)

## 🔴 Lỗi
```
Content-Type header is text/plain (not multipart/form-data)
```

## ✅ Fix

### Tìm dòng này trong `file-upload-ajax.html`:
```javascript
fetch(url, {
    method: "POST",
    headers: {"Content-Type": "application/json"},  // ❌ DELETE THIS
    body: formData
})
```

### Sửa thành:
```javascript
fetch(url, {
    method: "POST",
    body: formData  // ✅ NO headers!
})
```

## 🔄 Build & Test
```bash
mvn clean package
copy target\ROOT.war "C:\Program Files\Apache\Tomcat 10\webapps\"
taskkill /F /IM java.exe
"C:\Program Files\Apache\Tomcat 10\bin\startup.bat"
```

Wait 30 seconds, then test:
```
http://localhost:8080/file-upload-ajax.html
```

## ✅ Expected
- Select file
- Click upload
- Console shows: `Upload Result: {name: "...", type: "...", size: ...}`

## 📝 Key Rule
```
NEVER set Content-Type header with FormData
Browser auto-sets: multipart/form-data
```

Done! 🎉
