package com.magenta.echo.driverpay.core.bean.dao;

import com.magenta.echo.driverpay.core.entity.JobRate;
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
 * Created: 25-05-2016 23:22
 */
@Component
@Transactional
public class JobRateDao extends AbstractDao<JobRate> {

	@Autowired
	private Validator validator;

	@Override
	public Long insert(@NotNull final JobRate entity) {

		final Set<ConstraintViolation<JobRate>> errorSet = validator.validate(entity, Default.class);
		if(errorSet.size() > 0)	{
			throw new DaoException(errorSet);
		}

		getEntityManager().persist(entity);
		return entity.getId();

	}

	@Override
	public void update(@NotNull final JobRate entity) {

		final Set<ConstraintViolation<JobRate>> errorSet = validator.validate(entity, Default.class);
		if(errorSet.size() > 0)	{
			throw new DaoException(errorSet);
		}

		getEntityManager().merge(entity);

	}

	@Override
	public void delete(@NotNull final Long id) {

		getEntityManager().remove(find(id));

	}

	@Override
	public JobRate find(@NotNull final Long id) {

		return getEntityManager().find(JobRate.class, id);

	}
}
