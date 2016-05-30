package com.magenta.echo.driverpay.ui.util;

import com.magenta.echo.driverpay.core.enums.PaymentType;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 29-05-2016 00:02
 */
public class PaymentTypeToIcon {

	public static Image fromPaymentType(@NotNull final PaymentType paymentType)	{
		switch(paymentType)	{
			case REGULAR_JOB:
				return Constants.MONEY_PLUS;
			case CASH_JOB:
				return Constants.MONEY_MINUS;
			case CREDIT:
				return Constants.CREDIT_CARD_PLUS;
			case DEDUCTION:
				return Constants.CREDIT_CARD_MINUS;
			case DEPOSIT:
				return Constants.BANK_PLUS;
			case RELEASE_DEPOSIT:
				return Constants.BANK_MINUS;
			case TAKE_DEPOSIT:
				return Constants.BANK_ARROW;
			default:
				return Constants.MONEY_COIN;
		}
	}

}
