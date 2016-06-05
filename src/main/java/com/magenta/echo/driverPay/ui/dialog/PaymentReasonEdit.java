package com.magenta.echo.driverpay.ui.dialog;

import com.evgenltd.kwick.controls.objectbrowser.ObjectBrowser;
import com.evgenltd.kwick.ui.DialogScreen;
import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.BalanceBean;
import com.magenta.echo.driverpay.core.bean.PaymentLoader;
import com.magenta.echo.driverpay.core.bean.dao.CommonDao;
import com.magenta.echo.driverpay.core.entity.Balance;
import com.magenta.echo.driverpay.core.entity.Driver;
import com.magenta.echo.driverpay.core.entity.Payment;
import com.magenta.echo.driverpay.core.entity.PaymentReason;
import com.magenta.echo.driverpay.core.enums.PaymentType;
import com.magenta.echo.driverpay.core.enums.ScheduleType;
import com.magenta.echo.driverpay.ui.cellfactory.PaymentTypeListCell;
import com.magenta.echo.driverpay.ui.cellfactory.ScheduleTypeListCell;
import com.magenta.echo.driverpay.ui.util.ObjectBrowserHelper;
import com.magenta.echo.driverpay.ui.util.Utils;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SegmentedButton;
import org.jetbrains.annotations.NotNull;

import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 29-05-2016 20:17
 */
public class PaymentReasonEdit extends DialogScreen<Void> {

	@FXML private HBox toggleButtonPane;
	private SegmentedButton toggleButtonGroup;
	@FXML private ToggleButton toggleButtonGeneral;
	@FXML private ToggleButton toggleButtonPayments;

	// ==== general ====
	@FXML private HBox generalPane;
	@FXML private TextField name;
	@FXML private ComboBox<PaymentType> paymentType;
	@FXML private TextField nominalCode;
	@FXML private TextField taxCode;

	@FXML private VBox schedulePane;
	@FXML private HBox amountsPane;
	@FXML private GridPane nvtPane;
	@FXML private GridPane incrementalPane;
	@FXML private HBox everyPane;
	@FXML private HBox repeatPane;

	@FXML private ComboBox<ScheduleType> scheduleType;

	@FXML private TextField net;
	@FXML private TextField vat;
	@FXML private TextField total;

	@FXML private TextField opening;
	@FXML private TextField gross;
	@FXML private TextField frequencyValue;
	@FXML private ComboBox<ChronoUnit> frequencyType;

	@FXML private DatePicker endDate;
	@FXML private CheckBox endless;

	// ==== payments ====
	@FXML private VBox paymentsPane;
	@FXML private ObjectBrowser<Payment> paymentBrowser;

	// ==== data model ====
	private Long driverId;
	private Long paymentReasonId;
	private PaymentReason paymentReason = new PaymentReason();
	private List<Payment> paymentList = new ArrayList<>();

	// ==== bean ====
	private CommonDao commonDao = Context.get().getCommonDao();
	private PaymentLoader paymentLoader = Context.get().getPaymentLoader();
	private BalanceBean balanceBean = Context.get().getBalanceBean();

	public PaymentReasonEdit(@NotNull final Long driverId, final Long paymentReasonId) {
		super("/fxml/PaymentReasonEdit.fxml");
		this.driverId = driverId;
		this.paymentReasonId = paymentReasonId;
		initUI();
		loadData();
		loadPaymentsData();
	}

	public PaymentReasonEdit(@NotNull final Long driverId, @NotNull final PaymentType paymentType) {
		this(driverId, (Long)null);
		this.paymentType.getSelectionModel().select(paymentType);
	}

	@Override
	protected String getTitle() {
		return "Edit Payment Reason";
	}

	// ##############################################################
	// #                                                            #
	// #                   Initializations                          #
	// #                                                            #
	// ##############################################################

	private void initUI()	{

		getDialog().setTitle("Edit Payment Reason");
		getDialog().getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);

		toggleButtonPane.getChildren().clear();
		toggleButtonGroup = new SegmentedButton(toggleButtonGeneral,toggleButtonPayments);
		toggleButtonPane.getChildren().add(toggleButtonGroup);
		toggleButtonGroup.getToggleGroup().selectedToggleProperty().addListener(param -> updateSelectedPaneState());
		toggleButtonGroup.getToggleGroup().getToggles().addListener((InvalidationListener)observable -> {
			if(toggleButtonGroup.getToggleGroup().getToggles().size() == 2)	{
				updateSelectedPaneState();
			}
		});

