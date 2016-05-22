package com.magenta.echo.driverpay.core.bean;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.db.ConnectionExt;
import com.magenta.echo.driverpay.core.db.DataManager;
import com.magenta.echo.driverpay.core.entity.JobDto;
import com.magenta.echo.driverpay.core.entity.JobRateDto;
import com.magenta.echo.driverpay.core.entity.PaymentDto;
import com.magenta.echo.driverpay.core.entity.PaymentReasonDto;
import com.magenta.echo.driverpay.core.enums.JobType;
import com.magenta.echo.driverpay.core.enums.PaymentStatus;
import com.magenta.echo.driverpay.core.enums.PaymentType;
import com.magenta.echo.driverpay.core.exception.EntryNotExist;
import com.magenta.echo.driverpay.ui.Utils;

import java.sql.Types;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 13-05-2016 01:00
 */
public class JobBean extends AbstractBean{

	private static final DataManager.ResultSetReader<JobRateDto> JOB_RATE_READER = resultSet -> new JobRateDto(
			resultSet.getLong("id"),
			resultSet.getDouble("net"),
			resultSet.getString("nominal_code"),
			resultSet.getString("tax_code")
	);

	private PaymentBean getPaymentBean()	{
		return Context.get().getPaymentBean();
	}

	public List<JobDto> loadJobList(final LocalDate dateFrom, final LocalDate dateTo, final Long driverId, final JobType type)	{
		return getDataManager().executeQuery(
				"select\n" +
					"j.id,\n" +
					"j.job_date,\n" +
					"j.type,\n" +
					"j.driver_id,\n" +
					"d.name driver_value\n," +
					"sum(jr.net) total\n" +
				"from jobs j\n" +
				"join drivers d on j.driver_id = d.id\n" +
				"join job_rates jr on j.id = jr.job_id\n" +
				"where\n" +
					"j.job_date > ? \n" +
					"and j.job_date <= ? \n" +
					"and (? is null or j.driver_id = ?)\n" +
					"and (? is null or j.type = ?)\n" +
				"group by\n" +
					"j.id,\n" +
					"j.job_date,\n" +
					"j.driver_id,\n" +
					"d.name",
				preparedStatement -> {
					preparedStatement.setLong(1, Utils.startDateToLong(dateFrom));
					preparedStatement.setLong(2, Utils.endDateToLong(dateTo));
					if(driverId == null) {
						preparedStatement.setNull(3, Types.INTEGER);
						preparedStatement.setNull(4, Types.INTEGER);
					}else {
						preparedStatement.setLong(3, driverId);
						preparedStatement.setLong(4, driverId);
					}
					if(type == null)	{
						preparedStatement.setNull(5, Types.INTEGER);
						preparedStatement.setNull(6, Types.INTEGER);
					}else {
						preparedStatement.setString(5, type.name());
						preparedStatement.setString(6, type.name());
					}
				},
				resultSet -> new JobDto(
						resultSet.getLong("id"),
						LocalDate.ofEpochDay(resultSet.getLong("job_date")),
						JobType.valueOf(resultSet.getString("type")),
						resultSet.getLong("driver_id"),
						resultSet.getString("driver_value"),
						resultSet.getDouble("total")
				)
		);
	}

	public JobDto loadJob(final Long id)	{
		final List<JobDto> result = getDataManager().executeQuery(
				"select\n" +
					"j.id,\n" +
					"j.job_date,\n" +
					"j.type,\n" +
					"j.driver_id,\n" +
					"d.name driver_value\n" +
				"from jobs j\n" +
				"join drivers d on j.driver_id = d.id\n" +
				"where\n" +
					"j.id = ?",
				preparedStatement -> preparedStatement.setLong(1,id),
				resultSet -> new JobDto(
						resultSet.getLong("id"),
						LocalDate.ofEpochDay(resultSet.getLong("job_date")),
						JobType.valueOf(resultSet.getString("type")),
						resultSet.getLong("driver_id"),
						resultSet.getString("driver_value"),
						0D
				)
		);

		if(result.size() != 1)	{
			throw new EntryNotExist(id);
		}

		return result.get(0);
	}

	public List<JobRateDto> loadJobRateList(final Long jobId)	{
		return getDataManager().executeQuery(
				"select\n" +
					"id,\n" +
					"net,\n" +
					"nominal_code,\n" +
					"tax_code\n" +
				"from job_rates\n" +
				"where\n" +
					"job_id = ?",
				preparedStatement -> preparedStatement.setLong(1,jobId),
				JOB_RATE_READER
		);
	}

	public void updateJob(final JobDto jobDto, final List<JobRateDto> jobRateDtoList)	{

		try(final ConnectionExt connection = getDataManager().getConnection())	{

			final PaymentReasonDto paymentReason = makePaymentReasonBasedOnJob(jobDto);
			final PaymentDto payment = makePaymentBasedOnJobRates(jobDto, jobRateDtoList);

			Long jobId;

			if(jobDto.getId() == null)	{

				final Long paymentReasonId = getPaymentBean().updatePaymentReason(paymentReason, Collections.singletonList(payment));
				jobId = addJob(jobDto.getJobDate(), jobDto.getType(), jobDto.getDriverId(), paymentReasonId);

			}else {

				jobId = jobDto.getId();
				fillPaymentWithId(jobId, paymentReason, payment);
				getPaymentBean().updatePaymentReason(paymentReason, Collections.singletonList(payment));
				updateJob(jobId, jobDto.getJobDate(), jobDto.getType(), jobDto.getDriverId(), paymentReason.getId());

			}

			updateJobRateList(jobId, jobRateDtoList);

			connection.success();

		}catch(Exception e)	{
			throw new RuntimeException(e);
		}

	}

