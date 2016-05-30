package com.magenta.echo.driverpay.core.bean.dao;

import com.magenta.echo.driverpay.core.entity.Balance;
import com.magenta.echo.driverpay.core.exception.DaoException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 24-05-2016 23:32
 */
@Component
@Transactional
public class BalanceDao extends AbstractDao<Balance>	{

	@Autowired
	private Validator validator;

	@Override
	public Long insert(@NotNull final Balance balance)	{

		final Set<ConstraintViolation<Balance>> errorSet = validator.validate(balance, Default.class);
		if(errorSet.size() > 0)	{
			throw new DaoException(errorSet);
		}

		getEntityManager().persist(balance);
		return balance.getId();
	}

	@Override
	public void update(@NotNull final Balance balance)	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(@NotNull final Long id)	{

		final Balance balance = find(id);
		getEntityManager().remove(balance);
	}

	@Override
	public Balance find(@NotNull final Long id) {

		return getEntityManager().find(Balance.class, id);

	}
}
