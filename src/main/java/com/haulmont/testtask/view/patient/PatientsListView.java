package com.haulmont.testtask.view.patient;

public interface PatientsListView {
    interface PatientsListViewListener {
        void buttonClick(PatientListButtons nameOfButton);
    }
    public void addListener(PatientsListViewListener listener);
}
