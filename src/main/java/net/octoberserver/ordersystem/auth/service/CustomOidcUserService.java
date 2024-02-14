package net.octoberserver.ordersystem.auth.service;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.auth.LoginError;
import net.octoberserver.ordersystem.user.AppUser;
import net.octoberserver.ordersystem.user.AppUserRepository;
import net.octoberserver.ordersystem.user.Role;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final OAuthCommonUserService commonUserService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getEmail();
        String googleName = oidcUser.getAttribute("name");

        commonUserService.checkUserInfo(googleName, email);
        try {
            return commonUserService.createAppUser(googleName, email, oidcUser.getAttributes());
        } catch (Exception e) {
            throw new LoginErrorException(LoginError.ACCOUNT_FORMAT_ERROR.name(), LoginError.ACCOUNT_FORMAT_ERROR);
        }
    }
}


