package com.magenta.echo.driverpay.ui;

import com.magenta.echo.driverpay.core.validation.ValidationUtils;
import com.magenta.echo.driverpay.ui.util.InstantDialogs;
import org.controlsfx.dialog.ExceptionDialog;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 15:14
 */
public class UIExceptionHandler implements Thread.UncaughtExceptionHandler {

	@Override
	@SuppressWarnings("unchecked")
	public void uncaughtException(Thread t, Throwable e) {
		e.printStackTrace();

		final ConstraintViolationException constraintViolationException = ValidationUtils.getConstraintViolationException(e);
		if(constraintViolationException != null)	{
			final Set<ConstraintViolation<Object>> errors = (Set)constraintViolationException.getConstraintViolations();
			InstantDialogs.makeConstraintViolationDialog(errors).showAndWait();
			return;
		}

		ExceptionDialog exceptionDialog = new ExceptionDialog(e);
		exceptionDialog.showAndWait();
	}

}
