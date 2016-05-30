package com.magenta.echo.driverpay.core.bean.factory;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.BalanceBean;
import com.magenta.echo.driverpay.core.entity.*;
import com.magenta.echo.driverpay.core.enums.BalanceType;
import com.magenta.echo.driverpay.core.enums.PaymentStatus;
import com.magenta.echo.driverpay.core.enums.PaymentType;
import com.magenta.echo.driverpay.core.rule.JobAndPaymentTypesMatching;
import com.magenta.echo.driverpay.core.rule.PaymentTypeToTransactionRules;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 26-05-2016 00:52
 */
public class PaymentFactory {

	public static Payment build(
			@NotNull final Long id,
			@NotNull final PaymentReason paymentReason,
			@NotNull final Driver driver,
			@NotNull final PaymentDocument paymentDocument,
			@NotNull final Balance from,
			@NotNull final Balance to,
			@NotNull final PaymentType type,
			@NotNull final PaymentStatus status,
			@NotNull final LocalDate plannedDate,
			@NotNull final Double net,
			@NotNull final Double vat,
			@NotNull final Double total,
			@NotNull final String nominalCode,
			@NotNull final String taxCode
	)	{
		final Payment payment = new Payment();
		payment.setId(id);
		payment.setPaymentReason(paymentReason);
		payment.setDriver(driver);
		payment.setPaymentDocument(paymentDocument);
		payment.setFrom(from);
		payment.setTo(to);
		payment.setType(type);
		payment.setStatus(status);
		payment.setPlannedDate(plannedDate);
		payment.setNet(net);
		payment.setVat(vat);
		payment.setTotal(total);
		payment.setNominalCode(nominalCode);
		payment.setTaxCode(taxCode);
		return payment;
	}

	public static Payment build(
			@NotNull final PaymentReason paymentReason,
			@NotNull final Driver driver,
			@NotNull final PaymentDocument paymentDocument,
			@NotNull final Balance from,
			@NotNull final Balance to,
			@NotNull final PaymentType type,
			@NotNull final PaymentStatus status,
			@NotNull final LocalDate plannedDate,
			@NotNull final Double net,
			@NotNull final Double vat,
			@NotNull final Double total,
			@NotNull final String nominalCode,
			@NotNull final String taxCode
	)	{
		final Payment payment = new Payment();
		payment.setPaymentReason(paymentReason);
		payment.setDriver(driver);
		payment.setPaymentDocument(paymentDocument);
		payment.setFrom(from);
		payment.setTo(to);
		payment.setType(type);
		payment.setStatus(status);
		payment.setPlannedDate(plannedDate);
		payment.setNet(net);
		payment.setVat(vat);
		payment.setTotal(total);
		payment.setNominalCode(nominalCode);
		payment.setTaxCode(taxCode);
		return payment;
	}

	public static void fill(
			final @NotNull Payment payment,
			final @NotNull PaymentReason paymentReason,
			final @NotNull Job job
	)	{

		final Double totalNet = job
				.getJobRates()
				.stream()
				.collect(Collectors.summingDouble(JobRate::getNet));

		payment.setName(paymentReason.getName());
		payment.setPlannedDate(job.getJobDate());
		payment.setPaymentReason(paymentReason);
		payment.setDriver(paymentReason.getDriver());
		payment.setFrom(paymentReason.getFrom());
		payment.setTo(paymentReason.getTo());
		payment.setType(paymentReason.getPaymentType());
		payment.setStatus(PaymentStatus.NONE);
		payment.setNet(totalNet);
		payment.setVat(0D);
		payment.setTotal(totalNet);
		payment.setNominalCode("—");
		payment.setTaxCode("—");
	}

	public static Payment build(@NotNull final Long id, @NotNull final PaymentStatus paymentStatus)	{
		final Payment payment = new Payment();
		payment.setId(id);
		payment.setStatus(paymentStatus);
		return payment;
	}

	public static Payment build(
			@NotNull final LocalDate plannedDate,
			@NotNull final PaymentReason paymentReason
	)	{

		final Payment payment = new Payment();
		payment.setDriver(paymentReason.getDriver());
		payment.setPaymentReason(paymentReason);
		payment.setPlannedDate(plannedDate);
		payment.setFrom(paymentReason.getFrom());
		payment.setTo(paymentReason.getTo());
		payment.setType(paymentReason.getPaymentType());
		payment.setStatus(PaymentStatus.NONE);
		payment.setNet(paymentReason.getNet());
		payment.setVat(paymentReason.getVat());
		payment.setTotal(paymentReason.getTotal());
		payment.setNominalCode(paymentReason.getNominalCode());
		payment.setTaxCode(paymentReason.getTaxCode());

		return payment;
	}

	public static Payment build(final Job job)	{

		final Driver driver = job.getDriver();

		final PaymentType paymentType = JobAndPaymentTypesMatching.from(job.getType());

		final BalanceType fromType = PaymentTypeToTransactionRules.getFromBalanceType(paymentType);
		final BalanceType toType = PaymentTypeToTransactionRules.getToBalanceType(paymentType);

		final Balance from = getBalanceBean().getBalance(driver, fromType);
		final Balance to = getBalanceBean().getBalance(driver, toType);

		final Payment payment = job.getPayment() == null
				? new Payment()
				: job.getPayment();

		final Double totalNet = job
				.getJobRates()
				.stream()
				.collect(Collectors.summingDouble(JobRate::getNet));

		payment.setPlannedDate(job.getJobDate());
		payment.setName(paymentType.getLabel());
		payment.setDriver(driver);
		payment.setType(paymentType);
		payment.setFrom(from);
		payment.setTo(to);
		payment.setNet(totalNet);
		payment.setVat(0D);
		payment.setTotal(totalNet);
		payment.setNominalCode("—");
		payment.setTaxCode("—");

		return payment;
	}

	private static BalanceBean getBalanceBean() {
		return Context.get().getBalanceBean();
	}
}
