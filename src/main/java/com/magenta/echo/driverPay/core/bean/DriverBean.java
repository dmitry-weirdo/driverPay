package com.magenta.echo.driverpay.core.bean;

import com.magenta.echo.driverpay.core.Queries;
import com.magenta.echo.driverpay.core.entity.Driver;
import com.magenta.echo.driverpay.core.entity.dto.DriverDto;
import org.hibernate.internal.SQLQueryImpl;
import org.hibernate.transform.Transformers;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 28-05-2016 19:53
 */
@Component
@Transactional
public class DriverBean extends AbstractBean {

	@SuppressWarnings("unchecked")
	public List<DriverDto> loadDriverList() {

		return getEntityManager()
				.createNativeQuery(getQuery(Queries.LOAD_DRIVER_LIST_WITH_BALANCES))
				.unwrap(SQLQueryImpl.class)
				.setResultTransformer(Transformers.aliasToBean(DriverDto.class))
				.list();

	}

	public DriverDto loadDriver(@NotNull final Long id) {

		return (DriverDto)getEntityManager()
				.createNativeQuery(getQuery(Queries.LOAD_DRIVER_WITH_ADDITIONAL_INFO))
				.setParameter("id", id)
				.unwrap(SQLQueryImpl.class)
				.setResultTransformer(Transformers.aliasToBean(DriverDto.class))
				.uniqueResult();

	}

	public List<Driver> loadDriverList(@NotNull final String pattern) {

		return getEntityManager()
				.createQuery(getQuery(Queries.LOAD_DRIVER_LIST_BY_PATTERN), Driver.class)
				.setParameter("pattern", "%"+pattern+"%")
				.getResultList();

	}
}
