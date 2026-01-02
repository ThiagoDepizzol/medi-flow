package com.medi.flow.service.consultation;

import com.medi.flow.dto.consultation.ConsultationEventDTO;
import com.medi.flow.entity.consultation.Consultation;
import com.medi.flow.entity.user.User;
import com.medi.flow.repository.consultation.ConsultationRepository;
import com.medi.flow.service.consultation.producer.ConsultationProducer;
import com.medi.flow.utils.consultation.ConsultationValidationService;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultationService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationService.class);

    private final ConsultationValidationService consultationValidationService;

    public final ConsultationRepository consultationRepository;

    public final ConsultationProducer consultationProducer;

    public ConsultationService(final ConsultationValidationService consultationValidationService, final ConsultationRepository consultationRepository, final ConsultationProducer consultationProducer) {
        this.consultationValidationService = consultationValidationService;
        this.consultationRepository = consultationRepository;
        this.consultationProducer = consultationProducer;
    }

    @Transactional
    public Optional<Consultation> created(@NotNull final Consultation consultation) {

        logger.info("created() -> {}", consultation);

        consultationValidationService.userIsDoctor(consultation.getDoctor());

        consultationValidationService.userIsPatient(consultation.getPatient());

        return Optional.of(consultationRepository.save(consultation))
                .map(createdConsultation -> {

                    emitConsultationCreatedEvent(createdConsultation, true);

                    return createdConsultation;
                });

    }

    public Optional<Consultation> update(@NotNull final Long id, @NotNull final Consultation consultation) {

        logger.info("update() -> {}, {}", id, consultation);

        consultationValidationService.userIsDoctor(consultation.getDoctor());

        consultationValidationService.userIsPatient(consultation.getPatient());

        return Optional.of(consultationRepository.save(consultation))
                .map(updatedConsultation -> {

                    emitConsultationCreatedEvent(updatedConsultation, false);

                    return updatedConsultation;

                });

    }

    @Transactional(readOnly = true)
    public Page<Consultation> findAll(@NotNull final Pageable pageable) {

        logger.info("findAll() -> {}", pageable);

        return consultationRepository.findAllByActiveTrue(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Consultation> findById(@NotNull final Long id) {

        logger.info("findById() -> {}", id);

        return consultationRepository.findOneActiveTrueById(id);
    }


    public void delete(@NotNull final Consultation consultation) {

        logger.info("delete() -> {}", consultation);

        consultation.setActive(false);

        consultationRepository.save(consultation);

    }

    @Transactional(readOnly = true)
    public List<Consultation> getAllHistoryByPatient(@NotNull final Long patientId, final Boolean onlyFuture) {

        logger.info("getAllHistoryByPatient() -> {}, {}", patientId, onlyFuture);

        final Instant currentDate = Instant.now();

        return consultationRepository.getAllHistoryByPatient(patientId, onlyFuture, currentDate);
    }

    @Transactional(readOnly = true)
    public Page<Consultation> getAllByPatient(@NotNull final Long patientId, @NotNull final Pageable pageable) {

        logger.info("getAllByPatient() -> {}, {}", pageable, patientId);

        return consultationRepository.getAllByPatient(patientId, pageable);
    }

    public void emitConsultationCreatedEvent(@NotNull final Consultation consultation, @NotNull final Boolean isNew) {

        logger.info("emitConsultationCreatedEvent() -> {}, {}", consultation, isNew);

        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        final ZoneId zone = ZoneId.of("America/Sao_Paulo");

        final LocalDate consultationDate = Optional.ofNullable(consultation.getConsultationDate())
                .map(date -> date.atZone(zone))
                .map(ZonedDateTime::toLocalDate)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Consultation date not found"));

        final ConsultationEventDTO newDto = new ConsultationEventDTO();

        newDto.setId(consultation.getId());

        newDto.setDoctorName(Optional.ofNullable(consultation.getDoctor())
                .map(User::getUsername)
                .orElse(""));

        newDto.setPatientName(Optional.ofNullable(consultation.getPatient())
                .map(User::getUsername)
                .orElse(""));

        newDto.setConsultationDate(consultationDate.format(dateFormatter));
        newDto.setStartTime(consultation.getStartTime().format(timeFormatter));
        newDto.setEndTime(consultation.getEndTime().format(timeFormatter));

        newDto.setPatientEmail(Optional.ofNullable(consultation.getPatient())
                .map(User::getEmail)
                .orElse(""));

        newDto.setIsNew(isNew);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                consultationProducer.send(newDto);
            }
        });

    }
}
