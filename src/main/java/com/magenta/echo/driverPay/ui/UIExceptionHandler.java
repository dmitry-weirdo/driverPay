package com.magenta.echo.driverpay.ui;

import org.controlsfx.dialog.ExceptionDialog;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 15:14
 */
public class UIExceptionHandler implements Thread.UncaughtExceptionHandler {
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		e.printStackTrace();
		ExceptionDialog exceptionDialog = new ExceptionDialog(e);
		exceptionDialog.showAndWait();
	}
}
