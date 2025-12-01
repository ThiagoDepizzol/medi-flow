package com.medi.flow.controller.authentication;

import com.medi.flow.dto.login.LoginDTO;
import com.medi.flow.dto.login.ResetPasswordDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody final LoginDTO dto) {

        log.info("POST -> /login -> {} ", dto);

        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody final ResetPasswordDTO dto) {

        log.info("POST -> /reset-password -> {} ", dto);

        return ResponseEntity.ok()
                .build();
    }
}
