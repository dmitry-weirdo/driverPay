package com.magenta.echo.driverpay.ui.screen.driverEdit;

import com.magenta.echo.driverpay.core.entity.PaymentDocument;
import com.magenta.echo.driverpay.ui.util.Constants;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;

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
			final ImageView imageView = new ImageView(item.getProcessed()
					? Constants.DOCUMENT_INVOICE_TICK
					: Constants.DOCUMENT_INVOICE
			);
			setText(item.getPaymentDate().toString());
			setGraphic(imageView);
		}
	}
}
