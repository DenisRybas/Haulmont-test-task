package com.haulmont.testtask.view.patient;

import com.haulmont.testtask.view.doctor.CreateUpdate;
import com.haulmont.testtask.model.patient.PatientDao;
import com.haulmont.testtask.model.patient.Patient;
import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import java.util.regex.Pattern;

public class PatientWindow extends Window {
    private TextField name, surname, patronymic, phoneNumber;
    private Binder<Patient> binder;
    private Button okButton, cancelButton;
    private PatientsListViewImpl patientsList;
    private CreateUpdate mode;

    public PatientWindow(PatientsListViewImpl patientsList, CreateUpdate mode) {
        this.patientsList = patientsList;
        this.mode = mode;
        createComponents();
        createListeners();
    }

    private void createComponents() {
        HorizontalLayout content = new HorizontalLayout();
        binder = new Binder<>(Patient.class);

        name = new TextField("Name");
        binder.forField(name).asRequired("patient must have a name")
                .withValidator(name -> name.length() < 30,
                        "Name must have less than 30 characters")
                .withValidator(name -> Pattern.matches("[a-zA-Z]+",
                        this.name.getValue()),
                        "Name must contain only letters")
                .bind(Patient::getName, Patient::setName);

        surname = new TextField("Surname");
        binder.forField(surname).asRequired("patient must have a surname")
                .withValidator(surname -> surname.length() < 30,
                        "Surname must have less than 30 characters")
                .withValidator(surname -> Pattern.matches("[a-zA-Z]+",
                        this.surname.getValue()),
                        "Surname must contain only letters")
                .bind(Patient::getSurname, Patient::setSurname);

        patronymic = new TextField("Patronymic");
        binder.forField(patronymic)
                .withValidator(patronymic -> patronymic.length() < 30,
                        "Patronymic must have less than 30 characters")
                .withValidator(patronymic -> Pattern.matches("[a-zA-Z]+",
                        this.patronymic.getValue()),
                        "Patronymic must contain only letters")
                .bind(Patient::getPatronymic, Patient::setPatronymic);

        phoneNumber = new TextField("Phone number");
        binder.forField(phoneNumber).asRequired("patient must have a phone number")
                .withValidator(phoneNumber -> phoneNumber.length() < 30,
                        "Phone number must have less than 30 characters")
                .withValidator(phoneNumber -> !Pattern.matches("[a-zA-Z]+",
                        this.phoneNumber.getValue()),
                        "Phone number must contain only numbers")
                .bind(Patient::getPhoneNumber, Patient::setPhoneNumber);

        if (mode == CreateUpdate.UPDATE) {
            Patient patient = patientsList.getSelectedTableItem();
            name.setValue(patient.getName());
            surname.setValue(patient.getSurname());
            patronymic.setValue(patient.getPatronymic());
            phoneNumber.setValue(patient.getPhoneNumber());
        }

        cancelButton = new Button("Cancel");

        okButton = new Button("OK");

        content.addComponents(name, surname, patronymic, phoneNumber,
                okButton, cancelButton);
        this.setContent(content);
    }

    private void createListeners() {
        cancelButton.addClickListener(event -> close());
        okButton.addClickListener(event -> {
            if (binder.isValid()) {
                Patient patient = new Patient(name.getValue(),
                        surname.getValue(),
                        patronymic.getValue(), phoneNumber.getValue());
                PatientDao patientDao = new PatientDao();
                if (mode == CreateUpdate.CREATE) {
                    patientDao.save(patient);
                    patientsList.addPatient(patient);
                } else {
                    Patient patientToUpd = patientsList
                            .getSelectedTableItem();
                    patientDao.update(patientToUpd, patient);
                    patientToUpd.setName(patient.getName());
                    patientToUpd.setSurname(patient.getSurname());
                    patientToUpd.setPatronymic(patient.getPatronymic());
                    patientToUpd.setPhoneNumber(patient.getPhoneNumber());
                }
                patientsList.refreshTable();
                close();
            }
        });
    }
}
