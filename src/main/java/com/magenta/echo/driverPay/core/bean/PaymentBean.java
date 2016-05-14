package com.magenta.echo.driverpay.core.bean;

import com.magenta.echo.driverpay.core.db.ConnectionExt;
import com.magenta.echo.driverpay.core.db.DataManager;
import com.magenta.echo.driverpay.core.entity.PaymentDto;
import com.magenta.echo.driverpay.core.entity.PaymentReasonDto;
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
public class PaymentBean extends AbstractBean {

	private static final DataManager.ResultSetReader<PaymentReasonDto> PAYMENT_REASON_READER = resultSet -> new PaymentReasonDto(
			resultSet.getLong("id"),
			resultSet.getString("name"),
			resultSet.getLong("driver_id"),
			resultSet.getString("driver_value"),
			PaymentType.valueOf(resultSet.getString("type"))
	);

	public static final DataManager.ResultSetReader<PaymentDto> PAYMENT_READER = resultSet -> new PaymentDto(
			resultSet.getLong("id"),
			PaymentType.valueOf(resultSet.getString("type")),
			PaymentStatus.valueOf(resultSet.getString("status")),
			LocalDate.ofEpochDay(resultSet.getLong("planned_date")),
			resultSet.getLong("driver_id"),
			resultSet.getDouble("net"),
			resultSet.getDouble("vat"),
			resultSet.getDouble("total"),
			resultSet.getString("nominal_code"),
			resultSet.getString("tax_code")
	);

    public List<PaymentReasonDto> loadPaymentReasonList()   {
		return getDataManager().executeQuery(
				"select\n" +
				"pr.id,\n" +
				"pr.name,\n" +
				"d.id driver_id,\n" +
				"d.name driver_value,\n" +
				"pr.type\n" +
				"from payment_reasons pr\n" +
				"join drivers d on pr.driver_id = d.id",
				preparedStatement -> {},
				PAYMENT_REASON_READER
		);
    }

    public PaymentReasonDto loadPaymentReason(final Long paymentId) {
		final List<PaymentReasonDto> result = getDataManager().executeQuery(
				"select\n" +
				"pr.id,\n" +
				"pr.name,\n" +
				"d.id driver_id,\n" +
				"d.name driver_value,\n" +
				"pr.type\n" +
				"from payment_reasons pr\n" +
				"join drivers d on pr.driver_id = d.id\n" +
				"where pr.id = ?",
				preparedStatement -> preparedStatement.setLong(1,paymentId),
				PAYMENT_REASON_READER
		);

		if(result.size() != 1)	{
			throw new EntryNotExist(paymentId);
		}

        return result.get(0);
    }

	public List<PaymentDto> loadPaymentList(final Long paymentReasonId)	{
		return getDataManager().executeQuery(
				"select\n" +
				"p.id,\n" +
				"p.type,\n" +
				"p.status,\n" +
				"p.planned_date,\n" +
				"ifnull(j.driver_id,pr.driver_id) driver_id,\n" +
				"p.net,\n" +
				"p.vat,\n" +
				"p.total,\n" +
				"p.nominal_code,\n" +
				"p.tax_code\n" +
				"from payments p\n" +
				"left join payment_reasons pr on p.payment_reason_id = pr.id\n" +
				"left join jobs j on p.job_id = j.id\n" +
				"where p.payment_reason_id = ?",
				preparedStatement -> preparedStatement.setLong(1,paymentReasonId),
				PAYMENT_READER
		);
	}

	public List<PaymentDto> loadPaymentList(final LocalDate dateFrom, final LocalDate dateTo)	{
		return getDataManager().executeQuery(
				"select\n" +
				"p.id,\n" +
				"p.type,\n" +
				"p.status,\n" +
				"p.planned_date,\n" +
				"ifnull(j.driver_id,pr.driver_id) driver_id,\n" +
				"p.net,\n" +
				"p.vat,\n" +
				"p.total,\n" +
				"p.nominal_code,\n" +
				"p.tax_code\n" +
				"from payments p\n" +
				"left join payment_reasons pr on p.payment_reason_id = pr.id\n" +
				"left join jobs j on p.job_id = j.id\n" +
				"where p.planned_date > ? and p.planned_date <= ?",
				preparedStatement -> {
					preparedStatement.setLong(1, Utils.startDateToLong(dateFrom));
					preparedStatement.setLong(2, Utils.endDateToLong(dateTo));
				},
				PAYMENT_READER
		);
	}

