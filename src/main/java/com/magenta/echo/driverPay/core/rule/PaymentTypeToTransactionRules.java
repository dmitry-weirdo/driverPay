package com.magenta.echo.driverpay.core.rule;

import com.magenta.echo.driverpay.core.enums.BalanceType;
import com.magenta.echo.driverpay.core.enums.PaymentType;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 18:17
 */
public class PaymentTypeToTransactionRules {
	public static BalanceType getFromBalanceType(final PaymentType paymentType)	{
		switch(paymentType)	{
			case REGULAR_JOB:
			case CREDIT:
			case REFILL_DEPOSIT:
				return BalanceType.COMPANY;
			case CASH_JOB:
			case DEDUCTION:
			case DEPOSIT:
				return BalanceType.DRIVER;
			case RELEASE_DEPOSIT:
			case TAKE_DEPOSIT:
				return BalanceType.DEPOSIT;
			default:
				throw new IllegalArgumentException(String.format("Unknown PaymentType [%s]",paymentType));
		}
	}

	public static BalanceType getToBalanceType(final PaymentType paymentType)	{
		switch(paymentType)	{
			case CASH_JOB:
			case DEDUCTION:
			case TAKE_DEPOSIT:
				return BalanceType.COMPANY;
			case REGULAR_JOB:
			case CREDIT:
			case RELEASE_DEPOSIT:
				return BalanceType.DRIVER;
			case REFILL_DEPOSIT:
			case DEPOSIT:
				return BalanceType.DEPOSIT;
			default:
				throw new IllegalArgumentException(String.format("Unknown PaymentType [%s]",paymentType));
		}
	}
}
