package net.octoberserver.ordersystem.order;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.order.dao.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    GetOrderDataDAO getOrderData() {
        return orderService.getOrderData(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    @PostMapping
    CreateOrderDataResponseDAO createOrderData(@RequestBody CreateOrderDataRequestDAO request) {
        return orderService.createOrderData(request, Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    @PatchMapping
    void updateOrderData(@RequestBody UpdateOrderDataRequestDAO request) {
        orderService.updateOrderData(request);
    }

    @DeleteMapping
    void deleteOrderData(@RequestBody DeleteOrderDataDAO request) {
        orderService.deleteOrderData(request);
    }
}
