package com.haulmont.testtask.view.mainMenu;

public interface MainMenuView {
    interface MainMenuViewListener {
        void buttonClick(MainMenuButtons nameOfButton);
    }
    void addListener(MainMenuViewListener listener);
}
