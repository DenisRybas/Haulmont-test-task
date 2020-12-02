package com.haulmont.testtask.view.patient;

/**
 * Listener, отслеживабщий действия с {@link PatientsListView}
 */
public interface PatientsListViewListener {
    /**
     * @param nameOfButton {@link PatientListButtons} кнопки,
     *                     которая была нажата
     * @see PatientListButtons
     */
    void buttonClick(PatientListButtons nameOfButton);
}
