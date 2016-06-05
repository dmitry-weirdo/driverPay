package com.magenta.echo.driverpay.core.rule;

import com.magenta.echo.driverpay.core.bean.factory.TransactionFactory;
import com.magenta.echo.driverpay.core.entity.Driver;
import com.magenta.echo.driverpay.core.entity.Payment;
import com.magenta.echo.driverpay.core.entity.PaymentDocument;
import com.magenta.echo.driverpay.core.enums.BalanceType;
import com.magenta.echo.driverpay.core.enums.PaymentDocumentMethod;
import com.magenta.echo.driverpay.core.enums.PaymentDocumentType;
import com.magenta.echo.driverpay.core.enums.PaymentStatus;
import com.magenta.echo.driverpay.core.validation.ValidationUtils;
import com.magenta.echo.driverpay.core.validation.group.DocumentProcessing;
import com.magenta.echo.driverpay.core.validation.group.SalaryCalculation;
import com.magenta.echo.driverpay.core.validation.group.SalaryRollback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 05-06-2016 14:02
 */
public class PaymentDocumentProcessing {

	private static final Logger log = LogManager.getLogger(PaymentDocumentProcessing.class);

	public static Double calculateBalanceAmount(
			@NotNull final Collection<Payment> paymentList,
			@NotNull final BalanceType type,
			@NotNull final PaymentStatus status
	)	{

		final Double fromSum = paymentList
				.stream()
				.filter(
						payment -> Objects.equals(payment.getStatus(), status)
								&& Objects.equals(payment.getFrom().getType(), type)
				)
				.collect(Collectors.summarizingDouble(Payment::getTotal))
				.getSum();

		final Double toSum = paymentList
				.stream()
				.filter(
						payment -> Objects.equals(payment.getStatus(), status)
								&& Objects.equals(payment.getTo().getType(), type)
				)
				.collect(Collectors.summarizingDouble(Payment::getTotal))
				.getSum();

		return toSum - fromSum;

	}

	private static PaymentDocument processPayments(
			@NotNull final Driver driver,
			@NotNull final LocalDate date,
			@NotNull final PaymentDocumentMethod method,
			@NotNull final PaymentDocumentType type,
			@NotNull final List<Payment> paymentList	// will be changed
	)	{

		log.info(String.format(
				"Making payment document; date=[%s]; type=[%s]; method=[%s]",
				date,
				type,
				method
		));

		final PaymentDocument paymentDocument = new PaymentDocument();
		paymentDocument.setDriver(driver);
		paymentDocument.setPaymentDate(date);
		paymentDocument.setProcessed(false);
		paymentDocument.setType(type);
		paymentDocument.setMethod(method);
		paymentDocument.setPaymentSet(new HashSet<>(paymentList));

		ValidationUtils.validate(paymentDocument, SalaryCalculation.class);

		log.info("Updating payments");

		paymentList.forEach(payment -> {
			payment.setPaymentDocument(paymentDocument);
			payment.setStatus(PaymentStatus.CALCULATED);
		});

		return paymentDocument;

	}

	public static PaymentDocument calculateSalary(
			@NotNull final Driver driver,
			@NotNull final LocalDate date,
			@NotNull final PaymentDocumentMethod method,
			@NotNull final List<Payment> paymentList	// will be changed
	)	{

		return processPayments(driver, date, method, PaymentDocumentType.SALARY, paymentList);

	}

	public static void rollbackSalary(@NotNull final PaymentDocument paymentDocument)	{	// will be changed

		ValidationUtils.validate(paymentDocument, SalaryRollback.class);

		final Set<Payment> paymentSet = paymentDocument.getPaymentSet();

		paymentSet.forEach(payment -> {
			payment.setPaymentDocument(null);
			payment.setStatus(PaymentStatus.NONE);
		});

	}

	public static void processPaymentDocument(
			@NotNull final LocalDate date,
			@NotNull final PaymentDocument paymentDocument	// will be changed
	)	{

		log.info(String.format(
				"Processing payment document id=[%s] date=[%s]",
				paymentDocument.getId(),
				date
		));

		ValidationUtils.validate(paymentDocument, DocumentProcessing.class);

		paymentDocument.setProcessed(true);

		log.info("Making transactions");

		final Set<Payment> paymentSet = paymentDocument.getPaymentSet();

		paymentSet.forEach(payment -> {
			payment.setStatus(PaymentStatus.PROCESSED);
			payment.getTransactionSet().addAll(TransactionFactory.build(date,payment));
		});

	}

	public static PaymentDocument processImmediatePayment(
			@NotNull final LocalDate date,
			@NotNull final Payment payment,
			@NotNull final PaymentDocumentMethod method
	)	{

		final PaymentDocument paymentDocument = processPayments(
				payment.getDriver(),
				date,
				method,
				PaymentDocumentType.IMMEDIATE,
				Collections.singletonList(payment)
		);
		processPaymentDocument(date, paymentDocument);

		return paymentDocument;

	}

}
