package demo.codeexample.auth;

import demo.codeexample.user.CreateUserRequestDTO;
import demo.codeexample.user.LoginRequest;
import demo.codeexample.user.UserDTO;
import demo.codeexample.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth") // All URLs start with this
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // 1. REGISTRATION
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody CreateUserRequestDTO requestDTO) {
        // We call the service logic you just wrote
        UserDTO newUser = userService.registerUser(requestDTO);

        // Return 201 Created and the safe UserDTO
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // 2. LOGIN (Simple version for week 1)
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        // You will eventually add logic here to check credentials
        // and return a "Token" or a Success message

        // 1. We will eventually call userService.login(loginRequest) here
        // 2. For now, it just returns a success message
        return ResponseEntity.ok("Login successful!");
    }
}
