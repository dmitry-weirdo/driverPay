package com.magenta.echo.driverpay.core.bean.dao;

import com.magenta.echo.driverpay.core.bean.factory.BalanceFactory;
import com.magenta.echo.driverpay.core.entity.Balance;
import com.magenta.echo.driverpay.core.entity.Driver;
import com.magenta.echo.driverpay.core.enums.BalanceType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 24-05-2016 23:18
 */
@Component
@Transactional
public class DriverDao 	{

	@Autowired
	private CommonDao commonDao;

	public <T> void insert(@NotNull final T entity) {
		final Driver driver = (Driver) entity;
		final Balance from = BalanceFactory.build(BalanceType.DRIVER, driver);
		final Balance to = BalanceFactory.build(BalanceType.DEPOSIT, driver);

		final Set<Balance> balances = new HashSet<>();
		balances.add(from);
		balances.add(to);
		driver.setBalances(balances);

		commonDao.insert(driver);
	}

}
