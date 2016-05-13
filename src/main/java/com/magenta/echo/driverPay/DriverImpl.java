/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package com.magenta.echo.driverPay;

import java.util.List;

public class DriverImpl implements Driver
{
	private Long id;
	private String name;
	private Balance balance;
	private Balance depositBalance;
	private List<PaymentReason> paymentReasons;
	private List<PaymentDocument> paymentDocuments;

	@Override
	public Long getId() {
		return id;
	}
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public Balance getBalance() {
		return balance;
	}
	@Override
	public void setBalance(final Balance balance) {
		this.balance = balance;
	}

	@Override
	public Balance getDepositBalance() {
		return depositBalance;
	}
	@Override
	public void setDepositBalance(final Balance depositBalance) {
		this.depositBalance = depositBalance;
	}

	@Override
	public List<PaymentReason> getPaymentReasons() {
		return paymentReasons;
	}
	@Override
	public void setPaymentReasons(final List<PaymentReason> paymentReasons) {
		this.paymentReasons = paymentReasons;
	}

	@Override
	public List<PaymentDocument> getPaymentDocuments() {
		return paymentDocuments;
	}
	@Override
	public void setPaymentDocuments(final List<PaymentDocument> paymentDocuments) {
		this.paymentDocuments = paymentDocuments;
	}
}