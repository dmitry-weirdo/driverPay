package com.magenta.echo.driverpay.ui;

import java.time.LocalDate;
import java.time.Period;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 13-05-2016 19:17
 */
public class Utils {
    public static String toString(final Object object) {
        if(object == null)  {
            return "";
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
}
