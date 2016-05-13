/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package com.magenta.echo.driverPay.logic;

import com.magenta.echo.driverPay.Balance;
import com.magenta.echo.driverPay.BalanceImpl;
import com.magenta.echo.driverPay.BalanceType;

public class BalanceFactory
{
	public static Balance createBalance(final Long id, final BalanceType type) {
		final Balance balance = new BalanceImpl();
		balance.setId(id);
		balance.setTotal(0d);
		balance.setType(type);
		return balance;
	}

	public static Balance createCompanyBalance(final Long id) {
		return createBalance(id, BalanceType.COMPANY);
	}
	public static Balance createDriverBalance(final Long id) {
		return createBalance(id, BalanceType.DRIVER);
	}
	public static Balance createDriverDepositBalance(final Long id) {
		return createBalance(id, BalanceType.DRIVER_DEPOSIT);
	}
}