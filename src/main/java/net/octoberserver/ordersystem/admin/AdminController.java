package net.octoberserver.ordersystem.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/upcoming-dates")
    List<String> getUpcomingDates() {
        return adminService
            .getUpcomingDates()
            .stream()
            .map(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .toList();
    }
}
