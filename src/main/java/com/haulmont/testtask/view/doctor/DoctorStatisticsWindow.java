package com.haulmont.testtask.view.doctor;

import com.haulmont.testtask.model.doctor.Doctor;
import com.haulmont.testtask.model.prescription.PrescriptionDao;

import com.vaadin.ui.*;

import java.util.*;

public class DoctorStatisticsWindow extends Window {
    private VerticalLayout layout;
    private Grid<Doctor> table;
    private Map<Doctor, Long> doctors;
    private Button ok;

    public DoctorStatisticsWindow() {
        createComponents();
        createListeners();
    }

    private void createComponents() {
        layout = new VerticalLayout();
        table = new Grid<>();
        ok = new Button("OK");
        layout.setSizeFull();
        table.setHeight(800, Unit.PIXELS);
        table.setWidthFull();
        this.setModal(true);
        this.setContent(layout);
        this.setSizeFull();
        PrescriptionDao prescriptionDao = new PrescriptionDao();
        doctors = prescriptionDao.getQuantityOfPrescriptions();
        table.addColumn(Doctor::getId).setCaption("Id");
        table.addColumn(Doctor::getName).setCaption("Name");
        table.addColumn(Doctor::getSurname).setCaption("Surname");
        table.addColumn(Doctor::getPatronymic).setCaption("Patronymic");
        table.addColumn(doctor -> doctors.get(doctor)).setCaption("Quantity of prescriptions");

        table.setItems(List.copyOf(doctors.keySet()));
        layout.addComponent(table);
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponent(ok);
        layout.addComponent(buttonsLayout);
        layout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_CENTER);
    }

    private void createListeners() {
        ok.addClickListener(event -> this.close());
    }
}
