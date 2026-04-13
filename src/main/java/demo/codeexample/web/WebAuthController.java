package demo.codeexample.web;

import demo.codeexample.auth.application.AuthService;
import demo.codeexample.security.JwtService;
import demo.codeexample.user.UserLookup;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/web")
@RequiredArgsConstructor
public class WebAuthController {

    private final UserLookup userLookup;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TemplateEngine templateEngine;

    // Show login page
    @GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String loginPage(@RequestParam(required = false) String error) {
        var output = new StringOutput();
        templateEngine.render("auth/login.jte", Map.of(
                "error", error != null ? "Invalid email or password" : ""
        ), output);
        return output.toString();
    }

    // Handle login form submission
    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String handleLogin(@RequestParam String email,
                              @RequestParam String password,
                              HttpServletResponse response) throws IOException {

        // Find user via UserLookup — returns UserAuthDto (includes password hash)
        var userOpt = userLookup.findAuthByEmail(email);
        if (userOpt.isEmpty()) {
            return "redirect:/web/login?error=true";
        }

        UserLookup.UserAuthDto user = userOpt.get();

        // Check password
        if (!passwordEncoder.matches(password, user.password())) {
            return "redirect:/web/login?error=true";
        }

        // Check active
        if (!user.active()) {
            return "redirect:/web/login?error=true";
        }

        // Generate JWT — pass individual fields, not entity
        String token = jwtService.generateToken(
                user.id(),
                user.email(),
                user.role()
        );

        // Store in secure cookie
        response.setHeader("Set-Cookie",
                "jwt=" + token +
                        "; HttpOnly" +
                        "; Path=/" +
                        "; Max-Age=86400" +
                        "; SameSite=Strict"
                // "; Secure"  ← uncomment when running on HTTPS!
        );

        // Redirect based on role
        return switch (user.role()) {
            case ADMIN, DIRECTOR             -> "redirect:/web/dashboard";
            case PRODUCER, RECRUITER, EDITOR -> "redirect:/web/projects";
            default                          -> "redirect:/web/home";
        };
    }
}