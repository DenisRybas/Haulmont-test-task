package com.haulmont.testtask.view.doctor;

public interface DoctorsListView {
    interface DoctorsListViewListener {
        void buttonClick(DoctorListButtons nameOfButton);
    }
    void addListener(DoctorsListViewListener listener);
}
