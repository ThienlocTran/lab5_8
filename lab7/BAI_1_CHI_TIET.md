# Lab 7 - Bài 1: AJAX JSON Response (Chi Tiết)

## 📖 Khái Niệm Cơ Bản

### AJAX là gì?

**AJAX** = **Asynchronous JavaScript And XML**

- **Asynchronous** = Không đợi (gửi request rồi làm việc khác)
- **JavaScript** = Ngôn ngữ browser
- **XML/JSON** = Format dữ liệu (giờ dùng JSON nhiều hơn)

**Tác dụng:**
- Gửi/nhận dữ liệu từ server **mà không reload trang**
- Ví dụ: Chat app, Gmail notifications, Facebook likes

### Fetch API là gì?

**Fetch API** = Công cụ JavaScript để gửi HTTP requests đến server

Cú pháp:
```javascript
fetch(url, options)
  .then(response => response.json())
  .then(data => console.log(data))
  .catch(error => console.error(error));
```

**Giải thích:**
- `fetch(url)` - Gửi request đến URL
- `.then()` - Khi server trả response
- `.json()` - Convert response thành JSON object
- `.catch()` - Nếu có lỗi

### JSON là gì?

**JSON** = **JavaScript Object Notation**

Ví dụ JSON:
```json
{
  "manv": "TeoNV",
  "hoTen": "Nguyễn Văn Tèo",
  "gioiTinh": true,
  "luong": 950.5
}
```

**Giải thích từng dòng:**
- `"manv"` - Key (tên trường)
- `"TeoNV"` - Value (giá trị) - String (có `""`)
- `"hoTen": "Nguyễn Văn Tèo"` - Key-value pair
- `"gioiTinh": true` - Value là boolean (không có `""`)
- `"luong": 950.5` - Value là số (không có `""`)

---

## 🔄 Quy Trình Hoạt Động

### Cách Bài 1 Hoạt Động

```
[BROWSER - Client]                   [SERVER - Tomcat]
       ↓                                    ↑
1. User click button           
       ↓
2. JavaScript gọi fetch()
       ↓ (HTTP GET request)
3. Request đến server           →    Servlet nhận
                                     ↓
                                4. Servlet tạo JSON
                                     ↓
                                5. Gửi JSON response
       ↓ (HTTP Response)       ←
6. Browser nhận JSON
       ↓
7. Parse & console.log()
       ↓
8. Developer tools hiển thị
```

### Ví Dụ Thực Tế

**Step 1:** User click button trên HTML page
```html
<button onclick="loadData()">Load Employee Data</button>
```

**Step 2:** JavaScript function `loadData()` được gọi
```javascript
function loadData() {
  // Gửi request đến server
  fetch("http://localhost:8080/api/employee-json")
}
```

**Step 3:** Request được gửi đến server
```
GET http://localhost:8080/api/employee-json
Host: localhost:8080
```

**Step 4:** Servlet xử lý request
```java
@WebServlet("/api/employee-json")
public class JsonResponseServlet extends HttpServlet {
  protected void doGet(...) {
    // Tạo JSON string
    String json = "{...}";
    // Gửi response
    resp.getWriter().print(json);
  }
}
```

**Step 5:** Browser nhận response
```json
{
  "manv": "TeoNV",
  "hoTen": "Nguyễn Văn Tèo",
  "gioiTinh": true,
  "luong": 950.5
}
```

**Step 6:** JavaScript xử lý response
```javascript
.then(resp => resp.json())  // Parse JSON
.then(data => console.log(data))  // In ra console
```

---

## 🛠️ PHẦN 1: TẠO SERVLET

### Servlet là gì?

**Servlet** = Java class xử lý HTTP requests từ browser

**Cách hoạt động:**
1. Browser gửi GET request
2. Tomcat server nhận request
3. Tìm servlet match URL
4. Gọi `doGet()` method
5. Servlet xử lý, tạo response
6. Gửi response lại cho browser

### Annotation @WebServlet

```java
@WebServlet("/api/employee-json")
```

**Giải thích:**
- `@WebServlet` - Báo Tomcat cái này là servlet
- `"/api/employee-json"` - URL path (truy cập bằng: `http://localhost:8080/api/employee-json`)

### HttpServlet là gì?

```java
public class JsonResponseServlet extends HttpServlet {
```

**Giải thích:**
- `extends HttpServlet` - Kế thừa lớp HttpServlet (có sẵn HTTP support)
- `doGet()` - Method xử lý GET requests
- `doPost()` - Method xử lý POST requests

### doGet() Method

```java
protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
        throws ServletException, IOException {
}
```

