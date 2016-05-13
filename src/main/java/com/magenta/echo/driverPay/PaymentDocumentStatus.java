package com.magenta.echo.driverPay;

/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public enum PaymentDocumentStatus
{
	/**
	 * Transaction for this PaymentDocument has not been processed.
	 */
	UNPAID,

	/**
	 * Transaction for this PaymentDocument has been processed.
	 */
	PAID
}