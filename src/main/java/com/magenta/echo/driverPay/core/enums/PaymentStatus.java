package com.magenta.echo.driverpay.core.enums;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 02:37
 */
public enum PaymentStatus {
	NONE("None"), CALCULATED("Calculated"), PROCESSED("Processed");

	private String label;

	PaymentStatus(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}

