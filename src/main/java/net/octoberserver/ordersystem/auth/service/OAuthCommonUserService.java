package net.octoberserver.ordersystem.auth.service;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.auth.LoginError;
import net.octoberserver.ordersystem.user.AppUser;
import net.octoberserver.ordersystem.user.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthCommonUserService {

    private final AppUserRepository userRepository;

    AppUser createAppUser(String googleName, String email, Map<String, Object> attributes) {
        final long userID = Long.parseLong(email.substring(1, email.indexOf('@'))); // 學號
        final String name = googleName.substring(5);
        final short classNumber = Short.parseShort(googleName.substring(0, 3));
        final short seatNumber = Short.parseShort(googleName.substring(3, 5));

        AppUser user;
        final Optional<AppUser> userOptional = userRepository.findById(userID);

        if (userOptional.isEmpty()) {
            user = AppUser
                .builder()
                .ID(userID)
                .name(name)
                .googleName(googleName)
                .email(email)
                .roles(new ArrayList<>())
                .classNumber(classNumber)
                .seatNumber(seatNumber)
                .attributes(attributes)
                .build();
            userRepository.save(user);
            return user;
        }

        user = userOptional.get();
        if (!user.getGoogleName().equals(googleName)) {
            user.setGoogleName(googleName);
            user.setName(name);
            user.setClassNumber(classNumber);
            user.setSeatNumber(seatNumber);
            userRepository.save(user);
        }
        return user;
    }

    void checkUserInfo(String googleName, String email) {
        if (googleName == null || email == null)
            throw new LoginErrorException(LoginError.ACCOUNT_FORMAT_ERROR.name(), LoginError.ACCOUNT_FORMAT_ERROR);

        if (!email.contains("zlsh.tp.edu.tw"))
            throw new LoginErrorException(LoginError.NOT_SCHOOL_ACCOUNT.name(), LoginError.NOT_SCHOOL_ACCOUNT);
    }
}
