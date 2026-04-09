package demo.codeexample.security;

import demo.codeexample.enums.Role;
import demo.codeexample.user.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public OAuth2LoginSuccessHandler(UserRepository userRepository,
                                     JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // 1. Get user info from Google
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        System.out.println("OAuth2 login: " + email);

        // 2. Find or create user in YOUR database
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createNewOAuthUser(email, firstName, lastName));

        // 3. Check user is active
        if (!user.isActive()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Account is deactivated");
            return;
        }

        // 4. Issue YOUR JWT token — same as normal login!
        String token = jwtService.generateToken(user);

        // 5. Return token to frontend
        // For a REST API, redirect with token as query parameter
//        response.sendRedirect(
//                "http://localhost:3000/oauth2/callback?token=" + token
//        );
        // frontend reads the token from URL and stores it

// Replace the sendRedirect line with this temporarily:
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"token\": \"" + token + "\", \"role\": \"" + user.getRole() + "\"}"
        );



    }

    private User createNewOAuthUser(String email, String firstName, String lastName) {

        // New user via Google gets VISITOR role by default
        // Admin can upgrade their role later
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFirstName(firstName != null ? firstName : "Unknown");
        newUser.setLastName(lastName != null ? lastName : "Unknown");
        newUser.setPassword("OAUTH2_USER_NO_PASSWORD");
        // ↑ OAuth2 users have no password — they always log in via Google
        newUser.setRole(Role.VISITOR);
        newUser.setActive(true);
        newUser.setPasswordResetRequired(false);
        // ↑ no password reset needed — they don't have a password!

        return userRepository.save(newUser);

    }




}