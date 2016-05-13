package com.magenta.echo.driverPay;

/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 *
 * Balance type. This does not include virtual balances, which are not stored in the database.
 */
public enum BalanceType
{
	/**
	 * Driver balance (pocket).
	 */
	DRIVER,

	/**
	 * Driver deposit balance.
	 */
	DRIVER_DEPOSIT,

	/**
	 * Company balance.
	 *
	 * // todo: разделяются ли балансы по компаниям Эхо?
	 * // todo: если оплата идёт от драйвера, то оплата идёт на driver.depot.company?
	 * // todo: если оплата идёт от работы, они идут по той компании? Не существует ли ситуации, когда драйвер принадлежит депо одной компании, а работу выполняет от CA другой компании?
	 */
	COMPANY

	// todo: think about for charging. It seems that this is not required, as it belongs to charging, not to driver pay
}