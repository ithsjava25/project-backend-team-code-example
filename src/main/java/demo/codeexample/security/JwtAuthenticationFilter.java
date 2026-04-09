package demo.codeexample.security;

import demo.codeexample.user.doman.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/* Guards Every Request. This runs on every single HTTP request before it reaches your controller:*/

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // OncePerRequestFilter — guaranteed to run exactly once per request
    /*Why OncePerRequestFilter? In some configurations, filters can run multiple times per request.
    This base class guarantees exactly one execution — important for security filters.*/

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


        // 1. Look for the Authorization header
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No token — let it pass, Spring Security will handle it
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract the token (remove "Bearer " prefix)
        String token = authHeader.substring(7);

        // 3. Validate the token
        if (!jwtService.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 4. Extract email and load user
        String email = jwtService.extractEmail(token);
        String role = jwtService.extractRole(token);

        // 5. Tell Spring Security "this user is authenticated"
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role))
                );

        /*Why "ROLE_" + role? Spring Security expects role strings prefixed with ROLE_.
        So your ADMIN enum becomes ROLE_ADMIN.
        When you later use hasRole('ADMIN'), Spring automatically checks for ROLE_ADMIN.*/

        /*Why not load the user from the database here?
        We already have the email and role in the token.
        We don't need another DB call for basic authentication.
        We only load from DB if we need more user details.*/

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // SecurityContext = Spring's memory of "who is currently logged in"

        // 6. Continue to the actual endpoint
        filterChain.doFilter(request, response);

    }

}
