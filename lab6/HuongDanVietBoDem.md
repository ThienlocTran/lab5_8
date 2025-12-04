# Hướng Dẫn: Xây Dựng Bộ Đếm Số Lượt Khách Viếng Thăm Website (Lưu vào Database)

## Mục Đích
- Tạo bộ đếm visitor toàn website
- Mỗi lần khách truy cập chỉ đếm 1 lần (per session)
- Hiển thị số đếm trên tất cả các trang web
- **Lưu dữ liệu vào Database**

---

## Bước 0: Tạo Bảng Trong Database

**SQL Server:**
```sql
CREATE TABLE Visitor (
    Id INT PRIMARY KEY IDENTITY(1,1),
    VisitorCount INT DEFAULT 0
);

-- Thêm 1 dòng mặc định
INSERT INTO Visitor (VisitorCount) VALUES (0);
```

**MySQL:**
```sql
CREATE TABLE visitor (
    id INT PRIMARY KEY AUTO_INCREMENT,
    visitor_count INT DEFAULT 0
);

INSERT INTO visitor (visitor_count) VALUES (0);
```

---

## Bước 1: Tạo Entity Visitor

**Vị trí:** `src/main/java/com/thienloc/jakarta/lab58/entity/Visitor.java`

```java
package com.thienloc.jakarta.lab58.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Visitor")
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;
    
    @Column(name = "VisitorCount")
    private int visitorCount;
}
```

---

## Bước 2: Tạo DAO Interface

**Vị trí:** `src/main/java/com/thienloc/jakarta/lab58/DAO/VisitorDAO.java`

```java
package com.thienloc.jakarta.lab58.DAO;

import com.thienloc.jakarta.lab58.entity.Visitor;

public interface VisitorDAO {
    Visitor getVisitorCount();
    void updateVisitorCount(int count);
}
```

---

## Bước 3: Tạo DAO Implementation

**Vị trí:** `src/main/java/com/thienloc/jakarta/lab58/DAOImpl/VisitorDAOImpl.java`

```java
package com.thienloc.jakarta.lab58.DAOImpl;

import com.thienloc.jakarta.lab58.DAO.VisitorDAO;
import com.thienloc.jakarta.lab58.util.XJPA;
import com.thienloc.jakarta.lab58.entity.Visitor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class VisitorDAOImpl implements VisitorDAO {

    @Override
    public Visitor getVisitorCount() {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "select v from Visitor v where v.id = 1";
            TypedQuery<Visitor> query = em.createQuery(jpql, Visitor.class);
            try {
                return query.getSingleResult();
            } catch (Exception e) {
                // Nếu không có bản ghi, tạo mới
                Visitor visitor = new Visitor();
                visitor.setId(1);
                visitor.setVisitorCount(0);
                return visitor;
            }
        } finally {
            em.close();
        }
    }

    @Override
    public void updateVisitorCount(int count) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            
            // Tìm bản ghi với id = 1
            Visitor visitor = em.find(Visitor.class, 1);
            
            if (visitor != null) {
                visitor.setVisitorCount(count);
                em.merge(visitor);
            } else {
                // Nếu không có, tạo mới
                visitor = new Visitor();
                visitor.setId(1);
                visitor.setVisitorCount(count);
                em.persist(visitor);
            }
            
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
```

---

## Bước 4: Tạo File VisitorCounterListener

**Vị trí:** `src/main/java/com/thienloc/jakarta/lab58/listener/VisitorCounterListener.java`

```java
package com.thienloc.jakarta.lab58.listener;

import com.thienloc.jakarta.lab58.DAO.VisitorDAO;
import com.thienloc.jakarta.lab58.DAOImpl.VisitorDAOImpl;
import com.thienloc.jakarta.lab58.entity.Visitor;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.HttpSessionEvent;
import jakarta.servlet.HttpSessionListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class VisitorCounterListener implements ServletContextListener, HttpSessionListener {
    
    private VisitorDAO visitorDAO = new VisitorDAOImpl();
    
    // TODO 1: Implement phương thức contextInitialized()
    // - Đọc số lượt từ Database
    // - Lưu vào ServletContext với key "visitors"
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Bạn viết code ở đây
    }

    // TODO 2: Implement phương thức sessionCreated()
    // - Mỗi lần session tạo mới = khách mới vào
    // - Lấy số đếm từ ServletContext
    // - Tăng lên 1
    // - Lưu lại vào ServletContext
    // - Cập nhật vào Database
    
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // Bạn viết code ở đây
    }

    // TODO 3: Implement phương thức contextDestroyed()
    // - Khi ứng dụng tắt (tùy chọn, vì đã cập nhật DB realtime)
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Có thể để trống hoặc thêm log
    }
}
```

---

## Bước 5: Hướng Dẫn Chi Tiết Cho Mỗi Phương Thức

