package com.magenta.echo.driverpay.ui.screen;

import com.evgenltd.kwickui.controls.objectpicker.ObjectPicker;
import com.evgenltd.kwickui.controls.objectpicker.SimpleObject;
import com.evgenltd.kwickui.core.Screen;
import com.evgenltd.kwickui.core.UIContext;
import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.PaymentBean;
import com.magenta.echo.driverpay.core.bean.SalaryCalculationBean;
import com.magenta.echo.driverpay.core.entity.PaymentDto;
import com.magenta.echo.driverpay.core.enums.PaymentStatus;
import com.magenta.echo.driverpay.core.enums.PaymentType;
import com.magenta.echo.driverpay.ui.PickerHelper;
import com.magenta.echo.driverpay.ui.cellfactory.OptionalPaymentStatusListCell;
import com.magenta.echo.driverpay.ui.cellfactory.OptionalPaymentTypeListCell;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
	@FXML private ObjectPicker<SimpleObject> driver;
	@FXML private ComboBox<Optional<PaymentType>> type;
	@FXML private ComboBox<Optional<PaymentStatus>> status;

	@FXML private Button makePaymentsButton;

	@FXML private TableView<PaymentDto> table;
	@FXML private TableColumn<PaymentDto,Long> idColumn;
	@FXML private TableColumn<PaymentDto,String> driverColumn;
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

	@Override
	public String getTitle() {
		return "Salary Calculation";
	}

	// other

	private void initUI()	{

		type.setCellFactory(param -> new OptionalPaymentTypeListCell(false));
		type.setButtonCell(new OptionalPaymentTypeListCell(true));
		type.getItems().addAll(Arrays.asList(
				Optional.empty(),
				Optional.of(PaymentType.REGULAR_JOB),
				Optional.of(PaymentType.CASH_JOB),
				Optional.of(PaymentType.CREDIT),
				Optional.of(PaymentType.DEDUCTION),
				Optional.of(PaymentType.DEPOSIT),
				Optional.of(PaymentType.RELEASE_DEPOSIT),
				Optional.of(PaymentType.TAKE_DEPOSIT),
				Optional.of(PaymentType.REFILL_DEPOSIT)
		));
		type.getSelectionModel().select(Optional.empty());

		status.setCellFactory(param -> new OptionalPaymentStatusListCell(false));
		status.setButtonCell(new OptionalPaymentStatusListCell(true));
		status.getItems().addAll(Arrays.asList(
				Optional.empty(),
				Optional.of(PaymentStatus.NONE),
				Optional.of(PaymentStatus.CALCULATED)
		));
		status.getSelectionModel().select(Optional.of(PaymentStatus.NONE));

		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		driverColumn.setCellValueFactory(new PropertyValueFactory<>("driverValue"));
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
		PickerHelper.setupDriverPicker(driver);

		final List<PaymentDto> PaymentDtoList = paymentBean.loadPaymentList(
				dateFrom.getValue(),
				dateTo.getValue(),
				driver.getSelectedObject().map(SimpleObject::getId).orElse(null),
				type.getSelectionModel().getSelectedItem().orElse(null),
				status.getSelectionModel().getSelectedItem().orElse(null)
		);
		table.getItems().setAll(PaymentDtoList);
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
		type.getSelectionModel().select(Optional.empty());
		status.getSelectionModel().select(Optional.empty());
	}

	@FXML
	private void handleOpenPayments(ActionEvent event) {
		UIContext.get().openScreen(new PaymentDocumentBrowser());
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
