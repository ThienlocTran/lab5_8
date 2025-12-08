# Hướng Dẫn Test WebSocket Chat

## Bài 1 & 2: Test Text Chat

### Bước 1: Đảm bảo Tomcat chạy bình thường
- Không có lỗi WebSocket
- Application deployed thành công

### Bước 2: Mở trình duyệt
1. Mở tab 1: http://localhost:8080/websocket-client.html
2. Mở tab 2: http://localhost:8080/websocket-client.html (hoặc trình duyệt khác)

### Bước 3: Kiểm tra kết nối
- **Tab 1:** Sẽ hiển thị:
  ```
  Connected to chat server
  Someone joined the chat!
  Someone joined the chat!
  ```

### Bước 4: Test gửi tin nhắn
1. Ở **Tab 1**, gõ: "Xin chào từ Tab 1" → Nhấn Send
2. Kiểm tra **Tab 2**: Sẽ nhận thấy tin nhắn "Xin chào từ Tab 1"
3. Ở **Tab 2**, gõ: "Tab 2 trả lời" → Nhấn Send
4. Kiểm tra **Tab 1**: Sẽ nhận thấy tin nhắn "Tab 2 trả lời"

### Bước 5: Test ngắt kết nối
1. Đóng **Tab 2**
2. Kiểm tra **Tab 1**: Sẽ hiển thị:
   ```
   Someone left the chat!
   ```

## Các lỗi có thể gặp

### Lỗi 1: Không kết nối được
```
WebSocket is closed before the connection is established
```
**Nguyên nhân:** Tomcat chưa chạy hoặc endpoint sai
**Cách fix:** 
- Kiểm tra Tomcat đang chạy
- Kiểm tra URL: `ws://localhost:8080/websocket/text/chat`
- Kiểm tra context path trong application

### Lỗi 2: 404 Not Found
```
Failed to connect: Error in connection establishment
```
**Nguyên nhân:** Endpoint URL sai
**Cách fix:**
- Endpoint phải là: `/text/chat` (bắt đầu bằng `/`)
- Annotation: `@ServerEndpoint("/text/chat")`

### Lỗi 3: Tin nhắn không được gửi
**Nguyên nhân:** WebSocket chưa kết nối (readyState !== OPEN)
**Cách fix:**
- Kiểm tra console log xem đã connect chưa
- Dòng `websocket.readyState === WebSocket.OPEN` kiểm tra này

## Browser Developer Tools

### Mở console (F12)
- Xem WebSocket events
- Kiểm tra URL kết nối
- Xem errors

### Kiểm tra WebSocket tab
1. Mở DevTools (F12)
2. Chuyển đến tab "Network"
3. Filter: WS
4. Sẽ thấy WebSocket connection

## Điểm cần kiểm tra

✅ **Bài 1 (TextChatServerEndpoint):**
- [x] Khi client kết nối: gửi "Someone joined the chat!"
- [x] Khi client gửi tin nhắn: broadcast đến tất cả
- [x] Khi client ngắt: gửi "Someone left the chat!"
- [x] Không xảy ra lỗi khi disconnect

✅ **Bài 2 (websocket-client.html):**
- [x] Hiển thị tin nhắn nhận được
- [x] Gửi tin nhắn khi nhấn Send
- [x] Hiển thị thông báo lỗi
- [x] Hiển thị thông báo khi disconnect

## Test Script (Automation)

Nếu muốn test tự động, mở browser console và chạy:

```javascript
// Test gửi 5 tin nhắn
for (let i = 1; i <= 5; i++) {
    setTimeout(() => {
        document.getElementById('message').value = `Message ${i}`;
        send();
    }, i * 1000);
}
```

## Kết quả kỳ vọng

### Tab 1 (mở trước):
```
Connected to chat server
Someone joined the chat!
Someone joined the chat!
Xin chào từ Tab 1
Tab 2 trả lời
Someone left the chat!
```

### Tab 2 (mở sau):
```
Connected to chat server
Someone joined the chat!
Xin chào từ Tab 1
Tab 2 trả lời
```

---

## Tiếp theo: Test Bài 3 & 4 (JSON Chat)

Khi hoàn thành Bài 3 & 4, tạo file test tương tự:
- Mở http://localhost:8080/json-chat.html
- Nhập username
- Chat giữa các user

Sự khác biệt:
- Có username của người gửi
- Thông báo join/leave có username
- Hiển thị số lượng online users

---

**Chúc bạn test thành công! 💪**
