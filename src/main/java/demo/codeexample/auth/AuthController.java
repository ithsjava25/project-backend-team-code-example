package demo.codeexample.auth;

import demo.codeexample.security.JwtService;
import demo.codeexample.user.UserEntity;
import demo.codeexample.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Email received: " + request.getEmail());

        // 1. Find user by email
        Optional<UserEntity> userOptional = userRepository.findByEmail(request.getEmail());

        System.out.println("User found: " + userOptional.isPresent());

        if (userOptional.isEmpty()) {
            System.out.println("FAILED: email not found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        UserEntity user = userOptional.get();
        System.out.println("User role: " + user.getRole());
        System.out.println("User active: " + user.isActive());

        // 2. Check password
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        System.out.println("Password matches: " + passwordMatches);

        if (!passwordMatches) {
            System.out.println("FAILED: wrong password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        if (!user.isActive()) {
            System.out.println("FAILED: user inactive");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account is deactivated");
        }

        String token = jwtService.generateToken(user);

        // Tell frontend if password is required

        return ResponseEntity.ok(new LoginResponse(token, user.getRole(), user.isPasswordResetRequired()));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            @RequestHeader("Authorization") String authHeader) {

        // Extract email from their token
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify they know their current password
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

        // Update password and clear the reset flag
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetRequired(false);  // ← they've reset, flag cleared!
        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully");
    }

}
