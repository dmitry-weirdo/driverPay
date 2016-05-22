package com.magenta.echo.driverpay.ui.cellfactory;

import com.magenta.echo.driverpay.core.enums.JobType;
import javafx.scene.control.ListCell;

import java.util.Optional;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 22-05-2016 02:44
 */
public class OptionalJobTypeListCell extends ListCell<Optional<JobType>> {

	private boolean forPrompt;

	public OptionalJobTypeListCell(boolean forPrompt) {
		this.forPrompt = forPrompt;
	}

	@Override
	protected void updateItem(Optional<JobType> item, boolean empty) {
		super.updateItem(item, empty);
		if(empty || item == null) {
			setText("");
		}else if(!item.isPresent())	{
			setText(forPrompt ? "Type" : "Nothing");
		}else {
			setText(item.get().getLabel());
		}
	}
}
