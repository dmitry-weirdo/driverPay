/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package com.magenta.echo.driverPay.logic;

import com.magenta.echo.driverPay.*;

public class DriverFactory
{
	public static Driver createDriver(final long id, final String name) {
		final long balanceId = id * 10; // todo: improve
		final Balance balance = BalanceFactory.createDriverBalance(balanceId);

		final long depositBalanceId = id * 100; // todo: improve
		final Balance depositBalance = BalanceFactory.createDriverDepositBalance(depositBalanceId);

		final Driver driver = new DriverImpl();
		driver.setId(id);
		driver.setName(name);
		driver.setBalance(balance);
		driver.setDepositBalance(depositBalance);
		return driver;
	}
}