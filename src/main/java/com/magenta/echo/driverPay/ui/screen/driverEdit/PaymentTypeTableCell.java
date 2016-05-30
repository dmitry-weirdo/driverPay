package com.magenta.echo.driverpay.ui.screen.driverEdit;

import com.magenta.echo.driverpay.core.enums.PaymentType;
import com.magenta.echo.driverpay.ui.util.PaymentTypeToIcon;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 29-05-2016 00:01
 */
public class PaymentTypeTableCell extends TableCell {
	@Override
	@SuppressWarnings("unchecked")
	protected void updateItem(Object item, boolean empty) {
		super.updateItem(item, empty);
		if(empty || item == null)	{
			setGraphic(null);
		}else{
			final PaymentType paymentType = (PaymentType)item;
			final ImageView imageView = new ImageView(PaymentTypeToIcon.fromPaymentType(paymentType));
			final Tooltip tooltip = new Tooltip(paymentType.getLabel());
			Tooltip.install(imageView, tooltip);
			setGraphic(imageView);

		}
	}
}
