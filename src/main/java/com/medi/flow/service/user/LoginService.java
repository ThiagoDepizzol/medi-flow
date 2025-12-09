package com.medi.flow.service.user;

import com.medi.flow.dto.login.ResetPasswordDTO;
import com.medi.flow.entity.user.User;
import com.medi.flow.repository.user.UserRepository;
import com.medi.flow.utils.exceptions.InvalidPasswordException;
import com.medi.flow.utils.exceptions.UserNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    public final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public LoginService(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void resetPassword(@NotNull final ResetPasswordDTO dto) {

        logger.info("resetPassword -> {}", dto);

        final User user = userRepository.findByEmailAndActiveTrue(dto.getLogin())
                .orElseThrow(() -> {

                    logger.info("Email incorreto");

                    return new UserNotFoundException("Usuário ou senha inválidos");

                });

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {

            logger.info("Senha antiga incorreta");

            throw new InvalidPasswordException("Usuário ou senha inválidos");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        userRepository.save(user);

    }

    public void adminResetPassword(@NotNull final ResetPasswordDTO dto) {

        logger.info("adminResetPassword -> {}", dto);

        final User user = userRepository.findByEmailAndActiveTrue(dto.getLogin())
                .orElseThrow(() -> {

                    logger.info("Email incorreto");

                    return new UserNotFoundException("Usuário ou senha inválidos");

                });

        final String encodedPassword = passwordEncoder.encode(dto.getNewPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);

    }

}
