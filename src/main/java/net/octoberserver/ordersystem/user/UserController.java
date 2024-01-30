package net.octoberserver.ordersystem.user;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.user.dao.GetAccountDataResponseDAO;
import net.octoberserver.ordersystem.user.dao.GetHomeDataResponseDAO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/home")
    GetHomeDataResponseDAO getHomeData() {
        return userService.getHomeData(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    @GetMapping("/account")
    GetAccountDataResponseDAO getAccountData() {
        return userService.getAccountData(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()));
    }
}
