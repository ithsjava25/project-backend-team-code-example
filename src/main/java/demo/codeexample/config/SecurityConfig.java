package demo.codeexample.config;

import demo.codeexample.company.TenantContext;
import demo.codeexample.security.JwtAuthenticationFilter;
import demo.codeexample.security.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

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

    private static final Set<String> NON_TENANT_ROOTS = Set.of(
            "api", "css", "js", "images", "oauth2", "login", "logout",
            "aboutUs", "error", "favicon.ico", "actuator"
    );

    private String resolveTenantFromUri(String uri) {
        String[] parts = uri.split("/");

        if (parts.length <= 1) {
            return "";
        }

        String firstSegment = parts[1];

        if (firstSegment == null || firstSegment.isBlank()) {
            return "";
        }

        if (NON_TENANT_ROOTS.contains(firstSegment)) {
            return "";
        }

        return firstSegment;
    }

    private String safeRelativeTarget(String uri, String query) {
        String targetUrl = uri + (query != null ? "?" + query : "");

        if (!targetUrl.startsWith("/") || targetUrl.startsWith("//")) {
            return "/";
        }

        return targetUrl;
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
                                .requestMatchers(
                                        "/", "/*",
                                        "/aboutUs", "/*/aboutUs",
                                        "/login", "/*/login",
                                        "/login/change-password",
                                        "/logout", "/*/logout",
                                        "/css/**","/js/**", "/images/**", "/favicon.ico",
                                        "/api/auth/**",
                                        "/api/auth/login",
                                        "/oauth2/**",
                                        "/login/oauth2/**",
                                        "/*/*/info/*"  // <-- not goo solution. Temporary!
                                ).permitAll()
                                .requestMatchers(
                                        "/api/files/**",
                                        "/*/api/files/**"
                                ).permitAll()
                                .anyRequest()
                                .authenticated()
                        // everything else requires a valid token
                )


                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            String uri = request.getRequestURI();
                            String query = request.getQueryString();

                            String targetUrl = safeRelativeTarget(uri, query);
                            String company = resolveTenantFromUri(uri);

                            String loginUrl = company.isBlank()
                                    ? "/login"
                                    : "/" + company + "/login";

                            response.sendRedirect(loginUrl + "?redirect=" +
                                    java.net.URLEncoder.encode(
                                            targetUrl,
                                            java.nio.charset.StandardCharsets.UTF_8
                                    ));
                        })
                )

                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class)

                // Add OAuth2 login
                .oauth2Login(oauth2 -> oauth2
                                .successHandler(oAuth2LoginSuccessHandler)
                        // runs your handler after Google login succeeds
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("jwt")
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }
}
