package demo.codeexample.security;

import demo.codeexample.user.UserDto;
import demo.codeexample.user.UserLookup;
import demo.codeexample.user.Role;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserLookup userLookup;    // ← no UserRepository! ✅
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        // 1. Get user info from Google
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email     = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName  = oAuth2User.getAttribute("family_name");

        // 2. Find or create user via UserLookup — no direct DB access!
        UserDto user = userLookup.findByEmail(email)
                .orElseGet(() -> userLookup.createOAuthUser(
                        email, firstName, lastName
                ));

        // 3. Check user is active
        if (!user.isActive()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Account is deactivated");
            return;
        }

        // 4. Generate JWT
        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );

        // 5. Store in cookie + redirect based on role
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        cookie.setSecure(true);     //Only send this over HTTPS
        cookie.setAttribute("Samesite", "Strict");      //CSRF protection


        response.addCookie(cookie);

        // 6. Redirect based on role
        String redirectUrl = switch (user.getRole()) {
            case ADMIN, DIRECTOR             -> "/web/dashboard";
            case PRODUCER, RECRUITER, EDITOR -> "/web/projects";
            default                          -> "/web/home";
        };

        response.sendRedirect(redirectUrl);
    }
}