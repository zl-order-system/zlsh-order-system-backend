package net.octoberserver.ordersystem.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RoleService {
    public String roleToString (Role role) {
        return switch (role) {
            case USER -> "USER";
            case ADMIN -> "ADMIN";
        };
    }

    public Role stringToRole (String string) {
        return switch (string) {
            case "USER" -> Role.USER;
            case "ADMIN" -> Role.ADMIN;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Role: " + string);
        };
    }
}
