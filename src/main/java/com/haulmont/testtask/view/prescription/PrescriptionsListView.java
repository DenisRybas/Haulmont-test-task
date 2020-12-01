package com.haulmont.testtask.view.prescription;

public interface PrescriptionsListView {
    interface PrescriptionsListViewListener {
        void buttonClick(PrescriptionListButtons nameOfButton);
    }
    void addListener(PrescriptionsListViewListener listener);
}
