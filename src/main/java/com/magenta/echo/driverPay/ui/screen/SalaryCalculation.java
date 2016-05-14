package com.magenta.echo.driverpay.ui.screen;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.PaymentBean;
import com.magenta.echo.driverpay.core.bean.SalaryCalculationBean;
import com.magenta.echo.driverpay.core.entity.PaymentDto;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 03:25
 */
public class SalaryCalculation extends Screen {

	
	private SalaryCalculationBean salaryCalculationBean = Context.get().getSalaryCalculationBean();
	private PaymentBean paymentBean = Context.get().getPaymentBean();

	@FXML private DatePicker dateTo;
	@FXML private DatePicker dateFrom;
	@FXML private Button makePaymentsButton;
	@FXML private TableView<PaymentDto> table;
	@FXML private TableColumn<PaymentDto,Long> idColumn;
	@FXML private TableColumn<PaymentDto,String> typeColumn;
	@FXML private TableColumn<PaymentDto,String> statusColumn;
	@FXML private TableColumn<PaymentDto,String> dateColumn;
	@FXML private TableColumn<PaymentDto,Double> netColumn;
	@FXML private TableColumn<PaymentDto,Double> vatColumn;
	@FXML private TableColumn<PaymentDto,Double> totalColumn;


	public SalaryCalculation() {
		super("/fxml/SalaryCalculation.fxml");
		initUI();
		loadData();
	}

	// other

	private void initUI()	{
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		typeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getLabel()));
		statusColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus().getLabel()));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("plannedDate"));
		netColumn.setCellValueFactory(new PropertyValueFactory<>("net"));
		vatColumn.setCellValueFactory(new PropertyValueFactory<>("vat"));
		totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table.getSelectionModel().selectedItemProperty().addListener(param -> makePaymentsButton.setDisable(table.getSelectionModel().isEmpty()));

	}

	private void loadData()	{
		final List<PaymentDto> PaymentDtoList = paymentBean.loadPaymentList(dateFrom.getValue(), dateTo.getValue());
		table.getItems().setAll(PaymentDtoList);
	}

	// handlers

	@FXML
	private void handleSearch(ActionEvent event) {
		loadData();
	}

	@FXML
	private void handleOpenPayments(ActionEvent event) {
		Context.get().openScreen(new PaymentDocumentBrowser());
	}

	@FXML
	private void handleMakePayments(ActionEvent event) {
		if(table.getSelectionModel().isEmpty())	{
			return;
		}
		salaryCalculationBean.makePaymentDocuments(table.getSelectionModel().getSelectedItems());
		loadData();
	}
}
