package com.magenta.echo.driverpay.core.bean.dao;

import com.magenta.echo.driverpay.core.entity.PaymentDocument;
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
 * Created: 25-05-2016 23:33
 */
@Component
@Transactional
public class PaymentDocumentDao extends AbstractDao<PaymentDocument>	{

	@Autowired
	private Validator validator;

	@Override
	public Long insert(@NotNull final PaymentDocument entity) {

		final Set<ConstraintViolation<PaymentDocument>> errorSet = validator.validate(entity, Default.class);
		if(errorSet.size() > 0)	{
			throw new DaoException(errorSet);
		}

		getEntityManager().persist(entity);
		return entity.getId();

	}

	@Override
	public void update(@NotNull final PaymentDocument entity) {

		final Set<ConstraintViolation<PaymentDocument>> errorSet = validator.validate(entity, Default.class);
		if(errorSet.size() > 0)	{
			throw new DaoException(errorSet);
		}

		getEntityManager().merge(entity);

	}

	@Override
	public void delete(@NotNull final Long id) {

		final PaymentDocument paymentDocument = find(id);

		if(paymentDocument.getProcessed())	{
			throw new DaoException("processed","Unable to delete when already processed");
		}

		getEntityManager().remove(paymentDocument);

	}

	@Override
	public PaymentDocument find(@NotNull final Long id) {

		return getEntityManager().find(PaymentDocument.class, id);

	}
}
