package demo.codeexample.auth;

import demo.codeexample.exceptions.UnauthorizedException;
import demo.codeexample.security.JwtService;
import demo.codeexample.user.domain.User;
import demo.codeexample.user.domain.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


// The login endpoint


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
            // ↑ now the global handler catches this and returns ErrorResponse
        }

        if (!user.isActive()) {
            throw new UnauthorizedException("Account is deactivated");
        }

        String token = jwtService.generateToken(user);

        // ← now includes passwordResetRequired
        return ResponseEntity.ok(
                new LoginResponse(token, user.getRole(), user.isPasswordResetRequired())
        );
    }


    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            @RequestHeader("Authorization") String authHeader) {

        // Extract email from their token
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        // Load user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(),
                user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Current password is incorrect");
        }

        // Validate new password
        if (request.getNewPassword().length() < 8) {
            return ResponseEntity.badRequest()
                    .body("Password must be at least 8 characters");
        }

        // Make sure new password is different
        if (request.getCurrentPassword().equals(request.getNewPassword())) {
            return ResponseEntity.badRequest()
                    .body("New password must be different from current password");
        }

        // Update password and clear the reset flag
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetRequired(false);  // ← they've reset, flag cleared!
        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully");
    }

}