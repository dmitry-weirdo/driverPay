package com.magenta.echo.driverpay.core.rule;

import com.magenta.echo.driverpay.core.entity.*;
import com.magenta.echo.driverpay.core.enums.BalanceType;
import com.magenta.echo.driverpay.core.enums.ExportType;
import com.magenta.echo.driverpay.core.enums.PaymentStatus;
import com.magenta.echo.driverpay.core.enums.PaymentType;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 05-06-2016 21:04
 */
public class ReportBuilder {

	public static ExportHistory makeStatement(@NotNull final PaymentDocument paymentDocument)	{

		final StringBuilder sb = new StringBuilder();

		sb
				.append("Driver: ")
				.append(paymentDocument.getDriver().getName())
				.append("\n");

		sb
				.append("Reporting date: ")
				.append(paymentDocument.getPaymentDate())
				.append("\n");

		sb
				.append("Salary: ")
				.append(PaymentDocumentProcessing.calculateBalanceAmount(
						paymentDocument.getPaymentSet(),
						BalanceType.DRIVER,
						PaymentStatus.CALCULATED
				))
				.append("\n\n");

		sb.append("Regular jobs").append("\n");
		sb.append("id,date,charge\n");
		paymentDocument
				.getPaymentSet()
				.stream()
				.filter(payment -> Objects.equals(payment.getType(), PaymentType.REGULAR_JOB))
				.forEach(payment -> sb.append(jobToString(payment.getJob())));
		sb.append("\n");

		sb.append("Cash jobs").append("\n");
		sb.append("id,date,charge\n");
		paymentDocument
				.getPaymentSet()
				.stream()
				.filter(payment -> Objects.equals(payment.getType(), PaymentType.CASH_JOB))
				.forEach(payment -> sb.append(jobToString(payment.getJob())));
		sb.append("\n");

		sb.append("Credits").append("\n");
		sb.append("date,net,vat,total,nominalCode,taxCode\n");
		paymentDocument
				.getPaymentSet()
				.stream()
				.filter(payment -> Objects.equals(payment.getType(), PaymentType.CREDIT))
				.forEach(payment -> sb.append(paymentToString(payment)));
		sb.append("\n");

		sb.append("Deductions").append("\n");
		sb.append("date,net,vat,total,nominalCode,taxCode\n");
		paymentDocument
				.getPaymentSet()
				.stream()
				.filter(payment -> Objects.equals(payment.getType(), PaymentType.DEDUCTION))
				.forEach(payment -> sb.append(paymentToString(payment)));
		sb.append("\n");

		sb.append("Deposit").append("\n");
		sb.append("date,net,vat,total,nominalCode,taxCode\n");
		paymentDocument
				.getPaymentSet()
				.stream()
				.filter(payment ->
						Arrays.asList(PaymentType.DEPOSIT,PaymentType.RELEASE_DEPOSIT)
								.contains(payment.getType()))
				.forEach(payment -> sb.append(paymentToString(payment)));
		sb.append("\n");

		sb.append("Cash payment back").append("\n");
		sb.append("date,net,vat,total,nominalCode,taxCode\n");
		paymentDocument
				.getPaymentSet()
				.stream()
				.filter(payment -> Objects.equals(payment.getType(), PaymentType.CASH_PAYMENT))
				.forEach(payment -> sb.append(paymentToString(payment)));

		final FileContent fileContent = new FileContent();
		fileContent.setContent(sb.toString());

		final ExportHistory exportHistory = new ExportHistory();
		exportHistory.setDate(LocalDate.now());
		exportHistory.setType(ExportType.STATEMENT);
		exportHistory.setFileContent(fileContent);
		exportHistory.setPaymentDocumentSet(Collections.singleton(paymentDocument));

		return exportHistory;
	}

	private static String paymentToString(final @NotNull Payment payment)	{
		return payment.getPlannedDate() +
				"," +
				payment.getNet() +
				"," +
				payment.getVat() +
				"," +
				payment.getTotal() +
				"," +
				payment.getNominalCode() +
				"," +
				payment.getTaxCode() +
				"\n";
	}

	private static String jobToString(final @NotNull Job job)	{
		final Double totalCharge = job
				.getJobRates()
				.stream()
				.collect(Collectors.summarizingDouble(JobRate::getNet))
				.getSum();
		return String.valueOf(job.getId()) +
				"," +
				job.getJobDate() +
				"," +
				totalCharge +
				"\n";

	}
}
