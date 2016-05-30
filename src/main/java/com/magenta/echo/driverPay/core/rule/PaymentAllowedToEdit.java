package com.magenta.echo.driverpay.core.rule;

import com.magenta.echo.driverpay.core.enums.PaymentType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 30-05-2016 01:31
 */
public class PaymentAllowedToEdit {

	public static List<PaymentType> getPaymentTypeAllowedForEdit()	{
		return Arrays.asList(
				PaymentType.CREDIT,
				PaymentType.DEDUCTION,
				PaymentType.DEPOSIT
		);
	}

	public static boolean isAllowed(@NotNull final PaymentType paymentType)	{
		return getPaymentTypeAllowedForEdit().contains(paymentType);
	}

	public static boolean isNotAllowed(@NotNull final PaymentType paymentType)	{
		return !isAllowed(paymentType);
	}
}
