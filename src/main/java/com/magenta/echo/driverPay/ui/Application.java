package com.magenta.echo.driverpay.ui;

import com.evgenltd.kwick.ui.UIContext;
import com.magenta.echo.driverpay.core.ContextConfig;
import com.magenta.echo.driverpay.ui.screen.Main;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 12-05-2016 20:34
 */
public class Application extends javafx.application.Application {
    public void start(final Stage primaryStage) throws Exception {

        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ContextConfig.class);

		UIContext.get().initialization(primaryStage);
        UIContext.get().setHome(new Main().getRoot());

		primaryStage.setTitle(String.format(
				"%s %s",
				getClass().getPackage().getImplementationTitle(),
				getClass().getPackage().getImplementationVersion()
		));
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/image/app-icon-16.png")));
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/image/app-icon-32.png")));
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/image/app-icon-64.png")));
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/image/app-icon-128.png")));

        primaryStage.show();

		Thread.currentThread().setUncaughtExceptionHandler(new UIExceptionHandler());
    }

    public static void main(final String[] args) {
        launch(args);
    }
}
