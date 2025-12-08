package com.thienloc.jakarta.lab58.endpoint;


import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.PathParam;
import com.thienloc.jakarta.lab58.entity.Message;
import com.thienloc.jakarta.lab58.util.MessageEncoder;
import com.thienloc.jakarta.lab58.util.MessageDecoder;
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
