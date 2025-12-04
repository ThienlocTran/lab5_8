# Lab 7 - Bài 3: RESTful Employee API (Chi Tiết)

## 📖 Khái Niệm REST API

### API là gì?

**API** = **Application Programming Interface**

**Tác dụng:**
- Giao diện để 2 phần mềm communicate với nhau
- Ví dụ: Weather app → gọi Weather API → nhận dữ liệu

### REST là gì?

**REST** = **Representational State Transfer**

**Nguyên tắc REST:**
1. **Resources** - Data được chia thành resources (employees, posts, users)
2. **HTTP Methods** - Dùng GET/POST/PUT/DELETE để tương tác
3. **Stateless** - Mỗi request độc lập, không lưu session
4. **JSON** - Dùng JSON để exchange data

### REST API là gì?

**REST API** = API theo chuẩn REST

**Cấu trúc:**
- Base URL: `http://localhost:8080/employees`
- Resource: Employee
- Actions:
  - GET `/employees` - Lấy tất cả
  - GET `/employees/1` - Lấy một
  - POST `/employees` - Tạo
  - PUT `/employees/1` - Cập nhật
  - DELETE `/employees/1` - Xóa

### HTTP Methods

| Method | Purpose | Idempotent | Safe |
|--------|---------|-----------|------|
| GET | Read | Yes | Yes |
| POST | Create | No | No |
| PUT | Update (full) | Yes | No |
| PATCH | Update (partial) | No | No |
| DELETE | Delete | Yes | No |

**Giải thích:**
- **Idempotent**: Gọi nhiều lần = kết quả giống nhau
- **Safe**: Không thay đổi server state

---

## 🔄 REST API Flow

### Sequence Diagram

```
[CLIENT]                    [SERVER]
  ↓
1. GET /employees
  ↓ (HTTP GET)          →
                        2. Servlet tìm GET endpoint
                           ↓
                           3. doGet() method
                           ↓
                           4. Query database/map
                           ↓
                           5. Convert to JSON
                           ↓
                           6. Send response
  ↓ (HTTP Response)    ←
7. Browser nhận JSON
  ↓
8. Parse & display
```

### 5 Endpoints Bài 3

| # | Method | URL | Purpose | Request Body | Response |
|---|--------|-----|---------|--------------|----------|
| 1 | GET | `/employees` | Get all | - | Array of employees |
| 2 | GET | `/employees/{id}` | Get one | - | Single employee |
| 3 | POST | `/employees` | Create | Employee JSON | Created employee |
| 4 | PUT | `/employees/{id}` | Update | Employee JSON | {} |
| 5 | DELETE | `/employees/{id}` | Delete | - | {} |

---

## 💼 PHẦN 1: ENTITY CLASS (Employee.java)

### Lớp Entity là gì?

**Entity** = Class đại diện cho dữ liệu

**Ví dụ:**
```java
// Real employee: Tèo, Male, Salary 5000
// Java entity: Employee(id="NV01", name="Tèo", gender=true, salary=5000)
```

### Employee Properties

```
id       → String   → "NV01"
name     → String   → "Nguyễn Văn Tèo"
gender   → boolean  → true (Male), false (Female)
salary   → double   → 5000.0
```

### Jackson Serialization

**Jackson** = Library convert Object ↔ JSON

**Ví dụ:**
```java
// Java Object:
Employee e = new Employee("NV01", "Tèo", true, 5000);

// Jackson convert to JSON:
ObjectMapper mapper = new ObjectMapper();
String json = mapper.writeValueAsString(e);
// Result: {"id":"NV01","name":"Tèo","gender":true,"salary":5000}

// Jackson convert to Object:
Employee e2 = mapper.readValue(json, Employee.class);
```

### Employee.java Code

**File path:** `src/main/java/com/thienloc/jakarta/lab58/entity/Employee.java`

