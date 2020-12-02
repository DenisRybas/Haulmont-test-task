package com.haulmont.testtask.model.patient;

/**
 * Сущность пациента
 */
public class Patient {

    /**
     * Уникальный идентификатор пациента
     */
    private Long id;

    /**
     * Имя пациента
     */
    private String name;

    /**
     * Фамилия пациента
     */
    private String surname;

    /**
     * Отчество пациента
     */
    private String patronymic;

    /**
     * Номер телефолна пациента
     */
    private String phoneNumber;

    /**
     * Конструктор для создания пацента с заданными параметрами
     *
     * @param name        - имя
     * @param surname     - фамилия
     * @param patronymic  - отчество
     * @param phoneNumber - номер телефона
     */
    public Patient(String name, String surname, String patronymic,
                   String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Пустой конструктор для создания пациента
     */
    public Patient() {
    }

    /**
     * Устанавливает уникальный идентификатор пациента
     *
     * @param id - уникальный идентификатор пациента
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Устанавливает имя пациента
     *
     * @param name - имя пациента
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Устанавливает фамилию пациента
     *
     * @param surname - фамилия пациента
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Устанавливает отчество пациента
     *
     * @param patronymic - отчество пациента
     */
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    /**
     * Устанавливает номер телефона пацента
     *
     * @param phoneNumber - номер телефона пациента
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return возвращает уникальный иденцификтор пациента
     */
    public Long getId() {
        return id;
    }

    /**
     * @return возвращает имя пациента
     */
    public String getName() {
        return name;
    }

    /**
     * @return возвращает фамилию пациента
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @return возвращает отчество пациента
     */
    public String getPatronymic() {
        return patronymic;
    }

    /**
     * @return возвращает номер телефона пациента
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return возвращает представление сущности пациента в виде строки
     */
    @Override
    public String toString() {
        return "Patient {id=" + id + ", name=" + name + ", surname=" +
                surname + ", patronymic = " + patronymic +
                ", phone number=" + phoneNumber + "}";
    }
}
