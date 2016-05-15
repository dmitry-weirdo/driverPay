package com.magenta.echo.driverpay.ui.screen;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.PaymentProcessBean;
import com.magenta.echo.driverpay.core.entity.DriverDto;
import com.magenta.echo.driverpay.core.entity.PaymentDocumentDto;
import com.magenta.echo.driverpay.ui.dialog.SelectDriver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 03:45
 */
public class PaymentDocumentBrowser extends Screen {

	private PaymentProcessBean paymentProcessBean = Context.get().getPaymentProcessBean();

	@FXML private DatePicker dateFrom;
	@FXML private DatePicker dateTo;
	@FXML private Button driver;
	@FXML private CheckBox processed;

	@FXML private TableView<PaymentDocumentDto> table;
	@FXML private TableColumn<PaymentDocumentDto,Long> idColumn;
	@FXML private TableColumn<PaymentDocumentDto,String> driverColumn;
	@FXML private TableColumn<PaymentDocumentDto,String> paymentDateColumn;
	@FXML private TableColumn<PaymentDocumentDto,Boolean> statusColumn;

	@FXML private Button viewButton;
	@FXML private Button processButton;

	private Long selectedDriverId;

	public PaymentDocumentBrowser() {
		super("/fxml/PaymentDocumentBrowser.fxml");
		initUI();
		loadData();
	}

	// other

	private void initUI()	{

//		processed.selectedProperty().addListener(param -> loadData());

		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		driverColumn.setCellValueFactory(new PropertyValueFactory<>("driverValue"));
		paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("processed"));

		table.getSelectionModel().selectedItemProperty().addListener(param -> {
			processButton.setDisable(table.getSelectionModel().isEmpty());
			viewButton.setDisable(table.getSelectionModel().getSelectedIndices().size() != 1);
		});
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

	}

	private void loadData()	{
		table.getItems().setAll(paymentProcessBean.loadPaymentDocumentList(
				dateFrom.getValue(),
				dateTo.getValue(),
				selectedDriverId,
				processed.isSelected()
		));
	}

	// handlers

	@FXML
	private void handleSearch(ActionEvent event) {
		loadData();
	}

	@FXML
	private void handleClear(ActionEvent event) {
		dateFrom.setValue(null);
		dateTo.setValue(null);
		selectedDriverId = null;
		driver.setText("Select Driver...");
		processed.setSelected(false);
	}

	@FXML
	private void handleView(ActionEvent event) {
		if(table.getSelectionModel().getSelectedItems().size() != 1)	{
			return;
		}
		final PaymentDocumentDto paymentDocumentDto = table.getSelectionModel().getSelectedItem();
		Context.get().openScreen(new PaymentDocumentView(paymentDocumentDto.getId()));
	}

	@FXML
	private void handleProcess(ActionEvent event) {
		if(table.getSelectionModel().isEmpty())	{
			return;
		}
		paymentProcessBean.processPaymentDocuments(table.getSelectionModel().getSelectedItems());
		loadData();
	}

	@FXML
	private void handleSelectDriver(ActionEvent event) {
		final Optional<DriverDto> result = Context.get().openDialogAndWait(new SelectDriver());
		if(!result.isPresent())	{
			return;
		}
		driver.setText(result.get().getName());
		selectedDriverId = result.get().getId();
	}

}
