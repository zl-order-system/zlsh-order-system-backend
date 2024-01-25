package net.octoberserver.ordersystem.admin;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.meal.MealRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    final MealRepository mealRepository;

    public List<LocalDate> getUpcomingDates() {
        return mealRepository.findUpcomingDates(LocalDate.now());
    }
}
