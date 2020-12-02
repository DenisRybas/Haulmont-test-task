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

/**
 * View списка докторов
 *
 * @see Doctor
 */
public class DoctorsListViewImpl extends Window implements DoctorsListView,
        Button.ClickListener {

    /**
     * Список {@link DoctorsListViewListener},
     */
    private List<DoctorsListViewListener> listeners;

    /**
     * Основной layout для view списка докторов
     */
    private VerticalLayout layout;

    /**
     * Таблица, в которой находятся доктора
     */
    private Grid<Doctor> table;

    /**
     * Список докторов
     */
    private List<Doctor> doctors;

    /**
     * Словарь, связывающий {@link DoctorListButtons} - название кнопки
     * и саму кнопку
     */
    private Map<DoctorListButtons, Button> buttons;

    /**
     * Пустой конструктор для создания view списка докторов
     * Вызывает методы создания компонентов view
     * и создания Listener'ов для этих компонентов
     */
    public DoctorsListViewImpl() {
        createComponents();
        createListeners();
    }

    /**
     * Создаёт компоненты для View
     */
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

    /**
     * Создаёт Listener'ы для компонентов View
     */
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

    /**
     * @return значение выбранного в таблице доктора
     */
    public Doctor getSelectedTableItem() {
        return table.asSingleSelect().getValue();
    }

    /**
     * Перезагружает таблицу
     */
    public void refreshTable() {
        table.getDataProvider().refreshAll();
    }

    /**
     * @param doctor - доктор для добавления в {@link #doctors}
     */
    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
    }

    /**
     * @param doctor - доктор для удаления из {@link #doctors}
     */
    public void removeDoctor(Doctor doctor) {
        doctors.remove(doctor);
    }

    /**
     * Добавляет {@link DoctorsListViewListener} в {@link #listeners}
     *
     * @param listener -  {@link DoctorsListViewListener},
     *                 отслеживающий действия c {@link DoctorsListView}
     * @see DoctorsListView
     * @see DoctorsListViewListener
     */
    @Override
    public void addListener(DoctorsListViewListener listener) {
        listeners.add(listener);
    }

    /**
     * Отслеживает нажатие кнопки, определяет, какая кнопка была нажата,
     * после чего {@link DoctorsListViewListener} этой кнопки обрабатывает
     * нажатие
     *
     * @param clickEvent - событие нажатия на кнопку
     * @see DoctorsListViewListener
     */
    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        for (DoctorsListViewListener listener : listeners)
            listener.buttonClick((DoctorListButtons) clickEvent.getButton()
                    .getData());
    }
}
