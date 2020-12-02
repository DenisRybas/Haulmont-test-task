package com.haulmont.testtask.view.prescription;

import com.haulmont.testtask.model.prescription.Prescription;
import com.haulmont.testtask.model.prescription.PrescriptionDao;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * View списка рецептов
 *
 * @see Prescription
 */
public class PrescriptionsListViewImpl extends Window
        implements PrescriptionsListView, Button.ClickListener {

    /**
     * Список {@link PrescriptionsListViewListener},
     */
    private List<PrescriptionsListViewListener> listeners;

    /**
     * Основной layout для view списка рецептов
     */
    private VerticalLayout layout;

    /**
     * Таблица, в которой находятся рецепты
     */
    private Grid<Prescription> table;

    /**
     * Список рецептов
     */
    private List<Prescription> prescriptions;

    /**
     * Словарь, связывающий {@link PrescriptionListButtons} - название кнопки
     * и саму кнопку
     */
    private Map<PrescriptionListButtons, Button> buttons;

    /**
     * TextField для фильтра списка рецептов по описанию
     */
    private TextField descriptionFilter;

    /**
     * ComboBox для фильтра списка рецептов по
     * {@link com.haulmont.testtask.model.patient.Patient}
     *
     * @see com.haulmont.testtask.model.patient.Patient
     */
    private ComboBox<Long> patientFilter;

    /**
     * ComboBox для фильтра списка рецептов по {@link PrescriptionPriority}
     *
     * @see Prescription
     */
    private ComboBox<PrescriptionPriority> priorityFilter;

    /**
     * Класс, реализующий CRUD - методы для сущности {@link Prescription},
     *
     * @see PrescriptionDao
     */
    private PrescriptionDao prescriptionDao;

    /**
     * Пустой конструктор для создания view списка рецептов
     * Вызывает методы создания компонентов view
     * и создания Listener'ов для этих компонентов
     */
    public PrescriptionsListViewImpl() {
        prescriptionDao = new PrescriptionDao();
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
        table.setSizeFull();
        table.setHeight(800, Unit.PIXELS);
        this.setModal(true);
        this.setContent(layout);
        this.setSizeFull();

        table.addColumn(Prescription::getId)
                .setCaption("Id");
        table.addColumn(Prescription::getDescription)
                .setCaption("Description");
        table.addColumn(prescription -> prescription.getPatient().getId())
                .setCaption("Patient");
        table.addColumn(prescription -> prescription.getDoctor().getId())
                .setCaption("Doctor");
        table.addColumn(Prescription::getPriority)
                .setCaption("Priority");
        table.addColumn(Prescription::getDateCreated)
                .setCaption("Date created");
        table.addColumn(Prescription::getExpirationDate)
                .setCaption("Expiration Date");

        prescriptions = prescriptionDao.getAll();
        setDefaultDataProvider();
        layout.addComponent(table);
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        for (PrescriptionListButtons name : PrescriptionListButtons.values()) {
            Button button = new Button(name.toString(), this);
            button.setData(name);
            buttons.put(name, button);
            if (name == PrescriptionListButtons.REMOVE
                    || name == PrescriptionListButtons.UPDATE) {
                button.setEnabled(false);
            }
            buttonsLayout.addComponent(button);
        }

        HorizontalLayout filters = new HorizontalLayout();
        descriptionFilter = new TextField();
        descriptionFilter.setDescription("Description");

        patientFilter = new ComboBox<>();
        patientFilter.setTextInputAllowed(false);
        patientFilter.setDescription("Patient");
        patientFilter.setItems(prescriptions.stream()
                .map(p -> p.getPatient().getId())
                .collect(Collectors.toSet()));

        priorityFilter = new ComboBox<>();
        priorityFilter.setTextInputAllowed(false);
        priorityFilter.setDescription("Priority");
        priorityFilter.setItems(prescriptions.stream()
                .map(Prescription::getPriority)
                .collect(Collectors.toSet()));

        filters.addComponent(descriptionFilter);
        filters.addComponent(patientFilter);
        filters.addComponent(priorityFilter);

        buttonsLayout.addComponent(filters);
        buttonsLayout.setComponentAlignment(filters, Alignment.MIDDLE_LEFT);
        layout.addComponent(buttonsLayout);
        layout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_CENTER);
    }

    /**
     * Создаёт Listener'ы для компонентов View
     */
    private void createListeners() {
        listeners = new ArrayList<>();
        descriptionFilter.addValueChangeListener(event -> {
            if (!descriptionFilter.getValue().equals("")) {
                List<Prescription> filteredPrescriptions = new LinkedList<>();
                for (Prescription prescription : prescriptions) {
                    if (prescription.getDescription()
                            .contains(descriptionFilter.getValue())) {
                        filteredPrescriptions.add(prescription);
                    }
                }
                ListDataProvider<Prescription> dataProvider = DataProvider
                        .ofCollection(filteredPrescriptions);
                table.setDataProvider(dataProvider);
                buttons.forEach((k, v) -> v.setEnabled(false));
            } else {
                setDefaultDataProvider();
                disableDeleteUpdate();
            }
        });

        patientFilter.addValueChangeListener(event -> {
            if (patientFilter.getValue() != null) {
                List<Prescription> filteredPrescriptions = new LinkedList<>();
                for (Prescription prescription : prescriptions) {
                    if (prescription.getPatient().getId()
                            .equals(patientFilter.getValue())) {
                        filteredPrescriptions.add(prescription);
                    }
                }
                ListDataProvider<Prescription> dataProvider = DataProvider
                        .ofCollection(filteredPrescriptions);
                table.setDataProvider(dataProvider);
                buttons.forEach((k, v) -> v.setEnabled(false));
            } else {
                setDefaultDataProvider();
                disableDeleteUpdate();
            }
        });

        priorityFilter.addValueChangeListener(event -> {
            if (priorityFilter.getValue() != null) {
                List<Prescription> filteredPrescriptions = new LinkedList<>();
                for (Prescription prescription : prescriptions) {
                    if (prescription.getPriority()
                            .equals(priorityFilter.getValue())) {
                        filteredPrescriptions.add(prescription);
                    }
                }
                ListDataProvider<Prescription> dataProvider = DataProvider
                        .ofCollection(filteredPrescriptions);
                table.setDataProvider(dataProvider);
                buttons.forEach((k, v) -> v.setEnabled(false));
            } else {
                setDefaultDataProvider();
                disableDeleteUpdate();
            }
        });

        table.addSelectionListener(event -> {
            if (getSelectedTableItem() != null) {
                buttons.get(PrescriptionListButtons.REMOVE).setEnabled(true);
                buttons.get(PrescriptionListButtons.UPDATE).setEnabled(true);
            } else {
                buttons.get(PrescriptionListButtons.REMOVE).setEnabled(false);
                buttons.get(PrescriptionListButtons.UPDATE).setEnabled(false);
            }
        });

    }

    private void disableDeleteUpdate() {
        for (Map.Entry<PrescriptionListButtons, Button> entry
                : buttons.entrySet()) {
            if (entry.getKey() != PrescriptionListButtons.UPDATE
                    && entry.getKey() != PrescriptionListButtons.REMOVE) {
                entry.getValue().setEnabled(true);
            }
        }
    }

    private void setDefaultDataProvider() {
        ListDataProvider<Prescription> dataProvider = DataProvider
                .ofCollection(prescriptions);
        table.setDataProvider(dataProvider);
        table.setItems(prescriptions);
    }

    public void refreshTable() {
        table.getDataProvider().refreshAll();
    }

    public Prescription getSelectedTableItem() {
        return table.asSingleSelect().getValue();
    }

    public void addPrescription(Prescription prescription) {
        prescriptions.add(prescription);
    }

    public void removePrescription(Prescription prescription) {
        prescriptions.remove(prescription);
    }

    @Override
    public void addListener(PrescriptionsListViewListener listener) {
        listeners.add(listener);
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        for (PrescriptionsListViewListener listener : listeners)
            listener.buttonClick((PrescriptionListButtons) clickEvent
                    .getButton().getData());
    }
}
