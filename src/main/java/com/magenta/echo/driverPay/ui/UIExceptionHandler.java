package com.magenta.echo.driverpay.ui;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.ui.dialog.ExceptionView;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 15:14
 */
public class UIExceptionHandler implements Thread.UncaughtExceptionHandler {
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		Context.get().openDialogAndWait(new ExceptionView(e));
	}
}
