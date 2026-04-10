package demo.codeexample.user.web;

import demo.codeexample.exceptions.UserNotFoundException;
import demo.codeexample.user.CreateUserRequestDTO;
import demo.codeexample.user.UserDto;
import demo.codeexample.user.application.UserService;
import demo.codeexample.user.domain.Role;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestBody CreateUserRequestDTO request) {
        UserDto created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
        // ↑ was getAllUsers()
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR') or #id == authentication.principal")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(
                userService.findById(id)
                        .orElseThrow(() -> new UserNotFoundException(id))
        );
        // ↑ was getUserById() — now findById() returns Optional, unwrap with orElseThrow
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateRole(@PathVariable Long id,
                                              @RequestBody Role newRole) {
        return ResponseEntity.ok(userService.updateRole(id, newRole));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }
}