package com.magenta.echo.driverPay;

/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public enum PaymentStatus
{
	/**
	 * Payment was not yet put to the {@link PaymentDocument}.
	 */
	UNPAID,

	/**
	 * Payment was put to the {@link PaymentDocument}, but the {@link Transaction} of this document is not yet processed.
	 */
	PUT_TO_PAYMENT_DOCUMENT,

	/**
	 * Payment was put to the {@link PaymentDocument}, but the {@link Transaction} of this document is processed.
	 */
	PAID
}