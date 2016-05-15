package com.magenta.echo.driverpay.ui.cellfactory;

import com.magenta.echo.driverpay.core.enums.PaymentStatus;
import javafx.scene.control.ListCell;

import java.util.Optional;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 15-05-2016 22:01
 */
public class OptionalPaymentStatusListCell extends ListCell<Optional<PaymentStatus>> {

	private boolean forPrompt;

	public OptionalPaymentStatusListCell(boolean forPrompt) {
		this.forPrompt = forPrompt;
	}

	@Override
	protected void updateItem(Optional<PaymentStatus> item, boolean empty) {
		super.updateItem(item, empty);
		if(empty || item == null)	{
			setText("");
		}else if(!item.isPresent())	{
			setText(forPrompt ? "Status" : "Nothing");
		}else {
			setText(item.get().getLabel());
		}
	}
}