**Giải thích:**
- `protected` - Có thể access từ package
- `void` - Không return gì
- `HttpServletRequest req` - Object chứa thông tin request từ browser
- `HttpServletResponse resp` - Object để gửi response về browser
- `throws IOException` - Có thể throw exception

### Jackson ObjectMapper là gì?

```java
ObjectMapper mapper = new ObjectMapper();
String json = mapper.writeValueAsString(object);
```

**Giải thích:**
- `ObjectMapper` - Công cụ convert Object ↔ JSON
- `writeValueAsString()` - Convert Java Object thành JSON string
- Ví dụ:
  ```java
  Map<String, Object> data = new HashMap<>();
  data.put("name", "Tèo");
  String json = mapper.writeValueAsString(data);
  // Result: {"name":"Tèo"}
  ```

### Response Headers là gì?

```java
resp.setCharacterEncoding("utf-8");
resp.setContentType("application/json");
```

**Giải thích:**
- `setCharacterEncoding("utf-8")` - Báo browser là UTF-8 encoding
  - Để hiển thị đúng tiếng Việt (ñ, ơ, ư, etc.)
- `setContentType("application/json")` - Báo browser type của response
  - `"application/json"` = JSON format
  - Browser & JavaScript biết parse thành JSON

### Cách Gửi Response

```java
resp.getWriter().print(json);
resp.flushBuffer();
```

**Giải thích:**
- `getWriter()` - Lấy output stream
- `.print(json)` - Ghi JSON string vào output
- `flushBuffer()` - Gửi ngay (đảm bảo browser nhận đầy đủ)

---

## 💻 PHẦN 2: VIẾT SERVLET CODE

### Bước 1: Tạo File

**File path:** `src/main/java/com/thienloc/jakarta/lab58/servlet/JsonResponseServlet.java`

Tạo folder nếu chưa có:
```
src/main/java/com/thienloc/jakarta/lab58/servlet/
```

### Bước 2: Viết Servlet

**Code template - Copy & fill in TODOs:**

```java
package com.thienloc.jakarta.lab58.servlet;

// TODO 1: Import statements - copy nguyên cái này
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

// TODO 2: @WebServlet annotation - URL mapping
@WebServlet("/api/employee-json")
// TODO 3: Class declaration - extends HttpServlet
public class JsonResponseServlet extends HttpServlet {
    
    // TODO 4: Override doGet() method
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        // TODO 5: Create ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        
        // TODO 6: Create employee data using Map
        // Key: "manv", Value: "TeoNV"
        // Key: "hoTen", Value: "Nguyễn Văn Tèo"
        // Key: "gioiTinh", Value: true
        // Key: "luong", Value: 950.5
        Map<String, Object> employee = new LinkedHashMap<>();
        employee.put("manv", "TeoNV");
        employee.put("hoTen", "Nguyễn Văn Tèo");
        employee.put("gioiTinh", true);
        employee.put("luong", 950.5);
        
        // TODO 7: Convert Map to JSON string
        String json = mapper.writeValueAsString(employee);
        
        // TODO 8: Set response headers - UTF-8 & JSON type
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        
        // TODO 9: Send response to browser
        resp.getWriter().print(json);
        resp.flushBuffer();
    }
}
```

### Bước 3: Giải Thích Từng Part

**Part 1: Imports**
```java
import com.fasterxml.jackson.databind.ObjectMapper;  // JSON conversion
import jakarta.servlet.annotation.WebServlet;        // @WebServlet
import jakarta.servlet.http.HttpServlet;             // Base class
import jakarta.servlet.http.HttpServletRequest;      // Request object
import jakarta.servlet.http.HttpServletResponse;     // Response object
import java.util.LinkedHashMap;                      // For data
import java.util.Map;                                // Data structure
```

**Part 2: Class Annotation & Declaration**
```java
@WebServlet("/api/employee-json")  // URL = http://localhost:8080/api/employee-json
public class JsonResponseServlet extends HttpServlet {
```

**Part 3: doGet() Method**
```java
@Override  // Override parent method
protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    // Khi browser gửi GET request, method này được gọi
}
```

**Part 4: Create Data**
```java
Map<String, Object> employee = new LinkedHashMap<>();
employee.put("manv", "TeoNV");
employee.put("hoTen", "Nguyễn Văn Tèo");
employee.put("gioiTinh", true);
employee.put("luong", 950.5);
```

**Giải thích:**
- `LinkedHashMap` - Map giữ thứ tự insertion (quan trọng cho JSON)
- `put(key, value)` - Thêm key-value pair
- Sau bước này, data sẽ trông như:
  ```
  {
    manv: "TeoNV",
    hoTen: "Nguyễn Văn Tèo",
    gioiTinh: true,
    luong: 950.5
  }
  ```

