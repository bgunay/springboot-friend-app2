package org.pinsoft.friendapp.web.websocket;

public enum WebSocketEventName {
    CONNECT("/chat/login"),
    DISCONNECT("/chat/logout"),
    CHAT_LOGS("/chat/logs"),
    MESSAGE("");

    private String destination;

    WebSocketEventName(String destination) {
        this.destination = destination;
    }

    public String getDestination() {
        return this.destination;
    }
}
