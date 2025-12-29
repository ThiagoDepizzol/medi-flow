package com.medi.flow.utils.consultation;

import com.medi.flow.entity.user.User;
import com.medi.flow.enumerated.ModuleType;
import com.medi.flow.repository.user.UserRepository;
import com.medi.flow.utils.exceptions.UserIsNotDoctorException;
import com.medi.flow.utils.exceptions.UserIsNotPatientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ConsultationValidationService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationValidationService.class);

    private final UserRepository userRepository;

    public ConsultationValidationService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void userIsDoctor(User doctor) {

        logger.debug("userIsDoctor() -> {}", doctor);

        final String doctorAuthority = ModuleType.MED_DOCTOR.getName();

        Optional.ofNullable(doctor.getId())
                .map(doctorId -> userRepository.findByIdAndAuthority(doctorId, doctorAuthority)
                        .orElseThrow(() -> new UserIsNotDoctorException("Médico informado não pertence ao quadro")))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot find user id"));
    }

    public void userIsPatient(User patient) {

        logger.debug("userIsPatient() -> {}", patient);

        final String patientAuthority = ModuleType.MED_PATIENT.getName();

        Optional.ofNullable(patient.getId())
                .map(patientId -> userRepository.findByIdAndAuthority(patientId, patientAuthority)
                        .orElseThrow(() -> new UserIsNotPatientException("Paciente informado não está registrado")))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot find user id"));
    }
}
