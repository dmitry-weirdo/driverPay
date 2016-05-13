/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package com.magenta.echo.driverPay;

import com.magenta.echo.driverPay.common.Identified;

/**
 * Balance abstraction.
 *
 * Conceptually, any payment operation results in moving money between balances. Similarly, theoretically we can move money between any two balances.
 */
public interface Balance extends Identified
{
	BalanceType getType();
	void setType(BalanceType type);

	// todo: think about splitting to net/vat/total
	Double getTotal();
	void setTotal(Double total);
}