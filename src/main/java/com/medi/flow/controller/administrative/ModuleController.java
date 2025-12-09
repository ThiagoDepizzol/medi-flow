package com.medi.flow.controller.administrative;

import com.medi.flow.dto.administrative.ModuleDTO;
import com.medi.flow.entity.administrative.Module;
import com.medi.flow.mapper.administrative.ModuleMapper;
import com.medi.flow.service.administrative.ModuleService;
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
@RequestMapping("adm/modules")
public class ModuleController {

    private static final Logger log = LoggerFactory.getLogger(ModuleController.class);

    private final ModuleService moduleService;

    private final ModuleMapper moduleMapper;

    public ModuleController(final ModuleService moduleService, final ModuleMapper moduleMapper) {
        this.moduleService = moduleService;
        this.moduleMapper = moduleMapper;
    }

    @PostMapping
    public ResponseEntity<ModuleDTO> created(@RequestBody final Module module) {

        log.info("POST -> usr/modules -> {}", module);

        return ResponseEntity.ok(moduleService.created(module)
                .map(moduleMapper::fromDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot created module")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModuleDTO> update(@PathVariable("id") final Long id, @RequestBody final Module module) {

        log.info("PUT -> usr/modules/{id} -> {}, {}", id, module);

        return ResponseEntity.ok(moduleService.update(id, module)
                .map(moduleMapper::fromDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot update module")));
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<ModuleDTO>> findAll(final Pageable page) {

        log.info("GET -> /usr/modules -> {}", page);

        return ResponseEntity.ok(moduleService.findAll(page)
                .stream()
                .map(moduleMapper::fromDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<ModuleDTO> findById(@PathVariable("id") final Long id) {

        log.info("GET -> /usr/modules/{id} -> {} ", id);

        return ResponseEntity.ok(moduleService.findById(id)
                .map(moduleMapper::fromDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") final Long id) {

        log.info("DELETE -> usr/modules/{id} -> {}", id);

        moduleService.findById(id)
                .ifPresent(moduleService::delete);

        return ResponseEntity.noContent()
                .build();
    }
}
