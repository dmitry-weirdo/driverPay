package com.magenta.echo.driverpay.core.enums;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 05-06-2016 13:58
 */
public enum PaymentDocumentMethod {
	CASH("Cash"),ACCOUNT("Account");

	private String label;

	PaymentDocumentMethod(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
