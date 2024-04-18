package net.octoberserver.ordersystem.user;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.user.dao.GetAccountDataResponseDAO;
import net.octoberserver.ordersystem.user.dao.GetHomeDataResponseDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static net.octoberserver.ordersystem.Utils.getUserFromCtx;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/home")
    GetHomeDataResponseDAO getHomeData() {
        return userService.getHomeData(getUserFromCtx().getID());
    }

    @GetMapping("/account")
    GetAccountDataResponseDAO getAccountData() {
        return userService.getAccountData(getUserFromCtx().getID());
    }

    @GetMapping("/roles")
    List<String> getRoles() {
        return userService.getRoles(getUserFromCtx().getID());
    }
}
