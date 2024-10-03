package net.octoberserver.ordersystem.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.Err;
import net.octoberserver.ordersystem.order.OrderService;
import net.octoberserver.ordersystem.user.AppUser;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final Map<Long, List<WebSocketSession>> userIdToSession = new HashMap<>();
    private final Map<String, AppUser> sessionIdToUser = new HashMap<>();

    private final ObjectMapper objectMapper;

    public WebSocketMessage<?> createWSMessage(WSMessageType msgType, Object payload) throws JsonProcessingException {
        return switch (msgType) {
            case ERROR -> new TextMessage("ERROR\n" + objectMapper.writeValueAsString(payload));
            case SUCCESS -> new TextMessage("SUCCESS\n" + objectMapper.writeValueAsString(payload));
            case UPDATE -> new TextMessage("UPDATE\n" + objectMapper.writeValueAsString(payload));
        };
    }

    public Err sendOrderDataToClient(AppUser user, WebSocketSession session, OrderService orderService) {
        return sendOrderDataToClient(user.getID(), user.getClassNumber(), session, orderService);
    }

    public Err sendOrderDataToClients(Long userID, short classNumber, OrderService orderService) {
        if (!userIdToSession.containsKey(userID)) {
            return Err.ok("User not subscribed");
        }
        final var clients = userIdToSession.get(userID);
        return clients.stream()
            .map(client -> sendOrderDataToClient(userID, classNumber, client, orderService))
            .reduce(Err.ok("Successfully sent order data to clients"), (prev, current)-> current.error() ? current : prev);
    }

    public Err sendOrderDataToClient(Long userID, short classNumber, WebSocketSession session, OrderService orderService) {
        try {
            final var orderData = orderService.getOrderData(userID, classNumber);
            session.sendMessage(createWSMessage(WSMessageType.UPDATE, orderData));
            return Err.ok("Successfully sent order data to client");
        } catch (Exception e) {
            return Err.err(e.getMessage());
        }
    }

    public void addWhitelistedSession(WebSocketSession session, AppUser user) {
        sessionIdToUser.put(session.getId(), user);
        if (!userIdToSession.containsKey(user.getID())) {
            userIdToSession.put(user.getID(), new ArrayList<>(List.of(session)));
            return;
        }
        userIdToSession.get(user.getID()).add(session);
    }

    public void removeWhitelistedSession(String sessionID, long userID) {
        sessionIdToUser.remove(sessionID);
        userIdToSession
            .computeIfPresent(userID,
                (k, sessions) -> sessions
                    .stream()
                    .filter(session -> !session.getId().equals(sessionID))
                    .toList()
            );
        if (userIdToSession.containsKey(userID) && userIdToSession.get(userID).isEmpty()) {
            userIdToSession.remove(userID);
            return;
        }
    }

    public boolean isSessionWhitelisted(String sessionID) {
        return sessionIdToUser.containsKey(sessionID);
    }

    public AppUser getUserFromSessionID(String sessionID) {
        return sessionIdToUser.get(sessionID);
    }
}
