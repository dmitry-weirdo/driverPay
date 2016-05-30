package com.magenta.echo.driverpay.core.bean.dao;

import com.magenta.echo.driverpay.core.entity.Payment;
import com.magenta.echo.driverpay.core.enums.PaymentStatus;
import com.magenta.echo.driverpay.core.exception.DaoException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Objects;
import java.util.Set;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 25-05-2016 22:58
 */
@Component
@Transactional
public class PaymentDao extends AbstractDao<Payment> {

	@Autowired
	private Validator validator;

	@Override
	public Long insert(@NotNull final Payment entity) {

		final Set<ConstraintViolation<Payment>> errorSet = validator.validate(entity, Default.class);
		if(errorSet.size() > 0)	{
			throw new DaoException(errorSet);
		}

		getEntityManager().persist(entity);
		return entity.getId();
	}

	@Override
	public void update(@NotNull final Payment entity) {

		final Set<ConstraintViolation<Payment>> errorSet = validator.validate(entity, Default.class);
		if(errorSet.size() > 0)	{
			throw new DaoException(errorSet);
		}

		getEntityManager().merge(entity);

	}

	@Override
	public void delete(@NotNull final Long id) {

		final Payment payment = find(id);

		if(!Objects.equals(PaymentStatus.NONE,payment.getStatus()))	{
			throw new DaoException("status","Unable to delete when status is not equals to NONE");
		}

		getEntityManager().remove(payment);

	}

	@Override
	public Payment find(@NotNull final Long id) {

		return getEntityManager().find(Payment.class, id);

	}
}
