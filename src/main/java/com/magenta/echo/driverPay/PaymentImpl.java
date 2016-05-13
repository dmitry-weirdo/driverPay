/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package com.magenta.echo.driverPay;

import java.util.Date;

public class PaymentImpl implements Payment
{
	private Long id;
	private Date date;
	private PaymentStatus status;
	private PaymentDocument paymentDocument;
	private Transaction transaction;
	private Double net;
	private Double vat;
	private Double total;

	@Override
	public Long getId() {
		return id;
	}
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	@Override
	public Date getDate() {
		return date;
	}
	@Override
	public void setDate(final Date date) {
		this.date = date;
	}

	@Override
	public PaymentStatus getStatus() {
		return status;
	}
	@Override
	public void setStatus(final PaymentStatus status) {
		this.status = status;
	}

	@Override
	public PaymentDocument getPaymentDocument() {
		return paymentDocument;
	}
	@Override
	public void setPaymentDocument(final PaymentDocument paymentDocument) {
		this.paymentDocument = paymentDocument;
	}

	@Override
	public Transaction getTransaction() {
		return transaction;
	}
	@Override
	public void setTransaction(final Transaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public Double getNet() {
		return net;
	}
	@Override
	public void setNet(final Double net) {
		this.net = net;
	}

	@Override
	public Double getVat() {
		return vat;
	}
	@Override
	public void setVat(final Double vat) {
		this.vat = vat;
	}

	@Override
	public Double getTotal() {
		return total;
	}
	@Override
	public void setTotal(final Double total) {
		this.total = total;
	}
}