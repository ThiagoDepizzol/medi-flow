package com.medi.flow.service.user;

import com.medi.flow.entity.user.User;
import com.medi.flow.entity.user.UserAuthority;
import com.medi.flow.repository.user.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public final UserRepository userRepository;

    public final UserAuthorityService userAuthorityService;

    public UserService(final UserRepository userRepository, final UserAuthorityService userAuthorityService) {
        this.userRepository = userRepository;
        this.userAuthorityService = userAuthorityService;
    }

    public Optional<User> created(@NotNull final User user) {

        logger.info("created() -> {}", user);

        final List<UserAuthority> roles = Optional.ofNullable(user.getRoles())
                .map(ArrayList::new)
                .orElseGet(ArrayList::new);
        user.setRoles(null);

        return Optional.of(userRepository.save(user))
                .map(savedUser -> {

                    userAuthorityService.saveAllAndFlush(savedUser, roles);

                    return savedUser;
                });

    }

    public Optional<User> update(@NotNull final User oldUser, @NotNull final User newUser) {

        logger.info("update() -> {}, {}", oldUser, newUser);

        oldUser.setFirstName(newUser.getFirstName());
        oldUser.setLastName(newUser.getLastName());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setActive(newUser.getActive());

        final List<UserAuthority> roles = Optional.ofNullable(newUser.getRoles())
                .map(ArrayList::new)
                .orElseGet(ArrayList::new);
        oldUser.setRoles(null);

        return Optional.of(userRepository.save(oldUser))
                .map(savedUser -> {

                    userAuthorityService.saveAllAndFlush(savedUser, roles);

                    return savedUser;
                });

    }

    @Transactional(readOnly = true)
    public Page<User> findAll(@NotNull final Pageable pageable) {

        logger.info("findAll() -> {}", pageable);

        return userRepository.findAllByActiveTrue(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(@NotNull final Long id) {

        logger.info("findById() -> {}", id);

        return userRepository.findOneActiveTrueById(id);
    }


    public void delete(@NotNull final User user) {

        logger.info("delete() -> {}", user);

        user.setActive(false);

        userRepository.save(user);

    }
}