	public List<PaymentDto> loadPaymentListByPaymentDocument(final Long paymentDocumentId)	{
		return getDataManager().executeQuery(
				"select\n" +
				"p.id,\n" +
				"p.type,\n" +
				"p.status,\n" +
				"p.planned_date,\n" +
				"ifnull(j.driver_id,pr.driver_id) driver_id,\n" +
				"p.net,\n" +
				"p.vat,\n" +
				"p.total,\n" +
				"p.nominal_code,\n" +
				"p.tax_code\n" +
				"from payments p\n" +
				"left join payment_reasons pr on p.payment_reason_id = pr.id\n" +
				"left join jobs j on p.job_id = j.id\n" +
				"where p.payment_document_id = ?",
				preparedStatement -> preparedStatement.setLong(1,paymentDocumentId),
				PAYMENT_READER
		);
	}

	public void updatePaymentReason(final PaymentReasonDto paymentReasonDto, final List<PaymentDto> paymentDtoList)	{
		try(final ConnectionExt connection = getDataManager().getConnection()) {

			for(final PaymentDto paymentDto : paymentDtoList) {
				paymentDto.setType(paymentReasonDto.getType());
			}

			if(paymentReasonDto.getId() == null)	{

				final Long paymentReasonId = addPaymentReason(
						paymentReasonDto.getName(),
						paymentReasonDto.getDriverId(),
						paymentReasonDto.getType()
				);
				updatePaymentList(paymentReasonId, paymentDtoList);

			}else {

				updatePaymentReason(
						paymentReasonDto.getId(),
						paymentReasonDto.getName(),
						paymentReasonDto.getDriverId(),
						paymentReasonDto.getType()
				);
				updatePaymentList(paymentReasonDto.getId(), paymentDtoList);

			}

			connection.success();

		}catch(Exception e) {
			e.printStackTrace();
			getLogger().error(e);
		}
	}

	public void updatePayment(
			final Long paymentReasonId,
			final Long jobId,
			final PaymentDto paymentDto
	)	{

		if(paymentDto.getId() == null)	{
			addPayment(
					paymentReasonId,
					jobId,
					paymentDto.getType(),
					paymentDto.getStatus(),
					paymentDto.getPlannedDate(),
					paymentDto.getNet(),
					paymentDto.getVat(),
					paymentDto.getTotal(),
					paymentDto.getNominalCode(),
					paymentDto.getTaxCode()
			);
		}else {
			updatePayment(
					paymentDto.getId(),
					jobId,
					paymentReasonId,
					paymentDto.getType(),
					paymentDto.getStatus(),
					paymentDto.getPlannedDate(),
					paymentDto.getNet(),
					paymentDto.getVat(),
					paymentDto.getTotal(),
					paymentDto.getNominalCode(),
					paymentDto.getTaxCode()
			);
		}

	}

	//

	private Long addPaymentReason(final String name, final Long driverId, final PaymentType type)	{
		return getDataManager().executeInsert(
				"insert into payment_reasons (id,name,driver_id,type) values (null,?,?,?)",
				preparedStatement -> {
					preparedStatement.setString(1, name);
					preparedStatement.setLong(2, driverId);
					preparedStatement.setString(3, type.name());
				}
		);
	}

	private void updatePaymentReason(final Long id, final String name, final Long driverId, final PaymentType type)	{
		getDataManager().executeUpdate(
				"update payment_reasons set name = ?, driver_id = ?, type = ? where id = ?",
				preparedStatement -> {
					preparedStatement.setString(1, name);
					preparedStatement.setLong(2, driverId);
					preparedStatement.setString(3, type.name());
					preparedStatement.setLong(4, id);
				}
		);
	}

