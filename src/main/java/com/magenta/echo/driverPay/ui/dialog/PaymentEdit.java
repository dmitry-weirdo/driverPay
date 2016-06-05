package com.magenta.echo.driverpay.ui.dialog;

import com.evgenltd.kwick.ui.DialogScreen;
import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.BalanceBean;
import com.magenta.echo.driverpay.core.bean.dao.CommonDao;
import com.magenta.echo.driverpay.core.entity.Balance;
import com.magenta.echo.driverpay.core.entity.Driver;
import com.magenta.echo.driverpay.core.entity.Payment;
import com.magenta.echo.driverpay.core.entity.PaymentReason;
import com.magenta.echo.driverpay.core.enums.PaymentType;
import com.magenta.echo.driverpay.core.rule.PaymentAllowedToEdit;
import com.magenta.echo.driverpay.ui.cellfactory.PaymentTypeListCell;
import com.magenta.echo.driverpay.ui.util.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 29-05-2016 22:03
 */
public class PaymentEdit extends DialogScreen<Void> {

	@FXML private GridPane mainPane;
	@FXML private TextField name;
	@FXML private Label typeLabel;
	@FXML private ComboBox<PaymentType> type;
	@FXML private Label status;
	@FXML private DatePicker plannedDate;
	@FXML private TextField net;
	@FXML private TextField vat;
	@FXML private TextField total;
	@FXML private TextField nominalCode;
	@FXML private TextField taxCode;

	private Long driverId;
	private Long paymentReasonId;
	private Long paymentId;
	private PaymentType paymentType;
	private boolean restrictPaymentTypeChanging;
	private Payment payment = new Payment();
	private PaymentReason paymentReason = new PaymentReason();

	private CommonDao commonDao = Context.get().getCommonDao();
	private BalanceBean balanceBean = Context.get().getBalanceBean();

	public PaymentEdit(
			@NotNull final Long driverId,
			final Long paymentReasonId,
			final Long paymentId,
			final PaymentType paymentType,
			final boolean restrictPaymentTypeChanging
	) {
		super("/fxml/PaymentEdit.fxml");
		this.driverId = driverId;
		this.paymentReasonId = paymentReasonId;
		this.paymentId = paymentId;
		this.paymentType = paymentType;
		this.restrictPaymentTypeChanging = restrictPaymentTypeChanging;
		initUI();
		loadData();
	}

	@Override
	protected String getTitle() {
		return "Edit Payment";
	}

	private boolean isNew()	{
		return paymentId == null;
	}

	private boolean isPaymentReasonSpecified()	{
		return paymentReasonId != null;
	}

	private boolean isPaymentTypeSpecified()	{
		return paymentType != null;
	}

	// ##############################################################
	// #                                                            #
	// #                   Initializations                          #
	// #                                                            #
	// ##############################################################

	private void initUI()	{
		type.setButtonCell(new PaymentTypeListCell());
		type.setCellFactory(param -> new PaymentTypeListCell());
		type.getItems().setAll(Arrays.asList(
				PaymentType.CREDIT,
				PaymentType.DEDUCTION,
				PaymentType.DEPOSIT
		));

		getDialog().setTitle("Edit Payment");
		getDialog().getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
	}

	private void fillUI()	{
		status.setText(payment.getStatus() == null ? "" : payment.getStatus().getLabel());

		if(isNew() && isPaymentReasonSpecified()) {
			plannedDate.setValue(LocalDate.now());
			name.setText(Utils.toString(paymentReason.getName()));
			type.getSelectionModel().select(paymentReason.getPaymentType());
			net.setText(Utils.toString(paymentReason.getNet()));
			vat.setText(Utils.toString(paymentReason.getVat()));
			total.setText(Utils.toString(paymentReason.getTotal()));
			nominalCode.setText(Utils.toString(paymentReason.getNominalCode()));
			taxCode.setText(Utils.toString(paymentReason.getTaxCode()));
		}else if(isNew() && isPaymentTypeSpecified()) {
			plannedDate.setValue(LocalDate.now());
			name.setText(paymentType.getLabel());
			type.getSelectionModel().select(paymentType);
		}else {
			plannedDate.setValue(payment.getPlannedDate());
			name.setText(Utils.toString(payment.getName()));
			type.getSelectionModel().select(payment.getType());
			net.setText(Utils.toString(payment.getNet()));
			vat.setText(Utils.toString(payment.getVat()));
			total.setText(Utils.toString(payment.getTotal()));
			nominalCode.setText(Utils.toString(payment.getNominalCode()));
			taxCode.setText(Utils.toString(payment.getTaxCode()));
		}

		if(restrictPaymentTypeChanging)	{
			mainPane.getChildren().remove(typeLabel);
			mainPane.getChildren().remove(type);
		}
	}

	// ##############################################################
	// #                                                            #
	// #                       Load data                            #
	// #                                                            #
	// ##############################################################

	private void loadData()	{
		if(!isNew())	{
			payment = commonDao.find(Payment.class, paymentId);
			if(PaymentAllowedToEdit.isNotAllowed(payment.getType()))	{
				restrictPaymentTypeChanging = true;
			}
		}
		if(isPaymentReasonSpecified())	{
			paymentReason = commonDao.find(PaymentReason.class, paymentReasonId);
			payment.setPaymentReason(paymentReason);
		}
		fillUI();
	}

	// ##############################################################
	// #                                                            #
	// #                     Persist data                           #
	// #                                                            #
	// ##############################################################

	private void fillPayment()	{
		final Driver driver = new Driver();
		driver.setId(driverId);

		if(isNew() && isPaymentTypeSpecified())	{
			fillPaymentType(driver, paymentType);
		}else if(!restrictPaymentTypeChanging)	{
			fillPaymentType(driver, this.type.getSelectionModel().getSelectedItem());
		}

		payment.setDriver(driver);
		payment.setName(name.getText());
		payment.setPlannedDate(plannedDate.getValue());
		payment.setNet(Double.parseDouble(net.getText()));
		payment.setVat(Double.parseDouble(vat.getText()));
		payment.setTotal(Double.parseDouble(total.getText()));
		payment.setNominalCode(nominalCode.getText());
		payment.setTaxCode(taxCode.getText());

	}

	private void fillPaymentType(@NotNull final Driver driver, @NotNull final PaymentType paymentType)	{
		final Balance from = balanceBean.getBalanceFrom(driver,paymentType);
		final Balance to = balanceBean.getBalanceTo(driver,paymentType);

		payment.setType(paymentType);
		payment.setFrom(from);
		payment.setTo(to);
	}

	@Override
	protected Void resultConverter(ButtonType buttonType) {
		if(ButtonType.OK.equals(buttonType))	{
			fillPayment();
			if(paymentId == null)	{
				commonDao.insert(payment);
			}else {
				commonDao.update(payment);
			}
		}
		return null;
	}
}
