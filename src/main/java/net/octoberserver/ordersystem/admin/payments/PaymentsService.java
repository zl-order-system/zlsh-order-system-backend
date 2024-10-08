package net.octoberserver.ordersystem.admin.payments;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.payments.dao.GetPaymentDataResponseDAO;
import net.octoberserver.ordersystem.admin.payments.dao.UpdatePaymentStatusRequestDAO;
import net.octoberserver.ordersystem.meal.MealClassLock;
import net.octoberserver.ordersystem.meal.MealClassLockRepository;
import net.octoberserver.ordersystem.order.OrderService;
import net.octoberserver.ordersystem.order.lunchbox.LunchBoxService;
import net.octoberserver.ordersystem.meal.MealOption;
import net.octoberserver.ordersystem.meal.MealRepository;
import net.octoberserver.ordersystem.order.OrderData;
import net.octoberserver.ordersystem.order.OrderRepository;
import net.octoberserver.ordersystem.user.AppUser;
import net.octoberserver.ordersystem.user.AppUserRepository;
import net.octoberserver.ordersystem.websocket.WebSocketService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PaymentsService {

    private final LunchBoxService lunchBoxService;
    private final MealRepository mealRepository;
    private final OrderRepository orderRepository;
    private final AppUserRepository userRepository;
    private final MealClassLockRepository classLockRepository;
    private final WebSocketService webSocketService;
    private final OrderService orderService;

    GetPaymentDataResponseDAO getPaymentData(LocalDate date, short classNumber) {
        final var meal = mealRepository
            .findById(date)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find payment data for that date"));

        final var options = meal
            .getOptions()
            .stream().map(MealOption::getName)
            .toList();

        final var locked = classLockRepository.findById(MealClassLock.generateID(date, classNumber)).isPresent();

        return GetPaymentDataResponseDAO.builder()
            .locked(locked)
            .data(orderRepository.findOrdersWithUsersByDate(date, classNumber).stream().map(userOrder -> {
                final var user = userOrder.get(0, AppUser.class);
                final var order = userOrder.get(1, OrderData.class);
                return GetPaymentDataResponseDAO.Item.builder()
                    .userID(user.getID())
                    .name(user.getName())
                    .seatNumber(user.getSeatNumber())
                    .lunchBoxType(lunchBoxService.getLunchBoxString(order.getLunchBox()))
                    .mealName(options.get(order.getMealOption()))
                    .paid(order.isPaid())
                    .build();
            }).toList())
            .build();
    }

    void updatePaymentStatus(UpdatePaymentStatusRequestDAO request, short classNumber) {
        final var locked = classLockRepository.findById(MealClassLock.generateID(request.date(), classNumber)).isPresent();
        if (locked)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The date has been locked by an admin");

        final var orderData = orderRepository.findByDateAndUserID(request.date(), request.userID())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderData not found"));

        if (userRepository.findById(orderData.getUserID()).orElseThrow().getClassNumber() != classNumber)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot modify status of another class");

        orderData.setPaid(request.paid());
        orderRepository.save(orderData);

        webSocketService.sendOrderDataToClients(orderData.getUserID(), classNumber, orderService);
    }
}
