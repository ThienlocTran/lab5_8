# Lab 7 - Hướng Dẫn Test Với Postman và cURL

## I. BƯỚC CHUẨN BỊ

### 1. Build Project
```bash
cd D:\Java4\Lab5_8\lab7
mvnw.cmd clean package
# hoặc: mvn clean package
```

### 2. Deploy lên Tomcat
- Copy `target/ROOT.war` vào `$TOMCAT_HOME/webapps/`
- Hoặc deploy qua IDE

### 3. Kiểm tra Server
```bash
# Đợi Tomcat khởi động (thường 10-30 giây)
curl http://localhost:8080/
# Response: Should see welcome page
```

---

## II. BÀI 1: AJAX JSON RESPONSE

### URL: `http://localhost:8080/ajax-json-example.html`

### Test bằng Browser
1. Mở browser, truy cập URL trên
2. Click button "Load Employee Data"
3. Mở Developer Tools (F12)
4. Vào tab Console
5. Kiểm tra output:
   ```
   Employee Data: {manv: "TeoNV", hoTen: "Nguyễn Văn Tèo", gioiTinh: true, luong: 950.5}
   ```

### Test bằng cURL
```bash
# Lấy dữ liệu JSON từ servlet
curl -X GET http://localhost:8080/api/employee-json

# Expected Response:
# {"manv":"TeoNV","hoTen":"Nguyễn Văn Tèo","gioiTinh":true,"luong":950.5}
```

---

## III. BÀI 2: FILE UPLOAD AJAX

### URL: `http://localhost:8080/file-upload-ajax.html`

### Test bằng Browser
1. Mở browser, truy cập URL trên
2. Chọn file bất kỳ (txt, pdf, image, etc.)
3. Click "Upload File"
4. Mở Developer Tools (F12)
5. Vào tab Console
6. Kiểm tra output:
   ```
   Upload Result: {name: "filename.ext", type: "file/mime-type", size: 12345}
   ```

### Test bằng cURL
```bash
# Upload file từ command line
curl -X POST -F "file=@C:\path\to\file.txt" http://localhost:8080/api/upload

# Expected Response:
# {"name":"file.txt","type":"text/plain","size":12345}
```

### Kiểm tra file được lưu
```bash
# Trên Windows:
dir %TOMCAT_HOME%\webapps\ROOT\uploads\

# Hoặc trong Linux/Mac:
ls -la $TOMCAT_HOME/webapps/ROOT/uploads/
```

---

## IV. BÀI 3: RESTFUL API EMPLOYEE MANAGEMENT

### REST API Endpoints

#### 1. GET - Lấy tất cả nhân viên
```bash
curl -X GET http://localhost:8080/employees

# Expected Response:
# [
#   {"id":"NV01","name":"Nhân viên 01","gender":true,"salary":500.0},
#   {"id":"NV02","name":"Nhân viên 02","gender":false,"salary":1500.0},
#   {"id":"NV03","name":"Nhân viên 03","gender":true,"salary":5000.0},
#   {"id":"NV04","name":"Nhân viên 04","gender":false,"salary":2500.0},
#   {"id":"NV05","name":"Nhân viên 05","gender":true,"salary":3500.0}
# ]
```

#### 2. GET - Lấy nhân viên theo ID
```bash
curl -X GET http://localhost:8080/employees/NV03

# Expected Response:
# {"id":"NV03","name":"Nhân viên 03","gender":true,"salary":5000.0}
```

#### 3. POST - Thêm nhân viên mới
```bash
# Cách 1: Using cURL with inline JSON
curl -X POST http://localhost:8080/employees \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"NV06\",\"name\":\"Nhân viên 06\",\"gender\":false,\"salary\":9500.0}"

# Expected Response:
# {"id":"NV06","name":"Nhân viên 06","gender":false,"salary":9500.0}
```