		paymentType.setCellFactory(param -> new PaymentTypeListCell());
		paymentType.setButtonCell(new PaymentTypeListCell());
		paymentType.getItems().setAll(Arrays.asList(
				PaymentType.CREDIT,
				PaymentType.DEDUCTION,
				PaymentType.DEPOSIT
		));

		scheduleType.setCellFactory(param -> new ScheduleTypeListCell());
		scheduleType.setButtonCell(new ScheduleTypeListCell());
		scheduleType.getItems().setAll(Arrays.asList(
				ScheduleType.NONE,
				ScheduleType.REPEAT,
				ScheduleType.INCREMENTAL
		));
		scheduleType.getSelectionModel().selectedItemProperty().addListener(param -> updateSchedulePaneState());

		frequencyType.getItems().addAll(ChronoUnit.DAYS, ChronoUnit.WEEKS, ChronoUnit.MONTHS);

		endless.selectedProperty().addListener(param -> updateEndDateState());

		ObjectBrowserHelper.setupPaymentTable(paymentBrowser);
		paymentBrowser.addButton("Add", ObjectBrowser.NodeType.ADD, this::handleAdd);
		paymentBrowser.addButton("Edit", ObjectBrowser.NodeType.EDIT, this::handleEdit);
		paymentBrowser.addButton("Remove", ObjectBrowser.NodeType.REMOVE, this::handleRemove);

