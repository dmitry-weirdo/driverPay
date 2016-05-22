package com.magenta.echo.driverpay.core.bean;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.db.ConnectionExt;
import com.magenta.echo.driverpay.core.db.DataManager;
import com.magenta.echo.driverpay.core.entity.PaymentDocumentDto;
import com.magenta.echo.driverpay.core.entity.PaymentDto;
import com.magenta.echo.driverpay.core.entity.TransactionDto;
import com.magenta.echo.driverpay.core.enums.BalanceType;
import com.magenta.echo.driverpay.core.enums.PaymentStatus;
import com.magenta.echo.driverpay.core.enums.PaymentType;
import com.magenta.echo.driverpay.core.rule.PaymentTypeToTransactionRules;
import com.magenta.echo.driverpay.ui.Utils;

import java.time.LocalDate;
import java.util.List;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 13-05-2016 01:00
 */
public class PaymentProcessBean extends AbstractBean {

	private static final DataManager.ResultSetReader<TransactionDto> TRANSACTION_READER = resultSet -> new TransactionDto(
			resultSet.getLong("id"),
			LocalDate.ofEpochDay(resultSet.getLong("payment_date")),
			resultSet.getString("balance_from_value"),
			resultSet.getString("balance_to_value"),
			resultSet.getDouble("net"),
			resultSet.getDouble("vat"),
			resultSet.getDouble("total")
	);

	private static final DataManager.ResultSetReader<PaymentDocumentDto> PAYMENT_DOCUMENT_READER = resultSet -> new PaymentDocumentDto(
			resultSet.getLong("id"),
			resultSet.getLong("driver_id"),
			resultSet.getString("driver_value"),
			LocalDate.ofEpochDay(resultSet.getLong("payment_date")),
			resultSet.getLong("status") == 1
	);

	private PaymentBean getPaymentBean()	{
		return Context.get().getPaymentBean();
	}

	public List<PaymentDocumentDto> loadPaymentDocumentList(
			final LocalDate dateFrom,
			final LocalDate dateTo,
			final Long driverId,
			final boolean processed
	)	{

		return getDataManager().executeQuery(
				"select " +
				"pd.id, " +
				"pd.payment_date, " +
				"pd.driver_id, " +
				"d.name driver_value, " +
				"pd.status " +
				"from payment_documents pd " +
				"join drivers d on pd.driver_id = d.id " +
				"where " +
				"pd.payment_date > ? " +
				"and pd.payment_date <= ? " +
				(driverId == null ? "and 1=? " : "and pd.driver_id = ? ") +
				"and pd.status = ?",
				preparedStatement -> {
					preparedStatement.setLong(1, Utils.startDateToLong(dateFrom));
					preparedStatement.setLong(2, Utils.endDateToLong(dateTo));
					preparedStatement.setLong(3, driverId == null ? 1 : driverId);
					preparedStatement.setLong(4, processed ? 1 : 0);
				},
				PAYMENT_DOCUMENT_READER
		);

	}

	public PaymentDocumentDto loadPaymentDocument(final Long id)	{
		return getDataManager().executeSingleQuery(
				"select " +
				"pd.id, " +
				"pd.driver_id, " +
				"d.name driver_value, " +
				"pd.payment_date, " +
				"pd.status " +
				"from payment_documents pd " +
				"join drivers d on pd.driver_id = d.id " +
				"where " +
				"pd.id = ? ",
				preparedStatement -> preparedStatement.setLong(1,id),
				PAYMENT_DOCUMENT_READER
		);
	}

	public void processPaymentDocuments(final List<PaymentDocumentDto> paymentDocumentList)	{
		try(final ConnectionExt connection = getDataManager().getConnection())	{

			for(final PaymentDocumentDto paymentDocument : paymentDocumentList) {

				final List<PaymentDto> paymentDtoList = getPaymentBean().loadPaymentListByPaymentDocument(paymentDocument.getId());

				for(final PaymentDto payment : paymentDtoList) {

					addTransaction(
							paymentDocument.getPaymentDate(),
							payment.getId(),
							payment.getDriverId(),
							payment.getType(),
							payment.getNet(),
							payment.getVat(),
							payment.getTotal()
					);
					closePayment(payment.getId());
					closePaymentDocument(paymentDocument.getId());

				}

			}

			connection.success();

		}catch(Exception e)	{
			e.printStackTrace();
			getLogger().error("Unable to process payment documents",e);
		}
	}

