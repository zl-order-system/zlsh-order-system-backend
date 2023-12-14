package net.octoberserver.ordersystem.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("test")
    public void testInternalFunction() {
        orderService.getOrderData();
    }
}
