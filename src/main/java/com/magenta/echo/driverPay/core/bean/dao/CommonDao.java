package com.magenta.echo.driverpay.core.bean.dao;

import com.magenta.echo.driverpay.core.bean.AbstractBean;
import com.magenta.echo.driverpay.core.entity.Identified;
import com.magenta.echo.driverpay.core.validation.group.Delete;
import com.magenta.echo.driverpay.core.validation.group.Update;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 03-06-2016 00:11
 */
@Component
@Transactional
public class CommonDao extends AbstractBean {

	@Autowired
	private Validator validator;

	public <T> void insert(@NotNull final T entity)	{
		validate(entity, Default.class);
		getEntityManager().persist(entity);
	}

	public <T> void update(@NotNull final T entity)	{
		validate(entity, Update.class, Default.class);
		getEntityManager().merge(entity);
	}

	public <T> void delete(@NotNull final Class<T> entityClass, @NotNull final Long id)	{
		final T entity = find(entityClass, id);
		validate(entity, Delete.class);
		getEntityManager().remove(entity);
	}

	public void delete(@NotNull final Identified entity)	{
		delete(entity.getClass(), entity.getId());
	}

	public <T> T find(@NotNull final Class<T> entityClass, @NotNull final Long id)	{
		return getEntityManager().find(entityClass, id);
	}

	private <T> void validate(@NotNull final T entity, @NotNull final Class<?>... groups)	{
		final Set<ConstraintViolation<T>> errorSet = validator.validate(entity, groups);
		if(!errorSet.isEmpty())	{
			throw new ConstraintViolationException(errorSet);
		}
	}

}
