package com.medi.flow.controller.consultation;

import com.medi.flow.dto.consultation.ConsultationDTO;
import com.medi.flow.mapper.consultation.ConsultationMapper;
import com.medi.flow.service.consultation.ConsultationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ConsultationHistoryController {

    private static final Logger log = LoggerFactory.getLogger(ConsultationHistoryController.class);

    private final ConsultationService consultationService;

    private final ConsultationMapper consultationMapper;

    public ConsultationHistoryController(final ConsultationService consultationService, final ConsultationMapper consultationMapper) {
        this.consultationService = consultationService;
        this.consultationMapper = consultationMapper;
    }

    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','MED_DOCTOR', 'MED_NURSE', 'MED_PATIENT')")
    @QueryMapping(name = "getAllHistoryByPatient")
    public List<ConsultationDTO> getAllHistoryByPatient(@Argument Long patientId, @Argument Boolean onlyFuture) {

        log.info("medicalHistory -> {}, {}", patientId, onlyFuture);

        return consultationService.getAllHistoryByPatient(patientId, onlyFuture)
                .stream()
                .map(consultationMapper::fromDto)
                .collect(Collectors.toList());
    }

}
