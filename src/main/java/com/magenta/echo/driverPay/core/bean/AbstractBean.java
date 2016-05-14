package com.magenta.echo.driverpay.core.bean;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.db.DataManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 12:20
 */
public abstract class AbstractBean {

	private final Logger log = LogManager.getLogger(getClass());

	protected Logger getLogger()	{
		return log;
	}

	protected DataManager getDataManager()	{
		return Context.get().getDataManager();
	}

}
