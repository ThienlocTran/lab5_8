# LAB 8: LẬP TRÌNH JAVA #4 - WEBSOCKET CHAT APPLICATION

## MỤC TIÊU
- Ứng dụng ServerEndpoint API để tạo Chat Server
- Ứng dụng WebSocket API để tạo Chat Client
- Xây dựng ứng dụng chat với cấu trúc tin nhắn JSON

---

## PHẦN I: CHAT ĐƠNGIẢN VỚI TIN NHẮN TEXT

### BÀI 1: TextChatServerEndpoint (2 điểm)

**Mục tiêu:** Tạo WebSocket server endpoint với URL `/text/chat` xử lý kết nối và tin nhắn chat.

**Các yêu cầu:**
- Gửi "Someone joined the chat!" khi client kết nối
- Gửi "Someone left the chat!" khi client ngắt kết nối
- Chuyển tiếp tin nhắn đến tất cả client

**Hướng dẫn chi tiết:**

1. **Tạo file:** `src/main/java/com/thienloc/jakarta/lab58/endpoint/TextChatServerEndpoint.java`

2. **Cấu trúc lớp:**
```java
@ServerEndpoint("/text/chat")
public class TextChatServerEndpoint {
    // Giữ danh sách session của các client đang kết nối
    private static Map<String, Session> sessions = new HashMap<>();
    
    // Gửi @message đến tất cả client đang kết nối
    private void broadcast(String message) {
        sessions.forEach((id, session) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    @OnOpen
    public void onOpen(Session session) {
        sessions.put(session.getId(), session);
        this.broadcast("Someone joined the chat!");
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            this.broadcast(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @OnClose
    public void onClose(Session session) {
        sessions.remove(session.getId());
        this.broadcast("Someone left the chat!");
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

3. **Import cần thiết:**
```java
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
```

4. **Chú ý:**
   - `@ServerEndpoint` định nghĩa endpoint URL
   - `@OnOpen` xử lý khi client kết nối
   - `@OnMessage` xử lý khi nhận tin nhắn
   - `@OnClose` xử lý khi client ngắt kết nối
   - `@OnError` xử lý lỗi
   - `session.getBasicRemote().sendText()` gửi tin nhắn text

---

### BÀI 2: WebSocket Chat Client (2 điểm)

**Mục tiêu:** Tạo giao diện HTML và JavaScript để kết nối đến TextChatServerEndpoint.

**Các yêu cầu:**
- Hiển thị tin nhắn nhận được
- Gửi tin nhắn khi nhấn nút Send
- Thông báo lỗi và đóng kết nối

**Hướng dẫn chi tiết:**

1. **Tạo file HTML:** `src/main/webapp/websocket-client.html`

```html
<html>
<head>
    <meta charset="UTF-8">
    <title>Simple Chat - Websockets</title>
    <script src="js/text-chat.js"></script>
</head>
<body onload="init()">
    <div id="messages" style="height: 200px; overflow: auto; border: 1px solid #ccc; padding: 10px;"></div>
    <hr>
    <input id="message" type="text" placeholder="Enter message..." style="width: 80%; padding: 5px;">
    <button onclick="send()" style="padding: 5px 15px;">Send</button>
</body>
</html>
```

2. **Tạo file JavaScript:** `src/main/webapp/js/text-chat.js`

```javascript
var websocket = null; // Đối tượng WebSocket

