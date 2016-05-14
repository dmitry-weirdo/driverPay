package com.magenta.echo.driverpay.core.enums;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 13-05-2016 01:03
 */
@Deprecated
public enum ChargeStatus {
	NOT_CHARGED("Not Charged"), APPROVED("Approved"), CHARGED("Charged");

	private String label;

	ChargeStatus(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
