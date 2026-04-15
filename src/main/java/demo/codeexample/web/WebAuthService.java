package demo.codeexample.web;

import demo.codeexample.auth.AuthLookup;
import demo.codeexample.auth.ChangePasswordRequest;
import demo.codeexample.auth.LoginRequest;
import demo.codeexample.auth.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class WebAuthService {

    private final AuthLookup authLookup;

    @Value("${cookie.secure:true}")
    private boolean cookieSecure;

    // ─────────────────────────────────────────
    // LOGIN
    // ─────────────────────────────────────────

    public LoginResult handleLogin(LoginRequest request) {
        try {
            LoginResponse loginResponse = authLookup.getLoginResponse(request);
            String cookie   = buildJwtCookie(loginResponse.getToken());
            String redirect = resolveRedirectAfterLogin(loginResponse);
            return LoginResult.success(cookie, redirect);

        } catch (Exception e) {
            return LoginResult.failure();
        }
    }

    // ─────────────────────────────────────────
    // CHANGE PASSWORD
    // ─────────────────────────────────────────

    public String handleChangePassword(String currentPassword,
                                       String newPassword,
                                       String confirmPassword,
                                       String jwtToken) {
        if (isTokenMissing(jwtToken)) {
            return "redirect:/web/login";
        }
        if (!passwordsMatch(newPassword, confirmPassword)) {
            return encodeRedirect("/web/change-password",
                    "Passwords do not match");
        }
        try {
            changePassword(currentPassword, newPassword, jwtToken);
            return "redirect:/web/change-password" +
                    "?success=Password+changed+successfully!";

        } catch (Exception e) {
            return encodeRedirect("/web/change-password", e.getMessage());
        }
    }

    // ─────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────

    private void changePassword(String currentPassword,
                                String newPassword,
                                String jwtToken) {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword(currentPassword);
        request.setNewPassword(newPassword);
        authLookup.changePassword(request, "Bearer " + jwtToken);
    }

    private String buildJwtCookie(String token) {
        return "jwt=" + token
                + "; HttpOnly"
                + "; Path=/"
                + "; Max-Age=86400"
                + "; SameSite=Strict"
                + (cookieSecure ? "; Secure" : "");
    }

    private String resolveRedirectAfterLogin(LoginResponse response) {
        if (response.isPasswordResetRequired()) {
            return "redirect:/web/change-password";
        }
        return switch (response.getRole()) {
            case ADMIN, DIRECTOR             -> "redirect:/web/dashboard";
            case PRODUCER, RECRUITER, EDITOR -> "redirect:/web/projects";
            default                          -> "redirect:/web/home";
        };
    }

    private boolean isTokenMissing(String jwtToken) {
        return jwtToken == null || jwtToken.isBlank();
    }

    private boolean passwordsMatch(String newPassword,
                                   String confirmPassword) {
        return newPassword.equals(confirmPassword);
    }

    private String encodeRedirect(String path, String message) {
        String encoded = URLEncoder.encode(
                message != null ? message : "Something went wrong",
                StandardCharsets.UTF_8
        );
        return "redirect:" + path + "?error=" + encoded;
    }

    // ─────────────────────────────────────────
    // LOGIN RESULT — carries cookie + redirect
    // ─────────────────────────────────────────

    public record LoginResult(boolean success,
                              String cookie,
                              String redirect) {

        public static LoginResult success(String cookie, String redirect) {
            return new LoginResult(true, cookie, redirect);
        }

        public static LoginResult failure() {
            return new LoginResult(false, null,
                    "redirect:/web/login?error=true");
        }
    }
}