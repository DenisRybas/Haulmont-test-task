package com.haulmont.testtask.presenter.patient;

import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.model.exception.PrescriptionAvailabilityException;
import com.haulmont.testtask.model.patient.Patient;
import com.haulmont.testtask.view.CreateUpdate;
import com.haulmont.testtask.model.patient.PatientDao;
import com.haulmont.testtask.view.patient.*;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

/**
 * Presenter для {@link Patient}
 *
 * @see Patient
 * @see PatientDao
 * @see PatientsListViewImpl
 */
public class PatientsListPresenter implements PatientsListViewListener {

    /**
     * Модель, реализующая CRUD - методы для пациента
     */
    private PatientDao model;

    /**
     * View списка пациентов
     */
    private PatientsListViewImpl view;

    /**
     * @param model - модель, реализующая CRUD - методы для пациента
     * @param view  - view списка пациентов
     */
    public PatientsListPresenter(PatientDao model, PatientsListViewImpl view) {
        this.model = model;
        this.view = view;
        view.addListener(this);
    }

    /**
     * @return модель, реализующая CRUD - методы для пациента
     */
    public PatientDao getModel() {
        return model;
    }

    /**
     * @return view списка пациентов
     */
    public PatientsListViewImpl getView() {
        return view;
    }

    /**
     * Определяет, какие действия будут происходить
     * при нажатии заданной кнопки
     *
     * @param nameOfButton название кнопки, полученное из
     *                     {@link PatientListButtons}
     * @see PatientListButtons
     */
    @Override
    public void buttonClick(PatientListButtons nameOfButton) {
        switch (nameOfButton) {
            case ADD:
                MainUI.getCurrent().addWindow(new PatientWindow(view,
                        CreateUpdate.CREATE));
                break;
            case UPDATE:
                MainUI.getCurrent().addWindow(new PatientWindow(view,
                        CreateUpdate.UPDATE));
                break;
            case REMOVE:
                try {
                    Patient patient = view.getSelectedTableItem();
                    model.delete(patient);
                    view.removePatient(patient);
                    view.refreshTable();
                } catch (PrescriptionAvailabilityException e) {
                    Notification notification = new Notification("Warning",
                            "The patient has got recipes. " +
                                    "Deletion is not allowed",
                            Notification.Type.WARNING_MESSAGE);
                    notification.setDelayMsec(3000);
                    notification.show(Page.getCurrent());
                }
                break;
            case OK:
                view.close();
                break;
        }
    }
}
