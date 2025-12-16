package com.unisuam.sistemasdistribuidos2.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatHandler extends TextWebSocketHandler {

    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    private final Map<String, String> userNames = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("Nova conexão: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        String nome = userNames.remove(session.getId());
        System.out.println("Desconexão: " + (nome != null ? nome : session.getId()));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        if (!userNames.containsKey(session.getId())) {

            userNames.put(session.getId(), payload);
            System.out.println("Usuário registrado (Sessão " + session.getId() + ")");

            session.sendMessage(new TextMessage("Bem-vindo ao chat distribuído!"));
            return;
        }

        for (WebSocketSession s : sessions) {

            if (s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage(payload));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}