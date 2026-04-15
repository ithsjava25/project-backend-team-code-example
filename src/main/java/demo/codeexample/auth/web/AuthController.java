package demo.codeexample.auth.web;

import demo.codeexample.auth.ChangePasswordRequest;
import demo.codeexample.auth.LoginRequest;
import demo.codeexample.auth.LoginResponse;
import demo.codeexample.auth.application.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authLookup;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authLookup.getLoginResponse(request));
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        authLookup.changePassword(request, authHeader);
        return ResponseEntity.ok("Password changed successfully");
    }
}