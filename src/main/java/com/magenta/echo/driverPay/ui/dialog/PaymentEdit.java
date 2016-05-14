package com.magenta.echo.driverpay.ui.dialog;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.PaymentBean;
import com.magenta.echo.driverpay.core.entity.PaymentDto;
import com.magenta.echo.driverpay.ui.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 02:37
 */
public class PaymentEdit extends DialogExt<PaymentDto> {

	private PaymentBean paymentBean = Context.get().getPaymentBean();

	@FXML private Label id;
	@FXML private Label type;
	@FXML private Label status;
	@FXML private DatePicker plannedDate;
	@FXML private TextField net;
	@FXML private TextField vat;
	@FXML private TextField total;
	@FXML private TextField nominalCode;
	@FXML private TextField taxCode;

	private PaymentDto paymentDto;

	public PaymentEdit(final PaymentDto paymentDto) {
		super("/fxml/PaymentEdit.fxml");
		this.paymentDto = paymentDto;
		initUI();
		loadData();
	}

	// other

	private void initUI()	{
		getDialog().setTitle("Edit Payment");
		getDialog().getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
	}

	private void loadData()	{
		if(paymentDto == null)	{
			paymentDto = new PaymentDto();
		}
		fillUI();
	}

	private void fillUI()	{
		id.setText(Utils.toString(paymentDto.getId()));
		type.setText(paymentDto.getType() == null ? "" : paymentDto.getType().getLabel());
		status.setText(paymentDto.getStatus() == null ? "" : paymentDto.getStatus().getLabel());
		plannedDate.setValue(paymentDto.getPlannedDate());
		net.setText(Utils.toString(paymentDto.getNet()));
		vat.setText(Utils.toString(paymentDto.getVat()));
		total.setText(Utils.toString(paymentDto.getTotal()));
		nominalCode.setText(Utils.toString(paymentDto.getNominalCode()));
		taxCode.setText(Utils.toString(paymentDto.getTaxCode()));
	}

	private void fillDto()	{
		paymentDto.setPlannedDate(plannedDate.getValue());
		paymentDto.setNet(Double.parseDouble(net.getText()));
		paymentDto.setVat(Double.parseDouble(vat.getText()));
		paymentDto.setTotal(Double.parseDouble(total.getText()));
		paymentDto.setNominalCode(nominalCode.getText());
		paymentDto.setTaxCode(taxCode.getText());

	}

	// handlers

	@Override
	protected PaymentDto resultConverter(ButtonType buttonType) {
		if(ButtonType.APPLY.equals(buttonType))	{
			fillDto();
			return paymentDto;
		}
		return null;
	}
}
