package com.haulmont.testtask.view.doctor;

import com.haulmont.testtask.model.doctor.Doctor;
import com.haulmont.testtask.model.doctor.DoctorDao;
import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import java.util.regex.Pattern;

public class DoctorWindow extends Window {
    private TextField name, surname, patronymic, specialization;
    private Binder<Doctor> binder;
    private Button okButton, cancelButton;
    private DoctorsListViewImpl doctorsList;
    private CreateUpdate mode;

    public DoctorWindow(DoctorsListViewImpl doctorsList, CreateUpdate mode) {
        this.doctorsList = doctorsList;
        this.mode = mode;
        createComponents();
        createListeners();
    }

    private void createComponents() {
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
            Doctor doctor = doctorsList.getSelectedTableItem();
            name.setValue(doctor.getName());
            surname.setValue(doctor.getSurname());
            patronymic.setValue(doctor.getPatronymic());
            specialization.setValue(doctor.getSpecialization());
        }

        okButton = new Button("OK");

        cancelButton = new Button("OK");

        content.addComponents(name, surname, patronymic, specialization,
                okButton, cancelButton);
        this.setContent(content);
    }

    private void createListeners() {
        okButton.addClickListener(e -> {
            if (binder.isValid()) {
                Doctor doctor = new Doctor(name.getValue(), surname.getValue(),
                        patronymic.getValue(), specialization.getValue());
                DoctorDao doctorDao = new DoctorDao();
                if (mode == CreateUpdate.CREATE) {
                    doctorDao.save(doctor);
                    doctorsList.addDoctor(doctor);
                } else {
                    Doctor doctorToUpd = doctorsList.getSelectedTableItem();
                    doctorDao.update(doctorToUpd, doctor);
                    doctorToUpd.setName(doctor.getName());
                    doctorToUpd.setSurname(doctor.getSurname());
                    doctorToUpd.setPatronymic(doctor.getPatronymic());
                    doctorToUpd.setSpecialization(doctor.getSpecialization());
                }
                doctorsList.refreshTable();
                close();
            }
        });

        cancelButton.addClickListener(e -> close());
    }
}
