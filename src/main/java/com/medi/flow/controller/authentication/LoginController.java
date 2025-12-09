package com.medi.flow.controller.authentication;

import com.medi.flow.dto.login.LoginDTO;
import com.medi.flow.dto.login.ResetPasswordDTO;
import com.medi.flow.service.user.LoginService;
import com.medi.flow.utils.MessageResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;

    private final AuthenticationManager authenticationManager;

    public LoginController(final LoginService loginService, final AuthenticationManager authenticationManager) {
        this.loginService = loginService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody final LoginDTO dto) {

        log.info("POST -> /login -> {} ", dto);

        final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword());

        try {

            final Authentication authentication = authenticationManager.authenticate(loginToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok(new MessageResponseDTO("Usuário logado com sucesso"));

        } catch (AuthenticationException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponseDTO("Usuário ou senha inválidos"));

        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody final ResetPasswordDTO dto) {

        log.info("POST -> /reset-password -> {} ", dto);

        loginService.resetPassword(dto);

        return ResponseEntity.ok(new MessageResponseDTO("Senha alterada com sucesso"));
    }

    @PostMapping("/admin-reset-password")
    @PreAuthorize("hasAuthority('SYSTEM_ADMIN')")
    public ResponseEntity<?> adminResetPassword(@RequestBody final ResetPasswordDTO dto) {

        log.info("POST -> /admin-reset-password -> {} ", dto);

        loginService.adminResetPassword(dto);

        return ResponseEntity.ok(new MessageResponseDTO("Senha alterada com sucesso"));
    }
}
