package com.magenta.echo.driverpay.core.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 29-05-2016 02:40
 */
@Converter(autoApply = true)
public class BooleanAttributeConverter implements AttributeConverter<Boolean,Long> {
	@Override
	public Long convertToDatabaseColumn(Boolean aBoolean) {
		if(aBoolean == null)	{
			return null;
		}
		return aBoolean ? 1L : 0L;
	}

	@Override
	public Boolean convertToEntityAttribute(Long aLong) {
		if(aLong == null)	{
			return null;
		}
		return aLong == 1L;
	}
}
