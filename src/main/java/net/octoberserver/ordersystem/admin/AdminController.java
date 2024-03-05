package net.octoberserver.ordersystem.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.meal.MealClassLock;
import net.octoberserver.ordersystem.meal.MealClassLockRepository;
import net.octoberserver.ordersystem.meal.MealRepository;
import net.octoberserver.ordersystem.user.AppUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MealRepository mealRepository;
    private final MealClassLockRepository classLockRepository;
    private final AppUserRepository userRepository;

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
        private boolean locked;
    }

    @PatchMapping("/order/lock")
    void lockOrderingAndPayments(@RequestBody @Valid LockOrderingAndPaymentsDAO request) {
        final var userID = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        final var classNumber = userRepository.findById(userID).orElseThrow().getClassNumber();

        final var id = MealClassLock.generateID(request.getDate(), classNumber);

        if (request.isLocked()) {
            classLockRepository.save(
                MealClassLock
                    .builder()
                    .id(id)
                    .mealID(request.getDate())
                    .classNumber(classNumber)
                    .build()
            );
            return;
        }
        classLockRepository.deleteById(id);
    }
}
