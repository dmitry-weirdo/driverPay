package com.magenta.echo.driverpay.core.bean.factory;

import com.magenta.echo.driverpay.core.entity.Balance;
import com.magenta.echo.driverpay.core.entity.Driver;
import com.magenta.echo.driverpay.core.enums.BalanceType;
import org.jetbrains.annotations.NotNull;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 28-05-2016 19:21
 */
public class BalanceFactory {

	public static Balance build(
			final @NotNull BalanceType type,
			@NotNull final Driver driver
	)    {
		final Balance balance = new Balance();
		balance.setType(type);
		balance.setDriver(driver);
		return balance;
	}

}
