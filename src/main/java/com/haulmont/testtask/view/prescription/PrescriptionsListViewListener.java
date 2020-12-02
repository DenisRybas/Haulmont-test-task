package com.haulmont.testtask.view.prescription;

/**
 * Listener, отслеживабщий действия с {@link PrescriptionsListView}
 */
public interface PrescriptionsListViewListener {
    /**
     * @param nameOfButton {@link PrescriptionListButtons} кнопки,
     *                     которая была нажата
     * @see PrescriptionListButtons
     */
    void buttonClick(PrescriptionListButtons nameOfButton);
}
