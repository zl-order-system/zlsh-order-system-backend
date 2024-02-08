package net.octoberserver.ordersystem.admin.stats;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.stats.dao.GetStatDataRequestDAO;
import net.octoberserver.ordersystem.admin.stats.dao.GetStatDataResponseDAO;
import net.octoberserver.ordersystem.meal.MealRepository;
import net.octoberserver.ordersystem.order.LunchBox;
import net.octoberserver.ordersystem.order.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final MealRepository mealRepository;
    private final OrderRepository orderRepository;

    List<GetStatDataResponseDAO> getStatData(GetStatDataRequestDAO request) {
        final var options = mealRepository
            .findById(request.getDate())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find statistics data for that date"))
            .getOptions();
        return findStatData(request.getDate())
            .stream()
            .map(tuple ->
                GetStatDataResponseDAO
                    .builder()
                    .name(options.get(tuple.get(0, Short.class)).getName())
                    .personalBoxCount(tuple.get(1, Long.class))
                    .schoolBoxCount(tuple.get(2, Long.class))
                    .build()
            )
            .toList();
    }

    List<Tuple> findStatData(LocalDate date) {
        return orderRepository.findStatData(date, LunchBox.SCHOOL, LunchBox.PERSONAL);
    }
}
