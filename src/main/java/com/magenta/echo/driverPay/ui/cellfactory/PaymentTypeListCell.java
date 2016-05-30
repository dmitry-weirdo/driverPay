package com.magenta.echo.driverpay.ui.cellfactory;

import com.magenta.echo.driverpay.core.enums.PaymentType;
import javafx.scene.control.ListCell;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 15-05-2016 19:00
 */
public class PaymentTypeListCell extends ListCell<PaymentType> {

	@Override
	protected void updateItem(PaymentType item, boolean empty) {
		super.updateItem(item, empty);
		if(empty || item == null)	{
			setText("");
		}else {
			setText(item.getLabel());
		}
	}

}
