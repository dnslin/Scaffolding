package in.dnsl.service;

import in.dnsl.model.entity.Permission;
import in.dnsl.model.entity.User;
import in.dnsl.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final UserRepository userRepository;

    public Set<Permission> getPermissionsByUserId(Long userId) {
        return userRepository.findById(userId)
                .map(User::getRoles)
                .orElse(Collections.emptySet())
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .collect(Collectors.toSet());
    }

}
