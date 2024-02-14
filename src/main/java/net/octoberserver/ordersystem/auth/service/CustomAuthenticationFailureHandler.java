package net.octoberserver.ordersystem.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.octoberserver.ordersystem.AppEnv;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        if (!(exception instanceof LoginErrorException loginException)) return;
        response.sendRedirect(AppEnv.FRONTEND_ROOT_URL + "/#/login?error=" + URLEncoder.encode(loginException.getError().name(), StandardCharsets.UTF_8));
    }
}
