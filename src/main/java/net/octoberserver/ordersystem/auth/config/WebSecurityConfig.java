package net.octoberserver.ordersystem.auth.config;

import lombok.RequiredArgsConstructor;
import net.octoberserver.ordersystem.auth.CustomAuthenticationSuccessHandler;
import net.octoberserver.ordersystem.auth.CustomOAuth2UserService;
import net.octoberserver.ordersystem.auth.CustomOidcUserService;
import net.octoberserver.ordersystem.auth.JWTAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JWTAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authProvider;
    private final CustomOidcUserService oidcLoginHandler;
    private final CustomOAuth2UserService oauth2LoginHandler;
    private final CustomAuthenticationSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(requestRegistry -> requestRegistry
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/**").authenticated()
                .requestMatchers("/apt/admin/**").hasRole("admin")
                .anyRequest().permitAll()
            )
            .sessionManagement(sc -> sc
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .oauth2Login(lc -> lc
                .userInfoEndpoint(uie -> uie
                    .oidcUserService(oidcLoginHandler)
                    .userService(oauth2LoginHandler)
                )
                .successHandler(successHandler)
            )
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
