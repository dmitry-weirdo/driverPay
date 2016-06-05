package com.magenta.echo.driverpay.core;

import com.magenta.echo.driverpay.core.bean.factory.PaymentFactory;
import com.magenta.echo.driverpay.core.entity.Driver;
import com.magenta.echo.driverpay.core.entity.Payment;
import com.magenta.echo.driverpay.core.enums.PaymentType;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 05-06-2016 18:08
 */
public class TestUtils {

	public static Payment makePayment(
			@NotNull final Driver driver,
			@NotNull final PaymentType type
	)	{
		return PaymentFactory.build(
				driver,
				type,
				LocalDate.now(),
				10D,
				2D,
				12D,
				"-",
				"-"
		);
	}

}
