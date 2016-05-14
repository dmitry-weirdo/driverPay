package com.magenta.echo.driverpay.ui.screen;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.PaymentBean;
import com.magenta.echo.driverpay.core.entity.DriverDto;
import com.magenta.echo.driverpay.core.entity.PaymentDto;
import com.magenta.echo.driverpay.core.entity.PaymentReasonDto;
import com.magenta.echo.driverpay.core.enums.PaymentType;
import com.magenta.echo.driverpay.ui.Utils;
import com.magenta.echo.driverpay.ui.cellfactory.TransactionTypeListCell;
import com.magenta.echo.driverpay.ui.dialog.PaymentEdit;
import com.magenta.echo.driverpay.ui.dialog.SelectDriver;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 02:52
 */
public class PaymentReasonEdit extends Screen {

	private PaymentBean paymentBean = Context.get().getPaymentBean();

	@FXML private Label idField;
	@FXML private Button driverField;
	@FXML private TextField nameField;
	@FXML private ComboBox<PaymentType> typeField;

	@FXML private Button editPaymentButton;
	@FXML private Button removePaymentButton;

	@FXML private TableView<PaymentDto> paymentTable;
	@FXML private TableColumn<PaymentDto,Long> paymentIdColumn;
	@FXML private TableColumn<PaymentDto,String> paymentPlannedDateColumn;
	@FXML private TableColumn<PaymentDto,Double> paymentNetColumn;
	@FXML private TableColumn<PaymentDto,Double> paymentVatColumn;
	@FXML private TableColumn<PaymentDto,Double> paymentTotalColumn;
	@FXML private TableColumn<PaymentDto,String> paymentNominalCodeColumn;
	@FXML private TableColumn<PaymentDto,String> paymentTaxCodeColumn;
	@FXML private TableColumn<PaymentDto,String> statusColumn;

	private Long paymentReasonId;
	private PaymentReasonDto paymentReasonDto;
	private List<PaymentDto> paymentDtoList;

	public PaymentReasonEdit(final Long paymentReasonId) {
		super("/fxml/PaymentReasonEdit.fxml");
		this.paymentReasonId = paymentReasonId;
		initUI();
		loadData();
	}

	// other

	private void initUI()	{
		typeField.setButtonCell(new TransactionTypeListCell());
		typeField.setCellFactory(param -> new TransactionTypeListCell());
		typeField.getItems().setAll(Arrays.asList(
				PaymentType.CREDIT,
				PaymentType.DEDUCTION,
				PaymentType.DEPOSIT,
				PaymentType.RELEASE_DEPOSIT,
				PaymentType.TAKE_DEPOSIT,
				PaymentType.REFILL_DEPOSIT
		));
		typeField.setDisable(paymentReasonId != null);

		paymentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		paymentPlannedDateColumn.setCellValueFactory(new PropertyValueFactory<>("plannedDate"));
		paymentNetColumn.setCellValueFactory(new PropertyValueFactory<>("net"));
		paymentVatColumn.setCellValueFactory(new PropertyValueFactory<>("vat"));
		paymentTotalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
		paymentNominalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("nominalCode"));
		paymentTaxCodeColumn.setCellValueFactory(new PropertyValueFactory<>("taxCode"));
		statusColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus().getLabel()));


		paymentTable.getSelectionModel().selectedItemProperty().addListener(param -> {
			final boolean isNotSelected = paymentTable.getSelectionModel().isEmpty();
			editPaymentButton.setDisable(isNotSelected);
			removePaymentButton.setDisable(isNotSelected);
		});
	}

	private void loadData()	{
		if(paymentReasonId == null)	{
			paymentReasonDto = new PaymentReasonDto();
			paymentDtoList = new ArrayList<>();
		}else {
			paymentReasonDto = paymentBean.loadPaymentReason(paymentReasonId);
			paymentDtoList = paymentBean.loadPaymentList(paymentReasonId);
		}

		fillUI();
	}

	private void fillUI()	{
		idField.setText(Utils.toString(paymentReasonDto.getId()));
		nameField.setText(paymentReasonDto.getName());
		driverField.setText(Utils.toString(paymentReasonDto.getDriverValue()));
		typeField.getSelectionModel().select(paymentReasonDto.getType());

		paymentTable.getItems().setAll(paymentDtoList);
	}

	private void fillDto()	{
		paymentReasonDto.setName(nameField.getText());
		paymentReasonDto.setType(typeField.getValue());
		paymentDtoList = paymentTable.getItems();
	}

	// handlers

	@FXML
	private void handleSelectDriver(ActionEvent event) {
		final Optional<DriverDto> driverHolder = Context.get().openDialogAndWait(new SelectDriver());
		if(driverHolder.isPresent())	{
			driverField.setText(driverHolder.get().getName());
			paymentReasonDto.setDriverId(driverHolder.get().getId());
			paymentReasonDto.setDriverValue(driverHolder.get().getName());
		}
	}

	@FXML
	private void handleAddPayment(ActionEvent event) {
		final Optional<PaymentDto> paymentHolder = Context.get().openDialogAndWait(new PaymentEdit(null));
		if(paymentHolder.isPresent())	{
			paymentTable.getItems().add(paymentHolder.get());
		}
	}

	@FXML
	private void handleEditPayment(ActionEvent event) {
		if(paymentTable.getSelectionModel().isEmpty())	{
			return;
		}
		final PaymentDto selectedPaymentDto = paymentTable.getSelectionModel().getSelectedItem();

		final Optional<PaymentDto> paymentHolder = Context.get().openDialogAndWait(new PaymentEdit(selectedPaymentDto));
		if(paymentHolder.isPresent())	{
			paymentTable.getItems().remove(selectedPaymentDto);
			paymentTable.getItems().add(paymentHolder.get());
		}
	}

	@FXML
	private void handleRemovePayment(ActionEvent event) {
		if(paymentTable.getSelectionModel().isEmpty())	{
			return;
		}
		final PaymentDto selectedPaymentDto = paymentTable.getSelectionModel().getSelectedItem();

		paymentTable.getItems().remove(selectedPaymentDto);
	}

	@FXML
	private void handleApply(ActionEvent event) {
		fillDto();
		paymentBean.updatePaymentReason(paymentReasonDto, paymentDtoList);
		Context.get().openScreen(new PaymentReasonBrowser());
	}

	@FXML
	private void handleClose(ActionEvent event) {
		Context.get().openScreen(new PaymentReasonBrowser());
	}

}
