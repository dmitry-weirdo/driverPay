package com.magenta.echo.driverpay.ui.screen.driverEdit;

import com.magenta.echo.driverpay.core.entity.PaymentDocument;
import com.magenta.echo.driverpay.core.enums.PaymentDocumentMethod;
import com.magenta.echo.driverpay.ui.util.Constants;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.util.Objects;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 29-05-2016 00:32
 */
public class PaymentDocumentListCell extends ListCell<PaymentDocument> {
	@Override
	protected void updateItem(PaymentDocument item, boolean empty) {
		super.updateItem(item, empty);
		if(empty || item == null)	{
			setText("");
			setGraphic(null);
		}else {

			final BorderPane borderPane = new BorderPane();

			final ImageView processed = new ImageView(item.getProcessed()
					? Constants.DOCUMENT_INVOICE_TICK
					: Constants.DOCUMENT_INVOICE
			);
			borderPane.setLeft(processed);

			final Label content = new Label(String.format(
					"%s from %s",
					item.getType().getLabel(),
					item.getPaymentDate()
			));
			BorderPane.setAlignment(content, Pos.CENTER_LEFT);
			borderPane.setCenter(content);

			if(Objects.equals(PaymentDocumentMethod.CASH, item.getMethod()))	{
				final ImageView method = new ImageView(Constants.MONEY_COIN);
				borderPane.setRight(method);
			}

			setGraphic(borderPane);
		}
	}
}
