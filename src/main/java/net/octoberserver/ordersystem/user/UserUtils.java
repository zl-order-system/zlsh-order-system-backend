package net.octoberserver.ordersystem.user;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {
    public static AppUser getUserFromCtx() {
        final var credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if (!(credentials instanceof AppUser))
            throw new RuntimeException("Authentication credential is not an user");
        return (AppUser) credentials;
    }
}
