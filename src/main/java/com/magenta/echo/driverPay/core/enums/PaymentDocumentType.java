package com.magenta.echo.driverpay.core.enums;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 05-06-2016 13:56
 */
public enum PaymentDocumentType {
	SALARY("Salary"),IMMEDIATE("Immediate");

	private String label;

	PaymentDocumentType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
