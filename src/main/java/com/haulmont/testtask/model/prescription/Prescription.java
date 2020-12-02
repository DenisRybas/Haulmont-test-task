package com.haulmont.testtask.model.prescription;

import com.haulmont.testtask.model.doctor.Doctor;
import com.haulmont.testtask.model.patient.Patient;
import com.haulmont.testtask.view.prescription.PrescriptionPriority;

import java.sql.Date;

/**
 * Сущность рецепта
 */
public class Prescription {

    /**
     * уникальный идентификатор рецепта
     */
    private Long id;

    /**
     * Описание рецепта
     */
    private String description;

    /**
     * Пациент, для которого написан рецепт
     *
     * @see Patient
     */
    private Patient patient;

    /**
     * Доктор, написавший рецепт
     *
     * @see Doctor
     */
    private Doctor doctor;

    /**
     * Дата начала срока действия рецепта
     */
    private Date dateCreated;

    /**
     * Дата окончания срока действия рецепта
     */
    private Date expirationDate;

    /**
     * Приоритет рецепта
     *
     * @see PrescriptionPriority
     */
    private PrescriptionPriority priority;

    /**
     * Конструктор для создания рецепта с заданными параметрами
     *
     * @param description    - описание рецепта
     * @param patient        - пациент, для которого написан рецепт
     * @param doctor         - доктор, который написал рецепт
     * @param dateCreated    - дата начала срока действия рецепта
     * @param expirationDate - дата окончания срока действия рецепта
     * @param priority       - приоритет рецепта
     */
    public Prescription(String description, Patient patient, Doctor doctor,
                        Date dateCreated, Date expirationDate,
                        PrescriptionPriority priority) {
        this.description = description;
        this.patient = patient;
        this.doctor = doctor;
        this.dateCreated = dateCreated;
        this.expirationDate = expirationDate;
        this.priority = priority;
    }

    /**
     * Пустой конструктор для создания рецепта
     */
    public Prescription() {
    }

    /**
     * Устанавливает приоритет рецепта
     *
     * @param priority - проритет рецепта
     */
    public void setPriority(PrescriptionPriority priority) {
        this.priority = priority;
    }

    /**
     * Устанавливает уникальный идентификатор рецепта
     *
     * @param id - уникальный идентификатор рецепта
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Устанавливает описание рецепта
     *
     * @param description - описание рецепта
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Устанавливает пациента, для которого написан рецепт
     *
     * @param patient - пациент, для которого написан рецепт
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    /**
     * Устанавливает доктора, который написал рецепт
     *
     * @param doctor - доктор, который написал рецепт
     */
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    /**
     * Устанавливает дату начала срока действия рецепта
     *
     * @param dateCreated - дата начала срока действия рецепта
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Устанавливает дату окончания срока действия рецепта
     *
     * @param expirationDate - дата окончания срока действия рецепта
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * @return уникальный идентификатор рецепта
     */
    public Long getId() {
        return id;
    }

    /**
     * @return описание рецепта
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return пациент, для которого написан рецепт
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * @return доктор, который написал рецепт
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * @return дата начала срока действия рецепта
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @return дата окончания срока действия рецепта
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * @return приоритет рецепта
     */
    public PrescriptionPriority getPriority() {
        return priority;
    }

    /**
     * @return возвращает представление сущности рецепта в виде строки
     */
    @Override
    public String toString() {
        return "Prescription [id=" + id + ", description=" + description + ", " +
                patient.toString() + ", " + doctor.toString() +
                ", date created=" + dateCreated.toString() +
                ", expiration date=" + expirationDate.toString() +
                ", priority=" + priority.toString() + "]";
    }
}
