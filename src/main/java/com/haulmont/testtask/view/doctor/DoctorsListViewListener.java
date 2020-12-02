package com.haulmont.testtask.view.doctor;

/**
 * Listener, отслеживабщий действия с {@link DoctorsListView}
 */
public interface DoctorsListViewListener {
    /**
     * @param nameOfButton {@link DoctorListButtons} кнопки,
     *                     которая была нажата
     * @see DoctorListButtons
     */
    void buttonClick(DoctorListButtons nameOfButton);
}
