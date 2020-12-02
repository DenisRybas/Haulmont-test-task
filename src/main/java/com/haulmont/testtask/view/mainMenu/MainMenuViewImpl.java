package com.haulmont.testtask.view.mainMenu;

import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.List;

/**
 * View главного меню
 */
public class MainMenuViewImpl extends HorizontalLayout
        implements MainMenuView, Button.ClickListener {

    /**
     * Список {@link MainMenuViewListener},
     */
    private List<MainMenuViewListener> listeners;

    /**
     * Пустой конструктор для создания главного меню
     * Вызывает методы создания компонентов главного меню
     * и создания Listener'ов для этих компонентов
     */
    public MainMenuViewImpl() {
        createComponents();
        createListeners();
    }

    /**
     * Создаёт компоненты для главного меню
     */
    private void createComponents() {
        for (MainMenuButtons name : MainMenuButtons.values()) {
            Button button = new Button(name.toString(), this);
            button.setData(name);
            this.addComponent(button);
        }
    }

    /**
     * Создаёт Listener'ы для компонентов главного меню
     */
    private void createListeners() {
        listeners = new ArrayList<>();
    }

    /**
     * Добавляет {@link MainMenuViewListener} в {@link #listeners}
     *
     * @param listener -  {@link MainMenuViewListener},
     *                 отслеживающий действия c {@link MainMenuView}
     * @see MainMenuView
     * @see MainMenuViewListener
     */
    @Override
    public void addListener(MainMenuViewListener listener) {
        listeners.add(listener);
    }

    /**
     * Отслеживает нажатие кнопки, определяет, какая кнопка была нажата,
     * после чего {@link MainMenuViewListener} этой кнопки обрабатывает
     * нажатие
     *
     * @param clickEvent - событие нажатия на кнопку
     * @see MainMenuViewListener
     */
    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        for (MainMenuViewListener listener : listeners)
            listener.buttonClick((MainMenuButtons) clickEvent.getButton()
                    .getData());
    }
}
