package com.magenta.echo.driverpay.core.enums;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 16:17
 */
public enum PaymentDocumentStatus {
	NONE("None"), PROCESSED("Processed");

	private String label;

	PaymentDocumentStatus(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
