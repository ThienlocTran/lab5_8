# ⚡ Quick Fix Bài 3 - Jackson Error

## 🔴 Error
```
No serializer found for class java.lang.Object
InvalidDefinitionException from Jackson
```

## ✅ Problem
`writeEmptyObject()` sends `new Object()` which Jackson can't serialize

## 🔧 Solution

**Fix Applied:**

```java
// ❌ OLD
RestIO.writeObject(resp, new Object());

// ✅ NEW
RestIO.writeJson(resp, "{}");
```

Just send empty JSON string directly!

## 🔄 Rebuild

```bash
mvn clean package
copy target\ROOT.war "C:\Program Files\Apache\Tomcat 10\webapps\"
taskkill /F /IM java.exe
"C:\Program Files\Apache\Tomcat 10\bin\startup.bat"
```

## 🧪 Test

```
PUT /employees/NV06 → 200 OK, Response: {} ✅
DELETE /employees/NV06 → 200 OK, Response: {} ✅
```

Done! 🎉
