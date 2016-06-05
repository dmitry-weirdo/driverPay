package com.magenta.echo.driverpay.core.entity.checker;

import com.magenta.echo.driverpay.core.enums.PaymentStatus;
import com.magenta.echo.driverpay.core.validation.dirtyupdate.DirtyChecker;
import com.magenta.echo.driverpay.core.validation.dirtyupdate.DirtyInfo;

import java.util.Arrays;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 04-06-2016 18:04
 */
public class PaymentUpdateChecker implements DirtyChecker {

	@Override
	public boolean check(final DirtyInfo dirtyInfo) {

		if(!dirtyInfo.checkPropertyEquality("paymentReason"))	return false;
		if(!dirtyInfo.checkPropertyEquality("driver")) return false;

		final PaymentStatus previousStatus = dirtyInfo.getPreviousValue("status");
		final PaymentStatus currentStatus = dirtyInfo.getCurrentValue("status");

		if(!Arrays.asList(previousStatus,currentStatus).contains(PaymentStatus.PROCESSED))	{
			return true;
		}

		if(!dirtyInfo.checkPropertyEquality("name")) return false;
		if(!dirtyInfo.checkPropertyEquality("paymentDocument")) return false;
		if(!dirtyInfo.checkPropertyEquality("from")) return false;
		if(!dirtyInfo.checkPropertyEquality("to")) return false;
		if(!dirtyInfo.checkPropertyEquality("type")) return false;
		if(!dirtyInfo.checkPropertyEquality("plannedDate")) return false;
		if(!dirtyInfo.checkPropertyEquality("net")) return false;
		if(!dirtyInfo.checkPropertyEquality("vat")) return false;
		if(!dirtyInfo.checkPropertyEquality("total")) return false;
		if(!dirtyInfo.checkPropertyEquality("nominalCode")) return false;
		if(!dirtyInfo.checkPropertyEquality("taxCode")) return false;

		return true;
	}

}
