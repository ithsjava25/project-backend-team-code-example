package demo.codeexample.user.web;

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

    /* Why @Valid on the request?
    This triggers the validation annotations in UserRequest.
    Without it, @NotBlank and @Email are ignored completely. */

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequestDTO request) {
        UserResponse created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR') or #id == authentication.principal")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /* Why @PatchMapping for role update instead of @PutMapping?
    PUT means replace the entire resource. PATCH means update part of it.
    You're only changing the role — PATCH is semantically correct.*/

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateRole(@PathVariable Long id,
                                                   @RequestBody Role newRole) {
        return ResponseEntity.ok(userService.updateRole(id, newRole));
    }

    /* Why ResponseEntity.noContent().build() for deactivate?
    HTTP 204 No Content is the correct response
    when an action succeeds but there's nothing meaningful to return.
    Using 200 OK with an empty body is technically wrong.*/

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build(); // 204 — success, nothing to return
    }
}
