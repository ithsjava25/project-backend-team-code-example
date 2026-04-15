package demo.codeexample.web;

import demo.codeexample.auth.AuthLookup;
import demo.codeexample.auth.ChangePasswordRequest;
import demo.codeexample.auth.LoginRequest;
import demo.codeexample.auth.LoginResponse;
import demo.codeexample.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebAuthService {

    private final AuthLookup authLookup;
    private final JwtService jwtService;

    @Value("${cookie.secure:true}")
    private boolean cookieSecure;

    // ─────────────────────────────────────────
    // LOGIN
    // ─────────────────────────────────────────

    public LoginResponse login(LoginRequest request) {
        return authLookup.getLoginResponse(request);
    }

    public String buildJwtCookie(String token) {
        return "jwt=" + token
                + "; HttpOnly"
                + "; Path=/"
                + "; Max-Age=86400"
                + "; SameSite=Strict"
                + (cookieSecure ? "; Secure" : "");
    }

    public String resolveRedirectAfterLogin(LoginResponse loginResponse) {
        if (loginResponse.isPasswordResetRequired()) {
            return "redirect:/web/change-password";
        }
        return switch (loginResponse.getRole()) {
            case ADMIN, DIRECTOR             -> "redirect:/web/dashboard";
            case PRODUCER, RECRUITER, EDITOR -> "redirect:/web/projects";
            default                          -> "redirect:/web/home";
        };
    }

    // ─────────────────────────────────────────
    // CHANGE PASSWORD
    // ─────────────────────────────────────────

    public void changePassword(String currentPassword,
                               String newPassword,
                               String jwtToken) {
        ChangePasswordRequest request = buildChangePasswordRequest(
                currentPassword, newPassword
        );
        authLookup.changePassword(request, "Bearer " + jwtToken);
    }

    public boolean passwordsMatch(String newPassword, String confirmPassword) {
        return newPassword.equals(confirmPassword);
    }

    public boolean isTokenMissing(String jwtToken) {
        return jwtToken == null || jwtToken.isBlank();
    }

    // ─────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────

    private ChangePasswordRequest buildChangePasswordRequest(
            String currentPassword, String newPassword) {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword(currentPassword);
        request.setNewPassword(newPassword);
        return request;
    }
}