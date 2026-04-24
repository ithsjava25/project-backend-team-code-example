package demo.codeexample.auth.infrastructure;

import demo.codeexample.user.UserAuthDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final UserAuthDto user;

    public CustomUserDetails(UserAuthDto user) {
        this.user = user;
    }

    public Long getId() {
        return user.getId();
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public String getFullName() {
        String  first = user.getFirstName() != null ? user.getFirstName() : "";
        String last = user.getLastName() != null ? user.getLastName() : "";
        return (first + " " + last).trim();
    }

    public String getInitial() {
        if (user.getFirstName() != null && !user.getFirstName().isBlank()) {
            return user.getFirstName().substring(0, 1).toUpperCase();
        }
        return "?";
    }

    public String getRoleName() {
        return user.getRole() != null ? user.getRole().name() : "";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRole() == null
                ? List.of()
                : List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !user.isPasswordResetRequired();
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}