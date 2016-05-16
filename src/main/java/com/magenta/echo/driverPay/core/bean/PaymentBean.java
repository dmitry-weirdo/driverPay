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
			resultSet.getString("driver_value"),
			resultSet.getDouble("net"),
			resultSet.getDouble("vat"),
			resultSet.getDouble("total"),
			resultSet.getString("nominal_code"),
			resultSet.getString("tax_code")
	);

    public List<PaymentReasonDto> loadPaymentReasonList(final Long driverId, final PaymentType paymentType)   {
		return getDataManager().executeQuery(
				"select\n" +
				"pr.id,\n" +
				"pr.name,\n" +
				"d.id driver_id,\n" +
				"d.name driver_value,\n" +
				"pr.type\n" +
				"from payment_reasons pr\n" +
				"join drivers d on pr.driver_id = d.id\n" +
				"where (? is null or pr.driver_id = ?)\n" +
				" and (? is null or pr.type = ?)",
				preparedStatement -> {
					if(driverId == null)	{
						preparedStatement.setNull(1,Types.INTEGER);
						preparedStatement.setNull(2,Types.INTEGER);
					}else {
						preparedStatement.setLong(1, driverId);
						preparedStatement.setLong(2, driverId);
					}
					if(paymentType == null)	{
						preparedStatement.setNull(3,Types.INTEGER);
						preparedStatement.setNull(4,Types.INTEGER);
					}else {
						preparedStatement.setString(3,paymentType.name());
						preparedStatement.setString(4,paymentType.name());
					}
				},
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
				"p.driver_id,\n" +
				"d.name driver_value,\n" +
				"p.net,\n" +
				"p.vat,\n" +
				"p.total,\n" +
				"p.nominal_code,\n" +
				"p.tax_code\n" +
				"from payments p\n" +
				"left join drivers d on p.driver_id = d.id\n" +
				"where p.payment_reason_id = ?",
				preparedStatement -> preparedStatement.setLong(1,paymentReasonId),
				PAYMENT_READER
		);
	}

	public List<PaymentDto> loadPaymentList(
			final LocalDate dateFrom,
			final LocalDate dateTo,
			final Long driverId,
			final PaymentType paymentType,
			final PaymentStatus paymentStatus
	)	{
		return getDataManager().executeQuery(
				"select\n" +
				"p.id,\n" +
				"p.type,\n" +
				"p.status,\n" +
				"p.planned_date,\n" +
				"p.driver_id,\n" +
				"d.name driver_value,\n" +
				"p.net,\n" +
				"p.vat,\n" +
				"p.total,\n" +
				"p.nominal_code,\n" +
				"p.tax_code\n" +
				"from payments p\n" +
				"left join drivers d on p.driver_id = d.id\n" +
				"where p.planned_date > ? and p.planned_date <= ? \n" +
				"and (? is null or p.type = ?) \n" +
				"and (? is null or p.status = ?)\n" +
				(driverId == null ? "and 1=? " : " and p.driver_id = ?"),
				preparedStatement -> {
					preparedStatement.setLong(1, Utils.startDateToLong(dateFrom));
					preparedStatement.setLong(2, Utils.endDateToLong(dateTo));
					if(paymentType == null) {			// todo simplify it
						preparedStatement.setNull(3,Types.INTEGER);
						preparedStatement.setNull(4,Types.INTEGER);
					}else {
						preparedStatement.setString(3,paymentType.name());
						preparedStatement.setString(4,paymentType.name());
					}
					if(paymentStatus == null)	{		// todo simplify it
						preparedStatement.setNull(5,Types.INTEGER);
						preparedStatement.setNull(6,Types.INTEGER);
					}else {
						preparedStatement.setString(5,paymentStatus.name());
						preparedStatement.setString(6,paymentStatus.name());
					}
					preparedStatement.setLong(7,driverId==null ? 1 : driverId);
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
				"p.driver_id,\n" +
				"d.name driver_value,\n" +
				"p.net,\n" +
				"p.vat,\n" +
				"p.total,\n" +
				"p.nominal_code,\n" +
				"p.tax_code\n" +
				"from payments p\n" +
				"left join drivers d on p.driver_id = d.id\n" +
				"where p.payment_document_id = ?",
				preparedStatement -> preparedStatement.setLong(1,paymentDocumentId),
				PAYMENT_READER
		);
	}

	public void updatePaymentReason(final PaymentReasonDto paymentReasonDto, final List<PaymentDto> paymentDtoList)	{
		try(final ConnectionExt connection = getDataManager().getConnection()) {

			for(final PaymentDto paymentDto : paymentDtoList) {
				paymentDto.setType(paymentReasonDto.getType());
				paymentDto.setDriverId(paymentReasonDto.getDriverId());
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
					paymentDto.getDriverId(),
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
					paymentDto.getDriverId(),
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
			final Long driverId,
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
						"(id,payment_reason_id,job_id,driver_id,type,status,planned_date,net,vat,total,nominal_code,tax_code) " +
						"values (null,?,?,?,?,?,?,?,?,?,?,?)",
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
					preparedStatement.setLong(3,driverId);
					preparedStatement.setString(4,paymentType.name());
					preparedStatement.setString(5,paymentStatus.name());
					preparedStatement.setLong(6,plannedDate.toEpochDay());
					preparedStatement.setDouble(7,net);
					preparedStatement.setDouble(8,vat);
					preparedStatement.setDouble(9,total);
					preparedStatement.setString(10,nominalCode);
					preparedStatement.setString(11,taxCode);
				}
		);
	}

	private void updatePayment(
			final Long id,
			final Long paymentReasonId,
			final Long jobId,
			final Long driverId,
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
				"driver_id = ?, " +
				"type = ?, " +
				"status = ?, " +
				"planned_date = ?, " +
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
					preparedStatement.setLong(3,driverId);
					preparedStatement.setString(4,paymentType.name());
					preparedStatement.setString(5,paymentStatus.name());
					preparedStatement.setLong(6,plannedDate.toEpochDay());
					preparedStatement.setDouble(7,net);
					preparedStatement.setDouble(8,vat);
					preparedStatement.setDouble(9,total);
					preparedStatement.setString(10,nominalCode);
					preparedStatement.setString(11,taxCode);
					preparedStatement.setLong(12,id);
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
