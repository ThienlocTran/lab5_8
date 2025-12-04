# Hướng Dẫn Viết, Chạy và Test Yêu Cầu Bảo Vệ URL

## 1. Phân Tích Hiện Tại

Dự án của bạn hiện có:
- ✅ **LoginServlet** (`/login`): Xử lý đăng nhập
- ✅ **AuthFilter**: Filter bảo vệ các URL nhạy cảm
- ✅ **User Entity**: Có thuộc tính `admin` để phân biệt vai trò
- ⚠️ **AccountSetting**: Tuy được khai báo URL nhưng chưa có method xử lý

### URLs Cần Implement:

| Loại | URL | Yêu Cầu | Status |
|------|-----|---------|--------|
| Không bảo vệ | `/account/sign-in` | Hiển thị form login | ❌ Cần tạo |
| Bảo vệ User | `/account/change-password` | Đã khai báo filter | ⚠️ Cần method |
| Bảo vệ User | `/account/edit-profile` | Đã khai báo filter | ⚠️ Cần method |
| Bảo vệ User | `/video/like/*` | Đã khai báo filter | ❌ Không tồn tại |
| Bảo vệ User | `/video/share/*` | Đã khai báo filter | ❌ Không tồn tại |
| Bảo vệ Admin | `/admin/video` | Đã khai báo filter | ❌ Không tồn tại |
| Bảo vệ Admin | `/admin/user` | Đã khai báo filter | ❌ Không tồn tại |
| Bảo vệ Admin | `/admin/like` | Đã khai báo filter | ❌ Không tồn tại |
| Bảo vệ Admin | `/admin/sha` | Đã khai báo filter | ❌ Không tồn tại |

---

## 2. Các Bước Implement

### **Bước 1: Tạo AccountSetting Servlet hoàn chỉnh**

Chỉnh sửa file: `src/main/java/com/thienloc/jakarta/lab58/controller/AccountSetting.java`

```java
package com.thienloc.jakarta.lab58.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet({
        "/account/sign-in",
        "/account/change-password",
        "/account/edit-profile"
})
public class AccountSetting extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        String uri = path.substring(contextPath.length());
        
        if (uri.equals("/account/sign-in")) {
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        } else if (uri.equals("/account/change-password")) {
            response.getWriter().println("<h1>Change Password Page</h1><p>User: " + 
                request.getSession().getAttribute("user") + "</p>");
        } else if (uri.equals("/account/edit-profile")) {
            response.getWriter().println("<h1>Edit Profile Page</h1><p>User: " + 
                request.getSession().getAttribute("user") + "</p>");
        }
    }
}
```

### **Bước 2: Tạo Video Like/Share Servlets**

Tạo file: `src/main/java/com/thienloc/jakarta/lab58/controller/VideoLikeServlet.java`

```java
package com.thienloc.jakarta.lab58.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/video/like/*")
public class VideoLikeServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo(); // Get ID from URL
        response.getWriter().println("<h1>Video Like Page</h1>");
        response.getWriter().println("<p>Video ID: " + pathInfo + "</p>");
        response.getWriter().println("<p>User: " + request.getSession().getAttribute("user") + "</p>");
    }
}
```

Tạo file: `src/main/java/com/thienloc/jakarta/lab58/controller/VideoShareServlet.java`

```java
package com.thienloc.jakarta.lab58.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/video/share/*")
public class VideoShareServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo(); // Get ID from URL
        response.getWriter().println("<h1>Video Share Page</h1>");
        response.getWriter().println("<p>Video ID: " + pathInfo + "</p>");
        response.getWriter().println("<p>User: " + request.getSession().getAttribute("user") + "</p>");
    }
}
```

### **Bước 3: Tạo Admin Servlets**

Tạo file: `src/main/java/com/thienloc/jakarta/lab58/controller/AdminVideoServlet.java`

