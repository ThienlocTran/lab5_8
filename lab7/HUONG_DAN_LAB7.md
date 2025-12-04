# Lab 7 - Hướng Dẫn Làm Bài (AJAX & REST API)

## ⚡ Quick Summary

Lab 7 có **4 bài chính** (10 điểm):
- **Bài 1** (2 điểm): AJAX JSON Response
- **Bài 2** (2 điểm): AJAX File Upload  
- **Bài 3** (2 điểm): RESTful Employee API
- **Bài 4** (2 điểm): Employee Management Web Client
- **Bài 5** (2 điểm): Yêu cầu giảng viên thêm (TBD)

---

## 📚 Hướng Dẫn Chi Tiết (4 File)

Mỗi bài có một file hướng dẫn riêng:

### ✏️ **Bài 1: AJAX JSON Response**
📄 File: `BAI_1_HUONG_DAN.md`
- **Mục tiêu**: Tạo servlet gửi JSON, client lấy bằng Fetch API
- **Thời gian**: ~15-20 phút
- **Công việc**:
  1. Tạo servlet `JsonResponseServlet.java` → `/api/employee-json`
  2. Tạo HTML file `ajax-json-example.html`
  3. Implement Fetch API trong JavaScript
  4. Test: button click → console show JSON

[👉 Đọc chi tiết: `BAI_1_HUONG_DAN.md`](./BAI_1_HUONG_DAN.md)

---

### ✏️ **Bài 2: AJAX File Upload**
📄 File: `BAI_2_HUONG_DAN.md`
- **Mục tiêu**: Upload file lên server, server trả info dưới dạng JSON
- **Thời gian**: ~20-25 phút
- **Công việc**:
  1. Tạo servlet `FileUploadServlet.java` với `@MultipartConfig`
  2. Tạo HTML file `file-upload-ajax.html`
  3. Implement FormData + Fetch API
  4. Test: chọn file → upload → console show file info

[👉 Đọc chi tiết: `BAI_2_HUONG_DAN.md`](./BAI_2_HUONG_DAN.md)

---

### ✏️ **Bài 3: RESTful Employee API**
📄 File: `BAI_3_HUONG_DAN.md`
- **Mục tiêu**: Xây dựng REST API quản lý nhân viên (CRUD)
- **Thời gian**: ~30-40 phút
- **Công việc**:
  1. Tạo utility class `RestIO.java` (JSON conversion)
  2. Tạo entity class `Employee.java`
  3. Tạo servlet `EmployeeRestServlet.java` với 5 endpoints
  4. Test: dùng Postman để test GET/POST/PUT/DELETE

[👉 Đọc chi tiết: `BAI_3_HUONG_DAN.md`](./BAI_3_HUONG_DAN.md)

---

### ✏️ **Bài 4: Employee Management Web Client**
📄 File: `BAI_4_HUONG_DAN.md`
- **Mục tiêu**: Tạo web UI quản lý nhân viên (tương tác với API từ Bài 3)
- **Thời gian**: ~40-50 phút
- **Công việc**:
  1. Tạo HTML file `employee-rest-client.html`
  2. Implement JavaScript controller object với 9 methods
  3. CRUD operations: Create, Read, Update, Delete
  4. Test: form input → button click → table update

[👉 Đọc chi tiết: `BAI_4_HUONG_DAN.md`](./BAI_4_HUONG_DAN.md)

---

## 🚀 Các Bước Làm Việc

### Step 1: Chuẩn Bị
- ✅ Đọc yêu cầu từ đề bài
- ✅ Hiểu kiến thức cơ bản (Servlet, Fetch API, REST)
- ✅ Setup IDE (IntelliJ/Eclipse) mở project lab7

### Step 2: Làm Từng Bài Theo Thứ Tự
```
Bài 1 (Đơn giản nhất)
    ↓
Bài 2 (Phức tạp hơn một chút)
    ↓
Bài 3 (Phức tạp - công việc nhiều)
    ↓
Bài 4 (Phức tạp - JS code nhiều)
```

### Step 3: Test Mỗi Bài Khi Hoàn Thành
- Build: `mvn clean package`
- Deploy: copy `target/ROOT.war` to Tomcat
- Restart Tomcat
- Test theo hướng dẫn

### Step 4: Commit Code
```bash
git add .
git commit -m "Lab 7 - Bài 1 complete"
git commit -m "Lab 7 - Bài 2 complete"
# ... etc
```

---

## 📝 Điều Gì Cần Tạo

### File Java (Backend)

| Bài | File | Purpose |
|-----|------|---------|
| 1 | `JsonResponseServlet.java` | GET /api/employee-json |
| 2 | `FileUploadServlet.java` | POST /api/upload |
| 3 | `RestIO.java` | JSON utility class |
| 3 | `Employee.java` | Entity model |
| 3 | `EmployeeRestServlet.java` | REST API (GET/POST/PUT/DELETE /employees) |

