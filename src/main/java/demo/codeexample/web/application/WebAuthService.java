package demo.codeexample.web.application;

import demo.codeexample.auth.AuthLookup;
import demo.codeexample.auth.ChangePasswordRequest;
import demo.codeexample.auth.LoginRequest;
import demo.codeexample.auth.LoginResponse;
import demo.codeexample.company.TenantContext;
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
            return redirect("/login");
        }
        if (!passwordsMatch(newPassword, confirmPassword)) {
            return encodeRedirect("/login/change-password",
                    "Passwords do not match");
        }
        try {
            changePassword(currentPassword, newPassword, jwtToken);
            return redirect("/login/change-password")
                    + "?success=Password+changed+successfully!";

        } catch (Exception e) {
            return encodeRedirect("/login/change-password", "Could not change password");
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
            return redirect("/login/change-password");
        }
        return switch (response.getRole()) {
            case ADMIN, DIRECTOR, PRODUCER, RECRUITER, EDITOR -> redirect("/dashboard");
            default -> redirect("/home");
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

    private String redirect(String path) {
        String company = TenantContext.getTenant();
        String prefix = (company != null && !company.isBlank()) ? "/" + company : "";
        return "redirect:" + prefix + path;
    }

    public record LoginResult(boolean success,
                              String cookie,
                              String redirect) {

        public static LoginResult success(String cookie, String redirect) {
            return new LoginResult(true, cookie, redirect);
        }

        public static LoginResult failure() {
            String company = TenantContext.getTenant();
            String prefix = (company != null && !company.isBlank()) ? "/" + company : "";
            return new LoginResult(false, null,
                    "redirect:" + prefix + "/login?error=true");
        }
    }
}