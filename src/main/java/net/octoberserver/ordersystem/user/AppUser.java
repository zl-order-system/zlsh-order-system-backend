package net.octoberserver.ordersystem.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class AppUser implements OidcUser, UserDetails {
    @Id
    private long ID; // 學號
    private String name;
    private String googleName;
    private String email;
    private short classNumber;
    private short seatNumber;
    @Convert(converter = UserRoleConverter.class)
    private List<Role> roles;
    @Column(length = 2048)
    private String attributes;

    @Override
    public Map<String, Object> getAttributes() {
        try {
            return new ObjectMapper().readValue(attributes, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setAttributes(Map<String, Object> attributes) throws JsonProcessingException {
        this.attributes = new ObjectMapper().writeValueAsString(attributes);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).toList();
    }

    @Override
    public String getUsername() {
        return Long.toString(ID);
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }

    public static class AppUserBuilder {
        public AppUserBuilder attributes(Map<String, Object> attributes){
            try {
                this.attributes = new ObjectMapper().writeValueAsString(attributes);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return this;
        }
    }
}
