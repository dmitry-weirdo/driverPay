package com.magenta.echo.driverpay.core.bean.factory;

import com.magenta.echo.driverpay.core.entity.Job;
import com.magenta.echo.driverpay.core.entity.JobRate;
import com.magenta.echo.driverpay.core.entity.Payment;
import com.magenta.echo.driverpay.core.entity.Transaction;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 05-06-2016 14:33
 */
public class TransactionFactory {

	public static List<Transaction> build(
			@NotNull final LocalDate date,
			@NotNull final Payment payment
	)	{

		if(payment.getJob() != null)	{
			return build(date, payment.getJob());
		}

		final Transaction transaction = new Transaction();
		transaction.setPayment(payment);
		transaction.setPaymentDate(date);
		transaction.setFrom(payment.getFrom());
		transaction.setTo(payment.getTo());
		transaction.setNet(payment.getNet());
		transaction.setVat(payment.getVat());
		transaction.setTotal(payment.getTotal());
		transaction.setNominalCode(payment.getNominalCode());
		transaction.setTaxCode(payment.getTaxCode());
		return Collections.singletonList(transaction);

	}

	public static List<Transaction> build(
			@NotNull final LocalDate date,
			@NotNull final Job job
	) {
		return job
				.getJobRates()
				.stream()
				.map(jobRate -> build(date, job.getPayment(), jobRate))
				.collect(Collectors.toList());
	}

	public static Transaction build(
			@NotNull final LocalDate date,
			@NotNull final Payment payment,
			@NotNull final JobRate jobRate
	)	{

		final Transaction transaction = new Transaction();
		transaction.setPayment(payment);
		transaction.setFrom(payment.getFrom());
		transaction.setTo(payment.getTo());
		transaction.setPaymentDate(date);
		transaction.setNet(jobRate.getNet());
		transaction.setVat(0D);
		transaction.setTotal(jobRate.getNet());
		transaction.setNominalCode(jobRate.getNominalCode());
		transaction.setTaxCode(jobRate.getTaxCode());
		return transaction;

	}

}
