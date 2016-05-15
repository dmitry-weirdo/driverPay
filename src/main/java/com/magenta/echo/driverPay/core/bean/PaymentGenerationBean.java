package com.magenta.echo.driverpay.core.bean;

import com.magenta.echo.driverpay.core.entity.PaymentDto;
import com.magenta.echo.driverpay.ui.Utils;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 15-05-2016 20:20
 */
public class PaymentGenerationBean extends AbstractBean {

	public List<PaymentDto> generateRepeatPayments(
			final List<PaymentDto> alreadyCreatedPayments,
			final Double net,
			final Double vat,
			final Double total,
			final Period frequency,
			final Period allPeriod
	)	{

		final Optional<LocalDate> lastDate = getLastDate(alreadyCreatedPayments);

		final LocalDate startDate = lastDate.isPresent()
				? Utils.plusPeriod(lastDate.get(),frequency)
				: LocalDate.now();
		final LocalDate endDate = Utils.plusPeriod(startDate,allPeriod);

		final List<PaymentDto> result = new ArrayList<>();

		for(
				LocalDate date = startDate;
				date.isBefore(endDate);
				date = Utils.plusPeriod(date,frequency)
		)	{

			final PaymentDto payment = new PaymentDto();
			payment.setPlannedDate(date);
			payment.setNet(net);
			payment.setVat(vat);
			payment.setTotal(total);
			result.add(payment);

		}

		return result;

	}

	public List<PaymentDto> generateIncrementalPayments(
			final List<PaymentDto> alreadyCreatedPayments,
			final Double net,
			final Double vat,
			final Double total,
			final Double totalNet,
			final Double totalVat,
			final Double gross,
			final Period frequency
	)	{

		final Optional<LocalDate> lastDate = getLastDate(alreadyCreatedPayments);

		final int numberOfDeltas = gross.intValue() / total.intValue() + (
				gross.intValue() % total.intValue() != 0
						? 1
						: 0
		);

		final LocalDate startDate = lastDate.isPresent()
				? Utils.plusPeriod(lastDate.get(),frequency)
				: LocalDate.now();

		final List<PaymentDto> result = new ArrayList<>();

		for(int deltaCount = 0; deltaCount < numberOfDeltas; deltaCount++)	{

			final LocalDate date = Utils.plusPeriod(startDate, frequency.multipliedBy(deltaCount) );
			final PaymentDto payment = new PaymentDto();
			payment.setPlannedDate(date);
			payment.setNet(net);
			payment.setVat(vat);
			payment.setTotal(total);
			result.add(payment);

		}

		return result;
	}

	private Optional<LocalDate> getLastDate(final List<PaymentDto> paymentList)	{

		final Optional<PaymentDto> lastPaymentHolder = paymentList
				.stream()
				.sorted((o1, o2) -> o2.getPlannedDate().compareTo(o1.getPlannedDate()))
				.findFirst();

		if(!lastPaymentHolder.isPresent())	{

			return Optional.empty();

		}else {

			return Optional.of(lastPaymentHolder.get().getPlannedDate());

		}

	}

}
