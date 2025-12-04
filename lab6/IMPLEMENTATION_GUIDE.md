# Hướng Dẫn Implement Các Servlet (Chỉnh Sửa Code)

## 📊 Tóm Tắt

Bạn đã có 3 servlet được khai báo nhưng chưa viết code:
- `WatchVideo.java` - Xử lý `/video/like/*`, `/video/share/*`, `/video/list`, `/video/detail/*`
- `AccountSetting.java` - Xử lý `/account/change-password`, `/account/edit-profile`
- `AdminData.java` - Xử lý `/admin/video`, `/admin/user`, `/admin/like`, `/admin/share`

AuthFilter đã sẵn bảo vệ các URL này, chỉ cần viết logic xử lý.

---

## 1️⃣ Implement `AccountSetting.java`

**File:** `D:\Java4\Lab5_8\lab6\src\main\java\com\thienloc\jakarta\lab58\controller\AccountSetting.java`

```java
package com.thienloc.jakarta.lab58.controller;

import com.thienloc.jakarta.lab58.DAO.UserDAO;
import com.thienloc.jakarta.lab58.DAOImpl.UserDAOImpl;
import com.thienloc.jakarta.lab58.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet({
        "/account/sign-in",
        "/account/change-password",
        "/account/edit-profile"
})
public class AccountSetting extends HttpServlet {
    private UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        String uri = path.substring(contextPath.length());

        if (uri.equals("/account/sign-in")) {
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        } else if (uri.equals("/account/change-password")) {
            request.getRequestDispatcher("/WEB-INF/change-password.jsp").forward(request, response);
        } else if (uri.equals("/account/edit-profile")) {
            request.getRequestDispatcher("/WEB-INF/edit-profile.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        String uri = path.substring(contextPath.length());
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (uri.equals("/account/change-password")) {
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");

            if (currentUser != null && currentUser.getPassword().equals(oldPassword)) {
                currentUser.setPassword(newPassword);
                userDAO.update(currentUser);
                session.setAttribute("user", currentUser);
                request.setAttribute("message", "Password changed successfully!");
            } else {
                request.setAttribute("message", "Old password is incorrect!");
            }
            request.getRequestDispatcher("/WEB-INF/change-password.jsp").forward(request, response);

        } else if (uri.equals("/account/edit-profile")) {
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");

            if (currentUser != null) {
                currentUser.setFullName(fullName);
                currentUser.setEmail(email);
                userDAO.update(currentUser);
                session.setAttribute("user", currentUser);
                request.setAttribute("message", "Profile updated successfully!");
            }
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("/WEB-INF/edit-profile.jsp").forward(request, response);
        }
    }
}
```

---

## 2️⃣ Implement `WatchVideo.java`

**File:** `D:\Java4\Lab5_8\lab6\src\main\java\com\thienloc\jakarta\lab58\controller\WatchVideo.java`