// Được gọi tại sự kiện onload của trang web
function init() {
    // Mở kết nối đến chat server
    // URL: ws://[host]:[port]/[context-path]/[endpoint-url]
    websocket = new WebSocket('ws://localhost:8080/websocket/text/chat');
    
    // Xử lý sự kiện chấp nhận kết nối từ server
    websocket.onopen = function(resp) {
        console.log("onopen", resp);
    };
    
    // Xử lý sự kiện nhận tin nhắn chat từ server
    websocket.onmessage = function(resp) {
        var message = resp.data;
        var html = document.getElementById('messages').innerHTML;
        document.getElementById('messages').innerHTML = 
            `${html}<p>${message}</p>`;
        console.log("onmessage", resp.data);
    };
    
    // Xử lý sự kiện lỗi từ server
    websocket.onerror = function(resp) {
        alert('An error occured, closing application');
        console.log("onerror", resp);
    };
    
    // Xử lý sự kiện đóng kết nối từ server
    websocket.onclose = function(resp) {
        alert(resp.reason || 'Goodbye');
        console.log("onclose", resp);
    };
}

// Gửi tin nhắn chat đến server, được gọi khi nhấp vào nút Send
function send() {
    var message = document.getElementById("message").value;
    if (message.trim() !== '') {
        websocket.send(message);
        document.getElementById("message").value = '';
    }
}
```

3. **Cách kiểm tra:**
   - Mở http://localhost:8080/websocket-client.html trong trình duyệt
   - Nhập tin nhắn và nhấn Send
   - Mở nhiều tab để kiểm tra broadcast

---

## PHẦN II: CHAT VỚI CẤU TRÚC TIN NHẮN JSON

### Cấu trúc tin nhắn Message
```json
{
    "text": "nội dung tin nhắn",
    "type": 0,     // 0: vào, 1: ra, 2: lời thoại
    "sender": "username",  // null khi type là 0 hoặc 1
    "count": 5     // số client hiện tại, null khi type là 2
}
```

---

### BÀI 3: JsonChatServerEndpoint (2 điểm)

**Mục tiêu:** Tạo WebSocket server với JSON messages và encoder/decoder.

**Hướng dẫn chi tiết:**

1. **Bước 1 - Tạo Entity Message:** `src/main/java/com/thienloc/jakarta/lab58/entity/Message.java`

```java
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String text;
    private int type;
    private String sender;
    private int count;
}
```

2. **Bước 2 - Tạo MessageEncoder:** `src/main/java/com/thienloc/jakarta/lab58/encoder/MessageEncoder.java`

```java
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thienloc.jakarta.lab58.entity.Message;
import com.fasterxml.jackson.core.JsonProcessingException;

public class MessageEncoder implements Encoder.Text<Message> {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void destroy() {}

    @Override
    public void init(EndpointConfig config) {}

    @Override
    public String encode(Message message) throws EncodeException {
        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new EncodeException(message, "Unable to encode");
        }
    }
}
```

3. **Bước 3 - Tạo MessageDecoder:** `src/main/java/com/thienloc/jakarta/lab58/decoder/MessageDecoder.java`

```java
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import jakarta.websocket.EndpointConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thienloc.jakarta.lab58.entity.Message;
import java.io.IOException;

public class MessageDecoder implements Decoder.Text<Message> {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void destroy() {}

    @Override
    public void init(EndpointConfig config) {}

    @Override
    public Message decode(String json) throws DecodeException {
        try {
            return mapper.readValue(json, Message.class);
        } catch (IOException e) {
            throw new DecodeException(json, "Unable to decode");
        }
    }

    @Override
    public boolean willDecode(String json) {
        return json.contains("type") && json.contains("text");
    }
}
```

4. **Bước 4 - Tạo JsonChatServerEndpoint:** `src/main/java/com/thienloc/jakarta/lab58/endpoint/JsonChatServerEndpoint.java`

```java
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.PathParam;
import com.thienloc.jakarta.lab58.entity.Message;
import com.thienloc.jakarta.lab58.encoder.MessageEncoder;
import com.thienloc.jakarta.lab58.decoder.MessageDecoder;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(
    value = "/json/chat/{username}",
    encoders = {MessageEncoder.class},
    decoders = {MessageDecoder.class}
)
public class JsonChatServerEndpoint {
    private static Map<String, Session> sessions = new HashMap<>();

