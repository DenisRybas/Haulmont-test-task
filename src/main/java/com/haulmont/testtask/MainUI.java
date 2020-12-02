package com.haulmont.testtask;

import com.haulmont.testtask.presenter.mainMenu.MainMenuPresenter;
import com.haulmont.testtask.view.mainMenu.MainMenuViewImpl;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Главный UI - класс
 */
@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);

        var mainMenu = new MainMenuPresenter(new MainMenuViewImpl());
        layout.addComponent(mainMenu.getView());
        setContent(layout);
    }
}