package com.haulmont.testtask.view.patient;

import com.haulmont.testtask.view.CreateUpdate;
import com.haulmont.testtask.model.patient.PatientDao;
import com.haulmont.testtask.model.patient.Patient;
import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import java.util.regex.Pattern;

/**
 * Окно создания/изменения пациента
 *
 * @see Patient
 */
public class PatientWindow extends Window {

    /**
     * TextField для имени пациента
     */
    private TextField name;

    /**
     * TextField для фамилии пациента
     */
    private TextField surname;

    /**
     * TextField для отчества пациента
     */
    private TextField patronymic;

    /**
     * TextField для номера телефона пациента
     */
    private TextField phoneNumber;

    /**
     * Binder для пациента, определяет валидность введённых данных
     */
    private Binder<Patient> binder;

    /**
     * Кнопка ОК
     */
    private Button okButton;

    /**
     * Кнопка отмены
     */
    private Button cancelButton;

    /**
     * View списка пациентов
     *
     * @see PatientsListViewImpl
     */
    private PatientsListViewImpl patientsListView;

    /**
     * Режим создания/изменения пациента
     *
     * @see CreateUpdate
     */
    private CreateUpdate mode;

    /**
     * Конструктор для создания окна создания/изменения пациента
     * Вызывает методы создания компонентов окна
     * и создания Listener'ов для этих компонентов
     *
     * @param patientsListView - view списка пациентов
     * @param mode             - режим создания/изменения пациента
     */
    public PatientWindow(PatientsListViewImpl patientsListView, CreateUpdate mode) {
        this.patientsListView = patientsListView;
        this.mode = mode;
        createComponents();
        createListeners();
    }

    /**
     * Создаёт компоненты для окна создания/изменения пациента
     */
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
            Patient patient = patientsListView.getSelectedTableItem();
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

    /**
     * Создаёт Listener'ы для окна создания/изменения пациента
     */
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
                    patientsListView.addPatient(patient);
                } else {
                    Patient patientToUpd = patientsListView
                            .getSelectedTableItem();
                    patientDao.update(patientToUpd, patient);
                    patientToUpd.setName(patient.getName());
                    patientToUpd.setSurname(patient.getSurname());
                    patientToUpd.setPatronymic(patient.getPatronymic());
                    patientToUpd.setPhoneNumber(patient.getPhoneNumber());
                }
                patientsListView.refreshTable();
                close();
            }
        });
    }
}
