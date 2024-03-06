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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static net.octoberserver.ordersystem.user.UserUtils.getUserFromCtx;

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

    record LockOrderingAndPaymentsDAO  (
        @NotNull
        @FutureOrPresent
        LocalDate date,
        @NotNull
        Boolean locked
    ) {}

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @PatchMapping("/order/lock")
    void lockOrderingAndPayments(@RequestBody @Valid LockOrderingAndPaymentsDAO request) {
        final var classNumber = getUserFromCtx().getClassNumber();
        final var id = MealClassLock.generateID(request.date(), classNumber);
        final var lock = MealClassLock
            .builder()
            .id(id)
            .mealID(request.date())
            .classNumber(classNumber)
            .build();
        logger.info(lock.toString());
        if (request.locked()) {
            classLockRepository.save(lock);
            return;
        }
        classLockRepository.deleteById(id);
    }
}
