package com.magenta.echo.driverpay.core.bean.dao;

import com.magenta.echo.driverpay.core.bean.factory.PaymentFactory;
import com.magenta.echo.driverpay.core.entity.Job;
import com.magenta.echo.driverpay.core.entity.Payment;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 25-05-2016 23:13
 */
@Component
@Transactional
public class JobDao {

	@Autowired
	private CommonDao commonDao;

	public void insert(@NotNull final Job job) {

		job.getJobRates().forEach(jobRate -> jobRate.setJob(job));

		final Payment payment = PaymentFactory.build(job);
		job.setPayment(payment);

		commonDao.insert(job);

	}

	public void update(@NotNull final Job job)	{

		final Payment payment = PaymentFactory.build(job);
		payment.setId(job.getPayment().getId());

		commonDao.update(job);

	}
}
