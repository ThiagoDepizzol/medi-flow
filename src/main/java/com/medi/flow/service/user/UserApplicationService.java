package com.medi.flow.service.user;

import com.medi.flow.entity.administrative.Module;
import com.medi.flow.entity.user.User;
import com.medi.flow.entity.user.UserAuthority;
import com.medi.flow.repository.administrative.ModuleRepository;
import com.medi.flow.repository.user.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashSet;

@Service
public class UserApplicationService {

    public final UserService userService;

    public final UserRepository userRepository;

    public final PasswordEncoder encoder;

    public final ModuleRepository moduleRepository;

    @Lazy
    public UserApplicationService(final UserService userService, final UserRepository userRepository, final PasswordEncoder encoder, final ModuleRepository moduleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.moduleRepository = moduleRepository;
    }

    public User loadAuthorities(String email) {
        return userRepository.findByEmailWithAuthorities(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    public void newSystemAdminConfiguration() {
        if (userRepository.findByEmailAndActiveTrue("admin@fiap.com.br").isEmpty()) {

            final Module adminModule = moduleRepository.findOneSystemAdmin()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot find system admin module"));

            final User admin = new User();

            admin.setActive(true);
            admin.setActivated(true);
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setEmail("admin@fiap.com.br");
            admin.setPassword(encoder.encode("admin"));
            admin.setLastModifiedDate(Instant.now());

            admin.setRoles(new HashSet<>());

            final UserAuthority newAuthority = new UserAuthority();

            newAuthority.setActive(true);
            newAuthority.setModule(adminModule);

            admin.getRoles().add(newAuthority);

            userService.created(admin);
        }
    }


}
