package demo.codeexample.security;

import demo.codeexample.user.UserAuthDto;
import demo.codeexample.user.UserAuthPort;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAuthPort userAuthPort;

    public CustomUserDetailsService(UserAuthPort userAuthPort) {
        this.userAuthPort = userAuthPort;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAuthDto user = userAuthPort.findAuthByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new CustomUserDetails(user);
    }
}