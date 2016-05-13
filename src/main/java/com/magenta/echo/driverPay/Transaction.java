/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package com.magenta.echo.driverPay;

import com.magenta.echo.driverPay.common.Dated;
import com.magenta.echo.driverPay.common.Identified;

/**
 * Transaction processing covers these driver pay operations:
 * <ul>
 *   <li>Process driver payments</li>
 *   <li>Manual transaction</li>
 *   <li>Release from driver deposit to the driver</li>
 *   <li>Release from driver deposit to the company</li>
 * </ul>
 *
 * // todo: это же что-то типа DriverBalanceAction? То, что мы видим в окне View Transactions.
 */
public interface Transaction extends Identified, Dated
{
	Balance getFromBalance();
	void setFromBalance(Balance fromBalance);

	Balance getToBalance();
	void setToBalance(Balance toBalance);

	// todo: think about splitting to net/vat/total
	// todo: think whether this field is required or to use total from
	Double getTotal();
	void setTotal(Double total);

	// todo: think whether this link must be to Payment or to PaymentDoc?
	Payment getPayment();
	void setPayment(Payment payment);
}