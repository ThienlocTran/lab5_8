ạn k# Hướng Dẫn Test URLs

## ✅ Đã Implement

### 3 Servlet đã được cập nhật:
1. ✅ `AccountSetting.java` - Xử lý `/account/sign-in`, `/account/change-password`, `/account/edit-profile`
2. ✅ `WatchVideo.java` - Xử lý `/video/list`, `/video/detail/*`, `/video/like/*`, `/video/share/*`
3. ✅ `AdminData.java` - Xử lý `/admin/video`, `/admin/user`, `/admin/like`, `/admin/sha`

### AuthFilter đã bảo vệ:
- `/account/change-password`
- `/account/edit-profile`
- `/video/like/*`
- `/video/share/*`
- `/admin/*`

---

## 🏗️ Build Project

```bash
cd D:\Java4\Lab5_8\lab6
mvn clean package
```

Nếu build thành công → File `target/ROOT.war` sẽ được tạo

---

## 🚀 Chạy ứng dụng

Deploy `ROOT.war` lên Tomcat hoặc chạy từ IDE

Ứng dụng sẽ chạy tại: `http://localhost:8080`

---

## 🧪 Test URLs

### **Chuẩn Bị: Tạo test data hoặc đăng nhập**

Bạn cần:
- User bình thường: `id=user123`, `password=123456` (hoặc dữ liệu từ database)
- Admin user: `id=admin`, `password=admin123` (hoặc dữ liệu từ database)

Nếu chưa có, hãy insert vào database:

```sql
INSERT INTO [User] (id_User, fullName, email, password_User, Admin_User) 
VALUES ('user123', 'User Test', 'user@test.com', '123456', 0);

INSERT INTO [User] (id_User, fullName, email, password_User, Admin_User) 
VALUES ('admin', 'Admin User', 'admin@test.com', 'admin123', 1);
```

---

## 📋 Test Cases

### **1️⃣ Trang Không Bảo Vệ: `/account/sign-in`**

**Test 1.1: Không đăng nhập**
```bash
curl -i http://localhost:8080/account/sign-in
```
✅ **Kỳ vọng:** 
- Status: `200 OK`
- Body: `<h1>Sign In Page</h1>`

**Test 1.2: Sau khi đăng nhập (User)**
```bash
curl -i http://localhost:8080/account/sign-in
```
✅ **Kỳ vọng:** 
- Status: `200 OK`
- Body: `<h1>Sign In Page</h1>`

**Test 1.3: Sau khi đăng nhập (Admin)**
```bash
curl -i http://localhost:8080/account/sign-in
```
✅ **Kỳ vọng:** 
- Status: `200 OK`
- Body: `<h1>Sign In Page</h1>`

---

### **2️⃣ Trang Yêu Cầu Đăng Nhập: `/account/change-password`**

**Chuẩn Bị - Đăng nhập User:**
```bash
curl -c cookies_user.txt -d "idOrEmail=user123&password=123456" http://localhost:8080/login
```

**Chuẩn Bị - Đăng nhập Admin:**
```bash
curl -c cookies_admin.txt -d "idOrEmail=admin&password=admin123" http://localhost:8080/login
```

**Test 2.1: Không đăng nhập**
```bash
curl -i http://localhost:8080/account/change-password
```
✅ **Kỳ vọng:** 
- Status: `302 Moved Temporarily`
- Location: `http://localhost:8080/login`

**Test 2.2: User đã đăng nhập**
```bash
curl -i -b cookies_user.txt http://localhost:8080/account/change-password
```
✅ **Kỳ vọng:** 
- Status: `200 OK`
- Body: `<h1>Change Password Page</h1>`

**Test 2.3: Admin đã đăng nhập**
```bash
curl -i -b cookies_admin.txt http://localhost:8080/account/change-password
```
✅ **Kỳ vọng:** 
- Status: `200 OK`
- Body: `<h1>Change Password Page</h1>`

