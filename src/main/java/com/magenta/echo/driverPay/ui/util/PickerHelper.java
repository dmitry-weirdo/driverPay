package com.magenta.echo.driverpay.ui.util;

import com.evgenltd.kwickui.controls.objectpicker.ObjectPicker;
import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.DriverBean;
import com.magenta.echo.driverpay.core.entity.Driver;
import com.magenta.echo.driverpay.ui.dialog.SelectDriver;

import java.util.Collections;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 21-05-2016 14:58
 */
public class PickerHelper {

	public static void setupDriverPicker(final ObjectPicker<Driver> driverPicker)	{
		driverPicker.setAutoCompletionBinding(
				request -> request.isCancelled()
						? Collections.emptyList()
						: getDriverBean().loadDriverList(request.getUserText()),
				Driver::getName
		);
		driverPicker.setExternalSelectAction(() -> new SelectDriver().showAndWait());
	}

	private static DriverBean getDriverBean()	{
		return Context.get().getDriverBean();
	}
}
