package demo.codeexample.auth.application;

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
public class AuthService {

    private final UserAuthPort userAuthPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        UserAuthDto user = userAuthPort
                .findAuthByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        if (!user.isActive()) {                        // ← isActive() not active()
            throw new UnauthorizedException("Account is deactivated");
        }

        String token = jwtService.generateToken(
                user.getId(),                          // ← getId() not id()
                user.getEmail(),                       // ← getEmail() not email()
                user.getRole()                         // ← getRole() not role()
        );

        return new LoginResponse(
                token,
                user.getRole(),                        // ← getRole()
                user.isPasswordResetRequired()         // ← isPasswordResetRequired()
        );
    }

    public void changePassword(ChangePasswordRequest request, String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Invalid authorization header");
        }

        String token = authHeader.substring(7);

        if (!jwtService.isTokenValid(token)) {
            throw new UnauthorizedException("Invalid or expired token");
        }

        String email = jwtService.extractEmail(token);

        UserAuthDto user = userAuthPort                // ← UserAuthDto not UserAuthPort.UserAuthDto
                .findAuthByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {                 // ← getPassword() not password()
            throw new UnauthorizedException("Current password is incorrect");
        }

        if (request.getCurrentPassword().equals(request.getNewPassword())) {
            throw new UnauthorizedException(
                    "New password must be different from current password");
        }

        userAuthPort.updatePassword(
                email,
                passwordEncoder.encode(request.getNewPassword())
        );
    }
}