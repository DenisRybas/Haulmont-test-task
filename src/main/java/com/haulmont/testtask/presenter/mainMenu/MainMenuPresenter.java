package com.haulmont.testtask.presenter.mainMenu;


import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.model.doctor.DoctorDao;
import com.haulmont.testtask.model.prescription.PrescriptionDao;
import com.haulmont.testtask.presenter.doctor.DoctorsListPresenter;
import com.haulmont.testtask.presenter.prescription.PrescriptionsListPresenter;
import com.haulmont.testtask.view.doctor.DoctorsListViewImpl;
import com.haulmont.testtask.model.patient.PatientDao;
import com.haulmont.testtask.presenter.patient.PatientsListPresenter;
import com.haulmont.testtask.view.mainMenu.MainMenuButtons;
import com.haulmont.testtask.view.patient.PatientsListViewImpl;
import com.haulmont.testtask.view.mainMenu.MainMenuView;
import com.haulmont.testtask.view.mainMenu.MainMenuViewImpl;
import com.haulmont.testtask.view.prescription.PrescriptionsListViewImpl;

public class MainMenuPresenter implements MainMenuView.MainMenuViewListener {
    private MainMenuViewImpl view;

    public MainMenuPresenter(MainMenuViewImpl view) {
        this.view = view;
        view.addListener(this);
    }


    public MainMenuViewImpl getView() {
        return view;
    }

    @Override
    public void buttonClick(MainMenuButtons nameOfButton) {
        switch (nameOfButton) {
            case PATIENTS:
//                UI.getCurrent().getNavigator().navigateTo("ListOfPatients");
                MainUI.getCurrent().addWindow(new PatientsListPresenter(
                        new PatientDao(), new PatientsListViewImpl()).getView());
                break;
            case DOCTORS:
                MainUI.getCurrent().addWindow(new DoctorsListPresenter(
                        new DoctorDao(), new DoctorsListViewImpl())
                        .getView());
                break;
            case PRESCRIPTIONS:
                MainUI.getCurrent().addWindow(new PrescriptionsListPresenter(
                        new PrescriptionDao(), new PrescriptionsListViewImpl()).getView());
                break;
        }
    }
}
