package com.magenta.echo.driverpay.core.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDate;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 29-05-2016 02:13
 */
@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Long> {
	@Override
	public Long convertToDatabaseColumn(LocalDate localDate) {
		if(localDate == null)	{
			return null;
		}
		return localDate.toEpochDay();
	}

	@Override
	public LocalDate convertToEntityAttribute(Long aLong) {
		if(aLong == null)	{
			return null;
		}
		return LocalDate.ofEpochDay(aLong);
	}
}
