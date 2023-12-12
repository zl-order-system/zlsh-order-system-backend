package net.octoberserver.ordersystem.auth.service;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.user.AppUser;
import net.octoberserver.ordersystem.user.AppUserRepository;
import net.octoberserver.ordersystem.user.Role;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AppUserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            throw new RuntimeException("Email is null");
        }

        String googleName = oAuth2User.getAttribute("name");

        long userID = Long.parseLong(email.substring(1, email.indexOf('@'))); // 學號
        String name = googleName.substring(5);
        short classNumber = Short.parseShort(googleName.substring(0, 3));
        short seatNumber = Short.parseShort(googleName.substring(3, 5));

        AppUser user;
        Optional<AppUser> userOptional = userRepository.findById(userID);

        if (userOptional.isEmpty()) {
            user = AppUser
                .builder()
                .ID(userID)
                .name(name)
                .googleName(googleName)
                .email(email)
                .role(Role.USER)
                .classNumber(classNumber)
                .seatNumber(seatNumber)
                .attributes(oAuth2User.getAttributes())
                .build();
            userRepository.save(user);
        } else {
            user = userOptional.get();
            if (!user.getGoogleName().equals(googleName)) {
                user.setGoogleName(googleName);
                user.setName(name);
                user.setClassNumber(classNumber);
                user.setSeatNumber(seatNumber);
                userRepository.save(user);
            }
        }

        return user;
    }
}
