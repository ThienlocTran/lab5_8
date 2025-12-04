# ⚡ Quick Fix Bài 3 - 500 Error

## 🔴 Error
```
500 Internal Server Error
NullPointerException: Cannot invoke "String.substring(int)" 
because getPathInfo() is null
```

## ✅ Problem
**doPut()** và **doDelete()** không check null trước khi call `.substring()`

## 🔧 Solution

**Fix đã được apply.** Servlet bây giờ có:

```java
// ✅ doPut() - FIXED
String info = req.getPathInfo();
if (info == null || info.length() == 0 || info.equals("/")) {
    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    RestIO.writeObject(resp, Map.of("error", "ID required for PUT"));
    return;
}
String id = info.substring(1).trim();
// ... rest of code
```

Same for **doDelete()**

## 🔄 Rebuild

```bash
mvn clean package
copy target\ROOT.war "C:\Program Files\Apache\Tomcat 10\webapps\"
taskkill /F /IM java.exe
"C:\Program Files\Apache\Tomcat 10\bin\startup.bat"
```

## 🧪 Test

### ✅ Correct Request
```
PUT http://localhost:8080/employees/NV06
```
→ 200 OK ✅

### ❌ Wrong Request
```
PUT http://localhost:8080/employees (no ID)
```
→ 400 Bad Request + error message ✅

## Done! 🎉
