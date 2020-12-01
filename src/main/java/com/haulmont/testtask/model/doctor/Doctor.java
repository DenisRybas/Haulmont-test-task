package com.haulmont.testtask.model.doctor;

public class Doctor {

    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String specialization;

    public Doctor(String name, String surname, String patronymic,
                  String specialization) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.specialization = specialization;
    }

    public Doctor() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setSpecialization(String phoneNumber) {
        this.specialization = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getSpecialization() {
        return specialization;
    }

    @Override
    public String toString() {
        return "Patient [id=" + id + ", name=" + name + ", surname=" +
                surname + ", specialization=" + specialization + "]"; //TODO: TOSTRING
    }
}
