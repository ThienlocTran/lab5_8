# 🔧 FIX Bài 3: Jackson Serialization Error

## 🔴 Lỗi

```
No serializer found for class java.lang.Object and no properties 
discovered to create BeanSerializer
```

**Stack trace:**
```
at com.thienloc.jakarta.lab58.util.RestIO.writeEmptyObject(RestIO.java:46)
at com.thienloc.jakarta.lab58.servlet.EmployeeRestServlet.doPut(...)
```

---

## 🔍 Nguyên Nhân

Ở **RestIO.java**, method `writeEmptyObject()`:

```java
// ❌ WRONG
public static void writeEmptyObject(HttpServletResponse resp) throws IOException {
    RestIO.writeObject(resp, new Object());  // ❌ Jackson can't serialize Object()
}
```

**Problem:**
- `new Object()` không có properties
- Jackson không biết cách convert thành JSON
- Error!

---

## ✅ Giải Pháp

**Fix đã được apply!** Bây giờ:

```java
// ✅ CORRECT
public static void writeEmptyObject(HttpServletResponse resp) throws IOException {
    RestIO.writeJson(resp, "{}");  // ✅ Just send empty JSON object directly
}
```

**Why it works:**
- `"{}"` là valid JSON string (empty object)
- Không cần Jackson to serialize
- Direct write as JSON

---

## 📝 Thay Đổi

### ❌ BEFORE
```java
RestIO.writeObject(resp, new Object());
```

### ✅ AFTER
```java
RestIO.writeJson(resp, "{}");
```

---

## 🔄 Rebuild & Test

### Step 1: Build
```bash
cd D:\Java4\Lab5_8\lab7_api
mvn clean package
```

**Wait for:** `BUILD SUCCESS`

### Step 2: Deploy
```bash
copy target\ROOT.war "C:\Program Files\Apache\Tomcat 10\webapps\"
```

### Step 3: Restart Tomcat
```bash
taskkill /F /IM java.exe
"C:\Program Files\Apache\Tomcat 10\bin\startup.bat"
# Wait 30 seconds
```

---

## 🧪 Test PUT Request (Postman)

### Test 1: Create Employee First
```
Method: POST
URL: http://localhost:8080/employees
Headers: Content-Type: application/json
Body:
{
  "id": "NV06",
  "name": "Test Employee",
  "gender": false,
  "salary": 9000
}
```

**Expected:** 
```
Status: 200 OK
Response: {employee object}
```

---

### Test 2: Update Employee (PUT)
```
Method: PUT
URL: http://localhost:8080/employees/NV06
Headers: Content-Type: application/json
Body:
{
  "id": "NV06",
  "name": "Updated Name",
  "gender": true,
  "salary": 10000
}
```

**Expected:** 
```
Status: 200 OK
Response: {}
```

✅ **No error!**

---

### Test 3: Delete Employee (DELETE)
```
Method: DELETE
URL: http://localhost:8080/employees/NV06
```

**Expected:** 
```
Status: 200 OK
Response: {}
```

✅ **No error!**

---

## ✅ Checklist

- [ ] Fixed RestIO.java ✅
- [ ] `writeEmptyObject()` returns `"{}"`  ✅
- [ ] mvn clean package ✅
- [ ] Deployed ROOT.war
- [ ] Restarted Tomcat
- [ ] Waited 30 seconds
- [ ] Test PUT → 200 OK ✅
- [ ] Test DELETE → 200 OK ✅
- [ ] Response is `{}` ✅

---

## 📊 Complete REST API Test Flow

### 1. GET All (before create)
```
GET /employees
Response: [NV01, NV02, NV03, NV04, NV05]
```

### 2. POST Create
```
POST /employees
Body: {id: NV06, ...}
Response: {NV06 object}
```

### 3. GET All (after create)
```
GET /employees
Response: [NV01, NV02, NV03, NV04, NV05, NV06]
```

### 4. GET One
```
GET /employees/NV06
Response: {NV06 object}
```

### 5. PUT Update
```
PUT /employees/NV06
Body: {id: NV06, name: Updated, ...}
Response: {}  ✅
```

### 6. GET One (verify update)
```
GET /employees/NV06
Response: {NV06 object with updated data}
```

### 7. DELETE
```
DELETE /employees/NV06
Response: {}  ✅
```

### 8. GET All (verify delete)
```
GET /employees
Response: [NV01, NV02, NV03, NV04, NV05]  ← NV06 gone
```

---

## 🎓 Lesson

**Always be careful with Jackson serialization:**

```java
// ❌ BAD - Can't serialize plain Object
new Object()
Map.of()  // Without proper mapper config

// ✅ GOOD - Send JSON string directly
"{}"
"[]"

// ✅ ALSO GOOD - Use proper Map
new LinkedHashMap<>(Map.of(...))
```

---

## 🔗 Next Steps

1. ✅ Bài 1: AJAX JSON ✅
2. ✅ Bài 2: File Upload ✅
3. ✅ Bài 3: REST API - FIX JACKSON ERROR (THIS)
4. 🔜 Bài 4: Web Client

After this fix:
- All REST endpoints should work
- Ready for Bài 4 (Web Client)

---

**Ready? Rebuild & test all endpoints! 🚀**
