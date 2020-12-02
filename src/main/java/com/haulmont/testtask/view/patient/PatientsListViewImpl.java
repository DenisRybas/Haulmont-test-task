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

/**
 * View списка пациентов
 *
 * @see Patient
 */
public class PatientsListViewImpl extends Window
        implements PatientsListView, Button.ClickListener {

    /**
     * Список {@link PatientsListViewListener},
     */
    private List<PatientsListViewListener> listeners;

    /**
     * Основной layout для view списка пациентов
     */
    private GridLayout layout;

    /**
     * Таблица, в которой находятся пациенты
     */
    private Grid<Patient> table;

    /**
     * Список пациентов
     */
    private List<Patient> patients;

    /**
     * Словарь, связывающий {@link PatientListButtons} - название кнопки
     * и саму кнопку
     */
    private Map<PatientListButtons, Button> buttons;

    /**
     * Пустой конструктор для создания view списка пациентов
     * Вызывает методы создания компонентов view
     * и создания Listener'ов для этих компонентов
     */
    public PatientsListViewImpl() {
        createComponents();
        createListeners();
    }

    /**
     * Создаёт компоненты для View
     */
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

    /**
     * Создаёт Listener'ы для компонентов View
     */
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

    /**
     * @return значение выбранного в таблице пациента
     */
    public Patient getSelectedTableItem() {
        return table.asSingleSelect().getValue();
    }

    /**
     * Перезагружает таблицу
     */
    public void refreshTable() {
        table.getDataProvider().refreshAll();
    }

    /**
     * @param patient - пациент для добавления в {@link #patients}
     */
    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    /**
     * @param patient - пациент для удаления из {@link #patients}
     */
    public void removePatient(Patient patient) {
        patients.remove(patient);
    }

    /**
     * Добавляет {@link PatientsListViewListener} в {@link #listeners}
     *
     * @param listener -  {@link PatientsListViewListener},
     *                 отслеживающий действия c {@link PatientsListView}
     * @see PatientsListView
     * @see PatientsListViewListener
     */
    @Override
    public void addListener(PatientsListViewListener listener) {
        listeners.add(listener);
    }

    /**
     * Отслеживает нажатие кнопки, определяет, какая кнопка была нажата,
     * после чего {@link PatientsListViewListener} этой кнопки обрабатывает
     * нажатие
     *
     * @param clickEvent - событие нажатия на кнопку
     * @see PatientsListViewListener
     */
    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        for (PatientsListViewListener listener : listeners)
            listener.buttonClick((PatientListButtons) clickEvent.getButton()
                    .getData());
    }
}
