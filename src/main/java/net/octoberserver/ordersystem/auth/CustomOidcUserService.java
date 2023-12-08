package net.octoberserver.ordersystem.auth;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.user.AppUser;
import net.octoberserver.ordersystem.user.AppUserRepository;
import net.octoberserver.ordersystem.user.Role;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final AppUserRepository userRepository;
    private final JWTService jwtService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getEmail();
        String googleName = oidcUser.getFullName();

        long userID = Long.parseLong(email.substring(1, email.indexOf('@'))); // 學號
        String name = googleName.substring(5, googleName.length() - 1);
        short classNumber = Short.parseShort(googleName.substring(0, 2));
        short seatNumber = Short.parseShort(googleName.substring(3, 4));

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
                .attributes(oidcUser.getAttributes())
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


