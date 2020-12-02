package com.haulmont.testtask.view.patient;

/**
 * Интерфейс для View списка пациентов
 */
public interface PatientsListView {
    /**
     * добавляет {@link PatientsListViewListener}
     *
     * @param listener Listener, отслеживающий действия c
     *                 {@link PatientsListView}
     * @see PatientsListView
     * @see PatientsListViewListener
     */
    void addListener(PatientsListViewListener listener);
}
