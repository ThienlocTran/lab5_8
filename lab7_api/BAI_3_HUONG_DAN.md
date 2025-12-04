# Lab 7 - Bài 3: RESTful Employee API (2 điểm)

## 🎯 Mục Tiêu

Xây dựng **REST API** (Representational State Transfer) để quản lý nhân viên, hỗ trợ các thao tác **CRUD** (Create, Read, Update, Delete) sử dụng các HTTP methods khác nhau.

---

## 📝 Yêu Cầu

### 1. Tạo Utility Class RestIO

**File cần tạo**: `src/main/java/com/thienloc/jakarta/lab58/util/RestIO.java`

**Requirements:**
- Hỗ trợ chuyển đổi giữa **JSON string** và **Java Object**
- Có 5 static methods:
  - `readJson(HttpServletRequest)` - Đọc JSON từ request body
  - `writeJson(HttpServletResponse, String)` - Gửi JSON response
  - `readObject(HttpServletRequest, Class<T>)` - Đọc JSON và convert to Object
  - `writeObject(HttpServletResponse, Object)` - Convert Object to JSON và gửi
  - `writeEmptyObject(HttpServletResponse)` - Gửi empty `{}`

**Hints:**
- Dùng `ObjectMapper` từ Jackson library
- Đọc request body bằng `req.getReader()` trong loop
- Set headers: `Content-Type: application/json` và UTF-8

---

### 2. Tạo Entity Class Employee

**File cần tạo**: `src/main/java/com/thienloc/jakarta/lab58/entity/Employee.java`

**Requirements:**
- Properties:
  - `String id` - Mã nhân viên (e.g., "NV01")
  - `String name` - Tên nhân viên
  - `boolean gender` - true = Male, false = Female
  - `double salary` - Mức lương
- Có constructor không tham số và constructor với tất cả tham số
- Có getter/setter cho tất cả properties

---

### 3. Tạo REST Servlet

**File cần tạo**: `src/main/java/com/thienloc/jakarta/lab58/servlet/EmployeeRestServlet.java`

**Requirements:**
- URL mapping: `/employees/*`
- Hỗ trợ 5 HTTP methods: GET, POST, PUT, DELETE
- Lưu dữ liệu trong `Map<String, Employee>` (in-memory storage)
- Khởi tạo 5 nhân viên mặc định: NV01 → NV05
- Implement các endpoints:

| Endpoint | Method | Function |
|----------|--------|----------|
| `/employees` | GET | Lấy tất cả nhân viên (return collection) |
| `/employees/{id}` | GET | Lấy nhân viên theo ID (return single object) |
| `/employees` | POST | Tạo nhân viên mới (read JSON from body) |
| `/employees/{id}` | PUT | Cập nhật nhân viên (read JSON from body) |
| `/employees/{id}` | DELETE | Xóa nhân viên |

**Hints:**
- Override methods: `doGet()`, `doPost()`, `doPut()`, `doDelete()`
- Lấy path info: `req.getPathInfo()` → substring lấy ID
- Dùng `RestIO` class để handle JSON conversion
- GET `/employees` vs `/employees/ID` có logic khác nhau

---

## 🔧 Step-by-Step Hướng Dẫn

### Bước 1: Tạo RestIO Utility Class

1. **Tạo file** `RestIO.java`

2. **Implement readJson():**
   ```java
   public static String readJson(HttpServletRequest req) throws IOException {
       req.setCharacterEncoding("utf-8");
       BufferedReader reader = req.getReader();
       String line;
       StringBuffer buffer = new StringBuffer();
       while((line = reader.readLine()) != null) {
           buffer.append(line);
       }
       reader.close();
       return buffer.toString();
   }
   ```

3. **Implement writeJson():**
   ```java
   public static void writeJson(HttpServletResponse resp, String json) 
           throws IOException {
       resp.setCharacterEncoding("utf-8");
       resp.setContentType("application/json");
       resp.getWriter().print(json);
       resp.flushBuffer();
   }
   ```

