package com.haulmont.testtask.model.patient;

public class Patient {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String phoneNumber;

    public Patient(String name, String surname, String patronymic,
                   String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
    }

    public Patient() {
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

    public void setPatronymic(String patronymic) {this.patronymic = patronymic;}

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
