package com.medi.flow.dto.consultation;

import java.time.Instant;
import java.time.LocalTime;

public class ConsultationDTO {

    private Long id;

    private String doctorName;

    private String patientName;

    private Instant consultationDate;

    private LocalTime startTime;

    private LocalTime endTime;

    public ConsultationDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Instant getConsultationDate() {
        return consultationDate;
    }

    public void setConsultationDate(Instant consultationDate) {
        this.consultationDate = consultationDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
