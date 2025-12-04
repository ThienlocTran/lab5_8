# Lab 7: Lập Trình Java #4 - AJAX & REST API

## Tổng Quan

Lab 7 là bài học về các kỹ thuật lập trình web nâng cao:
- **Fetch API** - Gửi/nhận dữ liệu từ server mà không reload trang
- **REST API** - Thiết kế API theo chuẩn REST architecture
- **JSON** - Format dữ liệu phổ biến trong web services
- **FormData** - Upload file lên server

## Nội Dung

### Bài 1: AJAX JSON Response (2 điểm)
- **Mục tiêu**: Sử dụng Fetch API để tải dữ liệu JSON từ servlet
- **File chính**: 
  - `JsonResponseServlet.java` - Gửi JSON response
  - `ajax-json-example.html` - HTML client
- **Test**: Truy cập `http://localhost:8080/ajax-json-example.html`

### Bài 2: AJAX File Upload (2 điểm)
- **Mục tiêu**: Upload file lên server sử dụng Fetch API
- **File chính**:
  - `FileUploadServlet.java` - Xử lý file upload
  - `file-upload-ajax.html` - HTML client
- **Test**: Truy cập `http://localhost:8080/file-upload-ajax.html`

### Bài 3: RESTful API (2 điểm)
- **Mục tiêu**: Xây dựng REST API quản lý nhân viên
- **File chính**:
  - `EmployeeRestServlet.java` - REST endpoints
  - `Employee.java` - Entity model
  - `RestIO.java` - Utility class
- **Endpoints**:
  - `GET /employees` - Lấy tất cả
  - `GET /employees/{id}` - Lấy theo ID
  - `POST /employees` - Thêm mới
  - `PUT /employees/{id}` - Cập nhật
  - `DELETE /employees/{id}` - Xóa
- **Test**: Sử dụng Postman hoặc `test_api.bat`

### Bài 4: Employee Management Client (2 điểm)
- **Mục tiêu**: Tạo web interface quản lý nhân viên
- **File chính**: `employee-rest-client.html`
- **Tính năng**: Create, Read, Update, Delete employees
- **Test**: Truy cập `http://localhost:8080/employee-rest-client.html`

### Bài 5: Yêu Cầu Giảng Viên (2 điểm)
- Sẽ được cung cấp thêm

## Cấu Trúc Thư Mục

```
lab7/
├── pom.xml                                 # Maven configuration
├── build.bat                               # Build script
├── test_api.bat                            # Test REST API
├── README.md                               # This file
├── LAB7_IMPLEMENTATION_GUIDE.md            # Detailed guide
├── TEST_URLS.md                            # Testing guide with curl/Postman
├── src/
│   ├── main/
│   │   ├── java/com/thienloc/jakarta/lab58/
│   │   │   ├── util/
│   │   │   │   └── RestIO.java             # JSON utility
│   │   │   ├── entity/
│   │   │   │   └── Employee.java           # Employee model
│   │   │   ├── servlet/
│   │   │   │   ├── JsonResponseServlet.java
│   │   │   │   ├── FileUploadServlet.java
│   │   │   │   └── EmployeeRestServlet.java
│   │   │   └── filter/
│   │   │       └── CorsFilter.java         # CORS support
│   │   └── webapp/
│   │       ├── index.html                  # Home page
│   │       ├── ajax-json-example.html
│   │       ├── file-upload-ajax.html
│   │       └── employee-rest-client.html
│   └── resources/
└── target/
    └── ROOT.war                            # Build output
```

## Quick Start

### 1. Build Project
```bash
cd lab7
./build.bat
```

### 2. Deploy to Tomcat
```
Copy target/ROOT.war to $TOMCAT_HOME/webapps/
```

### 3. Start Tomcat
```
$TOMCAT_HOME/bin/startup.bat
```

### 4. Access Application
```
Browser: http://localhost:8080
```

## Testing

### Bài 1 & 2: Browser Testing
1. Mở browser → Vào trang HTML
2. Thực hiện thao tác
3. Mở Developer Tools (F12) → Console tab
4. Kiểm tra output

### Bài 3: REST API Testing

**Option 1: Using Postman**
- Download & install Postman
- Create requests for each endpoint
- (Chi tiết trong TEST_URLS.md)

**Option 2: Using cURL/Batch Script**
```bash
./test_api.bat
```

**Option 3: Manual cURL**
```bash
# Get all employees
curl http://localhost:8080/employees

# Create new
curl -X POST http://localhost:8080/employees \
  -H "Content-Type: application/json" \
  -d '{"id":"NV06","name":"Test","gender":true,"salary":5000}'
```

### Bài 4: Web Client Testing
1. Truy cập `http://localhost:8080/employee-rest-client.html`
2. Danh sách nhân viên tải tự động
3. Thực hiện: Create → Edit → Update → Delete
4. Kiểm tra console (F12) để xem requests

## Dependencies

Project uses:
- **Jackson** - JSON serialization/deserialization
- **Apache Commons FileUpload** - File upload handling
- **Jakarta Servlet API** - Web framework
- **Lombok** - Code generation (optional)

## Key Features

✅ **Fetch API** - Modern JavaScript HTTP requests
✅ **REST Architecture** - Proper HTTP methods (GET, POST, PUT, DELETE)
✅ **JSON Format** - Standard data exchange
✅ **File Upload** - Multipart form data handling
✅ **CORS Support** - Cross-origin requests
✅ **Responsive UI** - Clean, user-friendly interface

## Notes

1. **Default Data**: REST API comes with 5 employees (NV01-NV05)
2. **Upload Directory**: Files saved to `/uploads` folder
3. **CORS**: Enabled for all origins (production should restrict this)
4. **Validation**: Client-side validation in HTML forms
5. **Session**: Stateless API (no authentication in this lab)

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Port 8080 already in use | Change Tomcat port or kill process |
| 404 Not Found | Check deployment, restart Tomcat |
| CORS Error | Check CorsFilter is registered |
| JSON Parse Error | Validate JSON format with validator |
| Upload fails | Check /uploads directory permissions |

## File URLs

After deployment, access:
- Home: `http://localhost:8080/`
- Bài 1: `http://localhost:8080/ajax-json-example.html`
- Bài 2: `http://localhost:8080/file-upload-ajax.html`
- Bài 3: `http://localhost:8080/employees` (REST API)
- Bài 4: `http://localhost:8080/employee-rest-client.html`

## Additional Resources

- `LAB7_IMPLEMENTATION_GUIDE.md` - Detailed implementation guide
- `TEST_URLS.md` - Complete testing guide with curl/Postman examples

## Total Points

- Bài 1: 2 points
- Bài 2: 2 points
- Bài 3: 2 points
- Bài 4: 2 points
- Bài 5: 2 points (to be added)
- **Total: 10 points**

---

**Created**: 2025-12-04
**Status**: ✅ Bài 1-4 Completed
