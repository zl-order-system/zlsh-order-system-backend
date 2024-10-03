package net.octoberserver.ordersystem.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.order.dao.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import static net.octoberserver.ordersystem.Utils.getUserFromCtx;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    GetOrderDataResponseDAO getOrderData() {
        final var user = getUserFromCtx();
        return orderService.getOrderData(user.getID(), user.getClassNumber());
    }

    @PostMapping
    CreateOrderDataResponseDAO createOrderData(@RequestBody @Valid CreateOrderDataRequestDAO request) {
        final var user = getUserFromCtx();
        return orderService.createOrderData(request, user.getID(), user.getClassNumber());
    }

    @PatchMapping
    void updateOrderData(@RequestBody @Valid UpdateOrderDataRequestDAO request) {
        final var user = getUserFromCtx();
        orderService.updateOrderData(request, user.getID(), user.getClassNumber());
    }

    @DeleteMapping
    void deleteOrderData(@RequestBody @Valid DeleteOrderDataRequestDAO request) {
        final var user = getUserFromCtx();
        orderService.deleteOrderData(request, user.getID(), user.getClassNumber());
    }
}
