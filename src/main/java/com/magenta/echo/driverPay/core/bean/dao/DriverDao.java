package com.magenta.echo.driverpay.core.bean.dao;

import com.magenta.echo.driverpay.core.bean.factory.BalanceFactory;
import com.magenta.echo.driverpay.core.entity.Balance;
import com.magenta.echo.driverpay.core.entity.Driver;
import com.magenta.echo.driverpay.core.enums.BalanceType;
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
 * Created: 24-05-2016 23:18
 */
@Component
@Transactional
public class DriverDao extends AbstractDao<Driver>	{

	@Autowired
	private BalanceDao balanceDao;
	@Autowired
	private Validator validator;

	@Override
	public void update(@NotNull final Driver driver)	{

		final Set<ConstraintViolation<Driver>> errorSet = validator.validate(driver, Default.class);
		if(errorSet.size() > 0)	{
			throw new DaoException(errorSet);
		}

		getEntityManager().merge(driver);

	}

	@Override
	public Long insert(@NotNull final Driver driver)	{

		final Set<ConstraintViolation<Driver>> errorSet = validator.validate(driver, Default.class);
		if(errorSet.size() > 0)	{
			throw new DaoException(errorSet);
		}

		getEntityManager().persist(driver);

		final Balance from = BalanceFactory.build(BalanceType.DRIVER, driver);
		final Balance to = BalanceFactory.build(BalanceType.DEPOSIT, driver);

		balanceDao.insert(from);
		balanceDao.insert(to);

		return driver.getId();

	}

	@Override
	public void delete(@NotNull final Long id)	{

		getEntityManager().remove(find(id));

	}

	@Override
	public Driver find(@NotNull final Long id) {

		return getEntityManager().find(Driver.class, id);

	}
}