4. **Implement readObject():**
   - Gọi `readJson()` để lấy JSON string
   - Dùng `ObjectMapper.readValue(json, clazz)` để convert

5. **Implement writeObject():**
   - Dùng `ObjectMapper.writeValueAsString(object)` để convert to JSON
   - Gọi `writeJson()` để gửi

6. **Implement writeEmptyObject():**
   - Gọi `writeObject()` với `Map.of()` (empty map)

---

### Bước 2: Tạo Employee Entity

1. **Tạo file** `Employee.java`

2. **Khai báo properties:**
   ```java
   private String id;
   private String name;
   private boolean gender;
   private double salary;
   ```

3. **Tạo constructors:**
   - Default constructor (no args)
   - Constructor với tất cả parameters

4. **Tạo getter/setter** cho tất cả properties

---

### Bước 3: Tạo REST Servlet

1. **Tạo file** `EmployeeRestServlet.java`

2. **Khai báo class:**
   ```java
   @WebServlet("/employees/*")
   public class EmployeeRestServlet extends HttpServlet {
       private Map<String, Employee> map = new HashMap<>(Map.of(
           "NV01", new Employee("NV01", "Nhân viên 01", true, 500),
           "NV02", new Employee("NV02", "Nhân viên 02", false, 1500),
           "NV03", new Employee("NV03", "Nhân viên 03", true, 5000),
           "NV04", new Employee("NV04", "Nhân viên 04", false, 2500),
           "NV05", new Employee("NV05", "Nhân viên 05", true, 3500)
       ));
   ```

3. **Implement doGet():**
   ```java
   String info = req.getPathInfo();
   if(info == null || info.length() == 0) {
       // GET /employees → trả về tất cả
       RestIO.writeObject(resp, map.values());
   } else {
       // GET /employees/ID → trả về một
       String id = info.substring(1).trim();
       RestIO.writeObject(resp, map.get(id));
   }
   ```

4. **Implement doPost():**
   ```java
   Employee employee = RestIO.readObject(req, Employee.class);
   map.put(employee.getId(), employee);
   RestIO.writeObject(resp, employee);
   ```

5. **Implement doPut():**
   ```java
   String id = req.getPathInfo().substring(1).trim();
   Employee employee = RestIO.readObject(req, Employee.class);
   map.put(id, employee);
   RestIO.writeEmptyObject(resp);
   ```

6. **Implement doDelete():**
   ```java
   String id = req.getPathInfo().substring(1).trim();
   map.remove(id);
   RestIO.writeEmptyObject(resp);
   ```

---

## 🧪 Cách Test (Sử dụng Postman)

### 1. GET - Lấy tất cả nhân viên
```
Method: GET
URL: http://localhost:8080/employees
Expected: JSON array với 5 nhân viên
Status: 200 OK
```

### 2. GET - Lấy nhân viên theo ID
```
Method: GET
URL: http://localhost:8080/employees/NV03
Expected: JSON object NV03
Status: 200 OK
```

### 3. POST - Tạo nhân viên mới
```
Method: POST
URL: http://localhost:8080/employees
Headers: Content-Type: application/json
Body (raw):
{
  "id": "NV06",
  "name": "Nhân viên 06",
  "gender": false,
  "salary": 9500.0
}
Expected: JSON object NV06 được tạo
Status: 200 OK
```

### 4. PUT - Cập nhật nhân viên
```
Method: PUT
URL: http://localhost:8080/employees/NV06
Headers: Content-Type: application/json
Body (raw):
{
  "id": "NV06",
  "name": "Nguyễn Văn Tèo",
  "gender": true,
  "salary": 9500.0
}
Expected: Empty JSON {}
Status: 200 OK
```

### 5. DELETE - Xóa nhân viên
```
Method: DELETE
URL: http://localhost:8080/employees/NV06
Expected: Empty JSON {}
Status: 200 OK
```

