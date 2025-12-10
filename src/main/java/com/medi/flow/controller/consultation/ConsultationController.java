package com.medi.flow.controller.consultation;

import com.medi.flow.dto.consultation.ConsultationDTO;
import com.medi.flow.entity.consultation.Consultation;
import com.medi.flow.mapper.consultation.ConsultationMapper;
import com.medi.flow.service.consultation.ConsultationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("med/consultations")
public class ConsultationController {

    private static final Logger log = LoggerFactory.getLogger(ConsultationController.class);

    private final ConsultationService consultationService;

    private final ConsultationMapper consultationMapper;

    public ConsultationController(final ConsultationService consultationService, final ConsultationMapper consultationMapper) {
        this.consultationService = consultationService;
        this.consultationMapper = consultationMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN', 'MED_NURSE')")
    public ResponseEntity<ConsultationDTO> created(@RequestBody final Consultation consultation) {

        log.info("POST -> med/consultations -> {}", consultation);

        return ResponseEntity.ok(consultationService.created(consultation)
                .map(consultationMapper::fromDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot created consultation")));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN','MED_DOCTOR')")
    public ResponseEntity<ConsultationDTO> update(@PathVariable final Long id, @RequestBody final Consultation consultation) {

        log.info("PUT -> med/consultations/{id} -> {}, {}", id, consultation);

        return ResponseEntity.ok(consultationService.update(id, consultation)
                .map(consultationMapper::fromDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot update consultation")));
    }

    @GetMapping
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN','MED_DOCTOR', 'MED_NURSE')")
    public ResponseEntity<List<ConsultationDTO>> findAll(final Pageable page) {

        log.info("GET -> /med/consultations -> {}", page);

        return ResponseEntity.ok(consultationService.findAll(page)
                .stream()
                .map(consultationMapper::fromDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN','MED_DOCTOR', 'MED_NURSE', 'MED_PATIENT')")
    public ResponseEntity<ConsultationDTO> findById(@PathVariable final Long id) {

        log.info("GET -> /med/consultations/{id} -> {} ", id);

        return ResponseEntity.ok(consultationService.findById(id)
                .map(consultationMapper::fromDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultation not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final Long id) {

        log.info("DELETE -> med/consultations/{id} -> {}", id);

        consultationService.findById(id)
                .ifPresent(consultationService::delete);

        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/all-by-patient/{patientId}")
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN', 'MED_DOCTOR', 'MED_NURSE', 'MED_PATIENT')")
    public ResponseEntity<List<ConsultationDTO>> getAllByPatient(@PathVariable final Long patientId, final Pageable page) {

        log.info("GET -> /med/consultations/all-by-patient/{id} -> {}, {}", patientId, page);

        return ResponseEntity.ok(consultationService.getAllByPatient(patientId, page)
                .stream()
                .map(consultationMapper::fromDto)
                .collect(Collectors.toList()));
    }
}
