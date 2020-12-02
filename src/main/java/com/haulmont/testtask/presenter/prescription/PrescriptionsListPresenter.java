package com.haulmont.testtask.presenter.prescription;

import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.model.prescription.Prescription;
import com.haulmont.testtask.model.prescription.PrescriptionDao;
import com.haulmont.testtask.view.CreateUpdate;
import com.haulmont.testtask.view.prescription.*;

/**
 * Presenter для {@link Prescription}
 *
 * @see Prescription
 * @see PrescriptionDao
 * @see PrescriptionsListViewImpl
 */
public class PrescriptionsListPresenter implements PrescriptionsListViewListener {

    /**
     * Модель, реализующая CRUD - методы для рецепта
     */
    private PrescriptionDao model;

    /**
     * View списка рецептов
     */
    private PrescriptionsListViewImpl view;

    /**
     * @param model - модель, реализующая CRUD - методы для рецепта
     * @param view  - view списка рецептов
     */
    public PrescriptionsListPresenter(PrescriptionDao model,
                                      PrescriptionsListViewImpl view) {
        this.model = model;
        this.view = view;
        view.addListener(this);
    }

    /**
     * @return модель, реализующая CRUD - методы для рецепта
     */
    public PrescriptionDao getModel() {
        return model;
    }

    /**
     * @return view списка рецептов
     */
    public PrescriptionsListViewImpl getView() {
        return view;
    }

    /**
     * Определяет, какие действия будут происходить
     * при нажатии заданной кнопки
     *
     * @param nameOfButton название кнопки, полученное из
     *                     {@link PrescriptionListButtons}
     * @see PrescriptionListButtons
     */
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