		updateSelectedPaneState();
	}

	private void fillUI()	{
		name.setText(paymentReason.getName());
		paymentType.getSelectionModel().select(paymentReason.getPaymentType());
		scheduleType.getSelectionModel().select(paymentReason.getScheduleType() == null ? ScheduleType.NONE : paymentReason.getScheduleType());
		nominalCode.setText(Utils.toString(paymentReason.getNominalCode()));
		taxCode.setText(Utils.toString(paymentReason.getTaxCode()));

		net.setText(Utils.toString(paymentReason.getNet()));
		vat.setText(Utils.toString(paymentReason.getVat()));
		total.setText(Utils.toString(paymentReason.getTotal()));

		opening.setText(Utils.toString(paymentReason.getOpeningBalance()));
		gross.setText(Utils.toString(paymentReason.getGross()));

		final int frequencyDays = Utils.periodToDays(paymentReason.getFrequency());
		frequencyValue.setText(Utils.toString(frequencyDays));
		frequencyType.getSelectionModel().select(ChronoUnit.DAYS);

		endDate.setValue(paymentReason.getEndDate());
		endless.setSelected(paymentReason.getEndDate() == null);
	}

	private void fillPayments()	{
		paymentBrowser.getItems().setAll(paymentList);
	}

	// ##############################################################
	// #                                                            #
	// #                       Load data                            #
	// #                                                            #
	// ##############################################################

	private void loadData()	{
		if(paymentReasonId != null)	{
			paymentReason = commonDao.find(PaymentReason.class, paymentReasonId);
		}
		fillUI();
	}

	private void loadPaymentsData()	{
		if(paymentReasonId != null)	{
			paymentList = paymentLoader.loadPaymentList(paymentReasonId);
		}
		fillPayments();
	}

	// ##############################################################
	// #                                                            #
	// #                     Persist data                           #
	// #                                                            #
	// ##############################################################

	private void fillPaymentReason()	{

		final Driver driver = new Driver();
		driver.setId(driverId);

		final PaymentType paymentType = this.paymentType.getSelectionModel().getSelectedItem();
		final Balance from = balanceBean.getBalanceFrom(driver,paymentType);
		final Balance to = balanceBean.getBalanceTo(driver,paymentType);

		paymentReason.setDriver(driver);
		paymentReason.setName(name.getText());
		paymentReason.setPaymentType(paymentType);
		paymentReason.setFrom(from);
		paymentReason.setTo(to);
		paymentReason.setNominalCode(nominalCode.getText());
		paymentReason.setTaxCode(taxCode.getText());

		final ScheduleType selectedScheduleType = scheduleType.getSelectionModel().getSelectedItem();
		paymentReason.setScheduleType(selectedScheduleType);

		switch(selectedScheduleType)	{
			case INCREMENTAL:
				paymentReason.setOpeningBalance(Double.parseDouble(opening.getText()));
				paymentReason.setGross(Double.parseDouble(gross.getText()));
				paymentReason.setNet(Double.parseDouble(net.getText()));
				paymentReason.setVat(Double.parseDouble(vat.getText()));
				paymentReason.setTotal(Double.parseDouble(total.getText()));
				paymentReason.setFrequency(Utils.ofPeriod(
						Integer.parseInt(frequencyValue.getText()),
						frequencyType.getSelectionModel().getSelectedItem()
				));
				paymentReason.setEndDate(null);
				break;
			case REPEAT:
				paymentReason.setNet(Double.parseDouble(net.getText()));
				paymentReason.setVat(Double.parseDouble(vat.getText()));
				paymentReason.setTotal(Double.parseDouble(total.getText()));
				paymentReason.setFrequency(Utils.ofPeriod(
						Integer.parseInt(frequencyValue.getText()),
						frequencyType.getSelectionModel().getSelectedItem()
				));
				paymentReason.setEndDate(endDate.getValue());
				break;
			default:
				paymentReason.setOpeningBalance(0D);
				paymentReason.setGross(0D);
				paymentReason.setNet(0D);
				paymentReason.setVat(0D);
				paymentReason.setTotal(0D);
				paymentReason.setFrequency(Period.ZERO);
				paymentReason.setEndDate(null);
				break;
		}
	}

	@Override
	protected Void resultConverter(ButtonType buttonType) {
		if(Objects.equals(buttonType, ButtonType.OK))	{
			fillPaymentReason();
			if(paymentReasonId == null)	{
				commonDao.insert(paymentReason);
			}else {
				commonDao.update(paymentReason);
			}
		}
		return null;
	}


	// ##############################################################
	// #                                                            #
	// #                    Service methods                         #
	// #                                                            #
	// ##############################################################

	// actions

	private void openGeneralPane()	{
		toggleButtonGroup.getToggleGroup().selectToggle(toggleButtonGeneral);
	}

	private void openPaymentsPane()	{
		toggleButtonGroup.getToggleGroup().selectToggle(toggleButtonPayments);
	}

	private void handleAdd(ActionEvent event)	{
		new PaymentEdit(
				driverId,
				paymentReasonId,
				null,
				null,
				false
		).showAndWait();
		loadPaymentsData();
	}

	private void handleEdit(ActionEvent event)	{
		if(paymentBrowser.isSelectionEmpty())	{
			return;
		}

		final Payment selectedPayment = paymentBrowser.getSelectedItem();
		new PaymentEdit(
				driverId,
				paymentReasonId,
				selectedPayment.getId(),
				null,
				false
		).showAndWait();
		loadPaymentsData();
	}

	private void handleRemove(ActionEvent event)	{
		if(paymentBrowser.isSelectionEmpty())	{
			return;
		}

		final Payment selectedPayment = paymentBrowser.getSelectedItem();
		commonDao.delete(selectedPayment);
		loadPaymentsData();
	}

	// update state

	private void updateSelectedPaneState()	{
		toggleButtonPayments.setDisable(paymentReasonId == null);

		final boolean isGeneralPaneVisible = toggleButtonGeneral.isSelected();
		generalPane.setVisible(isGeneralPaneVisible);
		generalPane.setDisable(!isGeneralPaneVisible);

		final boolean isPaymentsPaneVisible = toggleButtonPayments.isSelected();
		paymentsPane.setVisible(isPaymentsPaneVisible);
		paymentsPane.setDisable(!isPaymentsPaneVisible);

		if(toggleButtonGroup.getToggleGroup().getSelectedToggle() == null)	{
			openGeneralPane();
		}
	}

	private void updateSchedulePaneState()	{
		final ScheduleType selectedScheduleType = scheduleType.getSelectionModel().getSelectedItem();
		amountsPane.getChildren().remove(nvtPane);
		amountsPane.getChildren().remove(incrementalPane);
		schedulePane.getChildren().remove(everyPane);
		schedulePane.getChildren().remove(repeatPane);

		if(selectedScheduleType == null)	{
			return;
		}

		switch(selectedScheduleType)	{
			case INCREMENTAL:
				amountsPane.getChildren().add(nvtPane);
				schedulePane.getChildren().add(everyPane);
				amountsPane.getChildren().add(incrementalPane);
				break;
			case REPEAT:
				amountsPane.getChildren().add(nvtPane);
				schedulePane.getChildren().add(everyPane);
				schedulePane.getChildren().add(repeatPane);
				break;
		}
	}

	private void updateEndDateState()	{
		endDate.setDisable(endless.isSelected());
	}

}
