package com.medi.flow.service.consultation;

import com.medi.flow.entity.consultation.Consultation;
import com.medi.flow.repository.consultation.ConsultationRepository;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ConsultationService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationService.class);

    public final ConsultationRepository consultationRepository;

    public ConsultationService(final ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    public Optional<Consultation> created(@NotNull final Consultation consultation) {

        logger.info("created() -> {}", consultation);

        return Optional.of(consultationRepository.save(consultation));

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
}
