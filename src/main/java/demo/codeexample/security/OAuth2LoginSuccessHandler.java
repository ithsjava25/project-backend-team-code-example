package demo.codeexample.security;

import demo.codeexample.user.UserDto;
import demo.codeexample.user.UserLookup;
import demo.codeexample.user.Role;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserLookup userLookup;
    private final JwtService jwtService;

    @Value("${cookie.secure:true}")
    private boolean cookieSecure;

    public OAuth2LoginSuccessHandler(UserLookup userLookup,
                                     JwtService jwtService) {
        this.userLookup = userLookup;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email     = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName  = oAuth2User.getAttribute("family_name");

        UserDto user = userLookup.findByEmail(email)
                .orElseGet(() -> userLookup.createOAuthUser(
                        email, firstName, lastName
                ));

        if (!user.isActive()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Account is deactivated");
            return;
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );

        // ← REPLACE old cookie code with this:
        String cookieValue = "jwt=" + token
                + "; HttpOnly"
                + "; Path=/"
                + "; Max-Age=86400"
                + "; SameSite=Strict"
                + (cookieSecure ? "; Secure" : "");

        response.setHeader("Set-Cookie", cookieValue);

        String redirectUrl = switch (user.getRole()) {
            case ADMIN, DIRECTOR             -> "/web/dashboard";
            case PRODUCER, RECRUITER, EDITOR -> "/web/projects";
            default                          -> "/web/home";
        };

        response.sendRedirect(redirectUrl);
    }
}