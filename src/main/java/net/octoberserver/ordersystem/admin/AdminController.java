package net.octoberserver.ordersystem.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.meal.Meal;
import net.octoberserver.ordersystem.meal.MealRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MealRepository mealRepository;

    @GetMapping("/upcoming-dates")
    List<String> getUpcomingDates() {
        return mealRepository
            .findUpcomingDates(LocalDate.now())
            .stream()
            .map(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .toList();
    }

    @Data
    @AllArgsConstructor
    static class LockOrderingAndPaymentsDAO {
        @NotNull
        @FutureOrPresent
        private LocalDate date;
        private boolean state;
    }

    @PatchMapping("/order/lock")
    void lockOrderingAndPayments(@RequestBody @Valid LockOrderingAndPaymentsDAO request) {
        final var options = mealRepository
            .findById(request.getDate())
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Meal Data Not Found For That Date"))
            .getOptions();
        mealRepository.save(new Meal(request.getDate(), options, request.state));
    }
}
