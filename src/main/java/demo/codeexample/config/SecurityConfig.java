package demo.codeexample.config;

import demo.codeexample.security.JwtAuthenticationFilter;
import demo.codeexample.security.OAuth2LoginSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // enables @PreAuthorize on controllers later
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter,
                          OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                    @Value("${app.security.enabled:true}") boolean securityEnabled)
                    throws Exception {

        if (!securityEnabled) {
            // Development mode — allow everything
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }


        http
                .csrf(csrf -> csrf.disable())
                // CSRF protection is for browser sessions — we use JWT, so it is disable

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Don't create server-side sessions — JWT is stateless

                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/").permitAll()

                                .requestMatchers("/api/auth/**").permitAll()

                                .requestMatchers("/login").permitAll()  // login is public

                                .requestMatchers("/login/change-password").permitAll()
                                // ↑ must be accessible — user has token in cookie but might

                                .requestMatchers("/oauth2/**").permitAll()
                                .requestMatchers("/login/oauth2/**").permitAll()
                                // ↑ OAuth2 callback must be public!

                                .requestMatchers("/api/users/**").authenticated()
                                // only admins can manage users

                                .anyRequest().authenticated()
                        // everything else requires a valid token
                )

                // ↓ Returns 401 instead of redirecting to Google login
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write(
                                    "{\"status\": 401, \"error\": \"Unauthorized\", " +
                                            "\"message\": \"Authentication required\"}"
                            );
                        })
                )


                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class)

                // Add OAuth2 login
                .oauth2Login(oauth2 -> oauth2
                                .successHandler(oAuth2LoginSuccessHandler)
                        // runs your handler after Google login succeeds
                );

        return http.build();
    }
}
