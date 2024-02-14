package net.octoberserver.ordersystem.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.AppEnv;
import net.octoberserver.ordersystem.user.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        handle(request, response, authentication);
        super.clearAuthenticationAttributes(request);
    }

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
//        response.sendRedirect(
//            UriComponentsBuilder
//                .fromUriString(AppEnv.FRONTEND_ROOT_URL)
//                .queryParam("token", jwtService.generateToken((AppUser) authentication.getPrincipal()))
//                .build()
//                .toUriString()
//        );
        final var user = (AppUser) authentication.getPrincipal();
        response.sendRedirect(AppEnv.FRONTEND_ROOT_URL + "/#/?token=" + URLEncoder.encode(jwtService.generateToken(user), StandardCharsets.UTF_8));
    }
}
