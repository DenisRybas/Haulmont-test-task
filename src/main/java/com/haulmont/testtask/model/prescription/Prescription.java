package com.haulmont.testtask.model.prescription;

import com.haulmont.testtask.model.doctor.Doctor;
import com.haulmont.testtask.model.patient.Patient;
import com.haulmont.testtask.view.prescription.PrescriptionPriority;

import java.sql.Date;


public class Prescription {
    private Long id; //TODO: Long
    private String description;
    private Patient patient;
    private Doctor doctor;
    private Date dateCreated;
    private Date expirationDate;
    private PrescriptionPriority priority;


    public Prescription(String description, Patient patient, Doctor doctor, Date dateCreated, Date expirationDate, PrescriptionPriority priority) {
        this.description = description;
        this.patient = patient;
        this.doctor = doctor;
        this.dateCreated = dateCreated;
        this.expirationDate = expirationDate;
        this.priority = priority;
    }

    public Prescription() {
    }

    public void setPriority(PrescriptionPriority priority) {
        this.priority = priority;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public PrescriptionPriority getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "Prescription [id=" + id + ", description=" + description + "]";
    }
}
