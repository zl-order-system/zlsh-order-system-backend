package net.octoberserver.ordersystem.admin.payments;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.payments.dao.GetPaymentDataRequestDAO;
import net.octoberserver.ordersystem.admin.payments.dao.GetPaymentDataResponseDAO;
import net.octoberserver.ordersystem.admin.payments.dao.UpdatePaymentStatusRequestDAO;
import net.octoberserver.ordersystem.order.LunchBoxService;
import net.octoberserver.ordersystem.meal.MealOption;
import net.octoberserver.ordersystem.meal.MealRepository;
import net.octoberserver.ordersystem.order.OrderData;
import net.octoberserver.ordersystem.order.OrderRepository;
import net.octoberserver.ordersystem.user.AppUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentsService {

    private final LunchBoxService lunchBoxService;
    private final MealRepository mealRepository;
    private final OrderRepository orderRepository;

    List<GetPaymentDataResponseDAO> getPaymentData(GetPaymentDataRequestDAO request) {
        final var options = mealRepository
            .findById(request.getDate())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found"))
            .getOptions()
            .stream().map(MealOption::getName)
            .toList();

        return orderRepository.findOrdersWithUsersByDate(request.getDate()).stream().map(userOrder -> {
            final var user = userOrder.get(0, AppUser.class);
            final var order = userOrder.get(1, OrderData.class);
            return GetPaymentDataResponseDAO.builder()
                .userID(user.getID())
                .name(user.getName())
                .seatNumber(user.getSeatNumber())
                .lunchBoxType(lunchBoxService.getLunchBoxString(order.getLunchBox()))
                .mealName(options.get(order.getMealOption()))
                .paid(order.isPaid())
                .build();
        }).toList();
    }

    void updatePaymentStatus(UpdatePaymentStatusRequestDAO request) {
        final var orderData = orderRepository.findByDateAndUserID(request.getDate(), request.getUserID());
        orderData.setPaid(request.isPaid());
        orderRepository.save(orderData);
    }
}
