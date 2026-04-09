package demo.codeexample.project.infrastructure.adapters.out.external;

import demo.codeexample.enums.Role;
import demo.codeexample.project.application.out.UserPort;
import demo.codeexample.user.UserLookup;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static demo.codeexample.enums.Role.PRODUCER;

@Component
public class CustomerAdapter implements UserPort {

    private final UserLookup userLookup;

    public CustomerAdapter(UserLookup userLookup){
        this.userLookup = userLookup;
    }

    @Override
    public Optional<UserInfo> findById(Long id) {
        return userLookup.findById(id)
                .map(dto -> new UserInfo(dto.id(), dto.firstName(), dto.lastName(), dto.role()));
    }

    @Override
    public List<UserInfo> findByRole(Role role) {
        return userLookup.findByRole(role).stream()
                .map(dto -> new UserInfo(dto.id(), dto.firstName(), dto.lastName(), dto.role()))
                .toList();
    }

    @Override
    public boolean validateProducer(Long id) {
        return userLookup.validateUserRole(id, PRODUCER);
    }

    @Override
    public List<UserLookup.UserDto> findAll() {
        return userLookup.findAll();
    }

}
