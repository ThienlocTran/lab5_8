var websocket = null;

function init() {
    // Kết nối đến WebSocket server
    // Context path = ROOT (empty), nên: ws://localhost:8080/text/chat
    websocket = new WebSocket('ws://localhost:8080/text/chat');
    
    websocket.onopen = function(resp) {
        console.log("Connected to server", resp);
        addMessage("Connected to chat server", "system");
    };
    
    websocket.onmessage = function(resp) {
        var message = resp.data;
        console.log("Received:", message);
        
        // Hiển thị tin nhắn
        if (message.includes("joined")) {
            addMessage(message, "system");
        } else if (message.includes("left")) {
            addMessage(message, "system");
        } else {
            addMessage(message, "chat");
        }
    };
    
    websocket.onerror = function(event) {
        console.error("WebSocket Error:", event);
        console.error("ReadyState:", websocket.readyState);
        console.error("Full event:", JSON.stringify(event, null, 2));
        alert('WebSocket Error: Check console for details');
        addMessage("Error: WebSocket connection failed", "error");
    };
    
    websocket.onclose = function(resp) {
        alert(resp.reason || 'Connection closed');
        console.log("Closed:", resp);
        addMessage("Disconnected from server", "system");
    };
}

function send() {
    var input = document.getElementById("message");
    var message = input.value.trim();
    
    if (message !== '' && websocket && websocket.readyState === WebSocket.OPEN) {
        websocket.send(message);
        addMessage("You: " + message, "own");
        input.value = '';
    } else if (!websocket || websocket.readyState !== WebSocket.OPEN) {
        alert('Not connected to server!');
    }
}

function addMessage(text, type) {
    var messagesDiv = document.getElementById('messages');
    var messageEl = document.createElement('div');
    messageEl.className = 'message ' + type;
    messageEl.textContent = text;
    messagesDiv.appendChild(messageEl);
    
    // Scroll to bottom
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

// Cho phép gửi tin nhắn bằng Enter
document.addEventListener('DOMContentLoaded', function() {
    var input = document.getElementById('message');
    if (input) {
        input.addEventListener('keypress', function(event) {
            if (event.key === 'Enter') {
                send();
            }
        });
    }
});
