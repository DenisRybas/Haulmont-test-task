package com.haulmont.testtask.model.doctor;

/**
 * Сущность доктора
 */
public class Doctor {

    /**
     * Уникальный идентификатор доктора
     */
    private Long id;

    /**
     * Имя доктора
     */
    private String name;

    /**
     * Фамилия доктора
     */
    private String surname;

    /**
     * Отчество доктора
     */
    private String patronymic;

    /**
     * Специализация доктора
     */
    private String specialization;

    /**
     * Конструктор для создания доктора с заданными параметрами
     *
     * @param name           - имя
     * @param surname        - фамилия
     * @param patronymic     - отчество
     * @param specialization - специализация
     */
    public Doctor(String name, String surname, String patronymic,
                  String specialization) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.specialization = specialization;
    }

    /**
     * Пустой конструктор для создания доктора
     */
    public Doctor() {
    }

    /**
     * Устанавливает уникальный идентификатор доктора
     *
     * @param id - уникальный идентификатор доктора
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Устанавливает имя доктора
     *
     * @param name - имя доктора
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Устанавливает фамилию доктора
     *
     * @param surname - фамилия доктора
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Устанавливает отчество доктора
     *
     * @param patronymic - отчество доктора
     */
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    /**
     * Устанавливает специализацию доктора
     *
     * @param specialization - специализация доктора
     */
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    /**
     * @return возвращает уникальный иденцификтор доктора
     */
    public Long getId() {
        return id;
    }

    /**
     * @return возвращает имя доктора
     */
    public String getName() {
        return name;
    }

    /**
     * @return возвращает фамилию доктора
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @return возвращает отчество доктора
     */
    public String getPatronymic() {
        return patronymic;
    }

    /**
     * @return возвращает специализацию доктора
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * @return возвращает представление сущности доктора в виде строки
     */
    @Override
    public String toString() {
        return "Doctor {id=" + id + ", name=" + name + ", surname=" +
                surname + ", patronymic = " + patronymic +
                ", specialization=" + specialization + "}";
    }
}
