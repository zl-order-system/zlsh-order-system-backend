package net.octoberserver.ordersystem.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.order.dao.*;
import net.octoberserver.ordersystem.user.AppUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static net.octoberserver.ordersystem.user.UserUtils.getUserFromCtx;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final AppUserRepository userRepository;

    @GetMapping
    GetOrderDataResponseDAO getOrderData() {
        return orderService.getOrderData(getUserFromCtx().getID());
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