```java
package com.thienloc.jakarta.lab58.entity;

public class Employee {
    
    // TODO 1: Properties
    private String id;
    private String name;
    private boolean gender;
    private double salary;
    
    // TODO 2: Default constructor (no args)
    // Required by Jackson for deserialization
    public Employee() {}
    
    // TODO 3: Constructor with all parameters
    public Employee(String id, String name, boolean gender, double salary) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.salary = salary;
    }
    
    // TODO 4: Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isGender() {
        return gender;
    }
    
    public void setGender(boolean gender) {
        this.gender = gender;
    }
    
    public double getSalary() {
        return salary;
    }
    
    public void setSalary(double salary) {
        this.salary = salary;
    }
    
    // TODO 5 (Optional): toString() for debugging
    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", salary=" + salary +
                '}';
    }
}
```

### Giải Thích

**Default Constructor:**
```java
public Employee() {}
```
- Required by Jackson
- Dùng khi deserialize JSON → Object

**All Args Constructor:**
```java
public Employee(String id, String name, boolean gender, double salary) {
    this.id = id;
    this.name = name;
    this.gender = gender;
    this.salary = salary;
}
```
- Tiện khi create instance trong code

**Getters/Setters:**
```java
public String getId() { return id; }
public void setId(String id) { this.id = id; }
```
- Required by Jackson for serialization
- Jackson automatically call getters/setters

---

## 🛠️ PHẦN 2: UTILITY CLASS (RestIO.java)

### RestIO Purpose

**RestIO** = Utility class để handle JSON ↔ Request/Response conversion

**Methods:**
- `readJson()` - Read JSON string from request body
- `writeJson()` - Write JSON string to response
- `readObject()` - Read JSON & convert to Object
- `writeObject()` - Convert Object to JSON & write
- `writeEmptyObject()` - Write empty {}

### Reading Request Body

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

**Giải thích:**
- `req.getReader()` - Get input stream
- Loop `readLine()` - Read line by line
- `append()` - Build complete JSON string
- `reader.close()` - Close stream

**Ví dụ:**
```
Client sends:
{"id":"NV01","name":"Tèo","gender":true,"salary":5000}

readJson() returns:
'{"id":"NV01","name":"Tèo","gender":true,"salary":5000}'
```

### Writing Response

```java
public static void writeJson(HttpServletResponse resp, String json)
        throws IOException {
    resp.setCharacterEncoding("utf-8");
    resp.setContentType("application/json");
    resp.getWriter().print(json);
    resp.flushBuffer();
}
```

**Giải thích:**
- `setCharacterEncoding()` - UTF-8 for Vietnamese
- `setContentType()` - Tell browser it's JSON
- `getWriter().print()` - Write JSON string
- `flushBuffer()` - Send immediately

### Reading Object from Request

```java
public static <T> T readObject(HttpServletRequest req, Class<T> clazz)
        throws IOException {
    String json = readJson(req);                    // 1. Get JSON string
    T bean = mapper.readValue(json, clazz);        // 2. Convert to Object
    return bean;
}
```

**Generic Syntax `<T>`:**
```java
<T> T readObject(...)
```
- `<T>` = Generic type parameter
- Can be any class: Employee, User, Product, etc.

**Ví dụ:**
```java
// Usage:
Employee emp = RestIO.readObject(req, Employee.class);
// emp = Employee object parsed from JSON

// Another usage:
User user = RestIO.readObject(req, User.class);
// user = User object parsed from JSON
```

### Writing Object to Response

```java
public static void writeObject(HttpServletResponse resp, Object data)
        throws IOException {
    String json = mapper.writeValueAsString(data);  // 1. Convert to JSON
    writeJson(resp, json);                          // 2. Write to response
}
```

**Ví dụ:**
```java
Employee emp = new Employee("NV01", "Tèo", true, 5000);
RestIO.writeObject(resp, emp);
// Sends: {"id":"NV01","name":"Tèo","gender":true,"salary":5000}
```

### Writing Empty Object

