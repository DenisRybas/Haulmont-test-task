package com.haulmont.testtask.view.prescription;

import com.haulmont.testtask.model.doctor.Doctor;
import com.haulmont.testtask.model.doctor.DoctorDao;
import com.haulmont.testtask.model.patient.Patient;
import com.haulmont.testtask.model.patient.PatientDao;
import com.haulmont.testtask.model.prescription.Prescription;
import com.haulmont.testtask.model.prescription.PrescriptionDao;
import com.haulmont.testtask.view.CreateUpdate;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Окно создания/изменения рецепта
 *
 * @see Prescription
 */
public class PrescriptionWindow extends Window {

    /**
     * TextField для описания рецепта
     */
    private TextField description;

    /**
     * ComboBox для выбора пациента
     */
    private ComboBox<Long> patientComboBox;

    /**
     * ComboBox для выбора доктора
     */
    private ComboBox<Long> doctorComboBox;

    /**
     * ComboBox для выбора {@link PrescriptionPriority}
     *
     * @see PrescriptionPriority
     */
    private ComboBox<PrescriptionPriority> priorityComboBox;

    /**
     * DateField для выбора даты начала срока действия рецепта
     */
    private DateField dateCreated;

    /**
     * DateField для выбора даты окончания срока действия рецепта
     */
    private DateField expirationDate;

    /**
     * Binder для рецепта, определяет валидность введённых данных
     */
    private Binder<Prescription> binder;

    /**
     * Кнопка ОК
     */
    private Button okButton;

    /**
     * Кнопка отмены
     */
    private Button cancelButton;

    /**
     * View списка рецептов
     *
     * @see PrescriptionsListViewImpl
     */
    private PrescriptionsListViewImpl prescriptionsListView;

    /**
     * Режим создания/изменения рецепта
     *
     * @see CreateUpdate
     */
    private CreateUpdate mode;

    /**
     * Класс, реализующий CRUD - методы для сущности {@link Patient},
     *
     * @see PatientDao
     */
    private PatientDao patientDao;

    /**
     * Класс, реализующий CRUD - методы для сущности {@link Doctor},
     *
     * @see DoctorDao
     */
    private DoctorDao doctorDao;

    /**
     * Конструктор для создания окна создания/изменения рецепта
     * Вызывает методы создания компонентов окна
     * и создания Listener'ов для этих компонентов
     *
     * @param prescriptionsListView - view списка рецептов
     * @param mode                  - режим создания/изменения рецептов
     */
    public PrescriptionWindow(PrescriptionsListViewImpl
                                      prescriptionsListView, CreateUpdate mode) {
        this.prescriptionsListView = prescriptionsListView;
        this.mode = mode;
        patientDao = new PatientDao();
        doctorDao = new DoctorDao();
        createComponents();
        createListeners();
    }

    /**
     * Создаёт компоненты для окна создания/изменения рецепта
     */
    private void createComponents() {
        HorizontalLayout content = new HorizontalLayout();
        binder = new Binder<>(Prescription.class);
        description = new TextField("Description");
        binder.forField(description)
                .asRequired("Prescription must have a description")
                .withValidator(description -> description.length() < 100,
                        "Description must have less than 100 characters")
                .bind(Prescription::getDescription, Prescription::setDescription);

        List<Long> patientIds = patientDao.getAll().stream().
                map(Patient::getId).collect(Collectors.toList());
        patientComboBox = new ComboBox<>("Patient");
        patientComboBox.setItems(patientIds);
        binder.forField(patientComboBox)
                .asRequired("Prescription must have a patient");
        patientComboBox.setTextInputAllowed(false);

        List<Long> doctorIds = doctorDao.getAll().stream().
                map(Doctor::getId).collect(Collectors.toList());
        doctorComboBox = new ComboBox<>("Doctor");
        doctorComboBox.setItems(doctorIds);
        binder.forField(doctorComboBox)
                .asRequired("Prescription must have a doctor");
        doctorComboBox.setTextInputAllowed(false);

        priorityComboBox = new ComboBox<>();
        priorityComboBox.setTextInputAllowed(false);
        binder.forField(priorityComboBox)
                .asRequired("Prescription must have a priority");
        priorityComboBox.setDataProvider(new ListDataProvider<>(
                Arrays.asList(PrescriptionPriority.values())));
        priorityComboBox.setCaption("Priority");

        dateCreated = new DateField();
        dateCreated.setCaption("Date created");
        dateCreated.setValue(LocalDate.now());
        binder.forField(dateCreated)
                .asRequired("Prescription must have a creation date");

        expirationDate = new DateField();
        expirationDate.setCaption("Expiration date");
        expirationDate.setRangeStart(dateCreated.getValue());
        dateCreated.setRangeEnd(expirationDate.getValue());
        expirationDate.addValueChangeListener(event ->
                dateCreated.setRangeEnd(expirationDate.getValue()));
        dateCreated.addValueChangeListener(event ->
                expirationDate.setRangeStart(dateCreated.getValue()));

        if (mode == CreateUpdate.UPDATE) {
            Prescription selectedItem = prescriptionsListView.getSelectedTableItem();
            description.setValue(selectedItem.getDescription());
            patientComboBox.setValue(selectedItem.getPatient().getId());
            doctorComboBox.setValue(selectedItem.getDoctor().getId());
            priorityComboBox.setValue(selectedItem.getPriority());
            dateCreated.setValue(selectedItem.getDateCreated().toLocalDate());
            expirationDate.setValue(selectedItem.getExpirationDate().toLocalDate());
        }

        okButton = new Button("OK");

        cancelButton = new Button("Cancel");

        content.addComponents(description, patientComboBox, doctorComboBox,
                priorityComboBox, dateCreated, expirationDate, okButton,
                cancelButton);
        this.setContent(content);
    }

    /**
     * Создаёт Listener'ы для окна создания/изменения рецепта
     */
    private void createListeners() {
        okButton.addClickListener(e -> {
            if (binder.isValid()) {
                Prescription prescription = new Prescription(
                        description.getValue(),
                        patientDao.get(patientComboBox.getValue()),
                        doctorDao.get(doctorComboBox.getValue()),
                        java.sql.Date.valueOf(dateCreated.getValue()),
                        java.sql.Date.valueOf(expirationDate.getValue()),
                        priorityComboBox.getValue());
                PrescriptionDao prescriptionDao = new PrescriptionDao();
                if (mode == CreateUpdate.CREATE) {
                    prescriptionDao.save(prescription);
                    prescriptionsListView.addPrescription(prescription);
                } else {
                    Prescription prescriptionToUpd = prescriptionsListView
                            .getSelectedTableItem();

                    prescriptionDao.update(prescriptionToUpd,
                            prescription);
                    prescriptionToUpd.setDescription(
                            prescription.getDescription());
                    prescriptionToUpd.setPatient(
                            prescription.getPatient());
                    prescriptionToUpd.setDoctor(
                            prescription.getDoctor());
                    prescriptionToUpd.setDateCreated(
                            prescription.getDateCreated());
                    prescriptionToUpd.setExpirationDate(
                            prescription.getExpirationDate());
                    prescriptionToUpd.setPriority(
                            prescription.getPriority());
                }
                prescriptionsListView.refreshTable();
                close();
            }
        });
        cancelButton.addClickListener(e -> close());
    }
}
