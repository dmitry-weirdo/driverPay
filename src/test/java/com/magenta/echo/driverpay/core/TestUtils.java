package com.magenta.echo.driverpay.core;

import com.magenta.echo.driverpay.core.db.DataManager;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 12:15
 */
public class TestUtils {

	public static void cleanup(final DataManager dataManager)	{
		dataManager.executeUpdate("delete from jobs");
		dataManager.executeUpdate("delete from job_rates");
		dataManager.executeUpdate("delete from drivers");
		dataManager.executeUpdate("delete from balances");
	}

}
