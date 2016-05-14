package com.magenta.echo.driverpay.ui.cellfactory;

import com.magenta.echo.driverpay.core.enums.PaymentType;
import javafx.scene.control.ListCell;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 16:32
 */
public class TransactionTypeListCell extends ListCell<PaymentType> {
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
