/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package com.magenta.echo.driverPay.logic;

import com.magenta.echo.driverPay.Balance;
import com.magenta.echo.driverPay.Transaction;

import java.util.Date;

public class PaymentHelper
{
	public static void process(final Transaction transaction) {
		move( transaction.getFromBalance(), transaction.getToBalance(), transaction.getTotal() );
		transaction.setDate( new Date() ); // todo: think about this;
		// todo: set transaction status if required
	}

	public static void move(final Balance fromBalance, final Balance toBalance, final double value) {
		// todo: check both balances for nulls and throw IllArgExc in this case
		// todo: check that balances are not the same and throw IllArgExc in this case

		if (value == 0)
			return;

		final double fromBalanceTotal = getDouble( fromBalance.getTotal() );
		final double toBalanceTotal = getDouble( toBalance.getTotal() );

		final double newFromBalanceTotal = fromBalanceTotal - value; // todo: use EchoUtils.subtractDouble
		final double newToBalanceTotal = toBalanceTotal + value; // todo: use EchoUtils.subtractDouble

		fromBalance.setTotal(newFromBalanceTotal);
		toBalance.setTotal(newToBalanceTotal);
	}

	public static double getDouble(final Double value) { // todo: use EchoUtils.getDouble instead
		if (value == null)
			return 0d;

		return value;
	}
}