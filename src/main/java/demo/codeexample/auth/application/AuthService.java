package demo.codeexample.auth.application;

import demo.codeexample.auth.AuthLookup;
import demo.codeexample.auth.ChangePasswordRequest;
import demo.codeexample.auth.LoginRequest;
import demo.codeexample.auth.LoginResponse;
import demo.codeexample.exceptions.UnauthorizedException;
import demo.codeexample.exceptions.UserNotFoundException;
import demo.codeexample.security.JwtService;
import demo.codeexample.user.UserAuthDto;
import demo.codeexample.user.UserAuthPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthLookup {

    private final UserAuthPort userAuthPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    // ─────────────────────────────────────────
    // PUBLIC API (AuthLookup implementation)
    // ─────────────────────────────────────────


    @Override
    public LoginResponse getLoginResponse(LoginRequest request) {
        String normalizedEmail = normalizeEmail(request.getEmail());
        UserAuthDto user       = findAndValidateUser(normalizedEmail,
                request.getPassword());
        String token           = generateToken(user);
        return buildLoginResponse(token, user);
    }

    @Override
    public void changePassword(ChangePasswordRequest request,
                               String authHeader) {
        String email = extractEmailFromHeader(authHeader);
        UserAuthDto user = findUserByEmail(email);
        validatePasswordChange(request, user);
        saveNewPassword(email, request.getNewPassword());
    }

    // ─────────────────────────────────────────
    // PRIVATE HELPERS — each does ONE thing
    // ─────────────────────────────────────────

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new UserNotFoundException("Invalid credentials");
        }
        return email.trim().toLowerCase();
    }

    private UserAuthDto findAndValidateUser(String email, String password) {
        UserAuthDto user = userAuthPort
                .findAuthByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!user.isActive()) {
            throw new UnauthorizedException("Account is deactivated");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        return user;
    }

    private String generateToken(UserAuthDto user) {
        return jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }

    private LoginResponse buildLoginResponse(String token, UserAuthDto user) {
        return new LoginResponse(
                token,
                user.getRole(),
                user.isPasswordResetRequired()
        );
    }

    private String extractEmailFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Invalid authorization header");
        }
        String token = authHeader.substring(7);
        if (!jwtService.isTokenValid(token)) {
            throw new UnauthorizedException("Invalid or expired token");
        }
        return jwtService.extractEmail(token);
    }

    private UserAuthDto findUserByEmail(String email) {
        return userAuthPort
                .findAuthByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    private void validatePasswordChange(ChangePasswordRequest request,
                                        UserAuthDto user) {
        if (request.getCurrentPassword() ==null || request.getCurrentPassword().isBlank()) {
            throw new UnauthorizedException("Current password is required");
        }
        if (request.getNewPassword() ==null || request.getNewPassword().isBlank()) {
            throw new UnauthorizedException("New password cannot be blank");
        }
        if (!passwordEncoder.matches(request.getCurrentPassword(),
                user.getPassword())) {
            throw new UnauthorizedException("Current password is incorrect");
        }
        if (request.getCurrentPassword().equals(request.getNewPassword())) {
            throw new UnauthorizedException(
                    "New password must be different from current password");
        }
    }

    private void saveNewPassword(String email, String newPassword) {
        userAuthPort.updatePassword(
                email,
                passwordEncoder.encode(newPassword)
        );
    }
}