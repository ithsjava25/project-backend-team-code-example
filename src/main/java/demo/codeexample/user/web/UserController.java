package demo.codeexample.user.web;

import demo.codeexample.exceptions.UserNotFoundException;
import demo.codeexample.user.CreateUserDto;
import demo.codeexample.user.UserDto;
import demo.codeexample.user.application.UserService;
import demo.codeexample.shared.Role;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestBody CreateUserDto request) {
        UserDto created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
        // ↑ was getAllUsers()
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(
                userService.findById(id)
                        .orElseThrow(() -> new UserNotFoundException(id))
        );
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<UserDto> updateRole(@PathVariable Long id,
                                              @RequestBody Role newRole) {
        return ResponseEntity.ok(userService.updateRole(id, newRole));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reset-password")
    public ResponseEntity<UserDto> resetPassword(@PathVariable Long id) {
        return ResponseEntity.ok(userService.resetPassword(id));
    }
}