```java
package com.thienloc.jakarta.lab58.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/video")
public class AdminVideoServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("<h1>Admin Video Management</h1>");
        response.getWriter().println("<p>Logged in as Admin: " + request.getSession().getAttribute("user") + "</p>");
    }
}
```

Tạo file: `src/main/java/com/thienloc/jakarta/lab58/controller/AdminUserServlet.java`

```java
package com.thienloc.jakarta.lab58.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/user")
public class AdminUserServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("<h1>Admin User Management</h1>");
        response.getWriter().println("<p>Logged in as Admin: " + request.getSession().getAttribute("user") + "</p>");
    }
}
```

Tạo file: `src/main/java/com/thienloc/jakarta/lab58/controller/AdminLikeServlet.java`

```java
package com.thienloc.jakarta.lab58.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/like")
public class AdminLikeServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("<h1>Admin Like Management</h1>");
        response.getWriter().println("<p>Logged in as Admin: " + request.getSession().getAttribute("user") + "</p>");
    }
}
```

Tạo file: `src/main/java/com/thienloc/jakarta/lab58/controller/AdminShareServlet.java`

```java
package com.thienloc.jakarta.lab58.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/sha")
public class AdminShareServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("<h1>Admin Share Management</h1>");
        response.getWriter().println("<p>Logged in as Admin: " + request.getSession().getAttribute("user") + "</p>");
    }
}
```

---

## 3. Build và Deploy

```bash
# Vào thư mục lab6
cd D:\Java4\Lab5_8\lab6

# Build project
mvn clean package

# Kết quả: D:\Java4\Lab5_8\lab6\target\ROOT.war
```

---

## 4. Chạy Ứng Dụng

1. **Nếu dùng Tomcat**: Copy file `ROOT.war` vào `tomcat/webapps/`
2. **Nếu dùng IDE (IntelliJ/Eclipse)**: Deploy thẳng từ IDE
3. **Truy cập**: `http://localhost:8080`

---

## 5. Test Các URLs

### **5.1 Test Trang Không Bảo Vệ: `/account/sign-in`**

```bash
# Test 1: Không đăng nhập → Kỳ vọng: 200 OK, hiển thị form login
curl -i http://localhost:8080/account/sign-in

# Test 2: Sau khi đăng nhập user → Kỳ vọng: 200 OK
# (Thực hiện bước login trước)
curl -b cookies.txt http://localhost:8080/account/sign-in

# Test 3: Sau khi đăng nhập admin → Kỳ vọng: 200 OK
curl -b cookies_admin.txt http://localhost:8080/account/sign-in
```

**Kết quả mong muốn:**
| Trạng thái | Kết quả |
|-----------|---------|
| Không login | ✅ 200 OK |
| User login | ✅ 200 OK |
| Admin login | ✅ 200 OK |

---

### **5.2 Test Trang Yêu Cầu Đăng Nhập**

#### **Chuẩn bị: Đăng nhập và lưu session**

```bash
# Đăng nhập với user bình thường
curl -c cookies_user.txt -d "idOrEmail=user123&password=123456" http://localhost:8080/login

# Đăng nhập với admin
curl -c cookies_admin.txt -d "idOrEmail=admin&password=admin123" http://localhost:8080/login
```

#### **Test `/account/change-password`**

```bash
# Test 1: Không đăng nhập → Kỳ vọng: 302 (Redirect to /login)
curl -i http://localhost:8080/account/change-password

# Test 2: User đã đăng nhập → Kỳ vọng: 200 OK
curl -b cookies_user.txt http://localhost:8080/account/change-password

# Test 3: Admin đã đăng nhập → Kỳ vọng: 200 OK
curl -b cookies_admin.txt http://localhost:8080/account/change-password
```

**Kết quả mong muốn:**
| Trạng thái | Kết quả |
|-----------|---------|
| Không login | ✅ 302 Redirect |
| User login | ✅ 200 OK |
| Admin login | ✅ 200 OK |