### contextInitialized(ServletContextEvent sce)

**Công việc:**
1. Lấy `ServletContext`
2. Gọi VisitorDAO để lấy số đếm từ Database
3. Lưu vào `ServletContext` với key `"visitors"`

**Gợi ý code:**
```java
@Override
public void contextInitialized(ServletContextEvent sce) {
    ServletContext context = sce.getServletContext();
    
    // Gọi DAO để lấy số lượt từ Database
    Visitor visitor = visitorDAO.getVisitorCount();
    
    int count = 0;
    if (visitor != null) {
        count = visitor.getVisitorCount();
    }
    
    // Lưu vào application scope
    context.setAttribute("visitors", count);
    
    System.out.println("✓ VisitorCounter initialized. Current visitors: " + count);
}
```

---

### sessionCreated(HttpSessionEvent se)

**Công việc:**
1. Lấy `ServletContext` từ `HttpSession`
2. Lấy số đếm từ `ServletContext`
3. Tăng lên 1
4. Lưu lại vào `ServletContext`
5. **Cập nhật vào Database ngay lập tức**

**Gợi ý code:**
```java
@Override
public void sessionCreated(HttpSessionEvent se) {
    ServletContext context = se.getSession().getServletContext();
    
    // Lấy số đếm hiện tại từ application scope
    Integer visitors = (Integer) context.getAttribute("visitors");
    
    // Kiểm tra nếu null thì khởi tạo = 0
    if (visitors == null) {
        visitors = 0;
    }
    
    // Tăng lên 1
    visitors++;
    
    // Lưu lại vào application scope
    context.setAttribute("visitors", visitors);
    
    // **QUAN TRỌNG: Cập nhật vào Database**
    visitorDAO.updateVisitorCount(visitors);
    
    System.out.println("✓ New session created. Total visitors: " + visitors);
}
```

---

### contextDestroyed(ServletContextEvent sce)

**Công việc:**
- Tùy chọn (vì đã cập nhật DB realtime ở `sessionCreated()`)
- Có thể để trống hoặc thêm log

**Gợi ý code:**
```java
@Override
public void contextDestroyed(ServletContextEvent sce) {
    System.out.println("✓ Application stopped. Visitor count saved to database.");
}
```

---

## Bước 3: Hiển Thị Số Đếm Trên Trang JSP

**Trong bất kỳ file JSP nào, thêm dòng này:**

```jsp
<p>Số lượt khách viếng thăm: ${applicationScope.visitors}</p>
```

**Hoặc đẹp hơn, thêm vào navbar:**

```jsp
<div class="navbar-text text-light">
    Visitors: <strong>${applicationScope.visitors}</strong>
</div>
```

---

## Bước 4: Kiểm Tra Kết Quả

1. **Chạy ứng dụng lần đầu**
   - Mở trình duyệt → truy cập bất kỳ trang nào
   - Số đếm sẽ là 1

2. **Tab mới / Trình duyệt mới**
   - Số đếm sẽ tăng lên 2, 3, 4...
   - Vì mỗi tab/trình duyệt = 1 session

3. **F5 hoặc reload trang**
   - Số đếm KHÔNG tăng
   - Vì đó là cùng 1 session

4. **Tắt ứng dụng**
   - Số đếm được lưu vào file `visitors.txt`
   - Bạn có thể tìm file này ở thư mục root của ứng dụng

---

## Lưu Ý Quan Trọng

1. **@WebListener** phải được import từ `jakarta.servlet.annotation`
2. **Không import từ `javax`** (nếu dùng Jakarta EE)
3. File `visitors.txt` sẽ được tạo tự động ở thư mục ứng dụng
4. Nếu dùng Maven, có thể file này ở `target/` hoặc `target/[app-name]/`

---

## Cấu Trúc Project Sau Khi Hoàn Thành

```
src/main/java/com/thienloc/jakarta/lab58/
├── listener/
│   └── VisitorCounterListener.java  ← Tạo thư mục này
├── entity/
├── DAO/
├── DAOImpl/
└── controller/
```

---

## Câu Hỏi Thường Gặp

**Q: Tại sao lại dùng `ApplicationScope` thay vì `session`?**  
A: Vì `ApplicationScope` là toàn bộ ứng dụng, dùng cho dữ liệu toàn cục. `Session` là từng người dùng.

**Q: Nếu ứng dụng crash, số đếm có bị mất không?**  
A: Không, vì nó được lưu vào file trong `contextDestroyed()`.

**Q: Làm sao kiểm tra file `visitors.txt` được tạo?**  
A: Sau khi chạy ứng dụng 1 lần rồi tắt, tìm trong thư mục gốc của project hoặc `target/`.

---

## Hoàn Thành!

Sau khi làm xong các bước trên, bạn sẽ có bộ đếm visitor đầy đủ!
