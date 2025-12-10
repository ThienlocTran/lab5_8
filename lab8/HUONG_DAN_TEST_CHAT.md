# Hướng Dẫn Test Chat - Lab 8

## Theo đề bài Lab 8

### **Bài 1 + 2**: Chat đơn giản (KHÔNG cần username)
- **URL**: `http://localhost:8080/websocket-client.html`
- **Tính năng**: Chat trực tiếp, không cần nhập tên
- **Server**: TextChatServerEndpoint (`/text/chat`)

### **Bài 3 + 4**: Chat với JSON (CÓ username)
- **URL**: `http://localhost:8080/json-chat.html`  
- **Tính năng**: Bắt nhập username trước khi chat
- **Server**: JsonChatServerEndpoint (`/json/chat/{username}`)

## Cách Test

### 1. Khởi động server
```bash
cd lab8
./mvnw.cmd clean package
./mvnw.cmd tomcat7:run
```

### 2. Test Bài 1+2 (Text Chat - Đơn giản)
1. Mở: `http://localhost:8080/websocket-client.html`
2. **KHÔNG** cần nhập tên
3. Mở tab mới, cùng URL
4. Chat trực tiếp giữa 2 tab

**Kết quả mong đợi:**
```
Someone joined the chat!
Hello everyone
How are you?
Someone left the chat!
```

### 3. Test Bài 3+4 (JSON Chat - Có username)
1. Mở: `http://localhost:8080/json-chat.html`
2. **Nhập username** khi được hỏi (ví dụ: "Thiên Lộc")
3. Mở tab mới, nhập username khác (ví dụ: "Minh Anh")
4. Chat qua lại với tên hiển thị

**Kết quả mong đợi:**
```
Thiên Lộc joined the chat
Chatters: 1
Thiên Lộc: Xin chào
Minh Anh: Chào bạn
Chatters: 2
```

## So sánh 2 loại chat

| Tính năng | Bài 1+2 (Text) | Bài 3+4 (JSON) |
|-----------|----------------|-----------------|
| Username | ❌ Không cần | ✅ Bắt buộc nhập |
| Format | Text thuần | JSON với type |
| URL | `/text/chat` | `/json/chat/{username}` |
| Đếm user | ❌ Không | ✅ Có |
| Màu sắc | ❌ Không | ✅ Có (join/leave) |

## Lưu ý
- **UTF-8**: Đã fix, hỗ trợ tiếng Việt
- **Theo đúng đề**: Bài 1+2 đơn giản, Bài 3+4 có username
- Mỗi tab = 1 client riêng biệt