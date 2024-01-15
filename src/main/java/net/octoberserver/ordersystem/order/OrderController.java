package net.octoberserver.ordersystem.order;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.order.dao.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderPersistentService orderPersistentService;

    @GetMapping("/home")
    public GetHomeDataDAO getHomeData() {
        return orderPersistentService.getHomeData(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()), LocalDate.now());
    }

    @GetMapping
    public GetOrderDataDAO getOrderData() {
        return orderPersistentService.getOrderData(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    @PostMapping
    public CreateOrderDataDAO.Response createOrderData(@RequestBody CreateOrderDataDAO.Request request) {
        return orderPersistentService.createOrderData(request, Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    @PatchMapping
    public void updateOrderData(@RequestBody UpdateOrderDataDAO request) {
        orderPersistentService.updateOrderData(request);
    }

    @DeleteMapping
    public void deleteOrderData(@RequestBody DeleteOrderDataDAO request) {
        orderPersistentService.deleteOrderData(request);
    }
}
