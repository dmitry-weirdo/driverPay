package com.magenta.echo.driverpay.core.enums;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 22-05-2016 02:21
 */
public enum JobType {
	REGULAR_JOB("Regular"),CASH_JOB("Cash");

	private String label;

	JobType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
