package com.magenta.echo.driverpay.core.enums;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 25-05-2016 01:42
 */
public enum ScheduleType {
	NONE("None"),REPEAT("Repeat"),INCREMENTAL("Incremental");

	private String label;

	ScheduleType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
