package net.octoberserver.ordersystem.auth.service;

import lombok.Getter;
import net.octoberserver.ordersystem.auth.LoginError;
import org.springframework.security.core.AuthenticationException;


@Getter
public class LoginErrorException extends AuthenticationException {
    private final LoginError error;
    public LoginErrorException(String message, LoginError error) {
        super(message);
        this.error = error;
    }
}