**Part 5: Convert to JSON**
```java
String json = mapper.writeValueAsString(employee);
// Result: '{"manv":"TeoNV","hoTen":"Nguyễn Văn Tèo","gioiTinh":true,"luong":950.5}'
```

**Part 6: Set Headers**
```java
resp.setCharacterEncoding("utf-8");     // Encoding cho tiếng Việt
resp.setContentType("application/json");  // Type là JSON
```

**Part 7: Send Response**
```java
resp.getWriter().print(json);  // Write JSON string
resp.flushBuffer();            // Send immediately
```

---

## 🌐 PHẦN 3: TẠO HTML CLIENT

### HTML Cơ Bản

**File path:** `src/main/webapp/ajax-json-example.html`

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>AJAX JSON Example - Bài 1</title>
</head>
<body>
    <h1>Fetch API - JSON Example</h1>
    
    <!-- Button để trigger fetch -->
    <button onclick="loadData()">Load Employee Data</button>
    
    <script>
        // JavaScript function sẽ viết ở đây
    </script>
</body>
</html>
```

**Giải thích:**
- `<head>` - Meta information
- `<meta charset="utf-8">` - Encoding cho tiếng Việt
- `<body>` - Nội dung hiển thị
- `<button onclick="loadData()">` - Khi click, gọi function `loadData()`
- `<script>` - JavaScript code

### JavaScript Function

**Cách hoạt động:**

```javascript
function loadData() {
  // Step 1: Gửi GET request
  fetch("http://localhost:8080/api/employee-json", {method: "GET"})
  
  // Step 2: Parse response thành JSON
  .then(resp => resp.json())
  
  // Step 3: Nhận JSON data
  .then(data => {
    console.log("Employee Data:", data);
  })
  
  // Step 4: Handle error nếu có
  .catch(error => console.error("Error:", error));
}
```

**Giải thích từng Step:**

**Step 1: Fetch Request**
```javascript
fetch("http://localhost:8080/api/employee-json", {method: "GET"})
```
- `fetch()` - Gửi HTTP request
- `"http://localhost:8080/api/employee-json"` - URL của servlet
- `{method: "GET"}` - Options object, specify HTTP method
- Return: Promise (sẽ resolve khi server trả response)

**Step 2: Parse JSON**
```javascript
.then(resp => resp.json())
```
- `.then()` - Khi fetch hoàn thành (server trả response)
- `resp` - Response object từ server
- `resp.json()` - Parse response body thành JSON object
- Return: Promise chứa JSON data

**Step 3: Handle Data**
```javascript
.then(data => {
    console.log("Employee Data:", data);
})
```
- `data` - JSON object đã được parse
- `console.log()` - Print ra browser console
- Kết quả hiển thị:
  ```javascript
  Employee Data: {manv: "TeoNV", hoTen: "Nguyễn Văn Tèo", gioiTinh: true, luong: 950.5}
  ```

**Step 4: Error Handling**
```javascript
.catch(error => console.error("Error:", error));
```
- `.catch()` - Nếu có lỗi ở bước nào
- `console.error()` - Print error message

### Full HTML Code

**Copy toàn bộ vào file:**

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>AJAX JSON Example - Bài 1</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        button {
            padding: 10px 20px;
            font-size: 16px;
            cursor: pointer;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
        }
        button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <h1>Fetch API - JSON Example</h1>
    <p>Click button to load employee data from server</p>
    
    <button onclick="loadData()">Load Employee Data</button>
    
    <script>
        function loadData() {
            // URL của servlet
            var url = "http://localhost:8080/api/employee-json";
            
            // Step 1: Gửi GET request
            fetch(url, {method: "GET"})
            
            // Step 2: Parse response thành JSON
            .then(resp => resp.json())
            
            // Step 3: Xử lý JSON data
            .then(data => {
                console.log("Employee Data:", data);
                alert("Data loaded! Check console (F12) for details.");
            })
            
            // Step 4: Handle error
            .catch(error => console.error("Error:", error));
        }
    </script>
</body>
</html>
```

---

## 🧪 PHẦN 4: TEST & DEBUG

### Bước 1: Build Project

**Command:**
```bash
cd D:\Java4\Lab5_8\lab7
mvn clean package
```

**Kết quả:**
```
BUILD SUCCESS
...
target/ROOT.war
```

**Giải thích:**
- `clean` - Xóa build cũ
- `package` - Compile & tạo WAR file
- `ROOT.war` - File deploy (ROOT = project root path trong Tomcat)

### Bước 2: Deploy

