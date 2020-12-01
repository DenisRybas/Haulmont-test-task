package com.haulmont.testtask.view.mainMenu;

import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.List;

public class MainMenuViewImpl extends HorizontalLayout implements MainMenuView, Button.ClickListener {
    private List<MainMenuViewListener> listeners;

    public MainMenuViewImpl() {
        createComponents();
        createListeners();
    }

    private void createComponents() {
        for (MainMenuButtons name : MainMenuButtons.values()) {
            Button button = new Button(name.toString(), this);
            button.setData(name);
            this.addComponent(button);
        }
    }

    private void createListeners() {
        listeners = new ArrayList<>();
    }

    @Override
    public void addListener(MainMenuViewListener listener) {
        listeners.add(listener);
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        for (MainMenuViewListener listener : listeners)
            listener.buttonClick((MainMenuButtons) clickEvent.getButton().getData());
    }
}
