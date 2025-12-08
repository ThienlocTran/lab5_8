package com.thienloc.jakarta.lab58.endpoint;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;


@ServerEndpoint("/text/chat")
public class TextChatServerEndpoint {
    private static final Map<String, Session> sessions = new HashMap<>();

    private void broadcast(String message)
    {
        sessions.forEach((id, session) -> {
            try{
                session.getBasicRemote().sendText(message);
            }catch (Exception e){
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
