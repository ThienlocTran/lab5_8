# Lab 7 - Bài 1: AJAX JSON Response (2 điểm)

## 🎯 Mục Tiêu

Tạo một servlet gửi dữ liệu JSON về client, sau đó sử dụng **Fetch API** để lấy dữ liệu này và xuất ra console.

---

## 📝 Yêu Cầu

### 1. Tạo Servlet JSON Response

**File cần tạo**: `src/main/java/com/thienloc/jakarta/lab58/servlet/JsonResponseServlet.java`

**Requirements:**
- URL mapping: `/api/employee-json`
- HTTP method: `GET`
- Response format: JSON chứa thông tin nhân viên với cấu trúc sau:
  ```json
  {
    "manv": "TeoNV",
    "hoTen": "Nguyễn Văn Tèo",
    "gioiTinh": true,
    "luong": 950.5
  }
  ```

**Hints:**
- Sử dụng `@WebServlet("/api/employee-json")` annotation
- Extend `HttpServlet`
- Override `doGet()` method
- Có thể dùng `ObjectMapper` từ Jackson library hoặc tạo JSON string thủ công
- Set header: `Content-Type: application/json` và charset UTF-8

---

### 2. Tạo HTML File Client

**File cần tạo**: `src/main/webapp/ajax-json-example.html`

**Requirements:**
- Có một button để trigger yêu cầu
- Sử dụng **Fetch API** để gọi servlet
- Lấy response JSON và xuất ra **browser console**
- Structure cơ bản:
  ```html
  <!DOCTYPE html>
  <html>
  <head>
    <title>AJAX JSON Example - Bài 1</title>
  </head>
  <body>
    <h1>Fetch API - JSON Example</h1>
    <button onclick="...">Load Employee Data</button>
    
    <script>
      function loadData() {
        // TODO: Viết Fetch API code ở đây
      }
    </script>
  </body>
  </html>
  ```

**Hints:**
- Dùng `fetch(url, {method: "GET"})`
- Chain `.then(resp => resp.json())`
- Chain `.then(data => console.log(data))`
- URL: `http://localhost:8080/api/employee-json`

---

## 🔧 Step-by-Step Hướng Dẫn

### Bước 1: Tạo Servlet

1. **Tạo file** `JsonResponseServlet.java` tại đường dẫn trên
2. **Khai báo class:**
   ```java
   @WebServlet("/api/employee-json")
   public class JsonResponseServlet extends HttpServlet {
   ```
3. **Override doGet() method** để xử lý GET request
4. **Tạo dữ liệu JSON** chứa thông tin nhân viên
5. **Set response headers:**
   ```java
   resp.setCharacterEncoding("utf-8");
   resp.setContentType("application/json");
   ```
6. **Gửi response** bằng `resp.getWriter().print(json)`

---

### Bước 2: Tạo HTML File

1. **Tạo file** `ajax-json-example.html` tại đường dẫn trên
2. **Viết HTML cơ bản** với button
3. **Viết JavaScript function `loadData()`:**
   - Gọi `fetch()` với URL servlet
   - Parse JSON response
   - In ra console: `console.log(data)`

---

## 🧪 Cách Test

1. **Build project**:
   ```bash
   mvn clean package
   ```

2. **Deploy** `ROOT.war` lên Tomcat

3. **Start Tomcat**

4. **Mở browser** vào: `http://localhost:8080/ajax-json-example.html`

5. **Click button** "Load Employee Data"

6. **Mở Developer Tools** (F12) → Console tab

7. **Kiểm tra output:**
   - Phải thấy JSON object được in ra
   - Phải thấy các field: `manv`, `hoTen`, `gioiTinh`, `luong`

**Expected console output:**
```javascript
{manv: "TeoNV", hoTen: "Nguyễn Văn Tèo", gioiTinh: true, luong: 950.5}
```

---

## 📚 Kiến Thức Cần Biết

### Fetch API Basic Syntax
```javascript
fetch(url, options)
  .then(response => response.json())
  .then(data => {
    console.log(data);
  })
  .catch(error => console.error('Error:', error));
```

### JSON Format
- Key-value pairs trong `{}`
- String values bọc trong `""`
- Numbers không cần `""`
- Booleans: `true` hoặc `false`
- Comma giữa các items

### HTTP Response Headers
- `Content-Type`: Loại dữ liệu (text/html, application/json, etc.)
- `charset=utf-8`: Encoding

---

## 💡 Tips & Tricks

1. **Kiểm tra network tab** (F12 → Network):
   - Click button và xem request/response
   - Kiểm tra status code (should be 200)

2. **Debug JavaScript**:
   - Dùng `console.log()` để print dữ liệu
   - Dùng `console.error()` để print errors

3. **JSON Validation**:
   - Copy response vào [jsonlint.com](https://jsonlint.com) để validate

4. **Charset issue**:
   - Luôn set UTF-8 cho request/response

---

## ❌ Lỗi Thường Gặp

| Lỗi | Nguyên Nhân | Giải Pháp |
|-----|-------------|----------|
| 404 Not Found | Servlet URL sai hoặc chưa deploy | Check @WebServlet annotation |
| Cannot read property 'json' | Response không phải JSON | Check Content-Type header |
| null/undefined data | JSON format sai | Validate JSON structure |
| Blank console | Function không được gọi | Check onclick handler |

---

## 🎓 Learning Outcomes

Sau bài này, bạn sẽ biết:
- ✅ Tạo servlet đơn giản trả về JSON
- ✅ Sử dụng Fetch API để gọi server
- ✅ Parse JSON response từ server
- ✅ Debug trong browser console
- ✅ Hiểu HTTP request/response cycle

---

## ✨ Bonus (Optional)

Sau khi hoàn thành, bạn có thể thử:
- Thêm multiple buttons cho các nhân viên khác nhau
- Parse JSON và hiển thị dữ liệu trong HTML (không chỉ console)
- Add error handling khi network request fail
- Làm cho UI đẹp hơn với CSS

---

**Status**: Ready for Implementation
**Points**: 2 điểm
**Time Estimate**: 15-20 phút