### File HTML/JS (Frontend)

| Bài | File | Purpose |
|-----|------|---------|
| 1 | `ajax-json-example.html` | Load JSON example |
| 2 | `file-upload-ajax.html` | Upload file example |
| 4 | `employee-rest-client.html` | Employee management UI |

---

## 📦 pom.xml (Dependencies)

Cần thêm 2 dependencies vào `pom.xml`:

```xml
<!-- Jackson for JSON -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.9.1</version>
</dependency>

<!-- Apache Commons FileUpload -->
<dependency>
    <groupId>commons-fileupload</groupId>
    <artifactId>commons-fileupload</artifactId>
    <version>1.5</version>
</dependency>
```

---

## 🧪 Cách Test

### Test Bài 1 & 2: Browser
```
1. Build & deploy
2. Mở browser → URL của HTML file
3. Click button → xem console (F12)
```

### Test Bài 3: Postman
```
1. Mở Postman
2. Tạo requests cho 5 endpoints
3. Test GET, POST, PUT, DELETE
```

### Test Bài 4: Browser (Full CRUD)
```
1. Mở HTML file
2. Create → table add new row
3. Edit → form fill, Update → table change
4. Delete → row disappear
```

---

## 💡 Tips Quan Trọng

### 1. **Đọc hướng dẫn kỹ**
- Mỗi bài có hướng dẫn chi tiết
- Đừng skip phần "Hints" - nó giúp rất nhiều

### 2. **Code từng method một**
- Không code tất cả cùng lúc
- Test sau mỗi method để kiểm tra

### 3. **Kiểm tra error messages**
- Đọc stack trace khi có lỗi
- Google lỗi nếu chưa gặp trước

### 4. **Sử dụng console.log() nhiều**
- Debug JavaScript bằng console
- In dữ liệu ra để verify

### 5. **Test thứ tự hợp lý**
- Bài 1 đơn giản → dễ hiểu kiến thức
- Bài 4 phức tạp nhất → cần hiểu tất cả

---

## 🔗 Kiến Thức Cần Học

### Servlet
- `@WebServlet` annotation
- `doGet()`, `doPost()`, `doPut()`, `doDelete()` methods
- Request/Response objects

### Fetch API (JavaScript)
- `fetch(url, options)` syntax
- `.then(resp => resp.json())`
- `.catch(error => ...)`

### JSON
- Format: `{key: value}`
- Serialization (Object → JSON)
- Deserialization (JSON → Object)

### REST Architecture
- HTTP Methods: GET, POST, PUT, DELETE
- Status codes: 200, 201, 404, 500
- Idempotency concept

### DOM Manipulation (JavaScript)
- `document.getElementById()`
- `.value`, `.innerHTML`, `.checked`
- Event handlers: `onclick=`

---

## ⚠️ Lỗi Thường Gặp

| Lỗi | Giải Pháp |
|-----|----------|
| 404 Not Found | Kiểm tra @WebServlet URL, rebuild project |
| null pointer | Kiểm tra element ID trong HTML/JS matching |
| JSON parse error | Validate JSON format, set UTF-8 |
| CORS error | Thêm CorsFilter hoặc dùng same origin |
| File not uploading | Kiểm tra @MultipartConfig, permissions |

---

## ✅ Checklist

Sau khi hoàn thành toàn bộ Lab 7:

- [ ] Bài 1: JSON servlet + HTML working
- [ ] Bài 2: Upload servlet + HTML working  
- [ ] Bài 3: REST API all 5 endpoints tested
- [ ] Bài 4: Web UI full CRUD working
- [ ] All tests pass (no errors)
- [ ] Code committed to git
- [ ] Ready to submit

---

## 📞 Khi Gặp Khó Khăn

1. **Đọc lại hướng dẫn** - có hints chi tiết
2. **Kiểm tra console** - error message thường nói rõ vấn đề
3. **Google error message** - nhiều khi dễ tìm solution
4. **Debug bằng console.log()** - print dữ liệu để kiểm tra
5. **Ask classmates** - sharing kinh nghiệm rất hữu ích

---

## 🎓 Sau Khi Hoàn Thành

Bạn sẽ biết:
- ✅ Tạo servlet xử lý khác nhau (JSON, file upload)
- ✅ Thiết kế & implement REST API
- ✅ Sử dụng Fetch API trong JavaScript
- ✅ CRUD operations từ web UI
- ✅ Debug & test web applications
- ✅ JSON serialization/deserialization
- ✅ HTTP request/response lifecycle

---

**Status**: Ready to Start! 🚀

Bắt đầu với **Bài 1** - nó đơn giản nhất!

[👉 Start: BAI_1_HUONG_DAN.md](./BAI_1_HUONG_DAN.md)
