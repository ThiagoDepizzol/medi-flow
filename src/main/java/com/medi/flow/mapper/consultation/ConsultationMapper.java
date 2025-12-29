package com.medi.flow.mapper.consultation;

import com.medi.flow.dto.consultation.ConsultationDTO;
import com.medi.flow.entity.consultation.Consultation;
import com.medi.flow.entity.user.User;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ConsultationMapper {

    private static final Logger log = LoggerFactory.getLogger(ConsultationMapper.class);


    public ConsultationDTO fromDto(@NotNull final Consultation consultation) {

        log.info("fromDto() -> {}", consultation);

        final ConsultationDTO newDto = new ConsultationDTO();

        newDto.setId(consultation.getId());
        newDto.setDoctorName(Optional.ofNullable(consultation.getDoctor())
                .map(User::getUsername)
                .orElse(""));
        newDto.setPatientName(Optional.ofNullable(consultation.getPatient())
                .map(User::getUsername)
                .orElse(""));
        newDto.setConsultationDate(consultation.getConsultationDate());
        newDto.setStartTime(consultation.getStartTime());
        newDto.setEndTime(consultation.getEndTime());

        return newDto;
    }
}
