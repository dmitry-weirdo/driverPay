package com.magenta.echo.driverpay.core.bean;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 05-06-2016 17:11
 */
@Component
@Transactional
public class TransactionRunner {

	public void runInTransaction(@NotNull final Runnable runnable)	{
		runnable.run();
	}

}
