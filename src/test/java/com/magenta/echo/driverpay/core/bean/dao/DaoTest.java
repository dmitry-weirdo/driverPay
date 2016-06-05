package com.magenta.echo.driverpay.core.bean.dao;

import com.magenta.echo.driverpay.core.TestContextConfig;
import com.magenta.echo.driverpay.core.bean.AbstractBeanTest;
import com.magenta.echo.driverpay.core.entity.Driver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * Project: driverPay-prototype
 * Author:  Lebedev
 * Created: 31-05-2016 18:21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestContextConfig.class, loader = AnnotationConfigContextLoader.class)
public class DaoTest extends AbstractBeanTest {

	@Autowired
	private DriverDao driverDao;

	@Test
	public void testInit()	{
		runInTransaction(() -> {

			final Driver driver = new Driver();
			driver.setName("Test");
			driverDao.insert(driver);

		});
	}

}