```java
public static void writeEmptyObject(HttpServletResponse resp)
        throws IOException {
    writeObject(resp, Map.of());  // Empty Map → {}
}
```

**Ví dụ:**
```java
// For DELETE/PUT (no response data)
RestIO.writeEmptyObject(resp);
// Sends: {}
```

### RestIO.java Full Code

**File path:** `src/main/java/com/thienloc/jakarta/lab58/util/RestIO.java`

```java
package com.thienloc.jakarta.lab58.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class RestIO {
    
    // TODO 1: Static ObjectMapper (shared)
    static private ObjectMapper mapper = new ObjectMapper();
    
    // TODO 2: Read JSON from request body
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
    
    // TODO 3: Write JSON to response
    public static void writeJson(HttpServletResponse resp, String json)
            throws IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        resp.getWriter().print(json);
        resp.flushBuffer();
    }
    
    // TODO 4: Read JSON from request & convert to Object
    public static <T> T readObject(HttpServletRequest req, Class<T> clazz)
            throws IOException {
        String json = RestIO.readJson(req);           // Step 1: Get JSON
        T bean = mapper.readValue(json, clazz);       // Step 2: Convert
        return bean;                                   // Step 3: Return
    }
    
    // TODO 5: Convert Object to JSON & write to response
    public static void writeObject(HttpServletResponse resp, Object data)
            throws IOException {
        String json = mapper.writeValueAsString(data);  // Convert to JSON
        RestIO.writeJson(resp, json);                   // Write to response
    }
    
    // TODO 6: Write empty object {}
    public static void writeEmptyObject(HttpServletResponse resp)
            throws IOException {
        RestIO.writeObject(resp, Map.of());  // Map.of() = empty map
    }
}
```

---

## 🔗 PHẦN 3: REST SERVLET (EmployeeRestServlet.java)

### Servlet URL Mapping

```java
@WebServlet("/employees/*")
```

**Giải thích:**
- `/employees/*` = Match any path under `/employees`
- Examples:
  - `/employees` ← matches
  - `/employees/NV01` ← matches
  - `/employees/` ← matches
  - `/employees/NV01/something` ← matches

### Getting Path Parameter

```java
String info = req.getPathInfo();
```

**Ví dụ:**
```
Request: GET /employees/NV01
info = "/NV01"

Request: GET /employees
info = null or ""

Request: GET /employees/
info = "/"
```

**Extract ID:**
```java
String id = info.substring(1).trim();  // Remove "/" prefix
// "/NV01" → "NV01"
```

### Data Storage

```java
private Map<String, Employee> map = new HashMap<>(Map.of(
    "NV01", new Employee("NV01", "Nhân viên 01", true, 500),
    "NV02", new Employee("NV02", "Nhân viên 02", false, 1500),
    // ...
));
```

**Giải thích:**
- `Map<String, Employee>` - Key: ID, Value: Employee
- `HashMap` - Mutable map (can add/remove)
- `Map.of()` - Initialize with default employees
- In production: dùng database thay vì in-memory map

---

### doGet() Implementation

**Purpose:** Handle GET requests

```java
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
    
    String info = req.getPathInfo();  // Get path: null, "/", "/NV01"
    
    if(info == null || info.length() == 0 || info.equals("/")) {
        // GET /employees → Return all employees
        RestIO.writeObject(resp, map.values());
    } else {
        // GET /employees/NV01 → Return specific employee
        String id = info.substring(1).trim();
        RestIO.writeObject(resp, map.get(id));
    }
}
```

**Hai cases:**

**Case 1: GET /employees**
```
info = null (or empty)
→ Return all employees
→ writeObject(resp, map.values())
→ Response: [employee1, employee2, employee3, ...]
```

**Case 2: GET /employees/NV01**
```
info = "/NV01"
→ Extract ID: "NV01"
→ Get from map: map.get("NV01")
→ Return: employee object (or null if not found)
```

---

### doPost() Implementation

