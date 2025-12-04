# 📮 Test Bài 2 với Postman

## ✅ Chrome Hoạt Động Rồi

Bạn đã test xong trên Chrome ✅

Bây giờ test trên **Postman** (để thấy API hoạt động tốt)

---

## 🔧 Cách Setup Postman

### Bước 1: Mở Postman
- Launch Postman app

### Bước 2: Create New Request
- Click "+ New" button
- Select "Request"
- Name: "Upload File Test"
- Save to collection (optional)

### Bước 3: Set Method & URL
```
Method: POST
URL: http://localhost:8080/api/upload
```

### Bước 4: Add File in Body

**Important:** Click "Body" tab

**Then select radio button:** `form-data`

**Key-value table:**
- Column 1 (KEY): `file`
- Column 2 (TYPE): Change from "Text" to **"File"** (dropdown)
- Column 3 (VALUE): Click "Select Files" → Choose file from computer

### Bước 5: Send

Click "Send" button

---

## 📸 Step by Step (Visual)

### Visual 1: Request Setup
```
[POST]  [http://localhost:8080/api/upload]

Tabs: Params | Headers | Body | Tests | Settings

Click "Body" tab
```

### Visual 2: Body Tab
```
Radio buttons:
○ none
○ form-data  ✅ SELECT THIS
○ x-www-form-urlencoded
○ raw
○ binary
○ GraphQL
```

### Visual 3: Form Data
```
[KEY]     [TYPE]    [VALUE]
file      File      [Select Files]

Click on "File" dropdown → Select file
```

---

## 🎯 Complete Postman Steps

### Step 1: Create Request
```
Method: POST
URL: http://localhost:8080/api/upload
```

### Step 2: Click "Body" Tab
```
Select: form-data (radio button)
```

### Step 3: Add File
```
Row 1:
KEY: file
TYPE: File (dropdown)
VALUE: Click "Select Files" → choose any file
```

### Step 4: Click Send
```
Red "Send" button at top right
```

### Step 5: Check Response

**Expected Response (200 OK):**
```json
{
  "name": "filename.txt",
  "type": "text/plain",
  "size": 1024
}
```

**Response tabs:**
- Status: `200 OK` ✅
- Body: JSON shown above
- Headers: `Content-Type: application/json`

---

## ✅ Success Criteria

### ✔️ Response Status
```
200 OK ✅
```

### ✔️ Response Body
```json
{
  "name": "your_filename",
  "type": "file/mime-type",
  "size": number
}
```

### ✔️ Response Headers
```
Content-Type: application/json
```

---

## 🔍 Screenshot Guide

If you're stuck, here's what Postman should look like:

### Screenshot 1: URL & Method
```
┌─────────────────────────────────────────────┐
│ [POST ▼] │ http://localhost:8080/api/upload │ [Send]
└─────────────────────────────────────────────┘
```

### Screenshot 2: Body Tab Selected
```
┌──────────────────────────────────┐
│ Params | Headers | Body | Tests  │
│        │         │ ✅   │        │
└──────────────────────────────────┘
```

### Screenshot 3: Form Data Selected
```
○ none
○ form-data  ✅
○ x-www-form-urlencoded
○ raw
○ binary
```

### Screenshot 4: File Selected
```
┌─────────────────────────────────┐
│ KEY    │ TYPE    │ VALUE         │
├─────────────────────────────────┤
│ file   │ File ▼  │ [Select Files]│
└─────────────────────────────────┘
```

### Screenshot 5: Response
```
Status: 200 OK ✅

Body (JSON):
{
  "name": "test.txt",
  "type": "text/plain",
  "size": 1024
}
```

---

## 🚀 Quick Checklist

- [ ] Postman open
- [ ] New request created
- [ ] Method: POST ✅
- [ ] URL: http://localhost:8080/api/upload ✅
- [ ] Body tab selected ✅
- [ ] form-data selected ✅
- [ ] Key: "file" ✅
- [ ] Type: "File" (not Text) ✅
- [ ] File selected ✅
- [ ] Click Send ✅
- [ ] Response 200 OK ✅
- [ ] JSON with name/type/size ✅

---

## ⚠️ Common Mistakes

### Mistake 1: Type is "Text" instead of "File"
```
KEY: file
TYPE: Text ❌ (WRONG)
TYPE: File ✅ (CORRECT)
```

**Solution:** Click dropdown next to "file" → Change to "File"

---

### Mistake 2: Using "raw" instead of "form-data"
```
○ form-data ✅ (CORRECT)
○ raw ❌ (WRONG)
```

**Solution:** Select "form-data" radio button

---

### Mistake 3: Key is "upload" instead of "file"
```
KEY: upload ❌ (WRONG)
KEY: file ✅ (CORRECT)
```

**Solution:** Must match servlet: `req.getPart("file")`

---

### Mistake 4: 404 Not Found
```
404 Not Found
```

**Solution:**
- Check URL: `http://localhost:8080/api/upload`
- Rebuild: `mvn clean package`
- Redeploy: Copy ROOT.war
- Restart Tomcat

---

## 📖 Postman Form-Data Explanation

### Why use "form-data"?

**form-data** sends files in **multipart/form-data** format:
```
------boundary
Content-Disposition: form-data; name="file"; filename="test.txt"
Content-Type: text/plain

[FILE BYTES HERE]
------boundary--
```

This is the ONLY way to send files in HTTP

---

### Other Body Types

```
❌ raw (JSON) - Cannot send files
❌ x-www-form-urlencoded - Cannot send files
✅ form-data - CORRECT for files
✅ binary - Also works, but less convenient
```

---

## 🎯 After Testing

### If works on Postman ✅
- File upload API is working correctly
- Any client can use it (browser, mobile, etc.)
- Ready for production

### If fails on Postman ❌
- Check error message
- Verify servlet deployed
- Check Tomcat logs

---

## 💾 Save Postman Request

After success, **save request**:

1. Click "Save" button (or Ctrl+S)
2. Name: "Upload File"
3. Select collection
4. Description: "Test file upload API - Bài 2"
5. Click "Save"

Now you can reuse this request anytime

---

## 📊 Summary

| Platform | Status | Method |
|----------|--------|--------|
| **Chrome** | ✅ Works | Fetch API |
| **Postman** | Testing... | form-data |
| **cURL** | Can work | multipart |

---

## 🔗 Next: Test with cURL (Optional)

Once Postman works, you can also test with **cURL**:

```bash
curl -X POST \
  -F "file=@C:\path\to\file.txt" \
  http://localhost:8080/api/upload
```

Response:
```json
{"name":"file.txt","type":"text/plain","size":1024}
```

---

**Ready to test on Postman? Follow steps above! 🚀**
