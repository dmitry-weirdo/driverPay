package com.magenta.echo.driverpay.ui.screen;

import com.evgenltd.kwick.ui.Screen;
import com.evgenltd.kwick.ui.UIContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 12-05-2016 20:46
 */
public class Main extends Screen {
    public Main() {
        super("/fxml/Main.fxml");
    }

    @Override
    public String getTitle() {
        return null;
    }

	@FXML
	private void handleOpenJobs(ActionEvent event) {
		UIContext.get().openScreenInNewTab(new JobBrowser());
	}

	@FXML
	private void handleOpenDrivers(ActionEvent event) {
		UIContext.get().openScreenInNewTab(new DriverBrowser());
	}

	@FXML
	private void handleGlobalSalaryCalculation(ActionEvent event) {

	}

	@FXML
	private void handleGlobalProcessing(ActionEvent event) {

	}
}
