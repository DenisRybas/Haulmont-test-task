package com.haulmont.testtask.presenter.doctor;

import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.model.doctor.Doctor;
import com.haulmont.testtask.model.doctor.DoctorDao;
import com.haulmont.testtask.model.exception.PrescriptionAvailabilityException;
import com.haulmont.testtask.view.CreateUpdate;
import com.haulmont.testtask.view.doctor.*;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

/**
 * Presenter для {@link Doctor}
 *
 * @see Doctor
 * @see DoctorDao
 * @see DoctorsListViewImpl
 */
public class DoctorsListPresenter implements DoctorsListViewListener {

    /**
     * Модель, реализующая CRUD - методы для доктора
     */
    private DoctorDao model;

    /**
     * View списка докторов
     */
    private DoctorsListViewImpl view;

    /**
     * @param model - модель, реализующая CRUD - методы для доктора
     * @param view  - view списка докторов
     */
    public DoctorsListPresenter(DoctorDao model, DoctorsListViewImpl view) {
        this.model = model;
        this.view = view;
        view.addListener(this);
    }

    /**
     * @return модель, реализующая CRUD - методы для доктора
     */
    public DoctorDao getModel() {
        return model;
    }

    /**
     * @return view списка докторов
     */
    public DoctorsListViewImpl getView() {
        return view;
    }

    /**
     * Определяет, какие действия будут происходить
     * при нажатии заданной кнопки
     *
     * @param nameOfButton название кнопки, полученное из
     *                     {@link DoctorListButtons}
     * @see DoctorListButtons
     */
    @Override
    public void buttonClick(DoctorListButtons nameOfButton) {
        switch (nameOfButton) {
            case ADD:
                MainUI.getCurrent().addWindow(new DoctorWindow(view,
                        CreateUpdate.CREATE));
                break;
            case UPDATE:
                MainUI.getCurrent().addWindow(new DoctorWindow(view,
                        CreateUpdate.UPDATE));
                break;
            case REMOVE:
                try {
                    Doctor doctor = view.getSelectedTableItem();
                    model.delete(doctor);
                    view.removeDoctor(doctor);
                    view.refreshTable();
                } catch (PrescriptionAvailabilityException e) {
                    Notification notification =
                            new Notification("Warning",
                                    "The doctor has got recipes. " +
                                            "Deletion is not allowed",
                                    Notification.Type.WARNING_MESSAGE);
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
