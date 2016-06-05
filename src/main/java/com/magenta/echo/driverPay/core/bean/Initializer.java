package com.magenta.echo.driverpay.core.bean;

import org.hibernate.Hibernate;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 01-06-2016 00:57
 */
@Component
@Transactional
public class Initializer {

	public <T> void initialize(@NotNull final T entity)	{
		Hibernate.initialize(entity);
	}

}
