package com.example;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat")
public class ChatServer {
    private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session) throws IOException {
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        try (JsonReader reader = Json.createReader(session.getBasicRemote().getSendStream())) {
            JsonObject jsonMessage = reader.readObject();
            String username = jsonMessage.getString("username");
            String text = jsonMessage.getString("text");
            JsonObject json = Json.createObjectBuilder()
                    .add("type", "message")
                    .add("username", username)
                    .add("text", text)
                    .build();
            broadcast(json.toString());
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        sessions.remove(session);
    }

    private void broadcast(String message) throws IOException {
        for (Session session : sessions) {
            session.getBasicRemote().sendText(message);
        }
    }
}

