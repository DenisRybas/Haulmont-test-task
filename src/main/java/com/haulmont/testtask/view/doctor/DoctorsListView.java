package com.haulmont.testtask.view.doctor;

/**
 * Интерфейс для View списка докторов
 */
public interface DoctorsListView {
    /**
     * добавляет {@link DoctorsListViewListener}
     *
     * @param listener Listener, отслеживающий действия c
     *                 {@link DoctorsListView}
     * @see DoctorsListView
     * @see DoctorsListViewListener
     */
    void addListener(DoctorsListViewListener listener);
}
