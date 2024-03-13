package net.octoberserver.ordersystem;

import net.octoberserver.ordersystem.user.AppUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static String formatApiDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static AppUser getUserFromCtx() {
        final var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AppUser user)
            return user;
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Authentication principal is not an AppUser");
    }
}
