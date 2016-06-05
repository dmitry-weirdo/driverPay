package com.magenta.echo.driverpay.core.entity.checker;

import com.magenta.echo.driverpay.core.validation.dirtyupdate.DirtyChecker;
import com.magenta.echo.driverpay.core.validation.dirtyupdate.DirtyInfo;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 04-06-2016 19:04
 */
public class PaymentDocumentUpdateChecker implements DirtyChecker {
	@Override
	public boolean check(DirtyInfo dirtyInfo) {
		if(!dirtyInfo.checkPropertyEquality("driver")) return false;
		if(!dirtyInfo.checkPropertyEquality("type")) return false;

		final Boolean processed = dirtyInfo.getPreviousValue("processed");

		if(!processed)	{
			return true;
		}

		if(!dirtyInfo.checkPropertyEquality("paymentDate")) return false;
		return dirtyInfo.checkPropertyEquality("method");

	}
}
