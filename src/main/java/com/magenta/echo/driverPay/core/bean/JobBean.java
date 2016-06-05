package com.magenta.echo.driverpay.core.bean;

import com.magenta.echo.driverpay.core.Queries;
import com.magenta.echo.driverpay.core.bean.dao.CommonDao;
import com.magenta.echo.driverpay.core.entity.Job;
import com.magenta.echo.driverpay.core.entity.dto.JobDto;
import com.magenta.echo.driverpay.core.enums.JobType;
import com.magenta.echo.driverpay.ui.util.Utils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 13-05-2016 01:00
 */
@Component
@Transactional
public class JobBean extends AbstractBean{

	@Autowired
	private CommonDao commonDao;

	public void butchDelete(@NotNull final List<Long> jobIdList)	{
		jobIdList.forEach(id -> commonDao.delete(Job.class,id));
	}

	@SuppressWarnings("unchecked")
	public List<JobDto> loadJobList(
			@NotNull final LocalDate dateFrom,
			@NotNull final LocalDate dateTo,
			final Long driverId,
			final JobType type
	)	{

		return getEntityManager()
				.createNativeQuery(getQuery(Queries.LOAD_JOB_LIST))
				.setParameter("from", Utils.startDateToLong(dateFrom))
				.setParameter("to", Utils.endDateToLong(dateTo))
				.setParameter("driver", driverId)
				.setParameter("type", type == null ? null : type.name())
				.unwrap(SQLQuery.class)
				.setResultTransformer(Transformers.aliasToBean(JobDto.class))
				.list();
	}

	public Job loadJob(@NotNull final Long id)	{

		return getEntityManager()
				.createQuery(getQuery(Queries.LOAD_JOB_BY_ID), Job.class)
				.setParameter("id",id)
				.getSingleResult();

	}
}
