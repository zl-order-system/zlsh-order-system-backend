package net.octoberserver.ordersystem.websocket;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.user.AppUser;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ApplicationWebSocketHandler implements WebSocketHandler {

    private final WebSocketRequestService webSocketRequestService;
    private final WebSocketService webSocketService;


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws IOException {
        if (!(message.getPayload() instanceof String msg)) { return; }

        if (msg.startsWith("AUTH\n")) {
            final var authResult = webSocketRequestService.doAuth(msg, session);
            session.sendMessage(webSocketService.createWSMessage(authResult.error() ? WSMessageType.ERROR : WSMessageType.SUCCESS, authResult));
            return;
        }

        final var checkAuthResult = webSocketRequestService.checkAuth(session.getId());
        if (checkAuthResult.v1().error()) {
            session.sendMessage(webSocketService.createWSMessage(WSMessageType.ERROR, checkAuthResult.v1()));
            return;
        }

        final var result = webSocketRequestService.parseAndHandleRequest(msg, checkAuthResult.v2(), session);
        if (result.error()) {
            session.sendMessage(webSocketService.createWSMessage(WSMessageType.ERROR, result));
            return;
        }
    }


    @Override
    public boolean supportsPartialMessages() { return false; }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {}

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {}

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        if (webSocketService.isSessionWhitelisted(session.getId())) {
            webSocketService.removeWhitelistedSession(
                session.getId(),
                webSocketService.getUserFromSessionID(session.getId()).getID()
            );
        }
    }
}