```java
package com.thienloc.jakarta.lab58.controller;

import com.thienloc.jakarta.lab58.DAO.FavoriteDAO;
import com.thienloc.jakarta.lab58.DAO.ShareDAO;
import com.thienloc.jakarta.lab58.DAO.VideoDAO;
import com.thienloc.jakarta.lab58.DAOImpl.FavoriteDAOImpl;
import com.thienloc.jakarta.lab58.DAOImpl.ShareDAOImpl;
import com.thienloc.jakarta.lab58.DAOImpl.VideoDAOImpl;
import com.thienloc.jakarta.lab58.entity.Favorite;
import com.thienloc.jakarta.lab58.entity.Share;
import com.thienloc.jakarta.lab58.entity.User;
import com.thienloc.jakarta.lab58.entity.Video;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet({
        "/video/list",
        "/video/detail/*",
        "/video/like/*",
        "/video/share/*"
})
public class WatchVideo extends HttpServlet {
    private VideoDAO videoDAO = new VideoDAOImpl();
    private FavoriteDAO favoriteDAO = new FavoriteDAOImpl();
    private ShareDAO shareDAO = new ShareDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (path.equals("/video/list")) {
            List<Video> videos = videoDAO.findAll();
            request.setAttribute("videos", videos);
            request.getRequestDispatcher("/WEB-INF/video-list.jsp").forward(request, response);

        } else if (path.equals("/video/detail/*")) {
            String videoId = pathInfo.substring(1);
            Video video = videoDAO.findById(videoId);
            request.setAttribute("video", video);
            request.getRequestDispatcher("/WEB-INF/video-detail.jsp").forward(request, response);

        } else if (path.equals("/video/like/*")) {
            String videoId = pathInfo.substring(1);
            Video video = videoDAO.findById(videoId);
            
            if (currentUser != null && video != null) {
                Favorite favorite = new Favorite();
                favorite.setUser(currentUser);
                favorite.setVideo(video);
                favoriteDAO.create(favorite);
                request.setAttribute("message", "Video liked successfully!");
            }
            request.setAttribute("video", video);
            request.getRequestDispatcher("/WEB-INF/video-detail.jsp").forward(request, response);

        } else if (path.equals("/video/share/*")) {
            String videoId = pathInfo.substring(1);
            Video video = videoDAO.findById(videoId);
            
            if (currentUser != null && video != null) {
                Share share = new Share();
                share.setUser(currentUser);
                share.setVideo(video);
                share.setSharedDate(new java.util.Date());
                shareDAO.create(share);
                request.setAttribute("message", "Video shared successfully!");
            }
            request.setAttribute("video", video);
            request.getRequestDispatcher("/WEB-INF/video-detail.jsp").forward(request, response);
        }
    }
}
```

---

## 3️⃣ Implement `AdminData.java`

**File:** `D:\Java4\Lab5_8\lab6\src\main\java\com\thienloc\jakarta\lab58\controller\AdminData.java`

```java
package com.thienloc.jakarta.lab58.controller;

import com.thienloc.jakarta.lab58.DAO.FavoriteDAO;
import com.thienloc.jakarta.lab58.DAO.ShareDAO;
import com.thienloc.jakarta.lab58.DAO.UserDAO;
import com.thienloc.jakarta.lab58.DAO.VideoDAO;
import com.thienloc.jakarta.lab58.DAOImpl.FavoriteDAOImpl;
import com.thienloc.jakarta.lab58.DAOImpl.ShareDAOImpl;
import com.thienloc.jakarta.lab58.DAOImpl.UserDAOImpl;
import com.thienloc.jakarta.lab58.DAOImpl.VideoDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet({
        "/admin/video",
        "/admin/user",
        "/admin/like",
        "/admin/share"
})
public class AdminData extends HttpServlet {
    private VideoDAO videoDAO = new VideoDAOImpl();
    private UserDAO userDAO = new UserDAOImpl();
    private FavoriteDAO favoriteDAO = new FavoriteDAOImpl();
    private ShareDAO shareDAO = new ShareDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if (path.equals("/admin/video")) {
            request.setAttribute("videos", videoDAO.findAll());
            request.getRequestDispatcher("/WEB-INF/admin-video.jsp").forward(request, response);

        } else if (path.equals("/admin/user")) {
            request.setAttribute("users", userDAO.findAll());
            request.getRequestDispatcher("/WEB-INF/admin-user.jsp").forward(request, response);

        } else if (path.equals("/admin/like")) {
            request.setAttribute("favorites", favoriteDAO.findAll());
            request.getRequestDispatcher("/WEB-INF/admin-like.jsp").forward(request, response);

        } else if (path.equals("/admin/share")) {
            request.setAttribute("shares", shareDAO.findAll());
            request.getRequestDispatcher("/WEB-INF/admin-share.jsp").forward(request, response);
        }
    }
}
```

---

## ✅ Verify AuthFilter (Đã Sẵn)

AuthFilter hiện tại đã bảo vệ đúng:

```java
@WebFilter({
        "/admin/*",
        "/account/change-password",
        "/account/edit-profile",
        "/video/like/*",
        "/video/share/*"
})
```

Logic kiểm tra:
- Nếu **không đăng nhập** (`user == null`) → Redirect `/login`
- Nếu **không phải admin** (`!user.isAdmin()`) truy cập `/admin/*` → Redirect `/login`
- Nếu **có quyền** → Cho phép (`chain.doFilter()`)

---

## 📝 Ghi Chú Quan Trọng

1. **`/account/sign-in`**: Không được bảo vệ bởi filter → Ai cũng có thể truy cập
2. **Các trang khác**: Được bảo vệ bởi `AuthFilter` → Phải đăng nhập
3. **`/admin/*`**: Chỉ admin mới vào được → Filter kiểm tra `user.isAdmin()`
4. **LoginServlet** (`/login`): Xử lý POST đăng nhập + lưu session `"user"`

---

## 🧪 Cách Test Sau Khi Implement

### **1. Build project**
```bash
cd D:\Java4\Lab5_8\lab6
mvn clean package
```

### **2. Deploy trên Tomcat** và chạy

### **3. Test từng URL**

#### **Test `/account/sign-in` (Không bảo vệ)**
```bash
curl http://localhost:8080/account/sign-in
# Kỳ vọng: 200 OK, hiển thị login form
```

#### **Test `/account/change-password` (Bảo vệ)**
```bash
# Chưa login
curl -i http://localhost:8080/account/change-password
# Kỳ vọng: 302 Redirect to /login

# Sau khi login
curl -b cookies.txt http://localhost:8080/account/change-password
# Kỳ vọng: 200 OK
```

#### **Test `/video/like/123` (Bảo vệ)**
```bash
# Chưa login
curl -i http://localhost:8080/video/like/123
# Kỳ vọng: 302 Redirect to /login

# Sau khi login
curl -b cookies.txt http://localhost:8080/video/like/123
# Kỳ vọng: 200 OK
```

#### **Test `/admin/video` (Chỉ Admin)**
```bash
# Chưa login
curl -i http://localhost:8080/admin/video
# Kỳ vọng: 302 Redirect to /login

# User bình thường
curl -i -b cookies_user.txt http://localhost:8080/admin/video
# Kỳ vọng: 302 Redirect to /login (AuthFilter chặn vì !user.isAdmin())

# Admin
curl -b cookies_admin.txt http://localhost:8080/admin/video
# Kỳ vọng: 200 OK
```

---

## 📋 Bảng Kết Quả Mong Muốn

| URL | Không Login | User | Admin |
|-----|-------------|------|-------|
| `/account/sign-in` | 200 ✅ | 200 ✅ | 200 ✅ |
| `/account/change-password` | 302 ✅ | 200 ✅ | 200 ✅ |
| `/account/edit-profile` | 302 ✅ | 200 ✅ | 200 ✅ |
| `/video/list` | 302 ✅ | 200 ✅ | 200 ✅ |
| `/video/detail/123` | 302 ✅ | 200 ✅ | 200 ✅ |
| `/video/like/123` | 302 ✅ | 200 ✅ | 200 ✅ |
| `/video/share/123` | 302 ✅ | 200 ✅ | 200 ✅ |
| `/admin/video` | 302 ✅ | 302 ✅ | 200 ✅ |
| `/admin/user` | 302 ✅ | 302 ✅ | 200 ✅ |
| `/admin/like` | 302 ✅ | 302 ✅ | 200 ✅ |
| `/admin/share` | 302 ✅ | 302 ✅ | 200 ✅ |

---

## ⚠️ Chú Ý

- Nếu JSP file không tồn tại (`/WEB-INF/change-password.jsp`, v.v.), tạo JSP đơn giản hoặc chỉ output text
- DAO methods phải tồn tại: `findAll()`, `findById()`, `create()`, `update()`
- Session attribute `"user"` phải được lưu bởi `LoginServlet` sau khi đăng nhập thành công