    private void broadcast(Message message) {
        sessions.forEach((username, session) -> {
            try {
                session.getBasicRemote().sendObject(message);
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        });
    }

    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {
        if (sessions.containsKey(username)) {
            throw new RuntimeException("Username already exists");
        } else {
            session.getUserProperties().put("username", username);
            sessions.put(username, session);
            Message message = new Message("joined the chat", 0, username, sessions.size());
            this.broadcast(message);
        }
    }

    @OnMessage
    public void onMessage(Message message, Session session) {
        this.broadcast(message);
    }

    @OnClose
    public void onClose(Session session) {
        String username = (String) session.getUserProperties().get("username");
        sessions.remove(username);
        Message message = new Message("left the chat", 1, username, sessions.size());
        this.broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            session.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to close session");
        }
    }
}
```

5. **Chú ý quan trọng:**
   - Jackson ObjectMapper đã có trong pom.xml (version 2.19.2)
   - `@PathParam("username")` lấy username từ URL
   - `session.getUserProperties()` lưu dữ liệu per-session
   - `encoder/decoders` tự động convert JSON <-> Object

---

### BÀI 4: JSON Chat Client (2 điểm)

**Mục tiêu:** Tạo giao diện chat với JSON messages.

**Hướng dẫn chi tiết:**

1. **Tạo file HTML:** `src/main/webapp/json-chat.html`

```html
<html>
<head>
    <meta charset="UTF-8">
    <title>JSON Chat - Websockets</title>
    <script src="js/json-chat.js"></script>
    <style>
        body { font-family: Arial, sans-serif; }
        #messages { 
            height: 300px; 
            overflow: auto; 
            border: 1px solid #ccc; 
            padding: 10px;
            background-color: #f9f9f9;
        }
        #client-count { 
            color: blue; 
            font-size: 14px;
        }
        input { 
            width: 80%; 
            padding: 8px;
        }
        button { 
            padding: 8px 15px;
        }
    </style>
</head>
<body onload="init()">
    <h2>JSON Chat Application</h2>
    <div id="messages"></div>
    <hr>
    <b id="client-count">Chatters: 0</b>
    <hr>
    <input id="message" type="text" placeholder="Enter message...">
    <button onclick="send()">Send</button>
</body>
</html>
```

2. **Tạo file JavaScript:** `src/main/webapp/js/json-chat.js`

```javascript
var username = null;
var websocket = null;

function init() {
    // Yêu cầu người dùng nhập username
    while (username === null) {
        username = prompt("Enter username");
    }
    
    // Kết nối đến server với username trong URL
    websocket = new WebSocket(`ws://localhost:8080/websocket/json/chat/${username}`);
    
    websocket.onopen = function(resp) {
        console.log("onopen", resp);
    };
    
    websocket.onmessage = function(resp) {
        // Parse JSON message từ server
        var msg = JSON.parse(resp.data);
        var output = document.getElementById('messages');
        
        // Hiển thị tin nhắn dựa trên type
        if (msg.type === 0) {
            // Type 0: người vào
            output.innerHTML = `${output.innerHTML}<p style="color: green;"><b>${msg.sender}</b> ${msg.text}</p>`;
        } else if (msg.type === 1) {
            // Type 1: người ra
            output.innerHTML = `${output.innerHTML}<p style="color: red;"><b>${msg.sender}</b> ${msg.text}</p>`;
        } else if (msg.type === 2) {
            // Type 2: lời thoại
            output.innerHTML = `${output.innerHTML}<p><b>${msg.sender}</b>: ${msg.text}</p>`;
        }
        
        // Cập nhật số lượng client (chỉ khi type là 0 hoặc 1)
        if (msg.type !== 2) {
            document.getElementById('client-count').innerHTML = `Chatters: ${msg.count}`;
        }
        
        // Scroll đến cuối
        output.scrollTop = output.scrollHeight;
    };
    
    websocket.onerror = function(resp) {
        alert('An error occured, closing application');
        console.log("onerror", resp);
    };
    
    websocket.onclose = function(resp) {
        alert(resp.reason || 'Goodbye');
        console.log("onclose", resp);
    };
}

