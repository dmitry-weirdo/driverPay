package com.magenta.echo.driverpay.ui.screen;

import com.evgenltd.kwickui.controls.objectpicker.ObjectPicker;
import com.evgenltd.kwickui.controls.objectpicker.SimpleObject;
import com.evgenltd.kwickui.core.Screen;
import com.evgenltd.kwickui.core.UIContext;
import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.PaymentProcessBean;
import com.magenta.echo.driverpay.core.entity.PaymentDocumentDto;
import com.magenta.echo.driverpay.ui.PickerHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 03:45
 */
public class PaymentDocumentBrowser extends Screen {

	private PaymentProcessBean paymentProcessBean = Context.get().getPaymentProcessBean();

	@FXML private DatePicker dateFrom;
	@FXML private DatePicker dateTo;
	@FXML private ObjectPicker<SimpleObject> driver;
	@FXML private CheckBox processed;

	@FXML private TableView<PaymentDocumentDto> table;
	@FXML private TableColumn<PaymentDocumentDto,Long> idColumn;
	@FXML private TableColumn<PaymentDocumentDto,String> driverColumn;
	@FXML private TableColumn<PaymentDocumentDto,String> paymentDateColumn;
	@FXML private TableColumn<PaymentDocumentDto,Boolean> statusColumn;

	@FXML private Button viewButton;
	@FXML private Button processButton;

	public PaymentDocumentBrowser() {
		super("/fxml/PaymentDocumentBrowser.fxml");
		initUI();
		loadData();
	}

	@Override
	public String getTitle() {
		return "Payment Document Browser";
	}

	// other

	private void initUI()	{

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
		PickerHelper.setupDriverPicker(driver);

		table.getItems().setAll(paymentProcessBean.loadPaymentDocumentList(
				dateFrom.getValue(),
				dateTo.getValue(),
				driver
						.getSelectedObject()
						.map(SimpleObject::getId)
						.orElse(null),
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
		driver.clear();
		processed.setSelected(false);
	}

	@FXML
	private void handleView(ActionEvent event) {
		if(table.getSelectionModel().getSelectedItems().size() != 1)	{
			return;
		}
		final PaymentDocumentDto paymentDocumentDto = table.getSelectionModel().getSelectedItem();
		UIContext.get().openScreen(new PaymentDocumentView(paymentDocumentDto.getId()));
	}

	@FXML
	private void handleProcess(ActionEvent event) {
		if(table.getSelectionModel().isEmpty())	{
			return;
		}
		paymentProcessBean.processPaymentDocuments(table.getSelectionModel().getSelectedItems());
		loadData();
	}

}
