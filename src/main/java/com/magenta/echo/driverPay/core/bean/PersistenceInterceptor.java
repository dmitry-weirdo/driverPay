package com.magenta.echo.driverpay.core.bean;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.validation.dirtyupdate.DirtyInfo;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.Serializable;
import java.util.Set;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 02-06-2016 23:51
 */
public class PersistenceInterceptor extends EmptyInterceptor {

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {

		final DirtyInfo dirtyInfo = new DirtyInfo(entity, id, currentState, previousState, propertyNames, types);
		final Set<ConstraintViolation<DirtyInfo>> errors = Context.get().getValidator().validate(dirtyInfo);
		if(!errors.isEmpty())	{
			throw new ConstraintViolationException(errors);
		}

		return false;
	}
}
