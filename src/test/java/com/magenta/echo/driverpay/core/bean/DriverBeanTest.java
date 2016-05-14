package com.magenta.echo.driverpay.core.bean;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.entity.DriverDto;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 12:45
 */
public class DriverBeanTest extends AbstractBeanTest	{

	private DriverBean driverBean = Context.get().getDriverBean();

	@Test
	public void testUpdateDriver()	{

		driverBean.updateDriver(new DriverDto("Test Driver"));

		final List<DriverDto> result = driverBean.loadDriverList();
		Assert.assertEquals(1, result.size());

		DriverDto driverDto = result.get(0);
		Assert.assertEquals("Test Driver", driverDto.getName());

		final Long driverId = driverDto.getId();
		driverDto = driverBean.loadDriver(driverId);
		Assert.assertEquals(driverId, driverDto.getId());
		Assert.assertEquals("Test Driver", driverDto.getName());

		driverDto.setName("New Name");
		driverBean.updateDriver(driverDto);

		driverDto = driverBean.loadDriver(driverId);
		Assert.assertEquals("New Name", driverDto.getName());

	}

}