	//

	private Long addJob(
			final LocalDate jobDate,
			final JobType jobType,
			final Long driverId,
			final Long paymentReasonId
	)	{
		return getDataManager().executeInsert(
				"insert into jobs (id,job_date,type,driver_id,payment_reason_id) values (null,?,?,?,?)",
				preparedStatement -> {
					preparedStatement.setLong(1,jobDate.toEpochDay());
					preparedStatement.setString(2,jobType.name());
					preparedStatement.setLong(3,driverId);
					preparedStatement.setLong(4,paymentReasonId);
				}
		);
	}

	private void updateJob(
			final Long jobId,
			final LocalDate jobDate,
			final JobType jobType,
			final Long driverId,
			final Long paymentReasonId
	)	{
		getDataManager().executeUpdate(
				"update jobs set job_date = ?, type = ?, driver_id = ?, payment_reason_id = ? where id = ?",
				preparedStatement -> {
					preparedStatement.setLong(1, jobDate.toEpochDay());
					preparedStatement.setString(2, jobType.name());
					preparedStatement.setLong(3, driverId);
					preparedStatement.setLong(4, paymentReasonId);
					preparedStatement.setLong(5, jobId);
				}
		);
	}

	private void updateJobRateList(final Long jobId, final List<JobRateDto> jobRateDtoList)	{


		final List<Long> oldRateIds = getDataManager().executeQuery(
				"select id from job_rates where job_id = ?",
				preparedStatement -> preparedStatement.setLong(1,jobId),
				resultSet -> resultSet.getLong("id")
		);

		jobRateDtoList.stream().forEach(jobRate -> oldRateIds.remove(jobRate.getId()));
		oldRateIds.forEach(this::deleteJobRate);

		for(final JobRateDto jobRateDto : jobRateDtoList) {

			if(jobRateDto.getId() == null)	{
				addJobRate(
						jobId,
						jobRateDto.getNet(),
						jobRateDto.getNominalCode(),
						jobRateDto.getTaxCode()
				);
			}else {
				updateJobRate(
						jobRateDto.getId(),
						jobId,
						jobRateDto.getNet(),
						jobRateDto.getNominalCode(),
						jobRateDto.getTaxCode()
				);
			}

		}

	}

	//

	private void addJobRate(
			final Long jobId,
			final Double net,
			final String nominalCode,
			final String taxCode
	)	{
		getDataManager().executeInsert(
				"insert into job_rates (id,job_id,net,nominal_code,tax_code) values (null,?,?,?,?)",
				preparedStatement -> {
					preparedStatement.setLong(1,jobId);
					preparedStatement.setDouble(2,net);
					preparedStatement.setString(3,nominalCode);
					preparedStatement.setString(4,taxCode);
				}
		);
	}

	private void updateJobRate(
			final Long id,
			final Long jobId,
			final Double net,
			final String nominalCode,
			final String taxCode
	)	{
		getDataManager().executeUpdate(
				"update job_rates set job_id = ?, net = ?, nominal_code = ?, tax_code = ? where id = ?",
				preparedStatement -> {
					preparedStatement.setLong(1,jobId);
					preparedStatement.setDouble(2,net);
					preparedStatement.setString(3,nominalCode);
					preparedStatement.setString(4,taxCode);
					preparedStatement.setLong(5,id);
				}
		);
	}

	private void deleteJobRate(final Long id)	{
		getDataManager().executeUpdate(
				"delete from job_rates where id = ?",
				preparedStatement -> preparedStatement.setLong(1,id)
		);
	}

	//

	private void fillPaymentWithId(final Long jobId, final PaymentReasonDto paymentReason, final PaymentDto payment)	{
		final List<Long> ids = getDataManager().executeSingleQuery(
				"select j.payment_reason_id, p.id payment_id " +
						"from jobs j " +
						"join payments p on j.payment_reason_id = p.payment_reason_id " +
						"where j.id = ?",
				preparedStatement -> preparedStatement.setLong(1,jobId),
				resultSet -> Arrays.asList(
						resultSet.getLong("payment_reason_id"),
						resultSet.getLong("payment_id")
				)
		);

		paymentReason.setId(ids.get(0));
		payment.setId(ids.get(1));

	}

	private PaymentReasonDto makePaymentReasonBasedOnJob(final JobDto job)	{
		return new PaymentReasonDto(
				null,
				"Regular Job",
				job.getDriverId(),
				job.getDriverValue(),
				PaymentType.REGULAR_JOB
		);
	}

	private PaymentDto makePaymentBasedOnJobRates(final JobDto job, final List<JobRateDto> jobRateList)	{

		final Double total = jobRateList.stream().collect(Collectors.summingDouble(JobRateDto::getNet));
		return new PaymentDto(
				null,
				PaymentType.REGULAR_JOB,
				PaymentStatus.NONE,
				job.getJobDate(),
				job.getDriverId(),
				job.getDriverValue(),
				total,
				0D,
				total,
				"",
				""
		);

	}

}
