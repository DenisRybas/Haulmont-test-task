package com.haulmont.testtask.view.prescription;

import com.haulmont.testtask.model.doctor.Doctor;
import com.haulmont.testtask.model.doctor.DoctorDao;
import com.haulmont.testtask.model.patient.Patient;
import com.haulmont.testtask.model.patient.PatientDao;
import com.haulmont.testtask.model.prescription.Prescription;
import com.haulmont.testtask.model.prescription.PrescriptionDao;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PrescriptionWindow extends Window {
    private TextField description;
    private ComboBox<Long> patientComboBox;
    private ComboBox<Long> doctorComboBox;
    private ComboBox<PrescriptionPriority> priorityComboBox;
    private DateField dateCreated, expirationDate;
    private Binder<Prescription> binder;
    private Button okButton, cancelButton;
    private PrescriptionsListViewImpl prescriptionsList;
    private CreateUpdate mode;
    private PatientDao patientDao;
    private DoctorDao doctorDao;

    public PrescriptionWindow(PrescriptionsListViewImpl prescriptionsList, CreateUpdate mode) {
        this.prescriptionsList = prescriptionsList;
        this.mode = mode;
        patientDao = new PatientDao();
        doctorDao = new DoctorDao();
        createComponents();
        createListeners();
    }

    private void createComponents() {
        HorizontalLayout content = new HorizontalLayout();
        binder = new Binder<>(Prescription.class);
        description = new TextField("Description");
        binder.forField(description).asRequired("Prescription must have a description")
                .withValidator(description -> description.length() < 100,
                        "Description must have less than 100 characters")
                .bind(Prescription::getDescription, Prescription::setDescription);

        List<Long> patientIds = patientDao.getAll().stream().
                map(Patient::getId).collect(Collectors.toList());
        patientComboBox = new ComboBox<>("Patient");
        patientComboBox.setItems(patientIds);
        binder.forField(patientComboBox).asRequired("Prescription must have a patient");
        patientComboBox.setTextInputAllowed(false);

        List<Long> doctorIds = doctorDao.getAll().stream().
                map(Doctor::getId).collect(Collectors.toList());
        doctorComboBox = new ComboBox<>("Doctor");
        doctorComboBox.setItems(doctorIds);
        binder.forField(doctorComboBox).asRequired("Prescription must have a doctor");
        doctorComboBox.setTextInputAllowed(false);

        priorityComboBox = new ComboBox<>();
        priorityComboBox.setTextInputAllowed(false);
        binder.forField(priorityComboBox).asRequired("Prescription must have a priority");
        priorityComboBox.setDataProvider(new ListDataProvider<>(Arrays.asList(PrescriptionPriority.values())));
        priorityComboBox.setCaption("Priority");

        dateCreated = new DateField();
        dateCreated.setCaption("Date created");
        dateCreated.setValue(LocalDate.now());
        binder.forField(dateCreated).asRequired("Prescription must have a creation date");

        expirationDate = new DateField();
        expirationDate.setCaption("Expiration date");
        expirationDate.setRangeStart(dateCreated.getValue());
        dateCreated.setRangeEnd(expirationDate.getValue());
        expirationDate.addValueChangeListener(event -> dateCreated.setRangeEnd(expirationDate.getValue()));
        dateCreated.addValueChangeListener(event -> expirationDate.setRangeStart(dateCreated.getValue()));

        if (mode == CreateUpdate.UPDATE) {
            Prescription selectedItem = prescriptionsList.getSelectedTableItem();
            description.setValue(selectedItem.getDescription());
            patientComboBox.setValue(selectedItem.getPatient().getId());
            doctorComboBox.setValue(selectedItem.getDoctor().getId());
            priorityComboBox.setValue(selectedItem.getPriority());
            dateCreated.setValue(selectedItem.getDateCreated().toLocalDate());
            expirationDate.setValue(selectedItem.getExpirationDate().toLocalDate());
        }

        okButton = new Button("OK");

        cancelButton = new Button("Cancel");

        content.addComponents(description, patientComboBox, doctorComboBox, priorityComboBox, dateCreated, expirationDate, okButton, cancelButton);
        this.setContent(content);
    }

    private void createListeners() {
        okButton.addClickListener(e -> {
            if (binder.isValid()) {
                Prescription prescription = new Prescription(description.getValue(),
                        patientDao.get(patientComboBox.getValue()),
                        doctorDao.get(doctorComboBox.getValue()),
                        java.sql.Date.valueOf(dateCreated.getValue()),
                        java.sql.Date.valueOf(expirationDate.getValue()),
                        priorityComboBox.getValue());
                PrescriptionDao prescriptionDao = new PrescriptionDao();
                if (mode == CreateUpdate.CREATE) {
                    prescriptionDao.save(prescription);
                    prescriptionsList.addPrescription(prescription);
                } else {
                    Prescription prescriptionToUpd = prescriptionsList.getSelectedTableItem();

                    prescriptionDao.update(prescriptionToUpd, prescription);
                    prescriptionToUpd.setDescription(prescription.getDescription());
                    prescriptionToUpd.setPatient(prescription.getPatient());
                    prescriptionToUpd.setDoctor(prescription.getDoctor());
                    prescriptionToUpd.setDateCreated(prescription.getDateCreated());
                    prescriptionToUpd.setExpirationDate(prescription.getExpirationDate());
                    prescriptionToUpd.setPriority(prescription.getPriority());
                }
                prescriptionsList.refreshTable();
                close();
            }
        });
        cancelButton.addClickListener(e -> close());
    }
}
