/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package com.magenta.echo.driverPay;

import com.magenta.echo.driverPay.common.Identified;

import java.util.List;

public interface Driver extends Identified
{
	String getName();
	void setName(String name);

	// balance (pocket)
	Balance getBalance();
	void setBalance(Balance balance);

	// deposit balance
	Balance getDepositBalance();
	void setDepositBalance(Balance depositBalance);

	// all payment reasons for this driver
	List<PaymentReason> getPaymentReasons();
	void setPaymentReasons(List<PaymentReason> paymentReasons);

	// all payment documents for this driver - including links to statements
	List<PaymentDocument> getPaymentDocuments();
	void setPaymentDocuments(List<PaymentDocument> paymentDocuments);
}