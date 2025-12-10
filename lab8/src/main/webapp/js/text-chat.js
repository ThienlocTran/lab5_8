var websocket = null; // biến giữ đối tượng WebSocket

// Được gọi tại sự kiện onload của trang web
function init() {
    // Mở kết nối đến chat server
    websocket = new WebSocket('ws://localhost:8080/text/chat');
    
    // Xử lý sự kiện chấp nhận kết nối từ server
    websocket.onopen = function(resp) {
        console.log("onopen", resp);
    };
    
    // Xử lý sự kiện nhận tin nhắn chat từ server
    websocket.onmessage = function(resp) {
        var message = resp.data;
        var html = document.getElementById('messages').innerHTML;
        document.getElementById('messages').innerHTML = `${html}<p>${message}</p>`;
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
    websocket.send(message);
    document.getElementById("message").value = '';
}
