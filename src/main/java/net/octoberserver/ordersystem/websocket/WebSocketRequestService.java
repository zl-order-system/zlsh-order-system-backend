package net.octoberserver.ordersystem.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.Err;
import net.octoberserver.ordersystem.Tuple;
import net.octoberserver.ordersystem.auth.service.JWTService;
import net.octoberserver.ordersystem.order.OrderService;
import net.octoberserver.ordersystem.order.dao.CreateOrderDataRequestDAO;
import net.octoberserver.ordersystem.order.dao.CreateOrderDataResponseDAO;
import net.octoberserver.ordersystem.order.dao.DeleteOrderDataRequestDAO;
import net.octoberserver.ordersystem.order.dao.UpdateOrderDataRequestDAO;
import net.octoberserver.ordersystem.user.AppUser;
import net.octoberserver.ordersystem.user.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WebSocketRequestService {

    private final JWTService jwtService;
    private final ObjectMapper objectMapper;
    private final AppUserRepository appUserRepository;

    private final OrderService orderService;
    private final WebSocketService webSocketService;


    public Err doAuth(String msg, WebSocketSession session) {
        final var token = msg.substring(5);
        Optional<String> userID = jwtService.extractUserID(token);
        if (userID.isEmpty()) {
            return Err.err("Authentication Failed: Could not identify user from token");
        }

        final var userOptional = appUserRepository.findById(Long.parseLong(userID.get()));
        if (userOptional.isEmpty()) {
            return Err.err("User Not Found");
        }

        webSocketService.addWhitelistedSession(session, userOptional.get());
        return Err.ok("Successfully Authenticated");
    }

    public Tuple<Err, AppUser> checkAuth(String sessionID) {
        if (webSocketService.isSessionWhitelisted(sessionID)) {
            return new Tuple<>(Err.ok("AuthCheck Success"), webSocketService.getUserFromSessionID(sessionID));
        }
        return new Tuple<>(Err.err("Please authenticate first"), null);
    }

    public Err parseAndHandleRequest(String msg, AppUser user, WebSocketSession session) {
        final var substrings = msg.split("\n");
        if (substrings.length < 2) {
            return Err.err("Invalid Request, must have action and body");
        }

        switch (substrings[0]) {
            case "GET":
                return webSocketService.sendOrderDataToClient(user, session, orderService);
            case "CREATE":
                final var cActionResult = new PerformActionService<CreateOrderDataRequestDAO, CreateOrderDataResponseDAO>()
                    .performAction(orderService::createOrderData, substrings[1], CreateOrderDataRequestDAO.class, user);
                if (cActionResult.error()) {
                    return cActionResult;
                }
                return Err.ok("Successfully created order data");
            case "UPDATE":
                final var uActionResult = new PerformActionService<UpdateOrderDataRequestDAO, Void>()
                    .performAction(
                        new AMT<UpdateOrderDataRequestDAO>().transform(orderService::updateOrderData),
                        substrings[1],
                        UpdateOrderDataRequestDAO.class,
                        user
                    );
                if (uActionResult.error()) {
                    return uActionResult;
                }
                return Err.ok("Successfully updated order data");
            case "DELETE":
                final var dActionResult = new PerformActionService<DeleteOrderDataRequestDAO, Void>()
                    .performAction(
                        new AMT<DeleteOrderDataRequestDAO>().transform(orderService::deleteOrderData),
                        substrings[1],
                        DeleteOrderDataRequestDAO.class,
                        user
                    );
                if (dActionResult.error()) {
                    return dActionResult;
                }
                return Err.ok("Successfully deleted order data");
            default:
                return Err.err("invalid request action");
        }
    }

    private interface PerformActionMethod<Req, Res> {
        Res run(Req dao, Long userID, short classNumber);
    }
    private interface PerformActionMethodReturnVoid<Req> {
        void run(Req dao, Long userID, short classNumber);
    }
    private static class AMT<Req> {
        private PerformActionMethod<Req, Void> transform(PerformActionMethodReturnVoid<Req> method) {
            return (Req dao, Long userID, short classNumber) -> {
                method.run(dao, userID, classNumber);
                return null;
            };
        }
    }
    private class PerformActionService<Req, Res> {
        public Err performAction(PerformActionMethod<Req, Res> method, String rawData, Class<Req> reqClass, AppUser user) {
            try {
                final var data = objectMapper.readValue(rawData, reqClass);
                final var result = method.run(data, user.getID(), user.getClassNumber());
                return Err.ok(objectMapper.writeValueAsString(result));
            } catch (Exception e) {
                return Err.err(e.getMessage());
            }
        }
    }
}
