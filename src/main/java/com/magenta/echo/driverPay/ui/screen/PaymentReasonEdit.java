package com.magenta.echo.driverpay.ui.screen;

import com.evgenltd.kwickui.controls.objectpicker.ObjectPicker;
import com.evgenltd.kwickui.controls.objectpicker.SimpleObject;
import com.evgenltd.kwickui.core.Screen;
import com.evgenltd.kwickui.core.UIContext;
import com.evgenltd.kwickui.extensions.tableview.TableViewExtension;
import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.PaymentBean;
import com.magenta.echo.driverpay.core.entity.PaymentDto;
import com.magenta.echo.driverpay.core.entity.PaymentReasonDto;
import com.magenta.echo.driverpay.core.enums.PaymentType;
import com.magenta.echo.driverpay.ui.PickerHelper;
import com.magenta.echo.driverpay.ui.Utils;
import com.magenta.echo.driverpay.ui.cellfactory.TransactionTypeListCell;
import com.magenta.echo.driverpay.ui.dialog.PaymentEdit;
import com.magenta.echo.driverpay.ui.dialog.PaymentGenerationWizard;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

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
	@FXML private ObjectPicker<SimpleObject> driverField;
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
	@FXML private HBox totalsPane;

	private Long paymentReasonId;
	private PaymentReasonDto paymentReasonDto;
	private List<PaymentDto> paymentDtoList;

	public PaymentReasonEdit(final Long paymentReasonId) {
		super("/fxml/PaymentReasonEdit.fxml");
		this.paymentReasonId = paymentReasonId;
		initUI();
		loadData();
	}

	@Override
	public String getTitle() {
		return "Payment Reason Edit";
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

		paymentTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		paymentTable.getSelectionModel().selectedItemProperty().addListener(param -> {
			final boolean isNotSelected = paymentTable.getSelectionModel().isEmpty();
			final boolean isNonSingleSelection = paymentTable.getSelectionModel().getSelectedItems().size() != 1;
			editPaymentButton.setDisable(isNonSingleSelection);
			removePaymentButton.setDisable(isNotSelected);
		});

		TableViewExtension.setupDoubleClickEvent(paymentTable, this::handleEditPayment);
		TableViewExtension.setupTotalSupport(paymentTable,totalsPane,Arrays.asList(paymentNetColumn,paymentVatColumn,paymentTotalColumn));
	}

	private void loadData()	{
		PickerHelper.setupDriverPicker(driverField);

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
		if(paymentReasonDto.getDriverId() != null) {
			driverField.setSelectedObject(new SimpleObject(
					paymentReasonDto.getDriverId(),
					paymentReasonDto.getDriverValue()
			));
		}
		typeField.getSelectionModel().select(paymentReasonDto.getType());

		paymentTable.getItems().setAll(paymentDtoList);
	}

	private void fillDto()	{
		paymentReasonDto.setName(nameField.getText());
		paymentReasonDto.setType(typeField.getValue());
		paymentReasonDto.setDriverId(
				driverField
						.getSelectedObject()
						.map(SimpleObject::getId)
						.orElse(null)
		);
		paymentDtoList = paymentTable.getItems();
	}

	// handlers

	private void handleEditPayment(final PaymentDto payment)	{
		if(payment == null)	{
			return;
		}

		new PaymentEdit(payment)
				.showAndWait()
				.map(newPayment -> {
					paymentTable.getItems().remove(payment);
					paymentTable.getItems().add(newPayment);
					return null;
				});
	}

	@FXML
	private void handleAddPayment(ActionEvent event) {
		new PaymentEdit(null)
				.showAndWait()
				.map(payment -> {
					paymentTable.getItems().add(payment);
					return null;
				});
	}

	@FXML
	private void handleEditPayment(ActionEvent event) {
		if(paymentTable.getSelectionModel().isEmpty())	{
			return;
		}
		final PaymentDto selectedPaymentDto = paymentTable.getSelectionModel().getSelectedItem();

		new PaymentEdit(selectedPaymentDto)
				.showAndWait()
				.map(payment -> {
					paymentTable.getItems().remove(selectedPaymentDto);
					paymentTable.getItems().add(payment);
					return null;
				});
	}

	@FXML
	private void handleRemovePayment(ActionEvent event) {
		if(paymentTable.getSelectionModel().isEmpty())	{
			return;
		}
		final List<PaymentDto> selectedPaymentList = paymentTable.getSelectionModel().getSelectedItems();

		selectedPaymentList.forEach(payment -> paymentTable.getItems().remove(payment));
	}

	@FXML
	private void handleApply(ActionEvent event) {
		fillDto();
		paymentBean.updatePaymentReason(paymentReasonDto, paymentDtoList);
		handleClose(event);
	}

	@FXML
	private void handleClose(ActionEvent event) {
		UIContext.get().closeScreen();
	}

	@FXML
	private void handleGeneratePaymentsByTemplate(ActionEvent event) {
		final Optional<List<PaymentDto>> result = new PaymentGenerationWizard(paymentTable.getItems()).showAndWait();
		if(!result.isPresent())	{
			return;
		}

		paymentTable.getItems().addAll(result.get());
	}
}
