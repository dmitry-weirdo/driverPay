package com.magenta.echo.driverpay.ui.cellfactory;

import com.magenta.echo.driverpay.core.entity.Driver;
import javafx.scene.control.ListCell;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 03:02
 */
public class DriverListCell extends ListCell<Driver> {
	@Override
	protected void updateItem(Driver item, boolean empty) {
		super.updateItem(item, empty);
		if(empty || item == null)	{
			setText("");
		}else {
			setText(item.getName());
		}
	}
}
