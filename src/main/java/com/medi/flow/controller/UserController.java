package com.medi.flow.controller;

import com.medi.flow.entity.user.User;
import com.medi.flow.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("usr/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> created(@RequestBody final User user) {

        log.info("POST -> usr/users -> {}", user);

        return ResponseEntity.ok(userService.created(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable("id") final Long id, @RequestBody final User user) {

        log.info("PUT -> usr/users/{id} -> {}, {}", id, user);

        return ResponseEntity.ok(userService.update(id, user));
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<User>> findAll(final Pageable page) {

        log.info("GET -> /usr/users -> {}", page);

        return ResponseEntity.ok(userService.findAll(page).getContent());
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<User> findById(@PathVariable("id") final Long id) {

        log.info("GET -> /usr/users/{id} -> {} ", id);

        return ResponseEntity.ok(userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }
}
