package com.magenta.echo.driverpay.ui.util;

import com.magenta.echo.driverpay.core.entity.Balance;
import com.magenta.echo.driverpay.core.entity.Payment;
import com.magenta.echo.driverpay.core.enums.BalanceType;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.function.Function;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 13-05-2016 19:17
 */
public class Utils {
    public static String toString(final Object object) {
        return toString(object,"");
    }

	public static String toString(final Object object, @NotNull final String placeholder)	{
		if(object == null)  {
			return placeholder;
		}
		return object.toString();
	}

	public static Long startDateToLong(final LocalDate date)	{
		return date == null ? 0 : date.toEpochDay();
	}

	public static Long endDateToLong(final LocalDate date)	{
		return date == null ? Long.MAX_VALUE : date.toEpochDay();
	}

	public static LocalDate plusPeriod(final LocalDate localDate, final Period period)	{
		return localDate
				.plusYears(period.getYears())
				.plusMonths(period.getMonths())
				.plusDays(period.getDays());
	}

	public static Integer periodToDays(final Period period)	{
		if(period == null)	{
			return 0;
		}
		return period.getYears()*365 + period.getMonths()*30 + period.getDays();
	}

	public static Period ofPeriod(final int frequency, final ChronoUnit chronoUnit)	{
		switch(chronoUnit)	{
			case DAYS:
				return Period.ofDays(frequency);
			case WEEKS:
				return Period.ofWeeks(frequency);
			case MONTHS:
				return Period.ofMonths(frequency);
			default:
				throw new IllegalArgumentException("ChronoUnit not supported, +"+chronoUnit);
		}
	}

	public static Callback<TableColumn.CellDataFeatures<Payment,Double>, ObservableValue<Double>> getAmountCellValueFactory(final @NotNull Function<Payment,Double> amountExtractor)	{
		return param -> {

			final Payment payment = param.getValue();
			final Balance from = payment.getFrom();
			final Balance to = payment.getTo();
			final Double amount = amountExtractor.apply(payment);

			if(Objects.equals(BalanceType.DRIVER, from.getType()))	{

				return new SimpleObjectProperty<>(-amount);

			}else if(Objects.equals(BalanceType.DRIVER, to.getType()))	{

				return new SimpleObjectProperty<>(amount);

			}else {

				throw new IllegalStateException(String.format(
						"Payment type does not contains driver balances; payment=[%s]; from=[%s]; to=[%s]; amount=[%s]",
						payment.getName(),
						from.getType(),
						to.getType(),
						amount
				));

			}
		};
	}
}
