var username = null;
var websocket = null;

function init() {
    while (username === null) {
        username = prompt("Enter username");
    }

    websocket = new WebSocket(`ws://localhost:8080/json/chat/${username}`);

    websocket.onopen = function(resp) {
        console.log("onopen", resp);
    };

    websocket.onmessage = function(resp) {
        var msg = JSON.parse(resp.data);
        var output = document.getElementById('messages');
        
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
    var msg = {sender: username, text: input.value, type: 2};
    websocket.send(JSON.stringify(msg));
    input.value = '';
}