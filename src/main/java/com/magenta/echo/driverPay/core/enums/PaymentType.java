package com.magenta.echo.driverpay.core.enums;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 16:19
 */
public enum PaymentType {
	REGULAR_JOB("Regular job"),			// from COMPANY to DRIVER
	CASH_JOB("Cash Job"),				// from DRIVER  to COMPANY
	CREDIT("Credit"),					// from COMPANY to DRIVER
	DEDUCTION("Deduction"),				// from DRIVER  to COMPANY
	DEPOSIT("Deposit"),					// from DRIVER  to DEPOSIT
	RELEASE_DEPOSIT("Release Deposit"),	// from DEPOSIT to DRIVER
	TAKE_DEPOSIT("Take Deposit"),		// from DEPOSIT to COMPANY
	REFILL_DEPOSIT("Refill Deposit"),	// from COMPANY to DEPOSIT ???
	CASH_PAYMENT("Cash Payment");		// from COMPANY to DRIVER

	private String label;

	PaymentType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
