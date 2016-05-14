package com.magenta.echo.driverpay.core.bean;

import com.magenta.echo.driverpay.core.db.ConnectionExt;
import com.magenta.echo.driverpay.core.entity.PaymentDto;
import com.magenta.echo.driverpay.core.enums.PaymentStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 13-05-2016 01:00
 */
public class SalaryCalculationBean extends AbstractBean {

	public void makePaymentDocuments(final List<PaymentDto> paymentList)	{
		try(final ConnectionExt connection = getDataManager().getConnection())	{

			final Long paymentDocumentId = addPaymentDocument(LocalDate.now());
			paymentList
					.stream()
					.filter(payment -> payment.getStatus().equals(PaymentStatus.NONE))
					.forEach(payment -> linkPaymentDocumentToPayments(payment.getId(), paymentDocumentId));

			connection.success();

		}catch(final Exception e)	{
			e.printStackTrace();
			getLogger().error(e);
		}
	}

	//

	private Long addPaymentDocument(final LocalDate paymentDate)	{

		return getDataManager().executeInsert(
				"insert into payment_documents (id,payment_date,status) values (null,?,0)",
				preparedStatement -> preparedStatement.setLong(1,paymentDate.toEpochDay())
		);

	}

	private void linkPaymentDocumentToPayments(final Long paymentId, final Long paymentDocumentId)	{
		getDataManager().executeUpdate(
				"update payments set payment_document_id = ?, status = ? where id = ?",
				preparedStatement -> {
					preparedStatement.setLong(1,paymentDocumentId);
					preparedStatement.setString(2,PaymentStatus.CALCULATED.name());
					preparedStatement.setLong(3,paymentId);
				}
		);
	}
}
