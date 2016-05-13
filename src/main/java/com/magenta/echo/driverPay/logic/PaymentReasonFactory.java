/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package com.magenta.echo.driverPay.logic;

import com.magenta.echo.driverPay.*;

public class PaymentReasonFactory
{
	public static PaymentReason createManualTransaction(final Driver driver, final Balance companyBalance) {
		final PaymentReason reason = new PaymentReasonImpl();

		reason.setType(PaymentReasonType.MANUAL_TRANSACTION);
		reason.setChargeType(ChargeType.SINGLE); // todo: think about this

		// from driver balance to company balance
		reason.setFromBalance( driver.getBalance() );
		reason.setToBalance( companyBalance );

		// todo: fill other fields

		return reason;
	}

	public static PaymentReason createDeposit(final Driver driver) {
		final PaymentReason reason = new PaymentReasonImpl();

		reason.setType(PaymentReasonType.DEPOSIT);
		reason.setChargeType(ChargeType.INCREMENTAL); // todo: think about this

		// from driver balance to driver deposit balance
		reason.setFromBalance( driver.getBalance() );
		reason.setToBalance( driver.getDepositBalance() );

		// todo: fill other fields

		return reason;
	}

	public static PaymentReason createReleaseDepositToDriver(final Driver driver) {
		final PaymentReason reason = new PaymentReasonImpl();

		reason.setType(PaymentReasonType.RELEASE_DEPOSIT_TO_DRIVER);
		reason.setChargeType(ChargeType.SINGLE); // todo: think about this

		// from driver deposit balance to driver balance
		reason.setFromBalance( driver.getDepositBalance() );
		reason.setToBalance( driver.getBalance() );

		// todo: fill other fields

		return reason;
	}

	public static PaymentReason createReleaseDepositToDriver(final Driver driver, final Balance companyBalance) {
		final PaymentReason reason = new PaymentReasonImpl();

		reason.setType(PaymentReasonType.RELEASE_DEPOSIT_TO_COMPANY);
		reason.setChargeType(ChargeType.SINGLE); // todo: think about this

		// from driver deposit balance to company balance
		reason.setFromBalance( driver.getDepositBalance() );
		reason.setToBalance( companyBalance );

		// todo: fill other fields

		return reason;
	}

	// todo: job payment - from company balance to driver balance
	// todo: think about cash jobs - is it a special case "from driver balance to company balance"?

	// todo: credits of all types - from driver balance to company balance

	// todo: deductions of all types - from company balance to driver balance

	// todo: what about opening balances?
}