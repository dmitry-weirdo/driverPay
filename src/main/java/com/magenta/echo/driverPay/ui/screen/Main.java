package com.magenta.echo.driverpay.ui.screen;

import com.magenta.echo.driverpay.core.Context;
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

    @FXML
    private void handleDriversOpen(ActionEvent actionEvent) {
        Context.get().openScreen(new DriverBrowser());
    }

    @FXML
    private void handleJobsOpen(ActionEvent actionEvent) {
        Context.get().openScreen(new JobBrowser());
    }

    @FXML
    private void handleChargeBasesOpen(ActionEvent actionEvent) {
        Context.get().openScreen(new PaymentReasonBrowser());
    }

    @FXML
    private void handleSalaryCalculationOpen(ActionEvent actionEvent) {
		Context.get().openScreen(new SalaryCalculation());
    }

    @FXML
    private void handlePaymentsOpen(ActionEvent actionEvent) {
		Context.get().openScreen(new PaymentDocumentBrowser());
    }

}
