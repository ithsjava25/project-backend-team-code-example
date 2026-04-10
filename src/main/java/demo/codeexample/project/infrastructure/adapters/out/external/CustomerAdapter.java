package demo.codeexample.project.infrastructure.adapters.out.external;

import demo.codeexample.user.Role;
import demo.codeexample.project.application.out.UserPort;
import demo.codeexample.user.UserLookup;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static demo.codeexample.user.Role.PRODUCER;

@Component
public class CustomerAdapter implements UserPort {

    private final UserLookup userLookup;

    public CustomerAdapter(UserLookup userLookup){
        this.userLookup = userLookup;
    }

    @Override
    public Optional<UserInfo> findById(Long id) {
        return userLookup.findById(id)
                .map(dto -> new UserInfo(
                        dto.getId(), dto.getFirstName(), dto.getLastName(), dto.getRole()));
    }

    @Override
    public List<UserInfo> findByRole(Role role) {
        return userLookup.findByRole(role).stream()
                .map(dto -> new UserInfo(dto.getId(), dto.getFirstName(), dto.getLastName(), dto.getRole()))
                .toList();
    }

    @Override
    public boolean validateProducer(Long id) {
        return userLookup.validateUserRole(id, PRODUCER);
    }

//    @Override
//    public List<UserDto> findAll() {
//        return userLookup.findAll();
//    }

    @Override
    public List<UserInfo> findAll() {
        return userLookup.findAll().stream()
                .map(dto -> new UserInfo(
                        dto.getId(),
                        dto.getFirstName(),
                        dto.getLastName(),
                        dto.getRole()
                ))
                .toList();
    }

}