	public List<TransactionDto> loadTransactionListByPayment(final Long paymentId)	{
		return getDataManager().executeQuery(
				"select\n" +
					"t.id,\n" +
					"t.payment_date,\n" +
					"case\n" +
						"when fb.type = 'DRIVER'  then fd.name+' (driver)'\n" +
						"when fb.type = 'DEPOSIT' then fd.name+' (deposit)'\n" +
						"when fb.type = 'COMPANY' then 'Company'\n" +
					"end from_balance_value,\n" +
					"case\n" +
						"when tb.type = 'DRIVER'  then td.name+' (driver)'\n" +
						"when tb.type = 'DEPOSIT' then td.name+' (deposit)'\n" +
						"when fb.type = 'COMPANY' then 'Company'\n" +
					"end to_balance_value,\n" +
					"t.net,\n" +
					"t.vat,\n" +
					"t.total\n" +
				"from transactions t\n" +
				"left join balances fb on t.from_id = fb.id\n" +
				"left join drivers fd on fb.driver_id = fd.id\n" +
				"left join balances tb on t.to_id = tb.id\n" +
				"left join drivers td on tb.driver_id = td.id\n" +
				"where t.payment_id = ?",
				preparedStatement -> preparedStatement.setLong(1,paymentId),
				TRANSACTION_READER
		);
	}

	public List<TransactionDto> loadTransactionListByPaymentDocument(final Long paymentDocumentId)	{
		return getDataManager().executeQuery(
				"select\n" +
					"t.id,\n" +
					"t.payment_date,\n" +
					"case\n" +
						"when fb.type = 'DRIVER'  then fd.name || ' (Driver)'\n" +
						"when fb.type = 'DEPOSIT' then fd.name || ' (Deposit)'\n" +
						"when fb.type = 'COMPANY' then 'Company'\n" +
					"end balance_from_value,\n" +
					"case\n" +
						"when tb.type = 'DRIVER'  then td.name || ' (Driver)'\n" +
						"when tb.type = 'DEPOSIT' then td.name || ' (Deposit)'\n" +
						"when tb.type = 'COMPANY' then 'Company'\n" +
					"end balance_to_value,\n" +
					"t.net,\n" +
					"t.vat,\n" +
					"t.total\n" +
				"from transactions t\n" +
				"join payments p on t.payment_id = p.id\n" +
				"left join balances fb on t.from_id = fb.id\n" +
				"left join drivers fd on fb.driver_id = fd.id\n" +
				"left join balances tb on t.to_id = tb.id\n" +
				"left join drivers td on tb.driver_id = td.id\n" +
				"where p.payment_document_id = ?",
		preparedStatement -> preparedStatement.setLong(1,paymentDocumentId),
		TRANSACTION_READER
);
	}

	//

	private void closePayment(final Long paymentId)	{
		getDataManager().executeUpdate(
				"update payments set status = ? where id = ?",
				preparedStatement -> {
					preparedStatement.setString(1, PaymentStatus.PROCESSED.name());
					preparedStatement.setLong(2, paymentId);
				}
		);
	}

	private void closePaymentDocument(final Long paymentDocumentId)	{
		getDataManager().executeUpdate(
				"update payment_documents set status = ? where id = ?",
				preparedStatement -> {
					preparedStatement.setLong(1, 1);
					preparedStatement.setLong(2, paymentDocumentId);
				}
		);
	}

	private void addTransaction(
			final LocalDate paymentDate,
			final Long paymentId,
			final Long driverId,
			final PaymentType paymentType,
			final Double net,
			final Double vat,
			final Double total
	)	{
		getDataManager().executeInsert(
				"insert into transactions (id,payment_date,payment_id,from_id,to_id,net,vat,total) values (null,?,?,?,?,?,?,?)",
				preparedStatement -> {
					preparedStatement.setLong(1,paymentDate.toEpochDay());
					preparedStatement.setLong(2,paymentId);
					preparedStatement.setLong(3,getFromBalance(driverId,paymentType));
					preparedStatement.setLong(4,getToBalance(driverId,paymentType));
					preparedStatement.setDouble(5,net);
					preparedStatement.setDouble(6,vat);
					preparedStatement.setDouble(7,total);
				}
		);
	}

	private Long getFromBalance(final Long driverId, final PaymentType paymentType)	{
		final BalanceType balanceType = PaymentTypeToTransactionRules.getFromBalanceType(paymentType);
		return loadBalance(driverId, balanceType);
	}

	private Long getToBalance(final Long driverId, final PaymentType paymentType)	{
		final BalanceType balanceType = PaymentTypeToTransactionRules.getToBalanceType(paymentType);
		return loadBalance(driverId, balanceType);
	}

	private Long loadBalance(final Long driverId, final BalanceType balanceType)	{

		if(BalanceType.COMPANY.equals(balanceType))	{

			return getDataManager().executeSingleQuery(
					"select id from balances where type = ?",
					preparedStatement -> preparedStatement.setString(1,BalanceType.COMPANY.name()),
					resultSet -> resultSet.getLong("id")
			);

		}else if(BalanceType.DRIVER.equals(balanceType) || BalanceType.DEPOSIT.equals(balanceType))	{

			return getDataManager().executeSingleQuery(
					"select id from balances where driver_id = ? and type = ?",
					preparedStatement -> {
						preparedStatement.setLong(1,driverId);
						preparedStatement.setString(2,balanceType.name());
					},
					resultSet -> resultSet.getLong("id")
			);

		}else {

			throw new IllegalArgumentException(String.format("Unknown BalanceType [%s]",balanceType));

		}

	}
}
