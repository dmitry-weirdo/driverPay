package com.magenta.echo.driverpay.core.bean.dao;

import com.magenta.echo.driverpay.core.TestContextConfig;
import com.magenta.echo.driverpay.core.bean.AbstractBeanTest;
import com.magenta.echo.driverpay.core.entity.Driver;
import com.magenta.echo.driverpay.core.enums.BalanceType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 05-06-2016 17:16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestContextConfig.class, loader = AnnotationConfigContextLoader.class)
public class DriverDaoTest extends AbstractBeanTest{

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private DriverDao driverDao;

	@Test
	public void testCommonOperations()	{

		final Driver expected = new Driver();
		expected.setName("Test");
		driverDao.insert(expected);

		runInTransaction(() -> {
			final Driver actual = commonDao.find(Driver.class, expected.getId());

			Assert.assertEquals(expected.getName(), actual.getName());
			Assert.assertTrue(actual.getBalances().size() == 2);

			Assert.assertEquals(actual.getDriverBalance().getType(), BalanceType.DRIVER);
			Assert.assertEquals(actual.getDepositBalance().getType(), BalanceType.DEPOSIT);
		});

		expected.setName("Test 2");
		commonDao.update(expected);

		final Driver actual = commonDao.find(Driver.class, expected.getId());
		Assert.assertEquals(expected.getName(), actual.getName());

		commonDao.delete(expected);

		final List<Driver> result = entityManager
				.createQuery("select d from Driver d",Driver.class)
				.getResultList();
		Assert.assertTrue(result.isEmpty());
	}


}
