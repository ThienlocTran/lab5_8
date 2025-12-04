# Lab 7: Lập Trình Java #4 - Hướng Dẫn Thực Hiện

## Tổng Quan
Lab 7 yêu cầu xây dựng các ứng dụng AJAX và REST API sử dụng Fetch API, bao gồm 5 bài:
- **Bài 1**: AJAX - JSON Response từ Servlet
- **Bài 2**: AJAX - File Upload
- **Bài 3**: RESTful API Employee Management
- **Bài 4**: Web Client quản lý nhân viên
- **Bài 5**: Yêu cầu giảng viên thêm

## Bài 1: AJAX JSON Response (2 điểm)

### Mục tiêu
Tạo servlet gửi JSON về client và sử dụng Fetch API để đọc dữ liệu.

### Các file đã tạo
1. **JsonResponseServlet** (`/api/employee-json`)
   - Gửi JSON object chứa thông tin nhân viên
   - Response format: `{manv, hoTen, gioiTinh, luong}`

2. **ajax-json-example.html**
   - Giao diện để test Fetch API
   - Click button "Load Employee Data" để gọi servlet
   - Kết quả hiển thị trong console

### Cách test
1. Build project: `mvn clean package`
2. Deploy lên Tomcat
3. Truy cập: `http://localhost:8080/ajax-json-example.html`
4. Click "Load Employee Data" button
5. Kiểm tra browser console (F12 → Console tab)
6. Dữ liệu JSON sẽ được in ra console

## Bài 2: AJAX File Upload (2 điểm)

### Mục tiêu
Tạo servlet nhận file upload và trả về thông tin file dưới dạng JSON.

### Các file đã tạo
1. **FileUploadServlet** (`/api/upload`)
   - Nhận file upload via FormData
   - Lưu file vào thư mục `/uploads`
   - Response: `{name, type, size}`

2. **file-upload-ajax.html**
   - Giao diện chọn file và upload
   - Hiển thị kết quả trong console

### Cách test
1. Build project: `mvn clean package`
2. Deploy lên Tomcat
3. Truy cập: `http://localhost:8080/file-upload-ajax.html`
4. Chọn file và click "Upload File"
5. Kiểm tra console để xem kết quả
6. Kiểm tra thư mục `/uploads` xem file có được lưu không

## Bài 3: RESTful API Employee Management (2 điểm)

### Mục tiêu
Xây dựng REST API quản lý nhân viên hỗ trợ các thao tác CRUD.

### Các file đã tạo

#### Utility Class
- **RestIO** - Hỗ trợ chuyển đổi giữa JSON và Java Object
  - `readJson()`: Đọc JSON từ request
  - `writeJson()`: Gửi JSON response
  - `readObject()`: Đọc JSON và convert thành Java Object
  - `writeObject()`: Convert Object thành JSON và gửi
  - `writeEmptyObject()`: Gửi empty object

#### Entity Class
- **Employee** - Model nhân viên
  - id: String (mã nhân viên)
  - name: String (tên nhân viên)
  - gender: boolean (true=Male, false=Female)
  - salary: double (lương)

#### REST Servlet
- **EmployeeRestServlet** (`/employees/*`)
  - GET /employees → Lấy tất cả nhân viên
  - GET /employees/ID → Lấy nhân viên theo ID
  - POST /employees → Thêm nhân viên mới
  - PUT /employees/ID → Cập nhật nhân viên
  - DELETE /employees/ID → Xóa nhân viên

### Cách test bằng Postman

1. **GET - Lấy tất cả nhân viên**
   ```
   Method: GET
   URL: http://localhost:8080/employees
   Response: Array of employees
   ```

2. **GET - Lấy nhân viên theo ID**
   ```
   Method: GET
   URL: http://localhost:8080/employees/NV03
   Response: Single employee object
   ```

3. **POST - Thêm nhân viên mới**
   ```
   Method: POST
   URL: http://localhost:8080/employees
   Headers: Content-Type: application/json
   Body (raw JSON):
   {
       "id": "NV06",
       "name": "Nhân viên 06",
       "gender": false,
       "salary": 9500.0
   }
   Response: Created employee object
   ```

