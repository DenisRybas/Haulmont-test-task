package com.haulmont.testtask.view.patient;

import com.haulmont.testtask.model.patient.Patient;
import com.haulmont.testtask.model.patient.PatientDao;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PatientsListViewImpl extends Window
        implements PatientsListView, Button.ClickListener {
    private List<PatientsListViewListener> listeners;
    private GridLayout layout;
    private Grid<Patient> table;
    private List<Patient> patients;
    private Map<PatientListButtons, Button> buttons;

    public PatientsListViewImpl() {
        createComponents();
        createListeners();
    }

    private void createComponents() {
        layout = new GridLayout();
        table = new Grid<>();
        buttons = new LinkedHashMap<>();
        layout.setSizeFull();
        table.setSizeFull();
        table.setHeight(800, Unit.PIXELS);
        this.setModal(true);
        this.setContent(layout);
        this.setSizeFull();
        table.addColumn(Patient::getId).setCaption("Id");
        table.addColumn(Patient::getName).setCaption("Name");
        table.addColumn(Patient::getSurname).setCaption("Surname");
        table.addColumn(Patient::getPatronymic).setCaption("Patronymic");
        table.addColumn(Patient::getPhoneNumber).setCaption("Phone number");

        PatientDao patientDao = new PatientDao();
        patients = patientDao.getAll();
        ListDataProvider<Patient> dataProvider = DataProvider.ofCollection(patients);
        table.setDataProvider(dataProvider);
        table.setItems(patients);

        layout.addComponent(table);
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        for (PatientListButtons name : PatientListButtons.values()) {
            Button button = new Button(name.toString(), this);
            button.setData(name);
            buttons.put(name, button);
            if (name == PatientListButtons.REMOVE
                    || name == PatientListButtons.UPDATE) {
                button.setEnabled(false);
            }
            buttonsLayout.addComponent(button);
        }

        layout.addComponent(buttonsLayout);
        layout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_CENTER);
    }

    private void createListeners() {
        listeners = new ArrayList<>();
        table.addSelectionListener(event -> {
            if (getSelectedTableItem() != null) {
                buttons.get(PatientListButtons.REMOVE).setEnabled(true);
                buttons.get(PatientListButtons.UPDATE).setEnabled(true);
            } else {
                buttons.get(PatientListButtons.REMOVE).setEnabled(false);
                buttons.get(PatientListButtons.UPDATE).setEnabled(false);
            }
        });
    }

    public void refreshTable() {
        table.getDataProvider().refreshAll();
    }

    public Patient getSelectedTableItem() {
        return table.asSingleSelect().getValue();
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void removePatient(Patient patient) {
        patients.remove(patient);
    }

    @Override
    public void addListener(PatientsListViewListener listener) {
        listeners.add(listener);
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        for (PatientsListViewListener listener : listeners)
            listener.buttonClick((PatientListButtons) clickEvent.getButton()
                    .getData());
    }
}