---

### **3️⃣ Trang Yêu Cầu Đăng Nhập: `/account/edit-profile`**

**Test 3.1: Không đăng nhập**
```bash
curl -i http://localhost:8080/account/edit-profile
```
✅ **Kỳ vọng:** 
- Status: `302 Moved Temporarily`
- Location: `http://localhost:8080/login`

**Test 3.2: User đã đăng nhập**
```bash
curl -i -b cookies_user.txt http://localhost:8080/account/edit-profile
```
✅ **Kỳ vọng:** 
- Status: `200 OK`
- Body: `<h1>Edit Profile Page</h1>`

**Test 3.3: Admin đã đăng nhập**
```bash
curl -i -b cookies_admin.txt http://localhost:8080/account/edit-profile
```
✅ **Kỳ vọng:** 
- Status: `200 OK`
- Body: `<h1>Edit Profile Page</h1>`

---

### **4️⃣ Trang Yêu Cầu Đăng Nhập: `/video/like/123`**

**Test 4.1: Không đăng nhập**
```bash
curl -i http://localhost:8080/video/like/123
```
✅ **Kỳ vọng:** 
- Status: `302 Moved Temporarily`
- Location: `http://localhost:8080/login`

**Test 4.2: User đã đăng nhập**
```bash
curl -i -b cookies_user.txt http://localhost:8080/video/like/123
```
✅ **Kỳ vọng:** 
- Status: `200 OK`
- Body: Chứa `<h1>Video Like Page</h1>` và `<p>Video ID: 123</p>`

**Test 4.3: Admin đã đăng nhập**
```bash
curl -i -b cookies_admin.txt http://localhost:8080/video/like/123
```
✅ **Kỳ vọng:** 
- Status: `200 OK`
- Body: Chứa `<h1>Video Like Page</h1>` và `<p>Video ID: 123</p>`

---

### **5️⃣ Trang Yêu Cầu Đăng Nhập: `/video/share/456`**

**Test 5.1: Không đăng nhập**
```bash
curl -i http://localhost:8080/video/share/456
```
✅ **Kỳ vọng:** 
- Status: `302 Moved Temporarily`
- Location: `http://localhost:8080/login`

**Test 5.2: User đã đăng nhập**
```bash
curl -i -b cookies_user.txt http://localhost:8080/video/share/456
```
✅ **Kỳ vọng:** 
- Status: `200 OK`
- Body: Chứa `<h1>Video Share Page</h1>` và `<p>Video ID: 456</p>`

**Test 5.3: Admin đã đăng nhập**
```bash
curl -i -b cookies_admin.txt http://localhost:8080/video/share/456
```
✅ **Kỳ vọng:** 
- Status: `200 OK`
- Body: Chứa `<h1>Video Share Page</h1>` và `<p>Video ID: 456</p>`

---

### **6️⃣ Trang Yêu Cầu Admin: `/admin/video`**

**Test 6.1: Không đăng nhập**
```bash
curl -i http://localhost:8080/admin/video
```
✅ **Kỳ vọng:** 
- Status: `302 Moved Temporarily`
- Location: `http://localhost:8080/login`

**Test 6.2: User bình thường đã đăng nhập**
```bash
curl -i -b cookies_user.txt http://localhost:8080/admin/video
```
✅ **Kỳ vọng:** 
- Status: `302 Moved Temporarily` (AuthFilter chặn)
- Location: `http://localhost:8080/login`

**Test 6.3: Admin đã đăng nhập**
```bash
curl -i -b cookies_admin.txt http://localhost:8080/admin/video
```
✅ **Kỳ vọng:** 
- Status: `200 OK`
- Body: `<h1>Admin Video Management Page</h1>`

---

### **7️⃣ Trang Yêu Cầu Admin: `/admin/user`**

