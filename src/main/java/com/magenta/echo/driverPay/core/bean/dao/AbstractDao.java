package com.magenta.echo.driverpay.core.bean.dao;

import com.magenta.echo.driverpay.core.bean.AbstractBean;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 24-05-2016 23:38
 */
public abstract class AbstractDao<T> extends AbstractBean {

	@Autowired
	private Validator validator;

	protected Validator getValidator()	{
		return validator;
	}

	protected void validate(@NotNull final T entity, @NotNull final Class<?>... groups)	{
		final Set<ConstraintViolation<T>> errorSet = validator.validate(entity, groups);
		if(!errorSet.isEmpty())	{
			throw new ConstraintViolationException(errorSet);
		}
	}

	public abstract Long insert(@NotNull final T entity);

	public abstract void update(@NotNull final T entity);

	public abstract void delete(@NotNull final Long id);

	public abstract T find(@NotNull final Long id);

}
