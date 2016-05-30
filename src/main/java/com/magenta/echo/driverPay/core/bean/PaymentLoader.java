package com.magenta.echo.driverpay.core.bean;

import com.magenta.echo.driverpay.core.Queries;
import com.magenta.echo.driverpay.core.entity.Payment;
import com.magenta.echo.driverpay.core.entity.PaymentDocument;
import com.magenta.echo.driverpay.core.entity.dto.PaymentReasonDto;
import com.magenta.echo.driverpay.core.enums.BalanceType;
import com.magenta.echo.driverpay.core.enums.PaymentStatus;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 28-05-2016 23:10
 */
@Component
@Transactional
public class PaymentLoader extends AbstractBean {

	@SuppressWarnings("unchecked")
	public List<PaymentReasonDto> loadPaymentReasonList(@NotNull final Long driverId)	{

		return getEntityManager()
				.createNativeQuery(getQuery(Queries.LOAD_PAYMENT_REASON_LIST_BY_DRIVER))
				.setParameter("driverId",driverId)
				.unwrap(SQLQuery.class)
				.setResultTransformer(Transformers.aliasToBean(PaymentReasonDto.class))
				.list();

	}

	public List<Payment> loadPaymentList(
			@NotNull final Long driverId,
			@NotNull final LocalDate upTo
	)	{

		return getEntityManager()
				.createQuery(getQuery(Queries.LOAD_PAYMENT_LIST_BY_DRIVER_AND_DATE), Payment.class)
				.setParameter("driverId",driverId)
				.setParameter("upTo",upTo)
				.setParameter("status", PaymentStatus.NONE)
				.setParameter("balance", BalanceType.DRIVER)
				.getResultList();

	}

	public List<Payment> loadPaymentList(
			@NotNull final Long driverId,
			@NotNull final Long paymentDocumentId
	)	{

		return getEntityManager()
				.createQuery(getQuery(Queries.LOAD_PAYMENT_LIST_BY_DRIVER_AND_PAYMENT_DOCUMENT), Payment.class)
				.setParameter("driverId",driverId)
				.setParameter("paymentDocumentId",paymentDocumentId)
				.getResultList();

	}

	public List<Payment> loadPaymentList(@NotNull final Long paymentReasonId)	{

		return getEntityManager()
				.createQuery(getQuery(Queries.LOAD_PAYMENT_LIST_BY_PAYMENT_REASON), Payment.class)
				.setParameter("paymentReasonId",paymentReasonId)
				.getResultList();

	}

	public List<PaymentDocument> loadPaymentDocumentList(@NotNull final Long driverId)	{

		return getEntityManager()
				.createQuery(getQuery(Queries.LOAD_PAYMENT_DOCUMENT_LIST_BY_DRIVER), PaymentDocument.class)
				.setParameter("driverId",driverId)
				.getResultList();

	}

}
