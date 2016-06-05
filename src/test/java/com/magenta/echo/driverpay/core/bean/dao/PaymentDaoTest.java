package com.magenta.echo.driverpay.core.bean.dao;

import com.magenta.echo.driverpay.core.TestContextConfig;
import com.magenta.echo.driverpay.core.bean.BalanceBean;
import com.magenta.echo.driverpay.core.entity.Driver;
import com.magenta.echo.driverpay.core.entity.Job;
import com.magenta.echo.driverpay.core.entity.JobRate;
import com.magenta.echo.driverpay.core.entity.Payment;
import com.magenta.echo.driverpay.core.enums.JobType;
import com.magenta.echo.driverpay.core.enums.PaymentStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.time.LocalDate;
import java.util.Collections;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 05-06-2016 17:33
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestContextConfig.class, loader = AnnotationConfigContextLoader.class)
public class PaymentDaoTest {

	@Autowired
	private CommonDao commonDao;
	@Autowired
	private DriverDao driverDao;
	@Autowired
	private JobDao jobDao;
	@Autowired
	private BalanceBean balanceBean;

	@Test
	public void test()	{

		final Driver driver = new Driver();
		driver.setName("Test");
		driverDao.insert(driver);

		final JobRate jobRate = new JobRate();
		jobRate.setNet(-60D);
		jobRate.setNominalCode("-");
		jobRate.setTaxCode("-");

		final Job job = new Job();
		job.setType(JobType.CASH_JOB);
		job.setDriver(driver);
		job.setJobDate(LocalDate.now());
		job.setPricing(100D);
		job.setJobRates(Collections.singleton(jobRate));

		jobDao.insert(job);

		final Payment payment = job.getPayment();
		payment.setStatus(PaymentStatus.CALCULATED);
		commonDao.update(payment);

		jobRate.setNet(-1D);
		jobDao.update(job);

		final Payment paymentActual = commonDao.find(Payment.class, job.getPayment().getId());
		Assert.assertEquals(Double.valueOf(-1D), paymentActual.getNet());
	}

}