4. **PUT - Cập nhật nhân viên**
   ```
   Method: PUT
   URL: http://localhost:8080/employees/NV06
   Headers: Content-Type: application/json
   Body (raw JSON):
   {
       "id": "NV06",
       "name": "Nguyễn Văn Tèo",
       "gender": true,
       "salary": 9500.0
   }
   Response: Empty object {}
   ```

5. **DELETE - Xóa nhân viên**
   ```
   Method: DELETE
   URL: http://localhost:8080/employees/NV06
   Response: Empty object {}
   ```

## Bài 4: Employee Management Web Client (2 điểm)

### Mục tiêu
Tạo web interface để quản lý nhân viên, tương tác với REST API thông qua Fetch API.

### Các file đã tạo
- **employee-rest-client.html** - Giao diện quản lý nhân viên

### Các chức năng
1. **Load All** - Hiển thị danh sách tất cả nhân viên trong bảng
2. **Create** - Tạo nhân viên mới từ form
3. **Update** - Cập nhật nhân viên đã chọn
4. **Delete** - Xóa nhân viên đã chọn
5. **Edit** - Chọn nhân viên từ bảng để sửa
6. **Reset** - Xóa trắng form

### Cách test
1. Build project: `mvn clean package`
2. Deploy lên Tomcat
3. Truy cập: `http://localhost:8080/employee-rest-client.html`
4. Thực hiện các thao tác:
   - Danh sách nhân viên tự động tải khi page load
   - Click "Edit" trên hàng trong bảng để chỉnh sửa
   - Nhập thông tin và click "Create" để thêm mới
   - Click "Update" để cập nhật
   - Click "Delete" để xóa
   - Click "Reset" để làm trống form

## Hướng Dẫn Build và Deploy

### Build Project
```bash
cd D:\Java4\Lab5_8\lab7
mvnw.cmd clean package
# Hoặc: mvn clean package (nếu có maven global)
```

Output: `target/ROOT.war`

### Deploy trên Tomcat
1. Copy file `ROOT.war` vào thư mục `$TOMCAT_HOME/webapps/`
2. Tomcat sẽ tự động giải nén war file
3. Truy cập: `http://localhost:8080`

### Deploy qua IDE (IntelliJ IDEA)
1. Cấu hình Tomcat trong IDE
2. Right-click project → Run on Server
3. IDE sẽ tự động build và deploy

## Các Dependencies Đã Thêm

```xml
<!-- Jackson for JSON processing -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.9.1</version>
</dependency>

<!-- Apache Commons FileUpload for file upload handling -->
<dependency>
    <groupId>commons-fileupload</groupId>
    <artifactId>commons-fileupload</artifactId>
    <version>1.5</version>
</dependency>
```

## Cấu Trúc Project

```
lab7/
├── pom.xml (updated with Jackson and FileUpload)
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/thienloc/jakarta/lab58/
│   │   │       ├── util/
│   │   │       │   └── RestIO.java
│   │   │       ├── entity/
│   │   │       │   └── Employee.java
│   │   │       └── servlet/
│   │   │           ├── JsonResponseServlet.java
│   │   │           ├── FileUploadServlet.java
│   │   │           └── EmployeeRestServlet.java
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml
│   │       ├── ajax-json-example.html
│   │       ├── file-upload-ajax.html
│   │       └── employee-rest-client.html
│   └── resources/
└── target/
    └── ROOT.war
```

## Lưu Ý Quan Trọng

1. **CORS Configuration**: Nếu client và server không cùng origin, có thể gặp CORS error. Trong trường hợp này, nên cấu hình CORS filter.

2. **File Upload Directory**: Thư mục `/uploads` sẽ được tạo tự động tại `$TOMCAT_HOME/webapps/ROOT/uploads`

3. **JSON Format**: Đảm bảo employee object có cách viết đúng:
   - `id` (không phải `manv`)
   - `name` (không phải `hoTen`)
   - `gender` (không phải `gioiTinh`)
   - `salary` (không phải `luong`)

4. **Testing Order**: Nên test theo thứ tự: Bài 1 → Bài 2 → Bài 3 (Postman) → Bài 4

## Bài 5: Yêu Cầu Giảng Viên Thêm

Bài 5 sẽ được giảng viên cung cấp thêm. Tất cả các file cần thiết cho bài 1-4 đã hoàn thành.

---

**Tác giả**: Lab 7 Implementation
**Ngày cập nhật**: 2025-12-04