#### **Test `/account/edit-profile`** (Tương tự)

```bash
# Không login
curl -i http://localhost:8080/account/edit-profile

# User login
curl -b cookies_user.txt http://localhost:8080/account/edit-profile

# Admin login
curl -b cookies_admin.txt http://localhost:8080/account/edit-profile
```

#### **Test `/video/like/123`** (ID = 123)

```bash
# Không login → Kỳ vọng: 302 Redirect
curl -i http://localhost:8080/video/like/123

# User login → Kỳ vọng: 200 OK
curl -b cookies_user.txt http://localhost:8080/video/like/123

# Admin login → Kỳ vọng: 200 OK
curl -b cookies_admin.txt http://localhost:8080/video/like/123
```

#### **Test `/video/share/456`** (ID = 456)

```bash
# Không login → Kỳ vọng: 302 Redirect
curl -i http://localhost:8080/video/share/456

# User login → Kỳ vọng: 200 OK
curl -b cookies_user.txt http://localhost:8080/video/share/456

# Admin login → Kỳ vọng: 200 OK
curl -b cookies_admin.txt http://localhost:8080/video/share/456
```

---

### **5.3 Test Trang Yêu Cầu Admin**

#### **Test `/admin/video`**

```bash
# Test 1: Không đăng nhập → Kỳ vọng: 302 Redirect
curl -i http://localhost:8080/admin/video

# Test 2: User bình thường → Kỳ vọng: 302 Redirect (AuthFilter chặn)
curl -i -b cookies_user.txt http://localhost:8080/admin/video

# Test 3: Admin → Kỳ vọng: 200 OK
curl -b cookies_admin.txt http://localhost:8080/admin/video
```

**Kết quả mong muốn:**
| Trạng thái | Kết quả |
|-----------|---------|
| Không login | ✅ 302 Redirect |
| User login | ✅ 302 Redirect (Forbidden) |
| Admin login | ✅ 200 OK |

#### **Test `/admin/user`, `/admin/like`, `/admin/sha`** (Tương tự)

```bash
# Không login
curl -i http://localhost:8080/admin/user

# User login
curl -i -b cookies_user.txt http://localhost:8080/admin/user

# Admin login
curl -b cookies_admin.txt http://localhost:8080/admin/user
```

---

## 6. Bảng Tóm Tắt Kết Quả

| URL | Không Login | User Bình Thường | Admin |
|-----|-------------|------------------|-------|
| **`/account/sign-in`** | 200 ✅ | 200 ✅ | 200 ✅ |
| **`/account/change-password`** | 302 ✅ | 200 ✅ | 200 ✅ |
| **`/account/edit-profile`** | 302 ✅ | 200 ✅ | 200 ✅ |
| **`/video/like/123`** | 302 ✅ | 200 ✅ | 200 ✅ |
| **`/video/share/456`** | 302 ✅ | 200 ✅ | 200 ✅ |
| **`/admin/video`** | 302 ✅ | 302 ✅ | 200 ✅ |
| **`/admin/user`** | 302 ✅ | 302 ✅ | 200 ✅ |
| **`/admin/like`** | 302 ✅ | 302 ✅ | 200 ✅ |
| **`/admin/sha`** | 302 ✅ | 302 ✅ | 200 ✅ |

---

## 7. Ghi Chú Quan Trọng

✅ **AuthFilter hiện tại:**
- Đã bảo vệ `/admin/*`, `/account/change-password`, `/account/edit-profile`, `/video/like/*`, `/video/share/*`
- Kiểm tra `user == null` → Redirect to login
- Kiểm tra `uri.contains("/admin/")` và `!user.isAdmin()` → Redirect to login

✅ **Hành vi mong muốn:**
- User không đăng nhập hoặc không có quyền → Redirect đến `/login`
- User có quyền → Truy cập thành công (200 OK)
- Admin có quyền truy cập mọi URL
