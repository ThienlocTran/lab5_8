# 🔧 FIX Bài 3: 500 Error - NullPointerException

## 🔴 Lỗi

```
HTTP Status 500 – Internal Server Error

Cannot invoke "String.substring(int)" because the return value of 
"jakarta.servlet.http.HttpServletRequest.getPathInfo()" is null
```

## 🔍 Nguyên Nhân

Lỗi ở **doPut()** và **doDelete()** methods:

```java
// ❌ WRONG - getPathInfo() có thể null
String id = req.getPathInfo().substring(1).trim();
```

**Khi nào null?**
- Bạn gửi PUT request đến `/employees` (không có ID)
- Thay vì `/employees/NV01` (có ID)

**Result:** `getPathInfo()` trả về null → `.substring()` crash

---

## ✅ Giải Pháp

Đã fix ở servlet! **doPut()** và **doDelete()** bây giờ:

### ✅ NEW doPut()
```java
@Override
protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String info = req.getPathInfo();
    
    // ✅ Check null trước khi substring
    if (info == null || info.length() == 0 || info.equals("/")) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        RestIO.writeObject(resp, Map.of("error", "ID required for PUT"));
        return;
    }
    
    String id = info.substring(1).trim();
    Employee employee = RestIO.readObject(req, Employee.class);
    map.put(id, employee);
    RestIO.writeEmptyObject(resp);
}
```

### ✅ NEW doDelete()
```java
@Override
protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String info = req.getPathInfo();
    
    // ✅ Check null trước khi substring
    if (info == null || info.length() == 0 || info.equals("/")) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        RestIO.writeObject(resp, Map.of("error", "ID required for DELETE"));
        return;
    }
    
    String id = info.substring(1).trim();
    map.remove(id);
    RestIO.writeEmptyObject(resp);
}
```

---

## 📝 Thay Đổi Chính

### Thay Đổi 1: Null Check
```java
// ❌ BEFORE
String id = req.getPathInfo().substring(1).trim();

// ✅ AFTER
String info = req.getPathInfo();
if (info == null || info.length() == 0 || info.equals("/")) {
    // Handle error
    return;
}
String id = info.substring(1).trim();
```

### Thay Đổi 2: Error Response
```java
resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // 400 error
RestIO.writeObject(resp, Map.of("error", "ID required for PUT"));
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

## 🧪 Test PUT Request

### ✅ Correct PUT Request (Should Work)

**Postman Setup:**
```
Method: PUT
URL: http://localhost:8080/employees/NV06
Headers: Content-Type: application/json
Body (raw JSON):
{
  "id": "NV06",
  "name": "Updated Name",
  "gender": true,
  "salary": 10000.0
}
```

**Expected Response:**
```
Status: 200 OK
Body: {}
```

---

### ❌ Wrong PUT Request (Without ID)

**Postman Setup:**
```
Method: PUT
URL: http://localhost:8080/employees  ← NO ID!
Headers: Content-Type: application/json
Body (raw JSON):
{...}
```

**Expected Response:**
```
Status: 400 Bad Request
Body: {
  "error": "ID required for PUT"
}
```

---

## 🎯 Test Sequence (Postman)

### Test 1: Create Employee
```
Method: POST
URL: http://localhost:8080/employees
Body: {"id":"NV06","name":"Test","gender":false,"salary":9000}
```
Expected: 200 OK, employee created

### Test 2: Update Employee (CORRECT)
```
Method: PUT
URL: http://localhost:8080/employees/NV06
Body: {"id":"NV06","name":"Updated","gender":true,"salary":10000}
```
Expected: 200 OK, employee updated ✅

### Test 3: Get Updated Employee
```
Method: GET
URL: http://localhost:8080/employees/NV06
```
Expected: Employee with updated data

### Test 4: Delete Employee (CORRECT)
```
Method: DELETE
URL: http://localhost:8080/employees/NV06
```
Expected: 200 OK, employee deleted ✅

### Test 5: Verify Delete
```
Method: GET
URL: http://localhost:8080/employees
```
Expected: NV06 not in list

---

## ✅ Checklist

- [ ] Fixed EmployeeRestServlet.java ✅
- [ ] doPut() has null check ✅
- [ ] doDelete() has null check ✅
- [ ] mvn clean package ✅
- [ ] Deployed ROOT.war
- [ ] Restarted Tomcat
- [ ] Waited 30 seconds
- [ ] Test PUT /employees/NV06 works ✅
- [ ] Test DELETE /employees/NV06 works ✅

---

## 📊 Before vs After

### ❌ BEFORE
```
PUT /employees/NV06 → 200 OK ✅
PUT /employees → 500 Error ❌ (NullPointerException)
```

### ✅ AFTER
```
PUT /employees/NV06 → 200 OK ✅
PUT /employees → 400 Error + JSON message ✅
```

---

## 🎓 Lesson

**Always check for null before calling methods:**

```java
// ❌ BAD
String id = req.getPathInfo().substring(1);

// ✅ GOOD
String info = req.getPathInfo();
if (info == null) {
    // Handle error
    return;
}
String id = info.substring(1);
```

---

## 🔗 Next Steps

1. ✅ Bài 1: AJAX JSON ✅
2. ✅ Bài 2: File Upload ✅
3. ✅ Bài 3: REST API - FIX NULL POINTER (THIS)
4. 🔜 Bài 4: Web Client

After fix:
- Test all PUT/DELETE requests
- Move to Bài 4 (Web Client)

---

**Ready? Rebuild & test now! 🚀**