function send() {
    var input = document.getElementById("message");
    var messageText = input.value.trim();
    
    if (messageText !== '') {
        // Tạo đối tượng message
        var msg = {
            sender: username,
            text: messageText,
            type: 2  // type 2 là lời thoại
        };
        
        // Gửi dưới dạng JSON string
        websocket.send(JSON.stringify(msg));
        input.value = '';
    }
}
```

3. **Cách kiểm tra:**
   - Mở http://localhost:8080/json-chat.html trong trình duyệt
   - Nhập username
   - Mở nhiều tab để chat giữa các user
   - Kiểm tra thông báo join/leave

---

## HƯỚNG DẪN CHẠY ỨNG DỤNG

### 1. Build và Deploy
```bash
# Build với Maven
mvn clean package

# Deploy lên Tomcat (copy ROOT.war từ target/ vào webapps/)
```

### 2. Cấu hình context path
- Kiểm tra file `web.xml` để xác nhận context-path
- URL endpoint: `ws://localhost:8080/[context]/[endpoint-url]`
   
### 3. Kiểm tra Tomcat version
- WebSocket yêu cầu Tomcat 7.0+
- Bài lab này dùng Jakarta EE (Tomcat 10+)

---

## KIỂM TRA CÁC LỖI THƯỜNG GẶP

### 1. Lỗi 404 khi kết nối WebSocket
- Kiểm tra URL endpoint có đúng không
- Kiểm tra server đã khởi động
- Kiểm tra context path trong application

### 2. Lỗi Import
- Chắc chắn dùng `jakarta.websocket.*` (không phải `javax.websocket`)
- Import đúng các annotation

### 3. JSON Parse Error
- Kiểm tra Message class có getter/setter không
- Kiểm tra willDecode() method trả về true

### 4. Username Already Exists
- Không thể login với username đã tồn tại
- Thử username khác hoặc logout user cũ

---

## TÓRUSM CÔNG NGHỆ DÙNG

| Công nghệ | Phiên bản | Mục đích |
|-----------|----------|---------|
| Jakarta WebSocket API | 2.1 | Server & Client endpoint |
| Jackson Databind | 2.19.2 | JSON serialization |
| Lombok | 1.18.42 | Reduce boilerplate code |
| Jakarta Servlet | 6.0 | Web server |

---

## TIPS & TRICKS

1. **Bảo vệ WebSocket:**
   - Thêm AuthFilter để kiểm tra authentication
   - Validate message input trước broadcast

2. **Cải thiện UX:**
   - Thêm timestamp cho mỗi message
   - Thêm typing indicator
   - Thêm emoji support

3. **Scalability:**
   - Dùng Redis pub/sub cho multiple servers
   - Implement message persistence
   - Thêm message history

---

## CÂU HỎI THƯỜNG GẶP

**Q: Có cần database không?**
A: Bài cơ bản không cần. Có thể thêm Logs table nếu muốn lưu chat history.

**Q: Làm sao để giới hạn số message hiển thị?**
A: Giới hạn DOM nodes bằng cách xóa messages cũ nhất khi vượt quá N messages.

**Q: WebSocket có an toàn không?**
A: Có, nhưng cần implement authentication, validation, rate limiting.

**Q: Có thể dùng HTTPS (WSS) không?**
A: Có, cần SSL certificate trên server.

---

## NEXT STEPS (BÀI 5 - NÂNG CAO)

Giảng viên có thể yêu cầu thêm:
- Message persistence (Database)
- Private messaging (DM)
- Chat rooms
- File sharing
- User authentication/authorization
- Message encryption

---

**Chúc các bạn thành công! 💪**
