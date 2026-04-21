package demo.codeexample.web.web;

import demo.codeexample.auth.LoginRequest;
import demo.codeexample.company.TenantContext;
import demo.codeexample.web.application.WebAuthService;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebAuthController {

    private final WebAuthService webAuthService;   // ← only service needed!
    private final TemplateEngine templateEngine;

    // ─────────────────────────────────────────
    // LOGIN
    // ─────────────────────────────────────────

    @GetMapping("/web/login")
    @ResponseBody
    public String loginPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String welcomeMessage) {

        String company = TenantContext.getTenant();
        String companyValue = company != null ? company : "";
        String welcomeMessageValue = welcomeMessage != null ? welcomeMessage : "";

        return render("auth/login.jte", Map.of(
                "company", companyValue,
                "welcomeMessage", welcomeMessageValue,
                "error", error != null ? "Invalid email or password" : ""
        ));
    }

    @PostMapping(
            value = "/web/login",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public String handleLogin(
            @ModelAttribute LoginRequest request,
            HttpServletResponse response) {

        WebAuthService.LoginResult result = webAuthService.handleLogin(request);
        if (result.success()) {
            response.addHeader("Set-Cookie", result.cookie());
        }
        return result.redirect();
    }

    // ─────────────────────────────────────────
    // CHANGE PASSWORD
    // ─────────────────────────────────────────

    @GetMapping("/web/change-password")
    @ResponseBody
    public String changePasswordPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String success) {

        String company = TenantContext.getTenant();
        String companyValue = company != null ? company : "";

        return render("auth/change-password.jte", Map.of(
                "company", companyValue,
                "error", error != null ? error : "",
                "success", success != null ? success : ""
        ));
    }

    @PostMapping(
            value = "/web/change-password",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public String handleChangePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            @CookieValue(name = "jwt", required = false) String jwtToken) {

        return webAuthService.handleChangePassword(
                currentPassword, newPassword, confirmPassword, jwtToken
        );
    }


    // ─────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────

    private String render(String template, Map<String, Object> params) {
        var output = new StringOutput();
        templateEngine.render(template, params, output);
        return output.toString();
    }
}