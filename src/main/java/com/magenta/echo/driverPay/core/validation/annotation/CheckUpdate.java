package com.magenta.echo.driverpay.core.validation.annotation;

import com.magenta.echo.driverpay.core.validation.dirtyupdate.DirtyChecker;

import java.lang.annotation.*;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 04-06-2016 16:02
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckUpdate {
	Class<? extends DirtyChecker> checker();
}
