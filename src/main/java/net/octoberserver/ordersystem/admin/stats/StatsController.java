package net.octoberserver.ordersystem.admin.stats;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.stats.dao.GetStatDataResponseDAO;
import net.octoberserver.ordersystem.admin.stats.dao.GetStatDetailedDataResponseDAO;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping
    List<GetStatDataResponseDAO> getStatData(@RequestParam(name = "date") @FutureOrPresent @NotNull LocalDate date) {
        return statsService.getStatData(date);
    }

    @GetMapping("/detailed")
    GetStatDetailedDataResponseDAO getStatDetailedData(@RequestParam(name = "date") @FutureOrPresent @NotNull LocalDate date, @RequestParam(name = "mealID") short mealOption) {
        return statsService.getStatDetailedData(date, mealOption);
    }
}
