package net.octoberserver.ordersystem.admin.stats;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.stats.dao.GetStatDataResponseDAO;
import net.octoberserver.ordersystem.admin.stats.dao.GetStatDetailedDataResponseDAO;
import net.octoberserver.ordersystem.user.AppUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;
    private final AppUserRepository userRepository;

    @GetMapping
    List<GetStatDataResponseDAO> getStatData(@RequestParam(name = "date") @FutureOrPresent @NotNull LocalDate date) {
        final var userID = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        final var classNumber = userRepository.findById(userID).orElseThrow().getClassNumber();
        return statsService.getStatData(date, classNumber);
    }

    @GetMapping("/detailed")
    GetStatDetailedDataResponseDAO getStatDetailedData(@RequestParam(name = "date") @FutureOrPresent @NotNull LocalDate date, @RequestParam(name = "mealID") short mealOption) {
        final var userID = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        final var classNumber = userRepository.findById(userID).orElseThrow().getClassNumber();
        return statsService.getStatDetailedData(date, classNumber, mealOption);
    }
}
