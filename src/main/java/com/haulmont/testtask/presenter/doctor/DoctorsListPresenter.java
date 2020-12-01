package com.haulmont.testtask.presenter.doctor;

import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.model.doctor.Doctor;
import com.haulmont.testtask.model.doctor.DoctorDao;
import com.haulmont.testtask.model.exception.PrescriptionAvailabilityException;
import com.haulmont.testtask.model.patient.Patient;
import com.haulmont.testtask.view.doctor.*;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

public class DoctorsListPresenter implements DoctorsListView.DoctorsListViewListener {
    private DoctorDao model;
    private DoctorsListViewImpl view;

    public DoctorsListPresenter(DoctorDao model, DoctorsListViewImpl view) {
        this.model = model;
        this.view = view;
        view.addListener(this);
    }

    public DoctorDao getModel() {
        return model;
    }

    public DoctorsListViewImpl getView() {
        return view;
    }

    @Override
    public void buttonClick(DoctorListButtons nameOfButton) {
        switch (nameOfButton) {
            case ADD:
                MainUI.getCurrent().addWindow(new DoctorWindow(view, CreateUpdate.CREATE));
                break;
            case UPDATE:
                MainUI.getCurrent().addWindow(new DoctorWindow(view, CreateUpdate.UPDATE));
                break;
            case REMOVE:
                try {
                    Doctor doctor = view.getSelectedTableItem();
                    model.delete(doctor);
                    view.removeDoctor(doctor);
                    view.refreshTable();
                } catch (PrescriptionAvailabilityException e) {
                    Notification notification = new Notification("Warning", "The patient has got recipes. " +
                            "Deletion is not allowed",  Notification.Type.WARNING_MESSAGE);
                    notification.setDelayMsec(3000);
                    notification.show(Page.getCurrent());
                }
                break;
            case STATISTICS:
                MainUI.getCurrent().addWindow(new DoctorStatisticsWindow());
                break;
            case OK:
                view.close();
                break;
        }
    }
}
