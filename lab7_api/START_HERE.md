# 🚀 Lab 7 - Bắt Đầu Tại Đây

## ⚡ Tóm Tắt (30 giây)

Lab 7 là bài học về **AJAX** và **REST API**:

- **Bài 1**: Servlet gửi JSON, client nhận bằng Fetch API
- **Bài 2**: Upload file từ browser, server trả file info
- **Bài 3**: REST API CRUD (Create, Read, Update, Delete)
- **Bài 4**: Web UI quản lý nhân viên (CRUD UI)
- **Bài 5**: Waiting cho giảng viên

---

## 📖 Đọc Hướng Dẫn (Bắt Buộc)

Có **1 file hướng dẫn chính** + **4 file hướng dẫn chi tiết cho mỗi bài**:

### File Hướng Dẫn Chính
📄 **`HUONG_DAN_LAB7.md`** ← 👈 Đọc cái này trước!
- Tổng quan toàn bộ Lab
- Thứ tự làm bài
- Kiến thức cần học

### 4 File Hướng Dẫn Chi Tiết (Đọc khi làm bài)

1. 📄 **`BAI_1_HUONG_DAN.md`** - AJAX JSON
   - Mục tiêu, yêu cầu, step-by-step
   - Hints & tips
   - Cách test

2. 📄 **`BAI_2_HUONG_DAN.md`** - File Upload
   - File upload handling
   - FormData + Fetch
   - Test tips

3. 📄 **`BAI_3_HUONG_DAN.md`** - REST API
   - REST architecture
   - 5 endpoints (GET/POST/PUT/DELETE)
   - Postman test guide

4. 📄 **`BAI_4_HUONG_DAN.md`** - Web Client
   - JavaScript controller
   - 9 methods to implement
   - Full CRUD test scenarios

---

## 🔧 Chuẩn Bị

### 1. Cài Đặt Dependencies

Thêm vào `pom.xml` (tìm `</dependencies>` tag):

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

### 2. Kiểm Tra Cấu Trúc Folder

```
lab7/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/thienloc/jakarta/lab58/
│   │   │       ├── servlet/      ← Tạo servlet ở đây
│   │   │       ├── entity/       ← Tạo entity ở đây
│   │   │       └── util/         ← Tạo utility ở đây
│   │   └── webapp/
│   │       └── *.html            ← Tạo HTML file ở đây
│   └── resources/
└── pom.xml                        ← Thêm dependencies ở đây
```

### 3. Build Project

```bash
cd D:\Java4\Lab5_8\lab7
mvn clean package
```

Hoặc nhấn Build button trong IDE.

---

## 📝 Thứ Tự Làm Bài

### Week 1: Bài 1 & 2
1. **Đọc** `HUONG_DAN_LAB7.md`
2. **Làm** `BAI_1_HUONG_DAN.md`
   - Tạo `JsonResponseServlet.java`
   - Tạo `ajax-json-example.html`
   - Test & verify
3. **Làm** `BAI_2_HUONG_DAN.md`
   - Tạo `FileUploadServlet.java`
   - Tạo `file-upload-ajax.html`
   - Test & verify

### Week 2: Bài 3 & 4
4. **Làm** `BAI_3_HUONG_DAN.md`
   - Tạo `RestIO.java`
   - Tạo `Employee.java`
   - Tạo `EmployeeRestServlet.java`
   - Test bằng Postman
5. **Làm** `BAI_4_HUONG_DAN.md`
   - Tạo `employee-rest-client.html`
   - Implement 9 JavaScript methods
   - Test full CRUD

---

## 🧪 Test Mỗi Bài

### Build & Deploy
```bash
# Build
mvn clean package

# Deploy (copy ROOT.war to Tomcat webapps/)
copy target\ROOT.war %TOMCAT_HOME%\webapps\

# Start Tomcat
%TOMCAT_HOME%\bin\startup.bat

# Wait ~30 seconds
```

### Test Bài 1
```
Browser → http://localhost:8080/ajax-json-example.html
Click button → F12 Console → See JSON
```

### Test Bài 2
```
Browser → http://localhost:8080/file-upload-ajax.html
Select file → Click Upload → F12 Console → See file info
```

### Test Bài 3
```
Postman → Create 5 requests
GET /employees, GET /employees/ID, POST, PUT, DELETE
Test all → All should return JSON
```

### Test Bài 4
```
Browser → http://localhost:8080/employee-rest-client.html
Create → Edit → Update → Delete (full CRUD test)
```

---

## 💻 Git Commits

Commit sau mỗi bài:

```bash
git add .
git commit -m "Lab 7 - Bài 1 complete: AJAX JSON"
git commit -m "Lab 7 - Bài 2 complete: File Upload"
git commit -m "Lab 7 - Bài 3 complete: REST API"
git commit -m "Lab 7 - Bài 4 complete: Web Client"
```

---

## 🆘 Khi Gặp Lỗi

### Error: `405 Method Not Allowed`
→ Servlet chưa override method (doPost(), doPut(), doDelete())

### Error: `404 Not Found`
→ Rebuild project, check @WebServlet URL

### Error: `Cannot read property X`
→ Element ID trong HTML ≠ Java code

### Error: `JSON parse error`
→ Invalid JSON format, validate với jsonlint.com

### Error: `null pointer exception`
→ Check xem Object có null không, add null check

---

## 🎯 Success Criteria

✅ Bài hoàn thành khi:
- [ ] Code compile không có error
- [ ] Dapat test theo hướng dẫn
- [ ] Kết quả match expected output
- [ ] Không có console error (F12)

---

## 📞 Frequently Asked Questions

**Q: Tôi nên code từ đâu?**
A: Bắt đầu từ Bài 1 - nó đơn giản nhất, giúp bạn hiểu foundational concepts

**Q: Tôi không hiểu REST API?**
A: Read "📚 Kiến Thức Cần Biết" section trong `BAI_3_HUONG_DAN.md`

**Q: Làm sao để debug?**
A: Dùng `console.log()` trong JavaScript, `System.out.println()` trong Java, F12 Network tab

**Q: Tôi code xong nhưng không work?**
A: Check error messages ở IDE console & browser console, follow troubleshooting guide

**Q: Code có phải 100% match hướng dẫn không?**
A: Không, hướng dẫn chỉ là reference. Your implementation có thể khác một chút

**Q: Tôi có thể skip bài nào không?**
A: Không. Bài 3 & 4 phụ thuộc vào kiến thức từ bài trước

---

## 📚 Extra Resources

Nếu muốn học thêm:
- **Fetch API**: [MDN Fetch API](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API)
- **REST API**: [REST API Best Practices](https://restfulapi.net/)
- **Servlet**: [Jakarta EE Servlet Docs](https://projects.eclipse.org/projects/ee4j.servlet)
- **Jackson JSON**: [Jackson Documentation](https://github.com/FasterXML/jackson-databind)

---

## ✨ Bonus Tips

1. **Write clean code** - Đặt tên biến rõ ràng, comment khi cần
2. **Test early & often** - Không code hết xong mới test
3. **Use IDE features** - Auto-complete, refactoring tools
4. **Read error messages** - Stack traces thường nói đúng vấn đề
5. **Commit frequently** - Mỗi feature/bài làm xong → commit

---

## 🚀 Ready?

### Step 1: Thêm dependencies vào pom.xml
### Step 2: Build project (`mvn clean package`)
### Step 3: Đọc `HUONG_DAN_LAB7.md`
### Step 4: Bắt đầu với `BAI_1_HUONG_DAN.md`

---

**Good luck! You got this! 💪**

---

*Last updated: 2025-12-04*
*Status: Ready to Start*
