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

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//
//        // 1. Find user by email
//        UserEntity user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
//        // Note: we say "Invalid credentials" not "Email not found"
//        // Never tell attackers which part was wrong!
//
//        // 2. Check password
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Invalid credentials");
//        }
//
//        /* Why say "Invalid credentials" for both wrong email AND wrong password?
//        This is called not leaking information. If you say "email not found", an attacker knows valid emails.
//        If you say "wrong password", they know the email exists. Vague errors protect your users.*/
//
//        // 3. Check user is active
//        if (!user.isActive()) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body("Account is deactivated");
//        }
//
//        // 4. Generate token
//        String token = jwtService.generateToken(user);
//
//        // 5. Return token to frontend
//        return ResponseEntity.ok(new LoginResponse(token, user.getRole()));
//    }

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
        System.out.println("SUCCESS: token generated");

        return ResponseEntity.ok(new LoginResponse(token, user.getRole()));
    }

}
