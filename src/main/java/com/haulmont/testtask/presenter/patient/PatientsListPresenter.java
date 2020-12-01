package com.haulmont.testtask.presenter.patient;

import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.model.exception.PrescriptionAvailabilityException;
import com.haulmont.testtask.model.patient.Patient;
import com.haulmont.testtask.view.doctor.CreateUpdate;
import com.haulmont.testtask.model.patient.PatientDao;
import com.haulmont.testtask.view.patient.*;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

public class PatientsListPresenter implements PatientsListViewListener {
    private PatientDao model;
    private PatientsListViewImpl view;

    public PatientsListPresenter(PatientDao model, PatientsListViewImpl view) {
        this.model = model;
        this.view = view;
        view.addListener(this);
    }

    public PatientDao getModel() {
        return model;
    }

    public PatientsListViewImpl getView() {
        return view;
    }

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
