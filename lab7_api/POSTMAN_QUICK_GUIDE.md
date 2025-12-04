# 🚀 Postman Quick Guide - Bài 2

## ⚡ 5 Bước Setup

### 1️⃣ URL & Method
```
POST | http://localhost:8080/api/upload
```

### 2️⃣ Body Tab
```
Click "Body" tab
```

### 3️⃣ Select form-data
```
○ form-data ✅ SELECT THIS
```

### 4️⃣ Add File
```
Row 1:
KEY:   file
TYPE:  File (dropdown) ← IMPORTANT!
VALUE: [Select Files] button
```

### 5️⃣ Send
```
Click red "Send" button
```

---

## ✅ Expected Result

```
Status: 200 OK

Response:
{
  "name": "filename.ext",
  "type": "text/plain",
  "size": 1024
}
```

---

## ❌ Common Mistake

❌ **WRONG:**
```
TYPE: Text
```

✅ **CORRECT:**
```
TYPE: File ← Click dropdown to change
```

---

## 💾 Save Request

After success:
```
Ctrl+S → Name: "Upload File" → Save
```

Now reusable! 🎉
