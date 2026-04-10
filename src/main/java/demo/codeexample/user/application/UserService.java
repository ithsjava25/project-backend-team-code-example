package demo.codeexample.user.application;

import demo.codeexample.user.domain.Role;
import demo.codeexample.exceptions.UserNotFoundException;
import demo.codeexample.user.CreateUserRequestDTO;
import demo.codeexample.user.UserDto;
import demo.codeexample.user.UserLookup;
import demo.codeexample.user.domain.Role;
import demo.codeexample.user.domain.User;
import demo.codeexample.user.domain.UserRepository;
import demo.codeexample.user.infrastructure.EmailService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@RequiredArgsConstructor
public class UserService implements UserLookup {

    private final UserRepository repository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // ─────────────────────────────────────────
    // READ OPERATIONS
    // ─────────────────────────────────────────

    @Override
    public Optional<UserDto> findByFullName(String firstname, String lastName) {
        return repository.findByFirstNameAndLastNameIgnoreCase(firstname, lastName)
                .map(entity -> mapper.map(entity, UserDto.class));
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(entity -> mapper.map(entity, UserDto.class));
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return repository.findById(id)
                .map(entity -> mapper.map(entity, UserDto.class));
    }

    @Override
    public Optional<UserDto> findByFullName(String firstname, String lastName) {
        return repository.findByFirstNameAndLastNameIgnoreCase(firstname, lastName)
                .map(entity -> mapper.map(entity, UserDto.class));
    }

    /**
     * Checks that each employee in set has unique role. Starts by converting id -> UserDto in order to see if user exists.
     * Continues with mapping each user to their role.
     * @param employeesId
     * @return true if each user has unique role
     */
    @Override
    public boolean validateUniqueRoles(Set<Long> employeesId) {
        List<Role> foundRoles = employeesId.stream()
                .map(id -> findById(id).orElseThrow(() -> new UserNotFoundException(id)))
                .map(UserDto::role)
                .toList();

        List<Role> requiredRoles = List.of(Role.PRODUCER, Role.DIRECTOR, Role.EDITOR, Role.RECRUITER);
        return new HashSet<>(foundRoles).containsAll(requiredRoles);
    }

    @Override
    public List<UserDto> findAll() {
        return repository.findAll().stream()
                .map(entity -> mapper.map(entity, UserDto.class))
                .toList();
    }

    @Override
    public List<UserDto> findByRole(Role role) {
        return repository.findAllByRoleIgnoreCase(role).stream()
                .map(entity -> mapper.map(entity, UserDto.class))
                .toList();
    }
}
