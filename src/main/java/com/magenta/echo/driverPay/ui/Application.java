package com.magenta.echo.driverpay.ui;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.Scheme;
import com.magenta.echo.driverpay.ui.screen.Main;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 12-05-2016 20:34
 */
public class Application extends javafx.application.Application {
    public void start(final Stage primaryStage) throws Exception {

        Context.get().setStage(primaryStage);
		Scheme.initDatabase();

        final Main main = new Main();
        final Scene scene = new Scene(main.getRoot(), 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

		Thread.currentThread().setUncaughtExceptionHandler(new UIExceptionHandler());
    }

    public static void main(final String[] args) {
        launch(args);
    }
}
