package demo.codeexample.web;

import demo.codeexample.auth.AuthLookup;
import demo.codeexample.auth.ChangePasswordRequest;
import demo.codeexample.auth.LoginRequest;
import demo.codeexample.auth.LoginResponse;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/web")
@RequiredArgsConstructor
public class WebAuthController {

    private final AuthLookup authLookup;
    private final TemplateEngine templateEngine;

    @Value("${cookie.secure:true}")  // ← true by default, false in local
    private boolean cookieSecure;

    @GetMapping("/login")
    @ResponseBody
    public String loginPage(@RequestParam(required = false) String error) {
        var output = new StringOutput();
        templateEngine.render("auth/login.jte", Map.of(
                "error", error != null ? "Invalid email or password" : ""
        ), output);
        return output.toString();
    }

    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String handleLogin(@RequestParam @Valid LoginRequest request,
                              HttpServletResponse response) {
        try {
            LoginResponse loginResponse = authLookup.getLoginResponse(request);

                // Build cookie string — Secure flag controlled by environment
            String cookieValue = "jwt=" + loginResponse.getToken()
                    + "; HttpOnly"
                    + "; Path=/"
                    + "; Max-Age=86400"
                    + "; SameSite=Strict"
                    + (cookieSecure ? "; Secure" : ""); // ← environment-aware!

            response.setHeader("Set-Cookie", cookieValue);

            // Check password reset before role redirect
            if (loginResponse.isPasswordResetRequired()) {
                return "redirect:/web/change-password";
            }

            return switch (loginResponse.getRole()) {
                case ADMIN, DIRECTOR             -> "redirect:/web/dashboard";
                case PRODUCER, RECRUITER, EDITOR -> "redirect:/web/projects";
                default                          -> "redirect:/web/home";
            };

        } catch (Exception e) {
            return "redirect:/web/login?error=true";
        }
    }

    // Show change-password page
    @GetMapping("/change-password")
    @ResponseBody
    public String changePasswordPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String success) {

        var output = new StringOutput();
        templateEngine.render("auth/change-password.jte", Map.of(
                "error",   error   != null ? error   : "",
                "success", success != null ? success : ""
        ), output);
        return output.toString();
    }

    // Handle change-password form submission
    @PostMapping(value = "/change-password",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String handleChangePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            @CookieValue(name = "jwt", required = false) String jwtToken) {

        // Check token exists — if not, redirect to login
        if (jwtToken == null) {
            return "redirect:/web/login";
        }

        // Check passwords match
        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/web/change-password?error=Passwords do not match";
        }

        try {
            // Build request and delegate to AuthLookup
            ChangePasswordRequest request = new ChangePasswordRequest();
            request.setCurrentPassword(currentPassword);
            request.setNewPassword(newPassword);

            authLookup.changePassword(request, "Bearer " + jwtToken);

            return "redirect:/web/change-password?success=Password changed successfully!";

        } catch (Exception e) {
            return "redirect:/web/change-password?error=" + e.getMessage();
        }
    }

}