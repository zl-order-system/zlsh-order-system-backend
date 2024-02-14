package net.octoberserver.ordersystem.auth.service;

import lombok.RequiredArgsConstructor;

import net.octoberserver.ordersystem.auth.LoginError;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuthCommonUserService commonUserService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            throw new RuntimeException("Email is null");
        }

        String googleName = oAuth2User.getAttribute("name");

        commonUserService.checkUserInfo(googleName, email);
        try {
            return commonUserService.createAppUser(googleName, email, oAuth2User.getAttributes());
        } catch (Exception e) {
            throw new LoginErrorException(LoginError.ACCOUNT_FORMAT_ERROR.name(), LoginError.ACCOUNT_FORMAT_ERROR);
        }
    }
}
