package demo.codeexample.auth;

import demo.codeexample.auth.application.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthService authService;

    public LoginResponse login(LoginRequest request) {
        return authService.login(request);
    }

    public void changePassword(ChangePasswordRequest request, String authHeader) {
        authService.changePassword(request, authHeader);
    }
}