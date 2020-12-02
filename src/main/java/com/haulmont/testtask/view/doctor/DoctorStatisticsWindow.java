package com.haulmont.testtask.view.doctor;

import com.haulmont.testtask.model.doctor.Doctor;
import com.haulmont.testtask.model.prescription.PrescriptionDao;
import com.vaadin.ui.*;

import java.util.*;

/**
 * Таблица с докторами и количеством выписанных ими рецептов
 *
 * @see Doctor
 * @see com.haulmont.testtask.model.prescription.Prescription
 */
public class DoctorStatisticsWindow extends Window {

    /**
     * Основной layout для view списка докторов
     */
    private VerticalLayout layout;

    /**
     * Таблица, в которой находятся доктора и их статистика
     */
    private Grid<Doctor> table;

    /**
     * Словарь, связывающий доктора с его id
     */
    private Map<Doctor, Long> doctors;

    /**
     * Кнопка ОК
     */
    private Button ok;

    /**
     * Пустой конструктор для создания таблицы докторов
     * и количества выписанных ими рецептов
     * Вызывает методы создания компонентов этой таблицы
     * и создания Listener'ов для этих компонентов
     */
    public DoctorStatisticsWindow() {
        createComponents();
        createListeners();
    }

    /**
     * Создаёт компоненты для таблицы докторов
     * и количества выписанных ими рецептов
     */
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
        table.addColumn(doctor -> doctors.get(doctor))
                .setCaption("Quantity of prescriptions");

        table.setItems(List.copyOf(doctors.keySet()));
        layout.addComponent(table);
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponent(ok);
        layout.addComponent(buttonsLayout);
        layout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_CENTER);
    }

    /**
     * Создаёт Listener'ы для компонентов таблицы докторов
     * и количества выписанных ими рецептов
     */
    private void createListeners() {
        ok.addClickListener(event -> this.close());
    }
}
