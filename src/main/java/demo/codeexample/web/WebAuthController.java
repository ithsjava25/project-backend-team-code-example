package demo.codeexample.web;

import demo.codeexample.auth.AuthFacade;
import demo.codeexample.auth.LoginRequest;
import demo.codeexample.auth.LoginResponse;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import jakarta.servlet.http.HttpServletResponse;
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

    private final AuthFacade authFacade;
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
    public String handleLogin(@RequestParam String email,
                              @RequestParam String password,
                              HttpServletResponse response) {
        try {
            LoginRequest request = new LoginRequest();
            request.setEmail(email);
            request.setPassword(password);

            LoginResponse loginResponse = authFacade.login(request);

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
}