#### 4. POST - Thêm nhân viên khác
```bash
curl -X POST http://localhost:8080/employees \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"NV06\",\"name\":\"Nguyễn Văn Tèo\",\"gender\":true,\"salary\":9500.0}"

# Expected Response:
# {"id":"NV06","name":"Nguyễn Văn Tèo","gender":true,"salary":9500.0}
# (Note: Overwrites NV06 from previous operation)
```

#### 5. PUT - Cập nhật nhân viên
```bash
curl -X PUT http://localhost:8080/employees/NV06 \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"NV06\",\"name\":\"Nguyễn Văn Tèo Updated\",\"gender\":true,\"salary\":10000.0}"

# Expected Response:
# {}
```

#### 6. DELETE - Xóa nhân viên
```bash
curl -X DELETE http://localhost:8080/employees/NV06

# Expected Response:
# {}
```

#### 7. GET - Kiểm tra sau khi xóa
```bash
curl -X GET http://localhost:8080/employees

# Expected Response: NV06 không còn trong list
# [
#   {"id":"NV01","name":"Nhân viên 01","gender":true,"salary":500.0},
#   {"id":"NV02","name":"Nhân viên 02","gender":false,"salary":1500.0},
#   {"id":"NV03","name":"Nhân viên 03","gender":true,"salary":5000.0},
#   {"id":"NV04","name":"Nhân viên 04","gender":false,"salary":2500.0},
#   {"id":"NV05","name":"Nhân viên 05","gender":true,"salary":3500.0}
# ]
```

### Test bằng Postman

1. **Mở Postman**
2. **Tạo Request - GET All Employees**
   - Method: GET
   - URL: `http://localhost:8080/employees`
   - Click Send
   - Kiểm tra Response

3. **Tạo Request - GET One Employee**
   - Method: GET
   - URL: `http://localhost:8080/employees/NV03`
   - Click Send

4. **Tạo Request - POST New Employee**
   - Method: POST
   - URL: `http://localhost:8080/employees`
   - Headers: `Content-Type: application/json`
   - Body (raw):
     ```json
     {
       "id": "NV06",
       "name": "Nhân viên 06",
       "gender": false,
       "salary": 9500.0
     }
     ```
   - Click Send

5. **Tạo Request - PUT Update Employee**
   - Method: PUT
   - URL: `http://localhost:8080/employees/NV06`
   - Headers: `Content-Type: application/json`
   - Body (raw):
     ```json
     {
       "id": "NV06",
       "name": "Nguyễn Văn Tèo",
       "gender": true,
       "salary": 9500.0
     }
     ```
   - Click Send

6. **Tạo Request - DELETE Employee**
   - Method: DELETE
   - URL: `http://localhost:8080/employees/NV06`
   - Click Send

7. **Verify - GET All Again**
   - Method: GET
   - URL: `http://localhost:8080/employees`
   - Click Send
   - Kiểm tra NV06 đã bị xóa

---

## V. BÀI 4: EMPLOYEE MANAGEMENT WEB CLIENT

### URL: `http://localhost:8080/employee-rest-client.html`

### Cách Test
1. Mở browser, truy cập URL trên
2. Trang sẽ tự động tải danh sách nhân viên

### Thực hiện các thao tác

#### 1. Xem Danh Sách
- Danh sách nhân viên đã tải tự động
- Hiển thị tất cả 5 nhân viên mặc định (NV01-NV05)

#### 2. Chỉnh Sửa Nhân Viên Hiện Tại
- Click link "Edit" trên hàng nhân viên muốn sửa
- Thông tin nhân viên sẽ được điền vào form
- Sửa thông tin và click "Update"
- Danh sách sẽ cập nhật

#### 3. Thêm Nhân Viên Mới
- Click "Reset" để làm trống form
- Điền thông tin nhân viên mới (ID, Name, Gender, Salary)
- Click "Create"
- Nhân viên mới sẽ xuất hiện trong danh sách

#### 4. Xóa Nhân Viên
- Click "Edit" để chọn nhân viên muốn xóa
- Click "Delete"
- Nhân viên sẽ bị xóa khỏi danh sách

