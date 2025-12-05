package com.medi.flow.service.user;

import com.medi.flow.entity.user.User;
import com.medi.flow.entity.user.UserAuthority;
import com.medi.flow.repository.user.UserAuthorityRepository;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserAuthorityService {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthorityService.class);

    public final UserAuthorityRepository userAuthorityRepository;

    public UserAuthorityService(final UserAuthorityRepository userAuthorityRepository) {
        this.userAuthorityRepository = userAuthorityRepository;
    }

    public void saveAllAndFlush(@NotNull final User user, @NotNull final List<UserAuthority> roles) {

        logger.info("created() -> {}, {}", user, roles);

        roles.stream()
                .filter(Objects::nonNull)
                .forEach(role -> {
                            role.setUser(user);

                            save(role);
                        }
                );

    }

    public void save(@NotNull final UserAuthority role) {

        logger.info("save() -> {}", role);

        userAuthorityRepository.save(role);

    }
}
