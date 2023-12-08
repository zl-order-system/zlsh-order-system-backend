package net.octoberserver.ordersystem.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final OAuth2AuthorizedClientService clientService;

    @Autowired
    public AuthController(OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/callback")
    public RedirectView loginSuccess(OAuth2AuthenticationToken authenticationToken) {
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
            authenticationToken.getAuthorizedClientRegistrationId(),
            authenticationToken.getName()
        );

        // Handle storing the user information and token, then redirect to a successful login page
        // For example, store user details in your database and generate a JWT token for further authentication.

        return new RedirectView("/");
    }
}
