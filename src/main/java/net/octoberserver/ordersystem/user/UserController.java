package net.octoberserver.ordersystem.user;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.order.OrderRepository;
import net.octoberserver.ordersystem.order.dao.GetHomeDataDAO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final RoleService roleService;
    private final UserService userService;
    private final AppUserRepository userRepository;
    private final OrderRepository orderRepository;

    @GetMapping("/home")
    GetHomeDataDAO getHomeData() {
        final var userID = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        final var result = userService.processHomeData(orderRepository.findUpcomingMealsWithOrders(userID), LocalDate.now());
        result.setRole(roleService.roleToString(
            userRepository
                .findById(userID)
                .orElseThrow()
                .getRole())
        );
        return result;
    }
}
