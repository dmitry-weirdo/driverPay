package com.magenta.echo.driverpay.core.bean;

import com.magenta.echo.driverpay.core.bean.dao.CommonDao;
import com.magenta.echo.driverpay.core.entity.Driver;
import com.magenta.echo.driverpay.core.entity.Payment;
import com.magenta.echo.driverpay.core.entity.PaymentDocument;
import com.magenta.echo.driverpay.core.enums.PaymentDocumentMethod;
import com.magenta.echo.driverpay.core.rule.PaymentDocumentProcessing;
import com.magenta.echo.driverpay.core.rule.ReportBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 31-05-2016 01:41
 */
@Component
@Transactional
public class PaymentDocumentProcessingBean extends AbstractBean {

	@Autowired
	private PaymentLoader paymentLoader;
	@Autowired
	private CommonDao commonDao;

	public void calculateSalary(
			@NotNull final Long driverId,
			@NotNull final LocalDate upToDate,
			@NotNull final PaymentDocumentMethod method
	)	{

		final Driver driver = commonDao.find(Driver.class, driverId);
		final List<Payment> paymentList = paymentLoader.loadPaymentList(driverId, upToDate);

		final PaymentDocument paymentDocument = PaymentDocumentProcessing.calculateSalary(
				driver,
				upToDate,
				method,
				paymentList
		);

		commonDao.insert(paymentDocument);

		commonDao.insert(ReportBuilder.makeStatement(paymentDocument));

	}

	public void rollbackSalaryCalculation(@NotNull final List<Long> paymentDocumentIdList)	{

		paymentDocumentIdList.forEach(paymentDocumentId -> {

			final PaymentDocument paymentDocument = commonDao.find(PaymentDocument.class, paymentDocumentId);

			PaymentDocumentProcessing.rollbackSalary(paymentDocument);

			paymentDocument.getPaymentSet().forEach(commonDao::update);
			commonDao.delete(paymentDocument);

		});

	}

	public void processPaymentDocument(
			@NotNull final LocalDate paymentDate,
			@NotNull final List<Long> paymentDocumentIdList
	)	{

		paymentDocumentIdList.forEach(paymentDocumentId -> {

			final PaymentDocument paymentDocument = commonDao.find(PaymentDocument.class, paymentDocumentId);

			PaymentDocumentProcessing.processPaymentDocument(paymentDate, paymentDocument);

			commonDao.update(paymentDocument);

		});

	}

	public void processImmediatePayment(
			@NotNull final LocalDate date,
			@NotNull final Long paymentId,
			@NotNull final PaymentDocumentMethod method
	)	{

		final Payment payment = commonDao.find(Payment.class, paymentId);
		final PaymentDocument paymentDocument = PaymentDocumentProcessing.processImmediatePayment(date, payment, method);

		commonDao.insert(paymentDocument);

	}

}
