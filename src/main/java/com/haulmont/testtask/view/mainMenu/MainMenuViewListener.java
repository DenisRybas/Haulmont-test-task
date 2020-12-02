package com.haulmont.testtask.view.mainMenu;

/**
 * Listener, отслеживабщий действия с {@link MainMenuView}
 */
public interface MainMenuViewListener {
    /**
     * @param nameOfButton {@link MainMenuButtons} кнопки,
     *                     которая была нажата
     * @see MainMenuButtons
     */
    void buttonClick(MainMenuButtons nameOfButton);
}
