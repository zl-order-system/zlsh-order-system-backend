package net.octoberserver.ordersystem.order;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.order.dao.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OrderPersistentService {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public GetHomeDataDAO getHomeData(long userID, LocalDate today) {
        return orderService.processHomeData(orderRepository.findUpcomingMealsWithOrders(userID), today);
    }

    public GetOrderDataDAO getOrderData(long userID) {
        return orderService.processOrderData(orderRepository.findUpcomingMealsWithOrders(userID));
    }

    public CreateOrderDataDAO.Response createOrderData(CreateOrderDataDAO.Request request, long userID) {
        return new CreateOrderDataDAO.Response(orderRepository.save(OrderData.builder()
            .date(request.date())
            .lunchBox(orderService.getLunchBoxEnum(request.lunchBoxType()))
            .userID(userID)
            .meal(request.selectedMeal())
            .paid(false)
            .build()
        ).getID());
    }

    public void updateOrderData(UpdateOrderDataDAO request) {
        final var orderDataOptional = orderRepository.findById(request.id());
        if (orderDataOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderData not found");
        }
        final var orderData = orderDataOptional.get();
        orderData.setMeal(request.selectedMeal());
        orderData.setLunchBox(orderService.getLunchBoxEnum(request.lunchBoxType()));
        orderRepository.save(orderData);
    }

    public void deleteOrderData(DeleteOrderDataDAO request) {
        try {
            orderRepository.deleteById(request.id());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}
