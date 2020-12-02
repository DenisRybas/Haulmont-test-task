package com.haulmont.testtask.view.mainMenu;

/**
 * Интерфейс для View главного меню
 */
public interface MainMenuView {
    /**
     * добавляет {@link MainMenuViewListener}
     *
     * @param listener Listener, отслеживающий действия c
     *                 {@link MainMenuView}
     * @see MainMenuView
     * @see MainMenuViewListener
     */
    void addListener(MainMenuViewListener listener);
}