#### 5. Làm Trống Form
- Click "Reset" để xóa tất cả thông tin trong form

### Kịch bản Test Hoàn Chỉnh
```
1. Load page → Hiển thị 5 nhân viên (NV01-NV05)
2. Edit NV03 → Thay đổi tên → Update
3. Create NV06 → Nhân viên mới xuất hiện
4. Edit NV06 → Thay đổi thông tin → Update
5. Delete NV06 → Nhân viên bị xóa
6. Verify → Danh sách quay về 5 nhân viên ban đầu
```

---

## VI. KIỂM TRA CONSOLE LOG

Tất cả các thao tác sẽ có log trong browser console (F12 → Console tab):
- Create/Update/Delete operations
- Network requests
- Errors (nếu có)

---

## VII. CURL Commands File (Test All)

Tạo file `test_lab7.sh` (hoặc `.bat` trên Windows):

```bash
#!/bin/bash

echo "=== LAB 7 TEST ==="
echo ""

echo "1. GET - All Employees"
curl -X GET http://localhost:8080/employees
echo ""
echo ""

echo "2. GET - Employee NV03"
curl -X GET http://localhost:8080/employees/NV03
echo ""
echo ""

echo "3. POST - Create NV06"
curl -X POST http://localhost:8080/employees \
  -H "Content-Type: application/json" \
  -d '{"id":"NV06","name":"Nhân viên 06","gender":false,"salary":9500.0}'
echo ""
echo ""

echo "4. PUT - Update NV06"
curl -X PUT http://localhost:8080/employees/NV06 \
  -H "Content-Type: application/json" \
  -d '{"id":"NV06","name":"Nguyễn Văn Tèo","gender":true,"salary":9500.0}'
echo ""
echo ""

echo "5. DELETE - Delete NV06"
curl -X DELETE http://localhost:8080/employees/NV06
echo ""
echo ""

echo "6. GET - All Employees (After Delete)"
curl -X GET http://localhost:8080/employees
echo ""
```

Chạy file:
```bash
# Linux/Mac:
chmod +x test_lab7.sh
./test_lab7.sh

# Windows (PowerShell):
.\test_lab7.ps1
```

---

## VIII. Troubleshooting

### Connection Refused
```
Error: Connection refused at localhost:8080
```
**Giải pháp**: Kiểm tra Tomcat đã khởi động chưa
```bash
# Khởi động Tomcat
$TOMCAT_HOME/bin/startup.sh  # Linux/Mac
$TOMCAT_HOME\bin\startup.bat # Windows
```

### 404 Not Found
```
Error: 404 Not Found for URL
```
**Giải pháp**: Kiểm tra URL và đảm bảo project đã deploy
- Check URL có đúng không
- Check file `.war` có được deploy vào webapps không
- Restart Tomcat

### CORS Error
```
Access to XMLHttpRequest blocked by CORS policy
```
**Giải pháp**: Kiểm tra browser console và cấu hình CORS nếu cần

### JSON Parse Error
```
SyntaxError: Unexpected token in JSON
```
**Giải pháp**: Kiểm tra JSON format có đúng không, sử dụng JSON validator

---

## IX. Expected Test Results

| Bài | Thao tác | Expected | Status |
|-----|---------|----------|--------|
| 1 | GET /api/employee-json | JSON response | ✓ |
| 2 | POST /api/upload | File info JSON | ✓ |
| 3 | GET /employees | All employees array | ✓ |
| 3 | GET /employees/NV03 | Single employee | ✓ |
| 3 | POST /employees | New employee | ✓ |
| 3 | PUT /employees/ID | Empty object | ✓ |
| 3 | DELETE /employees/ID | Empty object | ✓ |
| 4 | Load HTML | Employee list displayed | ✓ |
| 4 | Create | New employee added | ✓ |
| 4 | Update | Employee updated | ✓ |
| 4 | Delete | Employee removed | ✓ |

---

**Tác giả**: Lab 7 Testing Guide
**Ngày cập nhật**: 2025-12-04
