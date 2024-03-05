package net.octoberserver.ordersystem.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.order.dao.*;
import net.octoberserver.ordersystem.user.AppUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final AppUserRepository userRepository;

    @GetMapping
    GetOrderDataResponseDAO getOrderData() {
        return orderService.getOrderData(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    @PostMapping
    CreateOrderDataResponseDAO createOrderData(@RequestBody @Valid CreateOrderDataRequestDAO request) {
        final var userID = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        final var classNumber = userRepository.findById(userID).orElseThrow().getClassNumber();

        return orderService.createOrderData(request, Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()), classNumber);
    }

    @PatchMapping
    void updateOrderData(@RequestBody @Valid UpdateOrderDataRequestDAO request) {
        final var userID = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        final var classNumber = userRepository.findById(userID).orElseThrow().getClassNumber();

        orderService.updateOrderData(request, Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()), classNumber);
    }

    @DeleteMapping
    void deleteOrderData(@RequestBody @Valid DeleteOrderDataRequestDAO request) {
        final var userID = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        final var classNumber = userRepository.findById(userID).orElseThrow().getClassNumber();

        orderService.deleteOrderData(request, Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()), classNumber);
    }
}
