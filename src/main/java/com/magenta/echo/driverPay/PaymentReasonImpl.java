/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package com.magenta.echo.driverPay;

import java.util.List;

public class PaymentReasonImpl implements PaymentReason
{
	private Long id;
	private Driver driver;
	private PaymentReasonType type;
	private ChargeType chargeType;
	private Balance fromBalance;
	private Balance toBalance;
	private List<Payment> payments;
	private String name;
	private String description;
	private Double net;
	private Double vat;
	private Double total;
	private Double debtNet;
	private Double debtVat;
	private Double debtTotal;

	@Override
	public Long getId() {
		return id;
	}
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	@Override
	public Driver getDriver() {
		return driver;
	}
	@Override
	public void setDriver(final Driver driver) {
		this.driver = driver;
	}

	@Override
	public PaymentReasonType getType() {
		return type;
	}
	@Override
	public void setType(final PaymentReasonType type) {
		this.type = type;
	}

	@Override
	public ChargeType getChargeType() {
		return chargeType;
	}
	@Override
	public void setChargeType(final ChargeType chargeType) {
		this.chargeType = chargeType;
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
	public List<Payment> getPayments() {
		return payments;
	}
	@Override
	public void setPayments(final List<Payment> payments) {
		this.payments = payments;
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
	public String getDescription() {
		return description;
	}
	@Override
	public void setDescription(final String description) {
		this.description = description;
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

	@Override
	public Double getDebtNet() {
		return debtNet;
	}
	@Override
	public void setDebtNet(final Double debtNet) {
		this.debtNet = debtNet;
	}

	@Override
	public Double getDebtVat() {
		return debtVat;
	}
	@Override
	public void setDebtVat(final Double debtVat) {
		this.debtVat = debtVat;
	}

	@Override
	public Double getDebtTotal() {
		return debtTotal;
	}
	@Override
	public void setDebtTotal(final Double debtTotal) {
		this.debtTotal = debtTotal;
	}
}