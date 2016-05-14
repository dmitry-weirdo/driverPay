package com.magenta.echo.driverpay.ui.screen;

import com.magenta.echo.driverpay.core.Context;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 12-05-2016 20:47
 */
public abstract class Screen {

    private Pane root;

    public Screen(final String path) {
        root = buildPane(path);
    }

	public Screen()	{
		root = new StackPane();
	}

    //

    public Pane getRoot() {
        return root;
    }

    //

    public void handleHome(ActionEvent actionEvent) {
        Context.get().openScreen(new Main());
    }

    //

    private Pane buildPane(final String path)   {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(Screen.class.getResource(path));
            fxmlLoader.setControllerFactory(param -> this);
            fxmlLoader.load();
            return fxmlLoader.getRoot();
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
