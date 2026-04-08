package demo.codeexample.user;

import demo.codeexample.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;

/* This is what you send back to the frontend.
Critically — no password, not even the hash. */


@Data
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private boolean isActive;
    private LocalDateTime createdAt;


    /* Why a static factory method fromEntity()?
    It keeps the mapping logic inside UserResponse itself —
    the class knows how to build itself from an entity.
    Clean and easy to find.
    The alternative is writing this mapping in the service, which clutters it.*/


    // Constructor mapping from entity
    public static UserResponse fromEntity(UserEntity entity) {
        UserResponse response = new UserResponse();
        response.id = entity.getId();
        response.firstName = entity.getFirstName();
        response.lastName = entity.getLastName();
        response.email = entity.getEmail();
        response.role = entity.getRole();
        response.isActive = entity.isActive();
        response.createdAt = entity.getCreatedAt();
        return response;
    }
}
