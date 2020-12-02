package com.haulmont.testtask.view.doctor;

import com.haulmont.testtask.model.doctor.Doctor;
import com.haulmont.testtask.model.doctor.DoctorDao;
import com.haulmont.testtask.view.CreateUpdate;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;

import java.util.regex.Pattern;

/**
 * Окно создания/изменения доктора
 *
 * @see Doctor
 */
public class DoctorWindow extends Window {

    /**
     * TextField для имени доктора
     */
    private TextField name;

    /**
     * TextField для фамилии доктора
     */
    private TextField surname;

    /**
     * TextField для отчества доктора
     */
    private TextField patronymic;

    /**
     * TextField для специализации доктора
     */
    private TextField specialization;

    /**
     * Binder для доктора, определяет валидность введённых данных
     */
    private Binder<Doctor> binder;

    /**
     * Кнопка ОК
     */
    private Button okButton;

    /**
     * Кнопка отмены
     */
    private Button cancelButton;

    /**
     * View списка докторов
     *
     * @see DoctorsListViewImpl
     */
    private DoctorsListViewImpl doctorsListView;

    /**
     * Режим создания/изменения доктора
     *
     * @see CreateUpdate
     */
    private CreateUpdate mode;

    /**
     * Конструктор для создания окна создания/изменения доктора
     * Вызывает методы создания компонентов окна
     * и создания Listener'ов для этих компонентов
     *
     * @param doctorsListView - view списка докторов
     * @param mode            - режим создания/изменения доктора
     */
    public DoctorWindow(DoctorsListViewImpl doctorsListView, CreateUpdate mode) {
        this.doctorsListView = doctorsListView;
        this.mode = mode;
        createComponents();
        createListeners();
    }

    /**
     * Создаёт компоненты для окна создания/изменения доктора
     */
    private void createComponents() {
        this.setResizable(false);
        HorizontalLayout content = new HorizontalLayout();
        binder = new Binder<>(Doctor.class);

        name = new TextField("Name");
        name.setRequiredIndicatorVisible(true);
        binder.forField(name).asRequired("Doctor must have a name")
                .withValidator(name -> name.length() < 30,
                        "Name must have less than 30 characters")
                .bind(Doctor::getName, Doctor::setName);

        surname = new TextField("Surname");
        surname.setRequiredIndicatorVisible(true);
        binder.forField(surname).asRequired("Doctor must have a surname")
                .withValidator(surname -> surname.length() < 30,
                        "Surname must have less than 30 characters")
                .bind(Doctor::getSurname, Doctor::setSurname);

        patronymic = new TextField("Patronymic");
        patronymic.setRequiredIndicatorVisible(true);
        binder.forField(patronymic)
                .withValidator(patronymic -> patronymic.length() < 30,
                        "Patronymic must have less than 30 characters")
                .bind(Doctor::getPatronymic, Doctor::setPatronymic);

        specialization = new TextField("Specialization");
        specialization.setRequiredIndicatorVisible(true);
        binder.forField(specialization)
                .asRequired("Doctor must have a specialization")
                .withValidator(specialization -> specialization.length() < 30,
                        "Specialization must have less than 30 characters")
                .withValidator(name -> Pattern.matches("[a-zA-Z]+",
                        this.specialization.getValue()),
                        "Specialization must have only letters")
                .bind(Doctor::getSpecialization, Doctor::setSpecialization);

        if (mode == CreateUpdate.UPDATE) {
            Doctor doctor = doctorsListView.getSelectedTableItem();
            name.setValue(doctor.getName());
            surname.setValue(doctor.getSurname());
            patronymic.setValue(doctor.getPatronymic());
            specialization.setValue(doctor.getSpecialization());
        }

        okButton = new Button("OK");

        cancelButton = new Button("Cancel");

        content.addComponents(name, surname, patronymic, specialization,
                okButton, cancelButton);
        content.setComponentAlignment(cancelButton, Alignment.BOTTOM_CENTER);
        content.setComponentAlignment(okButton, Alignment.BOTTOM_CENTER);
        this.setContent(content);
    }

    /**
     * Создаёт Listener'ы для окна создания/изменения доктора
     */
    private void createListeners() {
        okButton.addClickListener(e -> {
            if (binder.isValid()) {
                Doctor doctor = new Doctor(name.getValue(), surname.getValue(),
                        patronymic.getValue(), specialization.getValue());
                DoctorDao doctorDao = new DoctorDao();
                if (mode == CreateUpdate.CREATE) {
                    doctorDao.save(doctor);
                    doctorsListView.addDoctor(doctor);
                } else {
                    Doctor doctorToUpd = doctorsListView.getSelectedTableItem();
                    doctorDao.update(doctorToUpd, doctor);
                    doctorToUpd.setName(doctor.getName());
                    doctorToUpd.setSurname(doctor.getSurname());
                    doctorToUpd.setPatronymic(doctor.getPatronymic());
                    doctorToUpd.setSpecialization(doctor.getSpecialization());
                }
                doctorsListView.refreshTable();
                close();
            }
        });

        cancelButton.addClickListener(e -> close());
    }
}
