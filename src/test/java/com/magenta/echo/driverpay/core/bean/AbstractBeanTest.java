package com.magenta.echo.driverpay.core.bean;

import com.magenta.echo.driverpay.core.Scheme;
import com.magenta.echo.driverpay.core.TestContext;
import com.magenta.echo.driverpay.core.TestUtils;
import com.magenta.echo.driverpay.core.db.DataManager;
import org.junit.After;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 12:46
 */
public abstract class AbstractBeanTest {

	private DataManager dataManager = TestContext.get().getDataManager();

	protected DataManager getDataManager()	{
		return dataManager;
	}

	{
		Scheme.initDatabase();
		TestUtils.cleanup(dataManager);
	}

	@After
	public void afterMethod()	{
		TestUtils.cleanup(dataManager);
	}
}
