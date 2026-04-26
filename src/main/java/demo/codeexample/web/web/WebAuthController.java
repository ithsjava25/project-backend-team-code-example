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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class WebAuthController {

    private final WebAuthService webAuthService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String redirect,
                            Model model) {
        model.addAttribute("error", error != null ? "Invalid email or password" : "");
        model.addAttribute("redirect", redirect);
        return "auth/login";
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String handleLogin(@ModelAttribute LoginRequest request,
                              @RequestParam(required = false) String redirect,
                              HttpServletResponse response) {

        WebAuthService.LoginResult result = webAuthService.handleLogin(request);

        if (result.success()) {
            response.addHeader("Set-Cookie", result.cookie());
            return safeRedirect(redirect, result.redirect());
        }
        return result.redirect();
    }

    private String safeRedirect(String redirect, String fallback) {
        if (redirect == null || redirect.isBlank()) {
            return fallback;
        }

        if (!redirect.startsWith("/") || redirect.startsWith("//")) {
            return fallback;
        }

        return "redirect:" + redirect;
    }

    @GetMapping("/login/change-password")
    @ResponseBody
    public String changePasswordPage(@RequestParam(required = false) String error,
                                     @RequestParam(required = false) String success) {

        return "auth/change-password";
    }

    @PostMapping(value = "/login/change-password", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String handleChangePassword(@RequestParam String currentPassword,
                                       @RequestParam String newPassword,
                                       @RequestParam String confirmPassword,
                                       @CookieValue(name = "jwt", required = false) String jwtToken) {

        return webAuthService.handleChangePassword(currentPassword, newPassword, confirmPassword, jwtToken);
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        response.setHeader(
                "Set-Cookie",
                "jwt=; HttpOnly; Path=/; Max-Age=0; SameSite=Strict"
        );

        return "redirect:/" + TenantContext.getTenant() + "";
    }
}