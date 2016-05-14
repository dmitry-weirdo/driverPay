package com.magenta.echo.driverpay.core;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 13:08
 */
public class TestContext extends Context {
	@Override
	public String getDatabaseName() {
		return "test-driver-pay.db";
	}
}
