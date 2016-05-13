/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package com.magenta.echo.driverPay;

public enum PaymentReasonType
{
	JOB, // todo: это для одной работы или для всех, входящих в statement?

	CREDIT,

	DEDUCTION,

	DEPOSIT,

	RELEASE_DEPOSIT_TO_DRIVER,

	RELEASE_DEPOSIT_TO_COMPANY,

	MANUAL_TRANSACTION,
}