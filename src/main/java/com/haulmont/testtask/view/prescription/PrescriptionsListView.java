package com.haulmont.testtask.view.prescription;

/**
 * Интерфейс для View списка рецептов
 */
public interface PrescriptionsListView {
    /**
     * добавляет {@link PrescriptionsListViewListener}
     *
     * @param listener Listener, отслеживающий действия c
     *                 {@link PrescriptionsListView}
     * @see PrescriptionsListView
     * @see PrescriptionsListViewListener
     */
    void addListener(PrescriptionsListViewListener listener);
}
