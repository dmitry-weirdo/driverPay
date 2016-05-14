package com.magenta.echo.driverpay.ui.cellfactory;

import com.magenta.echo.driverpay.core.enums.ChargeStatus;
import javafx.scene.control.ListCell;

import java.util.Optional;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 13-05-2016 01:30
 */
public class OptionalChargeStatusListCell extends ListCell<Optional<ChargeStatus>> {

	private boolean withPrompt;

	public OptionalChargeStatusListCell(boolean withPrompt) {
		this.withPrompt = withPrompt;
	}

	@Override
	protected void updateItem(Optional<ChargeStatus> item, boolean empty) {
		super.updateItem(item, empty);
		if(empty || item == null) {

			setText("");

		}else if(item.isPresent()) {

			setText(item.get().getLabel());

		}else if(!item.isPresent() && withPrompt)	{

			setText("Select status");

		}else {

			setText("Nothing");

		}
	}
}