**Purpose:** Create new employee

```java
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
    
    // Step 1: Read JSON from request body & convert to Employee
    Employee employee = RestIO.readObject(req, Employee.class);
    
    // Step 2: Add to map
    map.put(employee.getId(), employee);
    
    // Step 3: Return created employee
    RestIO.writeObject(resp, employee);
}
```

**Flow:**
```
1. Client sends:
   POST /employees
   {"id":"NV06","name":"New Employee","gender":true,"salary":9000}

2. readObject() converts JSON → Employee object

3. map.put() adds to map

4. writeObject() returns Employee (confirmation)
```

---

### doPut() Implementation

**Purpose:** Update existing employee

```java
@Override
protected void doPut(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
    
    // Step 1: Extract ID from URL
    String id = req.getPathInfo().substring(1).trim();
    
    // Step 2: Read updated employee from request body
    Employee employee = RestIO.readObject(req, Employee.class);
    
    // Step 3: Update in map
    map.put(id, employee);
    
    // Step 4: Return empty response
    RestIO.writeEmptyObject(resp);
}
```

**Flow:**
```
1. Client sends:
   PUT /employees/NV06
   {"id":"NV06","name":"Updated Name","gender":false,"salary":10000}

2. Extract ID from URL: "NV06"

3. readObject() converts JSON → Employee

4. map.put() updates (or creates if not exists)

5. writeEmptyObject() returns {}
```

---

### doDelete() Implementation

**Purpose:** Delete employee

```java
@Override
protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
    
    // Step 1: Extract ID from URL
    String id = req.getPathInfo().substring(1).trim();
    
    // Step 2: Remove from map
    map.remove(id);
    
    // Step 3: Return empty response
    RestIO.writeEmptyObject(resp);
}
```

**Flow:**
```
1. Client sends:
   DELETE /employees/NV06

2. Extract ID: "NV06"

3. map.remove() removes employee

4. writeEmptyObject() returns {}
```

---

### Full EmployeeRestServlet Code

**File path:** `src/main/java/com/thienloc/jakarta/lab58/servlet/EmployeeRestServlet.java`

```java
package com.thienloc.jakarta.lab58.servlet;

import com.thienloc.jakarta.lab58.entity.Employee;
import com.thienloc.jakarta.lab58.util.RestIO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// TODO 1: @WebServlet with /* wildcard
@WebServlet("/employees/*")

// TODO 2: Extend HttpServlet
public class EmployeeRestServlet extends HttpServlet {
    
    // TODO 3: In-memory storage with default data
    private Map<String, Employee> map = new HashMap<>(Map.of(
        "NV01", new Employee("NV01", "Nhân viên 01", true, 500),
        "NV02", new Employee("NV02", "Nhân viên 02", false, 1500),
        "NV03", new Employee("NV03", "Nhân viên 03", true, 5000),
        "NV04", new Employee("NV04", "Nhân viên 04", false, 2500),
        "NV05", new Employee("NV05", "Nhân viên 05", true, 3500)
    ));
    
    // TODO 4: GET - Lấy tất cả hoặc lấy một theo ID
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String info = req.getPathInfo();
        
        if(info == null || info.length() == 0 || info.equals("/")) {
            // GET /employees → Return all
            RestIO.writeObject(resp, map.values());
        } else {
            // GET /employees/ID → Return one
            String id = info.substring(1).trim();
            RestIO.writeObject(resp, map.get(id));
        }
    }
    
    // TODO 5: POST - Tạo nhân viên mới
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        Employee employee = RestIO.readObject(req, Employee.class);
        map.put(employee.getId(), employee);
        RestIO.writeObject(resp, employee);
    }
    
    // TODO 6: PUT - Cập nhật nhân viên
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String id = req.getPathInfo().substring(1).trim();
        Employee employee = RestIO.readObject(req, Employee.class);
        map.put(id, employee);
        RestIO.writeEmptyObject(resp);
    }
    
    // TODO 7: DELETE - Xóa nhân viên
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String id = req.getPathInfo().substring(1).trim();
        map.remove(id);
        RestIO.writeEmptyObject(resp);
    }
}
```

