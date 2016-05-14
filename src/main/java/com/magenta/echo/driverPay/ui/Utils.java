package com.magenta.echo.driverpay.ui;

import java.time.LocalDate;

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
}