	private void updatePaymentList(final Long paymentReasonId, final List<PaymentDto> paymentDtoList)	{

		final List<Long> oldPaymentIds = getDataManager().executeQuery(
				"select id from payments where payment_reason_id = ?",
				preparedStatement -> preparedStatement.setLong(1,paymentReasonId),
				resultSet -> resultSet.getLong("id")
		);

		paymentDtoList.stream().forEach(paymentDto -> oldPaymentIds.remove(paymentDto.getId()));
		oldPaymentIds.forEach(this::deletePayment);

		for(final PaymentDto paymentDto : paymentDtoList) {
			updatePayment(paymentReasonId, null, paymentDto);
		}

	}

	//

	private void addPayment(
			final Long paymentReasonId,
			final Long jobId,
			final PaymentType paymentType,
			final PaymentStatus paymentStatus,
			final LocalDate plannedDate,
			final Double net,
			final Double vat,
			final Double total,
			final String nominalCode,
			final String taxCode
	)	{
		getDataManager().executeInsert(
				"insert into payments " +
						"(id,payment_reason_id,job_id,type,status,planned_date,net,vat,total,nominal_code,tax_code) " +
						"values (null,?,?,?,?,?,?,?,?,?,?)",
				preparedStatement -> {
					if(paymentReasonId != null) {
						preparedStatement.setLong(1, paymentReasonId);
					}else {
						preparedStatement.setNull(1, Types.INTEGER);
					}
					if(jobId != null) {
						preparedStatement.setLong(2, jobId);
					}else {
						preparedStatement.setNull(2, Types.INTEGER);
					}
					preparedStatement.setString(3,paymentType.name());
					preparedStatement.setString(4,paymentStatus.name());
					preparedStatement.setLong(5,plannedDate.toEpochDay());
					preparedStatement.setDouble(6,net);
					preparedStatement.setDouble(7,vat);
					preparedStatement.setDouble(8,total);
					preparedStatement.setString(9,nominalCode);
					preparedStatement.setString(10,taxCode);
				}
		);
	}

	private void updatePayment(
			final Long id,
			final Long paymentReasonId,
			final Long jobId,
			final PaymentType paymentType,
			final PaymentStatus paymentStatus,
			final LocalDate plannedDate,
			final Double net,
			final Double vat,
			final Double total,
			final String nominalCode,
			final String taxCode
	)	{
		getDataManager().executeUpdate(
				"update payments set " +
				"payment_reason_id = ?, " +
				"job_id = ?, " +
				"planned_date = ?, " +
				"type = ?, " +
				"status = ?, " +
				"net = ?, " +
				"vat = ?, " +
				"total = ?, " +
				"nominal_code = ?, " +
				"tax_code = ? " +
				"where id = ?",
				preparedStatement -> {
					if(paymentReasonId != null) {
						preparedStatement.setLong(1, paymentReasonId);
					}else {
						preparedStatement.setNull(1, Types.INTEGER);
					}
					if(jobId != null) {
						preparedStatement.setLong(2, jobId);
					}else {
						preparedStatement.setNull(2, Types.INTEGER);
					}
					preparedStatement.setString(3,paymentType.name());
					preparedStatement.setString(4,paymentStatus.name());
					preparedStatement.setLong(5,plannedDate.toEpochDay());
					preparedStatement.setDouble(6,net);
					preparedStatement.setDouble(7,vat);
					preparedStatement.setDouble(8,total);
					preparedStatement.setString(9,nominalCode);
					preparedStatement.setString(10,taxCode);
					preparedStatement.setLong(11,id);
				}
		);
	}

	private void deletePayment(final Long id)	{
		getDataManager().executeUpdate(
				"delete from payments where id = ?",
				preparedStatement -> preparedStatement.setLong(1,id)
		);
	}

}
