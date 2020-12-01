package com.haulmont.testtask.presenter.prescription;

import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.model.prescription.Prescription;
import com.haulmont.testtask.model.prescription.PrescriptionDao;
import com.haulmont.testtask.view.prescription.*;

public class PrescriptionsListPresenter implements PrescriptionsListViewListener {
    private PrescriptionDao model;
    private PrescriptionsListViewImpl view;

    public PrescriptionsListPresenter(PrescriptionDao model,
                                      PrescriptionsListViewImpl view) {
        this.model = model;
        this.view = view;
        view.addListener(this);
    }

    public PrescriptionDao getModel() {
        return model;
    }

    public PrescriptionsListViewImpl getView() {
        return view;
    }

    @Override
    public void buttonClick(PrescriptionListButtons nameOfButton) {
        switch (nameOfButton) {
            case ADD:
                MainUI.getCurrent().addWindow(new PrescriptionWindow(view,
                        CreateUpdate.CREATE));
                break;
            case UPDATE:
                MainUI.getCurrent().addWindow(new PrescriptionWindow(view,
                        CreateUpdate.UPDATE));
                break;
            case REMOVE:
                Prescription prescription = view.getSelectedTableItem();
                view.removePrescription(prescription);
                view.refreshTable();
                model.delete(prescription);
                break;
            case OK:
                view.close();
                break;
        }
    }
}
