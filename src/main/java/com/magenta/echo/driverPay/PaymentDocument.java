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

import java.util.List;

/**
 * <p>
 * An entity that combines several not yet processed payments for one driver.
 * </p>
 *
 * <p>
 * PaymentDocument matches with these driver pay operations:
 * <ul>
 *   <li>Close driver working week - driver statement PDF is generated and sent to the driver when closing driver week</li>
 *   <li>Reopen driver working week</li>
 *   // todo: Manual Transaction?
 * </ul>
 * </p>
 *
 * <p>
 * Only payments which belong to PaymentDocument can be processed with {@link com.magenta.echo.driverPay.Transaction} // todo: это так?
 * </p>
 */
public interface PaymentDocument extends Identified, Dated
{
	Driver getDriver();
	void setDriver(Driver driver);

	PaymentDocumentStatus getStatus();
	void getStatus(PaymentDocumentStatus status);

	List<Payment> getPayments();
	void setPayments(List<Payment> payments);

	String getDescription();
	void setDescription();

	// todo: link to DriverStatement
}