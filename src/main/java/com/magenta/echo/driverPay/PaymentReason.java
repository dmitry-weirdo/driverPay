/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package com.magenta.echo.driverPay;

import com.magenta.echo.driverPay.common.Identified;

import java.util.List;

/**
 * Reason for any {@link Payment}. Reason is always linked to the driver.
 *
 * This may be one of these:
 * <ul>
 *   <li>Credit (single/incremental/repeat)</li>
 *   <li>Deposit (single/incremental/repeat)</li>
 *   <li>Manual transaction</li>
 *   <li>Release from driver deposit to the driver</li>
 *   <li>Release from driver deposit to the company</li>
 *   <li>Driver payment for job</li>
 *   // todo: something else?
 *   // todo: как тогда работает дедакшн по депозиту? Это два действия, одно с баланса драйвера на баланс компании, другое с баланса компании на баланс драйвер депозита. Или одно действие — с драйверского баланса на депозит?
 *   // todo: opening balance делаем как отдельную операцию или как отдельный Payment в рамках того же PaymentReaston?
 *   // todo: OpeningBalance возможен только для депозита, или для любого чарджа? Как быть с net/vat/total для OpeningBalance?
 * </ul>
 */
public interface PaymentReason extends Identified
{
	Driver getDriver();
	void setDriver(Driver driver);

	PaymentReasonType getType();
	void setType(PaymentReasonType type);

	// todo: для каких типов как заполняется?
	ChargeType getChargeType();
	void setChargeType(ChargeType chargeType);

	// balances logic depends on the intellectual code that depends upon payment action type
	Balance getFromBalance();
	void setFromBalance(Balance fromBalance);

	Balance getToBalance();
	void setToBalance(Balance toBalance);

	List<Payment> getPayments();
	void setPayments(List<Payment> payments);

	String getName();
	void setName(String name);

	String getDescription();
	void setDescription(String description);

	// total amounts
	Double getNet();
	void setNet(Double net);

	Double getVat();
	void setVat(Double vat);

	Double getTotal();
	void setTotal(Double total);

	// todo: think about weekly amounts for Incremental/Repeat/Deposit charges (but may be this must be an extension, not this interface)

	// outstanding amounts
	Double getDebtNet();
	void setDebtNet(Double debtNet);

	Double getDebtVat();
	void setDebtVat(Double debtVat);

	Double getDebtTotal();
	void setDebtTotal(Double debtTotal);

	// todo: не нужен ли признак типа includeInDriverStatement, который будет false для Manual Transaction?

	// todo: у наследников конкретных типов будут поля для чарджей (возможно, нужно выделить связанную сущность), для ManualTransaction
	// todo: нужно подумать об общих полях типа NominalCode, VatRate, TaxCode
	// todo: а также другие поля, например, из ManualTransaction
	// todo: у работы будет ссылка на Job
}