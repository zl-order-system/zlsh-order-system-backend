package net.octoberserver.ordersystem.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.user.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTService jwtService;

    private final String redirectUri = "/";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        handle(request, response, authentication);
        super.clearAuthenticationAttributes(request);
    }

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = redirectUri.isEmpty() ?
            determineTargetUrl(request, response, authentication) : redirectUri;

        String token = jwtService.generateToken((AppUser) authentication.getPrincipal());

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl).queryParam("token", token).build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
