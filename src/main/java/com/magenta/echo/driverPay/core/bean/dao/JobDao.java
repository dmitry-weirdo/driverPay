package com.magenta.echo.driverpay.core.bean.dao;

import com.magenta.echo.driverpay.core.bean.factory.PaymentFactory;
import com.magenta.echo.driverpay.core.entity.Job;
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
 * Created: 25-05-2016 23:13
 */
@Component
@Transactional
public class JobDao extends AbstractDao<Job> {

	@Autowired
	private JobRateDao jobRateDao;
	@Autowired
	private PaymentDao paymentDao;
	@Autowired
	private Validator validator;

	@Override
	public Long insert(@NotNull final Job entity) {

		final Set<ConstraintViolation<Job>> errorSet = validator.validate(entity, Default.class);
		if(errorSet.size() > 0)	{
			throw new DaoException(errorSet);
		}

		final Payment payment = PaymentFactory.build(entity);
		paymentDao.insert(payment);

		entity.setPayment(payment);
		entity.getJobRates().forEach(jobRate -> jobRate.setJob(entity));
		getEntityManager().persist(entity);

		return entity.getId();
	}

	@Override
	public void update(@NotNull final Job entity) {

		final Set<ConstraintViolation<Job>> errorSet = validator.validate(entity, Default.class);
		if(errorSet.size() > 0)	{
			throw new DaoException(errorSet);
		}

		final Payment payment = paymentDao.find(entity.getPayment().getId());
		if(!Objects.equals(PaymentStatus.NONE, payment.getStatus()))	{
			throw new DaoException("status","Unable to update when status is not equals to NONE");
		}

		getEntityManager().merge(entity);
		paymentDao.update(PaymentFactory.build(entity));

	}

	@Override
	public void delete(@NotNull final Long id) {

		final Job job = getEntityManager()
				.createQuery("select j from Job j left fetch join j.payment where j.id = :id",Job.class)
				.setParameter("id",id)
				.getSingleResult();

		if(!Objects.equals(PaymentStatus.NONE,job.getPayment().getStatus()))	{
			throw new DaoException("status","Unable to delete when status is not equals to NONE");
		}

		getEntityManager().detach(job);

	}

	@Override
	public Job find(@NotNull final Long id) {

		return getEntityManager().find(Job.class, id);

	}
}
