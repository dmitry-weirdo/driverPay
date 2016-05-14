package com.magenta.echo.driverpay.ui.screen;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.PaymentBean;
import com.magenta.echo.driverpay.core.entity.PaymentReasonDto;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 12-05-2016 21:30
 */
public class PaymentReasonBrowser extends Screen {

		private PaymentBean paymentBean = Context.get().getPaymentBean();

	@FXML private Button editPaymentReasonButton;
    @FXML private TableView<PaymentReasonDto> table;
    @FXML private TableColumn<PaymentReasonDto,Long> idColumn;
    @FXML private TableColumn<PaymentReasonDto,String> nameColumn;
	@FXML private TableColumn<PaymentReasonDto,String> driverColumn;
	@FXML private TableColumn<PaymentReasonDto,String> typeColumn;

    public PaymentReasonBrowser() {
        super("/fxml/PaymentReasonBrowser.fxml");
        initUI();
        loadData();
    }

    private void initUI()   {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		driverColumn.setCellValueFactory(new PropertyValueFactory<>("driverValue"));
		typeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getLabel()));

		table.getSelectionModel().selectedItemProperty().addListener(param -> editPaymentReasonButton.setDisable(table.getSelectionModel().isEmpty()));
    }

    private void loadData() {
        table.getItems().setAll(paymentBean.loadPaymentReasonList());
    }

    // handlers

    @FXML
    private void handleAddPaymentReason(ActionEvent actionEvent) {
		Context.get().openScreen(new PaymentReasonEdit(null));
    }

    @FXML
    public void handleEditPaymentReason(ActionEvent actionEvent) {
		if(table.getSelectionModel().isEmpty()) {
			return;
		}
		Context.get().openScreen(new PaymentReasonEdit(table.getSelectionModel().getSelectedItem().getId()));
    }
}
