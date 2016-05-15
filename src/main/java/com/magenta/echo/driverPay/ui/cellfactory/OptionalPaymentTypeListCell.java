package com.magenta.echo.driverpay.ui.cellfactory;

import com.magenta.echo.driverpay.core.enums.PaymentType;
import javafx.scene.control.ListCell;

import java.util.Optional;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 15-05-2016 19:00
 */
public class OptionalPaymentTypeListCell extends ListCell<Optional<PaymentType>> {

	private boolean forPrompt;

	public OptionalPaymentTypeListCell(boolean forPrompt) {
		this.forPrompt = forPrompt;
	}

	@Override
	protected void updateItem(Optional<PaymentType> item, boolean empty) {
		super.updateItem(item, empty);
		if(empty || item == null)	{
			setText("");
		}else if(!item.isPresent()) {
			setText(forPrompt ? "Type" : "Nothing");
		}else {
			setText(item.get().getLabel());
		}
	}
}
