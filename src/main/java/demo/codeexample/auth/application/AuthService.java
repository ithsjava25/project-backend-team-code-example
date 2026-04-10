package demo.codeexample.auth.application;

import demo.codeexample.auth.ChangePasswordRequest;
import demo.codeexample.auth.LoginRequest;
import demo.codeexample.auth.LoginResponse;
import demo.codeexample.exceptions.UnauthorizedException;
import demo.codeexample.exceptions.UserNotFoundException;
import demo.codeexample.security.JwtService;
import demo.codeexample.user.UserLookup;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserLookup userLookup;          // ← no UserRepository! ✅
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        // Find user via UserLookup — not UserRepository!
        UserLookup.UserAuthDto user = userLookup
                .findAuthByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.password())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        // Check active
        if (!user.active()) {
            throw new UnauthorizedException("Account is deactivated");
        }

        // Generate token with individual fields — not User entity!
        String token = jwtService.generateToken(
                user.id(),
                user.email(),
                user.role()
        );

        return new LoginResponse(token, user.role(), user.passwordResetRequired());
    }

    public void changePassword(ChangePasswordRequest request, String authHeader) {

        // Extract email from token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Invalid authorization header");
        }
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        // Find user via UserLookup
        UserLookup.UserAuthDto user = userLookup
                .findAuthByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.password())) {
            throw new UnauthorizedException("Current password is incorrect");
        }

        // Make sure new password is different
        if (request.getCurrentPassword().equals(request.getNewPassword())) {
            throw new UnauthorizedException(
                    "New password must be different from current password");
        }

        // Update via UserLookup — no direct repo access!
        userLookup.updatePassword(
                email,
                passwordEncoder.encode(request.getNewPassword())
        );
    }
}