package com.magenta.echo.driverpay.ui.screen;

import com.evgenltd.kwickui.core.Screen;
import com.evgenltd.kwickui.core.UIContext;
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
    private void handleDriversOpen(ActionEvent actionEvent) {
        UIContext.get().openScreenInNewTab(new DriverBrowser());
    }

    @FXML
    private void handleJobsOpen(ActionEvent actionEvent) {
        UIContext.get().openScreenInNewTab(new JobBrowser());
    }

    @FXML
    private void handleChargeBasesOpen(ActionEvent actionEvent) {
        UIContext.get().openScreenInNewTab(new PaymentReasonBrowser());
    }

    @FXML
    private void handleSalaryCalculationOpen(ActionEvent actionEvent) {
		UIContext.get().openScreenInNewTab(new SalaryCalculation());
    }

    @FXML
    private void handlePaymentsOpen(ActionEvent actionEvent) {
		UIContext.get().openScreenInNewTab(new PaymentDocumentBrowser());
    }

}
