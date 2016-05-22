package com.magenta.echo.driverpay.ui.dialog;

import com.evgenltd.kwickui.core.DialogScreen;
import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.PaymentGenerationBean;
import com.magenta.echo.driverpay.core.entity.PaymentDto;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 15-05-2016 20:18
 */
public class PaymentGenerationWizard extends DialogScreen<List<PaymentDto>> {

	private static final String INCREMENTAL = "Incremental";
	private static final String REPEAT = "Repeat";

	private PaymentGenerationBean paymentGenerationBean = Context.get().getPaymentGenerationBean();

	@FXML private ChoiceBox<String> type;
	@FXML private TextField net;
	@FXML private TextField vat;
	@FXML private TextField total;
	@FXML private HBox incrementalTotalPane;
	@FXML private TextField totalNet;
	@FXML private TextField totalVat;
	@FXML private TextField gross;
	@FXML private TextField frequency;
	@FXML private ComboBox<ChronoUnit> frequencyPeriod;
	@FXML private HBox repeatDurationPane;
	@FXML private TextField duration;
	@FXML private ComboBox<ChronoUnit> durationPeriod;

	private List<PaymentDto> alreadyCreatedPayments;

	public PaymentGenerationWizard(final List<PaymentDto> alreadyCreatedPayments) {
		super("/fxml/PaymentGenerationWizard.fxml");
		this.alreadyCreatedPayments = alreadyCreatedPayments;
		initUI();
	}

	@Override
	protected String getTitle() {
		return "Payment Generation Wizard";
	}

	// other

	private void initUI()	{

		getDialog().setTitle("Payment Generation Wizard");
		getDialog().getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);

		frequencyPeriod.getItems().addAll(ChronoUnit.DAYS, ChronoUnit.WEEKS, ChronoUnit.MONTHS);
		durationPeriod.getItems().addAll(ChronoUnit.DAYS, ChronoUnit.WEEKS, ChronoUnit.MONTHS);

		type.getItems().addAll(INCREMENTAL, REPEAT);
		type.getSelectionModel().selectedItemProperty().addListener(this::handleTypeChanged);
		type.getSelectionModel().select(INCREMENTAL);
	}

	private List<PaymentDto> generateIncrementalPayments()	{
		return paymentGenerationBean.generateIncrementalPayments(
				alreadyCreatedPayments,
				Double.parseDouble(net.getText()),
				Double.parseDouble(vat.getText()),
				Double.parseDouble(total.getText()),
				Double.parseDouble(totalNet.getText()),
				Double.parseDouble(totalVat.getText()),
				Double.parseDouble(gross.getText()),
				ofPeriod(
						Integer.parseInt(frequency.getText()),
						frequencyPeriod.getSelectionModel().getSelectedItem()
				)
		);
	}

	private List<PaymentDto> generateRepeatPayments()	{
		return paymentGenerationBean.generateRepeatPayments(
				alreadyCreatedPayments,
				Double.parseDouble(net.getText()),
				Double.parseDouble(vat.getText()),
				Double.parseDouble(total.getText()),
				ofPeriod(
						Integer.parseInt(frequency.getText()),
						frequencyPeriod.getSelectionModel().getSelectedItem()
				),
				ofPeriod(
						Integer.parseInt(duration.getText()),
						durationPeriod.getSelectionModel().getSelectedItem()
				)
		);
	}

	// handlers

	@Override
	protected List<PaymentDto> resultConverter(ButtonType buttonType) {
		if(buttonType.equals(ButtonType.OK))	{
			return type.getValue().equals(INCREMENTAL)	// returns only NEW payments
					? generateIncrementalPayments()
					: generateRepeatPayments();
		}
		return null;
	}

	private void handleTypeChanged(final Observable observable)	{

		final String selectedType = type.getSelectionModel().getSelectedItem();

		incrementalTotalPane.setVisible(  selectedType.equals(INCREMENTAL)	);
		incrementalTotalPane.setDisable( !selectedType.equals(INCREMENTAL)	);
		repeatDurationPane.setVisible(    selectedType.equals(REPEAT)		);
		repeatDurationPane.setDisable(   !selectedType.equals(REPEAT)		);

	}

	// util

	private static Period ofPeriod(final int frequency, final ChronoUnit chronoUnit)	{
		switch(chronoUnit)	{
			case DAYS:
				return Period.ofDays(frequency);
			case WEEKS:
				return Period.ofWeeks(frequency);
			case MONTHS:
				return Period.ofMonths(frequency);
			default:
				throw new IllegalArgumentException("ChronoUnit not supported, +"+chronoUnit);
		}
	}
}
