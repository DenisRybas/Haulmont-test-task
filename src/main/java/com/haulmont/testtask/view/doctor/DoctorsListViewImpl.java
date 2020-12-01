package com.haulmont.testtask.view.doctor;

import com.haulmont.testtask.model.doctor.Doctor;
import com.haulmont.testtask.model.doctor.DoctorDao;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DoctorsListViewImpl extends Window implements DoctorsListView, Button.ClickListener {
    private List<DoctorsListViewListener> listeners;
    private VerticalLayout layout;
    private Grid<Doctor> table;
    private List<Doctor> doctors;
    private Map<DoctorListButtons, Button> buttons;

    public DoctorsListViewImpl() {
        createComponents();
        createListeners();
    }

    private void createComponents() {
        layout = new VerticalLayout();
        table = new Grid<>();
        buttons = new LinkedHashMap<>();
        layout.setSizeFull();
        table.setHeight(800, Unit.PIXELS);
        table.setWidthFull();
        this.setModal(true);
        this.setContent(layout);
        this.setSizeFull();
        DoctorDao doctorDao = new DoctorDao();
        table.addColumn(Doctor::getId).setCaption("Id");
        table.addColumn(Doctor::getName).setCaption("Name");
        table.addColumn(Doctor::getSurname).setCaption("Surname");
        table.addColumn(Doctor::getPatronymic).setCaption("Patronymic");
        table.addColumn(Doctor::getSpecialization).setCaption("Specialization");

        doctors = doctorDao.getAll();
        ListDataProvider<Doctor> dataProvider = DataProvider.ofCollection(doctors);
        table.setDataProvider(dataProvider);
        table.setItems(doctors);
        layout.addComponent(table);
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        for (DoctorListButtons name : DoctorListButtons.values()) {
            Button button = new Button(name.toString(), this);
            button.setData(name);
            buttons.put(name, button);
            if (name == DoctorListButtons.REMOVE
                    || name == DoctorListButtons.UPDATE) {
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
                buttons.get(DoctorListButtons.UPDATE).setEnabled(true);
                buttons.get(DoctorListButtons.REMOVE).setEnabled(true);
            } else {
                buttons.get(DoctorListButtons.UPDATE).setEnabled(false);
                buttons.get(DoctorListButtons.REMOVE).setEnabled(false);
            }
        });
    }
    public Doctor getSelectedTableItem() {
        return table.asSingleSelect().getValue();
    }

    public void refreshTable() {
        table.getDataProvider().refreshAll();
    }

    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
    }

    public void removeDoctor(Doctor doctor) {
        doctors.remove(doctor);
    }

    @Override
    public void addListener(DoctorsListViewListener listener) {
        listeners.add(listener);
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        for (DoctorsListViewListener listener : listeners)
            listener.buttonClick((DoctorListButtons) clickEvent.getButton().getData());
    }
}
