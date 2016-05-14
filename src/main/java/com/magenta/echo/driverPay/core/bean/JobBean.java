package com.magenta.echo.driverpay.core.bean;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.db.ConnectionExt;
import com.magenta.echo.driverpay.core.db.DataManager;
import com.magenta.echo.driverpay.core.entity.JobDto;
import com.magenta.echo.driverpay.core.entity.JobRateDto;
import com.magenta.echo.driverpay.core.entity.PaymentDto;
import com.magenta.echo.driverpay.core.enums.PaymentStatus;
import com.magenta.echo.driverpay.core.enums.PaymentType;
import com.magenta.echo.driverpay.core.exception.EntryNotExist;
import com.magenta.echo.driverpay.ui.Utils;

import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 13-05-2016 01:00
 */
public class JobBean extends AbstractBean{
	private static final DataManager.ResultSetReader<JobDto> JOB_READER = resultSet -> new JobDto(
			resultSet.getLong("id"),
			LocalDate.ofEpochDay(resultSet.getLong("job_date")),
			resultSet.getLong("driver_id"),
			resultSet.getString("driver_value")
	);

	private static final DataManager.ResultSetReader<JobRateDto> JOB_RATE_READER = resultSet -> new JobRateDto(
			resultSet.getLong("id"),
			resultSet.getDouble("net"),
			resultSet.getDouble("vat"),
			resultSet.getDouble("total"),
			resultSet.getString("nominal_code"),
			resultSet.getString("tax_code")
	);

	private PaymentBean getPaymentBean()	{
		return Context.get().getPaymentBean();
	}

	public List<JobDto> loadJobList(final LocalDate dateFrom, final LocalDate dateTo, final Long driverId)	{
		return getDataManager().executeQuery(
				"select\n" +
				"j.id,\n" +
				"j.job_date,\n" +
				"j.driver_id,\n" +
				"d.name driver_value\n" +
				"from jobs j\n" +
				"join drivers d on j.driver_id = d.id\n" +
				"where\n" +
				"j.job_date > ? and j.job_date <= ? and (? is null or j.driver_id = ?)",
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
				},
				JOB_READER
		);
	}

	public JobDto loadJob(final Long id)	{
		final List<JobDto> result = getDataManager().executeQuery(
				"select\n" +
				"j.id,\n" +
				"j.job_date,\n" +
				"j.driver_id,\n" +
				"d.name driver_value\n" +
				"from jobs j\n" +
				"join drivers d on j.driver_id = d.id\n" +
				"where\n" +
				"j.id = ?",
				preparedStatement -> preparedStatement.setLong(1,id),
				JOB_READER
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
				"vat,\n" +
				"total,\n" +
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

			Long jobId;

			if(jobDto.getId() == null)	{

				jobId = addJob(jobDto.getJobDate(), jobDto.getDriverId());

			}else {

				jobId = jobDto.getId();
				updateJob(jobId, jobDto.getJobDate(), jobDto.getDriverId());

			}

			updateJobRateList(jobId, jobRateDtoList);
			updateJobPayment(jobId);

			connection.success();

		}catch(Exception e)	{
			e.printStackTrace();
			getLogger().error(e);
		}

	}

	//

	private Long addJob(final LocalDate jobDate, final Long driverId)	{
		return getDataManager().executeInsert(
				"insert into jobs (id,job_date,driver_id) values (null,?,?)",
				preparedStatement -> {
					preparedStatement.setLong(1,jobDate.toEpochDay());
					preparedStatement.setLong(2,driverId);
				}
		);
	}

	private void updateJob(final Long jobId, final LocalDate jobDate, final Long driverId)	{
		getDataManager().executeUpdate(
				"update jobs set job_date = ?, driver_id = ? where id = ?",
				preparedStatement -> {
					preparedStatement.setLong(1, jobDate.toEpochDay());
					preparedStatement.setLong(2, driverId);
					preparedStatement.setLong(3, jobId);
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
						jobRateDto.getVat(),
						jobRateDto.getTotal(),
						jobRateDto.getNominalCode(),
						jobRateDto.getTaxCode()
				);
			}else {
				updateJobRate(
						jobRateDto.getId(),
						jobId,
						jobRateDto.getNet(),
						jobRateDto.getVat(),
						jobRateDto.getTotal(),
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
			final Double vat,
			final Double total,
			final String nominalCode,
			final String taxCode
	)	{
		getDataManager().executeInsert(
				"insert into job_rates (id,job_id,net,vat,total,nominal_code,tax_code) values (null,?,?,?,?,?,?)",
				preparedStatement -> {
					preparedStatement.setLong(1,jobId);
					preparedStatement.setDouble(2,net);
					preparedStatement.setDouble(3,vat);
					preparedStatement.setDouble(4,total);
					preparedStatement.setString(5,nominalCode);
					preparedStatement.setString(6,taxCode);
				}
		);
	}

	private void updateJobRate(
			final Long id,
			final Long jobId,
			final Double net,
			final Double vat,
			final Double total,
			final String nominalCode,
			final String taxCode
	)	{
		getDataManager().executeUpdate(
				"update job_rates set job_id = ?, net = ?, vat = ?, total = ?, nominal_code = ?, tax_code = ? where id = ?",
				preparedStatement -> {
					preparedStatement.setLong(1,jobId);
					preparedStatement.setDouble(2,net);
					preparedStatement.setDouble(3,vat);
					preparedStatement.setDouble(4,total);
					preparedStatement.setString(5,nominalCode);
					preparedStatement.setString(6,taxCode);
					preparedStatement.setLong(7,id);
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

	private void updateJobPayment(final Long jobId)	{
		final List<Long> jobPaymentIds = getDataManager().executeQuery(
				"select id from payments where job_id = ?",
				preparedStatement -> preparedStatement.setLong(1,jobId),
				resultSet -> resultSet.getLong("id")
		);

		final PaymentDto newPayment = makePaymentBasedOnJobRates(jobId);

		if(jobPaymentIds.isEmpty())	{
			newPayment.setId(null);
			getPaymentBean().updatePayment(null,jobId,newPayment);
		}

	}

	private PaymentDto makePaymentBasedOnJobRates(final Long jobId)	{
		final List<PaymentDto> result = getDataManager().executeQuery(
				"select " +
				"null id, " +
				"j.job_date planned_date, " +
				"j.driver_id," +
				"sum(jr.net) net, " +
				"sum(jr.vat) vat, " +
				"sum(jr.total) total, " +
				"'' nominal_code, " +
				"'' tax_code, " +
				"? type, " +
				"? status " +
				"from jobs j " +
				"join job_rates jr on j.id = jr.job_id " +
				"where job_id = ? " +
				"group by j.id",
				preparedStatement -> {
					preparedStatement.setString(1, PaymentType.REGULAR_JOB.name());
					preparedStatement.setString(2, PaymentStatus.NONE.name());
					preparedStatement.setLong(3, jobId);
				},
				PaymentBean.PAYMENT_READER
		);

		if(result.size() != 1)	{
			throw new RuntimeException("Unable to make job payment");
		}

		return result.get(0);
	}

}
