package net.octoberserver.ordersystem.admin.stats;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.stats.dao.GetStatDataResponseDAO;
import net.octoberserver.ordersystem.admin.stats.dao.GetStatDetailedDataResponseDAO;
import net.octoberserver.ordersystem.user.AppUserRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static net.octoberserver.ordersystem.Utils.getUserFromCtx;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;
    private final AppUserRepository userRepository;

    @GetMapping
    List<GetStatDataResponseDAO> getStatData(@RequestParam(name = "date") @FutureOrPresent @NotNull LocalDate date) {
        return statsService.getStatData(date, getUserFromCtx().getClassNumber());
    }

    @GetMapping("/detailed")
    // TODO: Change mealID to mealOption
    GetStatDetailedDataResponseDAO getStatDetailedData(@RequestParam(name = "date") @FutureOrPresent @NotNull LocalDate date, @RequestParam(name = "mealID") short mealOption) {
        return statsService.getStatDetailedData(date, getUserFromCtx().getClassNumber(), mealOption);
    }
}
