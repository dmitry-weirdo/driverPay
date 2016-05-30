package com.magenta.echo.driverpay.core.bean;

import com.magenta.echo.driverpay.core.bean.dao.PaymentDao;
import com.magenta.echo.driverpay.core.bean.dao.PaymentDocumentDao;
import com.magenta.echo.driverpay.core.entity.Driver;
import com.magenta.echo.driverpay.core.entity.Payment;
import com.magenta.echo.driverpay.core.entity.PaymentDocument;
import com.magenta.echo.driverpay.core.enums.PaymentStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 13-05-2016 01:00
 */
@Component
@Transactional
public class SalaryCalculationBean extends AbstractBean {

	@Autowired
	private DriverBean driverBean;
	@Autowired
	private PaymentLoader paymentLoader;
	@Autowired
	private PaymentDocumentDao paymentDocumentDao;
	@Autowired
	private PaymentDao paymentDao;

	@SuppressWarnings("unchecked")
	public void calculateSalary(@NotNull final LocalDate upToDate)	{

		getLogger().info(String.format("Start salary calculation up to date '%s'",upToDate));

		final List<Long> driverList = getEntityManager()
				.createNativeQuery("select d.id from drivers d")
				.getResultList();

		driverList.forEach(driver -> calculateSalary(driver, upToDate));

		getLogger().info("Salary calculation complete");

	}

	public void calculateSalary(@NotNull final Long driverId, @NotNull final LocalDate upToDate)	{

		getLogger().info(String.format("Start salary calculation for driver '%s'",driverId));

		final List<Payment> paymentList = paymentLoader.loadPaymentList(driverId, upToDate);

		if(paymentList.isEmpty())	{
			getLogger().info(String.format("Driver '%s' does not have any actual payments",driverId));
		}

		getLogger().info("Creating payment document");

		final Driver driver = new Driver();
		driver.setId(driverId);

		final PaymentDocument paymentDocument = new PaymentDocument();
		paymentDocument.setDriver(driver);
		paymentDocument.setPaymentDate(upToDate);
		paymentDocument.setProcessed(false);

		paymentDocumentDao.insert(paymentDocument);

		getLogger().info("Updating driver payments");

		paymentList.forEach(payment -> {
			payment.setStatus(PaymentStatus.CALCULATED);
			payment.setPaymentDocument(paymentDocument);
			paymentDao.update(payment);
		});

		getLogger().info(String.format("Salary calculation for driver '%s' complete",driverId));

	}

	public void rollbackSalaryCalculation(@NotNull final Long driverId, @NotNull final List<Long> paymentDocumentIdList)	{

		paymentDocumentIdList.forEach(paymentDocumentId -> {

			final PaymentDocument paymentDocument = paymentDocumentDao.find(paymentDocumentId);
			if(paymentDocument.getProcessed())	{
				return;
			}

			final List<Payment> paymentList = paymentLoader.loadPaymentList(driverId, paymentDocumentId);
			paymentList.forEach(payment -> {
				payment.setStatus(PaymentStatus.NONE);
				payment.setPaymentDocument(null);
				paymentDao.update(payment);
			});

			paymentDocumentDao.delete(paymentDocumentId);

		});

	}
}
