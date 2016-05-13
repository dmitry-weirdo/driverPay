package com.magenta.echo.driverPay;

/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 *
 * <p>
 * This maps to <code>DriverChargeType</code> from Echo.
 * </p>
 *
 * <p>
 * This is actual for:
 * <ul>
 *   <li>Credits</li>
 *   <li>Deductions</li>
 *   // todo: do deposit has ChargeType.INCREMENTAL?
 *   // todo: do MT has ChargeType.SINGLE?
 *   // todo: do release deposit and opening balance operations have ChargeType.SINGLE?
 * </ul>
 * </p>
 */
public enum ChargeType
{
	SINGLE,

	INCREMENTAL,

	REPEAT
}