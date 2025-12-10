package com.medi.flow.service.consultation;

import com.medi.flow.dto.consultation.ConsultationEventDTO;
import com.medi.flow.entity.consultation.Consultation;
import com.medi.flow.entity.user.User;
import com.medi.flow.repository.consultation.ConsultationRepository;
import com.medi.flow.service.consultation.producer.ConsultationProducer;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Optional;

@Service
public class ConsultationService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationService.class);

    public final ConsultationRepository consultationRepository;

    public final ConsultationProducer consultationProducer;

    public ConsultationService(final ConsultationRepository consultationRepository, final ConsultationProducer consultationProducer) {
        this.consultationRepository = consultationRepository;
        this.consultationProducer = consultationProducer;
    }

    public Optional<Consultation> created(@NotNull final Consultation consultation) {

        logger.info("created() -> {}", consultation);

        return Optional.of(consultationRepository.save(consultation))
                .map(savedConsultation -> {

                    emitConsultationCreatedEvent(savedConsultation);

                    return savedConsultation;
                });

    }

    public Optional<Consultation> update(@NotNull final Long id, @NotNull final Consultation consultation) {

        logger.info("update() -> {}, {}", id, consultation);

        return Optional.of(consultationRepository.save(consultation));

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
    public Page<Consultation> getAllByPatient(@NotNull final Long patientId, @NotNull final Pageable pageable) {

        logger.info("getAllByPatient() -> {}, {}", pageable, patientId);

        return consultationRepository.getAllByPatient(patientId, pageable);
    }

    public void emitConsultationCreatedEvent(@NotNull final Consultation consultation) {

        logger.info("emitConsultationCreatedEvent() -> {}", consultation);

        final ConsultationEventDTO newDto = new ConsultationEventDTO();

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

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                consultationProducer.send(newDto);
            }
        });

    }
}