---

## 🧪 PHẦN 4: TEST VỚI POSTMAN

### Postman Installation

1. Download từ [postman.com](https://www.postman.com/downloads/)
2. Install & open

### Test 1: GET All Employees

**Setup:**
- Method: `GET`
- URL: `http://localhost:8080/employees`
- Headers: (none needed)
- Body: (none)

**Click "Send"**

**Expected Response (200 OK):**
```json
[
  {
    "id": "NV01",
    "name": "Nhân viên 01",
    "gender": true,
    "salary": 500.0
  },
  {
    "id": "NV02",
    "name": "Nhân viên 02",
    "gender": false,
    "salary": 1500.0
  },
  ...
]
```

---

### Test 2: GET Single Employee

**Setup:**
- Method: `GET`
- URL: `http://localhost:8080/employees/NV03`

**Expected Response (200 OK):**
```json
{
  "id": "NV03",
  "name": "Nhân viên 03",
  "gender": true,
  "salary": 5000.0
}
```

---

### Test 3: POST Create

**Setup:**
- Method: `POST`
- URL: `http://localhost:8080/employees`
- Headers: `Content-Type: application/json`
- Body (raw, JSON):
```json
{
  "id": "NV06",
  "name": "Nhân viên 06",
  "gender": false,
  "salary": 9500.0
}
```

**Click "Send"**

**Expected Response (200 OK):**
```json
{
  "id": "NV06",
  "name": "Nhân viên 06",
  "gender": false,
  "salary": 9500.0
}
```

---

### Test 4: PUT Update

**Setup:**
- Method: `PUT`
- URL: `http://localhost:8080/employees/NV06`
- Headers: `Content-Type: application/json`
- Body (raw, JSON):
```json
{
  "id": "NV06",
  "name": "Nguyễn Văn Tèo",
  "gender": true,
  "salary": 9500.0
}
```

**Expected Response (200 OK):**
```json
{}
```

---

### Test 5: DELETE

**Setup:**
- Method: `DELETE`
- URL: `http://localhost:8080/employees/NV06`

**Expected Response (200 OK):**
```json
{}
```

---

### Test 6: Verify Delete

**Setup:**
- Method: `GET`
- URL: `http://localhost:8080/employees`

**Expected Response:**
- NV06 không còn trong list
- Chỉ còn 5 employees (NV01-NV05)

---

## 📊 SUMMARY TABLE

| # | Method | URL | Body | Response | Purpose |
|---|--------|-----|------|----------|---------|
| 1 | GET | `/employees` | - | Array | Get all |
| 2 | GET | `/employees/NV03` | - | Object | Get one |
| 3 | POST | `/employees` | JSON | Object | Create |
| 4 | PUT | `/employees/NV06` | JSON | {} | Update |
| 5 | DELETE | `/employees/NV06` | - | {} | Delete |

---

## ✅ CHECKLIST

- [ ] Employee.java created with all properties/getters/setters
- [ ] RestIO.java created with 5 methods
- [ ] EmployeeRestServlet.java created with all 5 endpoints
- [ ] @WebServlet, @Override methods correct
- [ ] Build: `mvn clean package` success
- [ ] Deploy ROOT.war
- [ ] Tomcat started
- [ ] Test 1: GET /employees ✓
- [ ] Test 2: GET /employees/NV03 ✓
- [ ] Test 3: POST /employees (create) ✓
- [ ] Test 4: PUT /employees/NV06 (update) ✓
- [ ] Test 5: DELETE /employees/NV06 (delete) ✓
- [ ] Test 6: GET /employees (verify) ✓

---

**Congrats! Bài 3 xong! 🎉**

Next: Bài 4 - Web Client (cuối cùng!)
