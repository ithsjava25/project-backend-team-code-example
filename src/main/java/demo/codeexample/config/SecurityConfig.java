package demo.codeexample.config;

import demo.codeexample.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/* This replaces Spring's default login page with your own setup:*/

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // enables @PreAuthorize on controllers later
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /*Why SessionCreationPolicy.STATELESS? Tells Spring never to create an HttpSession.
    With JWT, every request is self-contained — no server memory of previous requests.
    This is what makes JWT scalable.*/

    /*Why csrf.disable()? CSRF attacks exploit browser cookies and sessions.
    Since we use JWT in the Authorization header (not cookies), CSRF attacks don't apply.
    Disabling it removes unnecessary overhead.*/


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // CSRF protection is for browser sessions — we use JWT, so disable it

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Don't create server-side sessions — JWT is stateless

                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/auth/**").permitAll()
                                // login is public

                                .requestMatchers("/api/users/**").authenticated()
                                // only admins can manage users

                                .anyRequest().authenticated()
                        // everything else requires a valid token
                )

                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class);
        // run our JWT filter BEFORE Spring's default auth filter

        return http.build();
    }
}
