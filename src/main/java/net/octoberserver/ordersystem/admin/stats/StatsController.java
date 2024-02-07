package net.octoberserver.ordersystem.admin.stats;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.admin.stats.dao.GetStatDataRequestDAO;
import net.octoberserver.ordersystem.admin.stats.dao.GetStatDataResponseDAO;
import net.octoberserver.ordersystem.admin.stats.dao.GetStatDetailedDataRequestDAO;
import net.octoberserver.ordersystem.admin.stats.dao.GetStatDetailedDataResponseDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping
    List<GetStatDataResponseDAO> getStatData(@RequestBody @Valid GetStatDataRequestDAO request) {
        return statsService.getStatData(request);
    }

    @GetMapping("/detailed")
    GetStatDetailedDataResponseDAO getStatDetailedData(@RequestBody @Valid GetStatDetailedDataRequestDAO request) {
        return null;
    }
}
