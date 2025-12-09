package com.medi.flow.controller.user;

import com.medi.flow.dto.user.UserDTO;
import com.medi.flow.entity.user.User;
import com.medi.flow.mapper.user.UserMapper;
import com.medi.flow.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("usr/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final UserMapper userMapper;

    public UserController(final UserService userService, final UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<UserDTO> created(@RequestBody final User user) {

        log.info("POST -> usr/users -> {}", user);

        return ResponseEntity.ok(userService.created(user)
                .map(userMapper::fromDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot created user")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable("id") final Long id, @RequestBody final User user) {

        log.info("PUT -> usr/users/{id} -> {}, {}", id, user);

        return ResponseEntity.ok(userService.findById(id)
                .flatMap(bdUser -> userService.update(bdUser, user)
                        .map(userMapper::fromDto))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot update user")));
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<UserDTO>> findAll(final Pageable page) {

        log.info("GET -> /usr/users -> {}", page);

        return ResponseEntity.ok(userService.findAll(page)
                .stream()
                .map(userMapper::fromDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<UserDTO> findById(@PathVariable("id") final Long id) {

        log.info("GET -> /usr/users/{id} -> {} ", id);

        return ResponseEntity.ok(userService.findById(id)
                .map(userMapper::fromDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") final Long id) {

        log.info("DELETE -> usr/users/{id} -> {}", id);

        userService.findById(id)
                .ifPresent(userService::delete);

        return ResponseEntity.noContent()
                .build();
    }
}
