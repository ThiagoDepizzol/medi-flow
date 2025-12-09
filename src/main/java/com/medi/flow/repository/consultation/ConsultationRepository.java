package com.medi.flow.repository.consultation;

import com.medi.flow.entity.consultation.Consultation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    Page<Consultation> findAllByActiveTrue(Pageable pageable);

    @Query(nativeQuery = true,
            value = "select consultations.* " +//
                    "from med_consultations consultations " +//
                    "where consultations.active = true " +//
                    "  and consultations.id = :consultationId ")
    Optional<Consultation> findOneActiveTrueById(@Param("consultationId") Long consultationId);

    @Query(nativeQuery = true,
            value = "select consultations.* " +//
                    "from med_consultations consultations " +//
                    "         join usr_users patients on consultations.patient_usr_user_id = patients.id " +//
                    "where consultations.active = true " +//
                    "  and patients.active = true " +//
                    "  and patients.id = :patientId")
    Page<Consultation> getAllByPatient(@Param("patientId") Long patientId, Pageable pageable);
}