### 6. Verify - GET tất cả sau DELETE
```
Method: GET
URL: http://localhost:8080/employees
Expected: NV06 không còn trong list, còn 5 nhân viên
Status: 200 OK
```

---

## 📚 Kiến Thức Cần Biết

### REST Architecture
- **GET** - Lấy dữ liệu (safe, idempotent)
- **POST** - Tạo dữ liệu mới (không idempotent)
- **PUT** - Cập nhật toàn bộ resource (idempotent)
- **PATCH** - Cập nhật một phần (không idempotent)
- **DELETE** - Xóa dữ liệu (idempotent)

### URL Path vs Query String
- Path: `/employees/NV03` - part of resource identifier
- Query: `/employees?name=test` - filter/search parameters

### Jackson ObjectMapper
```java
ObjectMapper mapper = new ObjectMapper();
String json = mapper.writeValueAsString(object);  // Object → JSON
Object obj = mapper.readValue(json, Employee.class);  // JSON → Object
```

### HTTP Response Codes
- 200 OK - Request successful
- 201 Created - Resource created
- 204 No Content - Successful but no content
- 400 Bad Request - Invalid request
- 404 Not Found - Resource not found
- 500 Internal Server Error - Server error

---

## 💡 Tips & Tricks

1. **Testing REST API dengan cURL:**
   ```bash
   # GET
   curl http://localhost:8080/employees
   
   # POST
   curl -X POST http://localhost:8080/employees \
     -H "Content-Type: application/json" \
     -d '{"id":"NV06","name":"Test","gender":true,"salary":5000}'
   ```

2. **Postman Collection:**
   - Save requests trong collection để reuse
   - Use environment variables cho URL

3. **Error Handling:**
   - Wrap code trong try-catch
   - Send appropriate error responses

4. **Testing Order:**
   - Test GET trước (safest)
   - Test POST (create)
   - Test PUT (update)
   - Test DELETE (remove)
   - Test GET lại (verify)

---

## ❌ Lỗi Thường Gặp

| Lỗi | Nguyên Nhân | Giải Pháp |
|-----|-------------|----------|
| 405 Method Not Allowed | Method không được implement | Override doPut(), doDelete() |
| null object | readObject() fail | Check JSON format, set UTF-8 |
| Empty response | writeObject() gọi sai | Verify Object không null |
| 404 on POST/PUT/DELETE | Servlet chưa deploy | Rebuild và redeploy |
| JSON parse error | Invalid JSON format | Validate JSON structure |

---

## 🔍 Debugging Tips

1. **Print request body:**
   ```java
   String json = RestIO.readJson(req);
   System.out.println("Request: " + json);
   ```

2. **Print object thành JSON:**
   ```java
   System.out.println(mapper.writeValueAsString(employee));
   ```

3. **Check map contents:**
   ```java
   map.forEach((k, v) -> System.out.println(k + ": " + v));
   ```

4. **Postman Console:**
   - View → Show Postman Console
   - See request/response details

---

## 🎓 Learning Outcomes

Sau bài này, bạn sẽ biết:
- ✅ Thiết kế REST API theo chuẩn
- ✅ Implement các HTTP methods (GET, POST, PUT, DELETE)
- ✅ JSON serialization/deserialization
- ✅ Xử lý path parameters (`/employees/{id}`)
- ✅ Test API bằng Postman
- ✅ Hiểu idempotency trong REST

---

## ✨ Bonus (Optional)

Sau khi hoàn thành, bạn có thể thử:
- Persist data to database (instead of in-memory)
- Add validation (validate employee data)
- Add error responses (return error JSON)
- Add HTTP status codes (201 for create, 204 for delete, etc.)
- Add CORS support (for cross-origin requests)
- Implement PATCH method (partial updates)

---

**Status**: Ready for Implementation
**Points**: 2 điểm
**Time Estimate**: 30-40 phút