**Test 7.1: Không đăng nhập**
```bash
curl -i http://localhost:8080/admin/user
```
✅ **Kỳ vọng:** 
- Status: `302 Moved Temporarily`
- Location: `http://localhost:8080/login`

**Test 7.2: User bình thường đã đăng nhập**
```bash
curl -i -b cookies_user.txt http://localhost:8080/admin/user
```
✅ **Kỳ vọng:** 
- Status: `302 Moved Temporarily` (AuthFilter chặn)

**Test 7.3: Admin đã đăng nhập**
```bash
curl -i -b cookies_admin.txt 
```
✅ **Kỳ vọng:** 
- Status: `200 OK`
- Body: `<h1>Admin User Management Page</h1>`

---

### **8️⃣ Trang Yêu Cầu Admin: `/admin/like`**

**Test 8.1: Không đăng nhập**
```bash
curl -i http://localhost:8080/admin/like
```
✅ **Kỳ vọng:** 
- Status: `302 Moved Temporarily`
- Location: `http://localhost:8080/login`

**Test 8.2: User bình thường đã đăng nhập**
```bash
curl -i -b cookies_user.txt http://localhost:8080/admin/like
```
✅ **Kỳ vọng:** 
- Status: `302 Moved Temporarily`

**Test 8.3: Admin đã đăng nhập**
```bash
curl -i -b cookies_admin.txt http://localhost:8080/admin/like
```
✅ **Kỳ vọng:** 
- Status: `200 OK`
- Body: `<h1>Admin Like Management Page</h1>`

---

### **9️⃣ Trang Yêu Cầu Admin: `/admin/sha`**

**Test 9.1: Không đăng nhập**
```bash
curl -i http://localhost:8080/admin/sha
```
✅ **Kỳ vọng:** 
- Status: `302 Moved Temporarily`
- Location: `http://localhost:8080/login`

**Test 9.2: User bình thường đã đăng nhập**
```bash
curl -i -b cookies_user.txt http://localhost:8080/admin/sha
```
✅ **Kỳ vọng:** 
- Status: `302 Moved Temporarily`

**Test 9.3: Admin đã đăng nhập**
```bash
curl -i -b cookies_admin.txt http://localhost:8080/admin/sha
```
✅ **Kỳ vọng:** 
- Status: `200 OK`
- Body: `<h1>Admin Share Management Page</h1>`

---

## 📊 Bảng Tóm Tắt Kết Quả

| URL | Không Login | User | Admin |
|-----|-------------|------|-------|
| `/account/sign-in` | 200 ✅ | 200 ✅ | 200 ✅ |
| `/account/change-password` | 302 ✅ | 200 ✅ | 200 ✅ |
| `/account/edit-profile` | 302 ✅ | 200 ✅ | 200 ✅ |
| `/video/list` | 302 ✅ | 200 ✅ | 200 ✅ |
| `/video/detail/123` | 302 ✅ | 200 ✅ | 200 ✅ |
| `/video/like/123` | 302 ✅ | 200 ✅ | 200 ✅ |
| `/video/share/456` | 302 ✅ | 200 ✅ | 200 ✅ |
| `/admin/video` | 302 ✅ | 302 ✅ | 200 ✅ |
| `/admin/user` | 302 ✅ | 302 ✅ | 200 ✅ |
| `/admin/like` | 302 ✅ | 302 ✅ | 200 ✅ |
| `/admin/sha` | 302 ✅ | 302 ✅ | 200 ✅ |

---

## 📝 Ghi Chú Quan Trọng

1. **Thay `localhost:8080` bằng domain/port thực tế** nếu khác
2. **Cookies cần được lưu** (`-c cookies_user.txt`) để duy trì session
3. **Cookies cần được gửi** (`-b cookies_user.txt`) trong các request tiếp theo
4. **Status 302 có nghĩa redirect** → AuthFilter chặn và chuyển hướng đến `/login`
5. **Status 200 có nghĩa OK** → Servlet xử lý thành công
