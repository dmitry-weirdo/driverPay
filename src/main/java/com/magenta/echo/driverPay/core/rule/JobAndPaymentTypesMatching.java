package com.magenta.echo.driverpay.core.rule;

import com.magenta.echo.driverpay.core.enums.JobType;
import com.magenta.echo.driverpay.core.enums.PaymentType;
import org.jetbrains.annotations.NotNull;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 26-05-2016 00:41
 */
public class JobAndPaymentTypesMatching {

	public static JobType from(@NotNull final PaymentType paymentType)	{
		switch(paymentType)	{
			case REGULAR_JOB:
				return JobType.REGULAR_JOB;
			case CASH_JOB:
				return JobType.CASH_JOB;
			default:
				throw new IllegalArgumentException(String.format("Unable to convert '%s' to JobType",paymentType));
		}
	}

	public static PaymentType from(@NotNull final JobType jobType)	{
		switch(jobType)	{
			case REGULAR_JOB:
				return PaymentType.REGULAR_JOB;
			case CASH_JOB:
				return PaymentType.CASH_JOB;
			default:
				throw new IllegalArgumentException(String.format("Unknown JobType '%s'",jobType));
		}
	}

}
