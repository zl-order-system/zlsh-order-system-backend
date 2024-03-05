package net.octoberserver.ordersystem.admin.stats;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.stats.dao.GetStatDataResponseDAO;
import net.octoberserver.ordersystem.admin.stats.dao.GetStatDetailedDataResponseDAO;
import net.octoberserver.ordersystem.meal.MealRepository;
import net.octoberserver.ordersystem.order.lunchbox.LunchBox;
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

    List<GetStatDataResponseDAO> getStatData(LocalDate date, short classNumber) {
        final var options = mealRepository
            .findById(date)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find statistics data for that date"))
            .getOptions();
        return findStatData(date, classNumber)
            .stream()
            .map(tuple ->
                GetStatDataResponseDAO
                    .builder()
                    .id(tuple.get(0, Short.class))
                    .name(options.get(tuple.get(0, Short.class)).getName())
                    .personalBoxCount(tuple.get(1, Long.class))
                    .schoolBoxCount(tuple.get(2, Long.class))
                    .build()
            )
            .toList();
    }

    List<Tuple> findStatData(LocalDate date, short classNumber) {
        return orderRepository.findStatData(date, classNumber, LunchBox.PERSONAL, LunchBox.SCHOOL);
    }

    GetStatDetailedDataResponseDAO getStatDetailedData(LocalDate date, short classNumber, short mealOption) {
        final var result = orderRepository.findStatDetailed(date, classNumber, mealOption);
        return GetStatDetailedDataResponseDAO.builder()
            .personalLunchBox(result
                .stream()
                .filter(tuple -> tuple.get(1, LunchBox.class).equals(LunchBox.PERSONAL))
                .map(tuple -> tuple.get(0, Short.class))
                .toList()
            ).schoolLunchBox(result
                .stream()
                .filter(tuple -> tuple.get(1, LunchBox.class).equals(LunchBox.SCHOOL))
                .map(tuple -> tuple.get(0, Short.class))
                .toList()
            ).build();

    }
}