**Copy file:**
```bash
copy target\ROOT.war %TOMCAT_HOME%\webapps\
```

**Kết quả:**
- Tomcat sẽ tự động giải nén war file thành folder `ROOT`
- Files trong `src/main/webapp` → `ROOT/` folder

### Bước 3: Start Tomcat

```bash
%TOMCAT_HOME%\bin\startup.bat
```

**Đợi ~30 giây** cho Tomcat khởi động

**Kiểm tra startup log:**
```bash
type %TOMCAT_HOME%\logs\catalina.out
```

Nên thấy:
```
Server startup in X ms
```

### Bước 4: Test Servlet

**Cách 1: Browser direct**
```
URL: http://localhost:8080/api/employee-json
```

**Kết quả:** Browser sẽ show JSON (hoặc download file)
```json
{"manv":"TeoNV","hoTen":"Nguyễn Văn Tèo","gioiTinh":true,"luong":950.5}
```

**Cách 2: cURL command**
```bash
curl http://localhost:8080/api/employee-json
```

**Kết quả:**
```json
{"manv":"TeoNV","hoTen":"Nguyễn Văn Tèo","gioiTinh":true,"luong":950.5}
```

### Bước 5: Test HTML File

**URL:**
```
http://localhost:8080/ajax-json-example.html
```

**Test flow:**
1. Mở URL → HTML page load
2. Click "Load Employee Data" button
3. Mở Developer Tools (F12)
4. Vào "Console" tab
5. Kiểm tra output:
   ```javascript
   Employee Data: {manv: "TeoNV", hoTen: "Nguyễn Văn Tèo", gioiTinh: true, luong: 950.5}
   ```

### Bước 6: Debug Nếu Có Lỗi

**Error 1: 404 Not Found**
```
GET http://localhost:8080/api/employee-json 404
```

**Nguyên nhân:**
- Servlet URL sai
- Servlet chưa deploy
- Project chưa build

**Giải pháp:**
- Check `@WebServlet("/api/employee-json")`
- Rebuild: `mvn clean package`
- Restart Tomcat

---

**Error 2: SyntaxError: Unexpected token < in JSON at position 0**

**Nguyên nhân:**
- Response không phải JSON (maybe HTML error page)
- Server error 500

**Giải pháp:**
- Check Tomcat logs: `%TOMCAT_HOME%\logs\catalina.out`
- Search `Exception` in logs
- Check console output

---

**Error 3: Cannot read property 'json' of undefined**

**Nguyên nhân:**
- `resp` chưa resolve
- Network error

**Giải pháp:**
```javascript
.then(resp => {
    console.log("Response:", resp);  // Debug
    return resp.json();
})
```

---

### Developer Tools Console

**Cách mở:**
- Press `F12` hoặc `Ctrl+Shift+I`
- Click "Console" tab

**Dùng console để debug:**
```javascript
console.log("Data:", data);        // Print data
console.error("Error:", error);    // Print error
console.table(data);               // Pretty print
```

---

## 📊 SUMMARY

### Cái Gì Xảy Ra Khi Bấm Button

```
1. User click button
   ↓
2. JavaScript function loadData() gọi
   ↓
3. fetch() gửi GET request đến http://localhost:8080/api/employee-json
   ↓
4. Tomcat server nhận request → tìm @WebServlet("/api/employee-json")
   ↓
5. Gọi JsonResponseServlet.doGet()
   ↓
6. doGet() tạo JSON: {"manv":"TeoNV",...}
   ↓
7. doGet() gửi response với header: Content-Type: application/json
   ↓
8. Browser nhận response
   ↓
9. resp.json() parse JSON string thành object
   ↓
10. console.log(data) print data ra console
   ↓
11. F12 Console hiển thị: Employee Data: {...}
```

---

## ✅ CHECKLIST

- [ ] `JsonResponseServlet.java` tạo xong
- [ ] `ajax-json-example.html` tạo xong
- [ ] `mvn clean package` - build success
- [ ] Copy `ROOT.war` to Tomcat webapps
- [ ] Tomcat started
- [ ] `http://localhost:8080/ajax-json-example.html` accessible
- [ ] Click button → F12 console show JSON data
- [ ] Thấy: `Employee Data: {manv: "TeoNV", ...}`

---

## 🎯 EXPECTED OUTPUT

**Browser Console (F12 → Console tab):**
```javascript
Employee Data: {
  manv: "TeoNV",
  hoTen: "Nguyễn Văn Tèo",
  gioiTinh: true,
  luong: 950.5
}
```

---

**Congrats! Bài 1 xong! 🎉**

Next: Bài 2 - File Upload (phức tạp hơn một chút)
