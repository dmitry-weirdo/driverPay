/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package com.magenta.echo.driverPay;

import java.util.Date;

public class TransactionImpl implements Transaction
{
	private Long id;
	private Date date;
	private Balance fromBalance;
	private Balance toBalance;
	private Double total;
	private Payment payment;

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
	public Balance getFromBalance() {
		return fromBalance;
	}
	@Override
	public void setFromBalance(final Balance fromBalance) {
		this.fromBalance = fromBalance;
	}

	@Override
	public Balance getToBalance() {
		return toBalance;
	}
	@Override
	public void setToBalance(final Balance toBalance) {
		this.toBalance = toBalance;
	}

	@Override
	public Double getTotal() {
		return total;
	}
	@Override
	public void setTotal(final Double total) {
		this.total = total;
	}

	@Override
	public Payment getPayment() {
		return payment;
	}
	@Override
	public void setPayment(final Payment payment) {
		this.payment = payment;
	}
}