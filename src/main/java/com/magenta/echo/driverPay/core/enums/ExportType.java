package com.magenta.echo.driverpay.core.enums;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 05-06-2016 20:39
 */
public enum ExportType {
	STATEMENT("Statement"),SAGE("Sage"),BARCLAYS("Barclays");

	private String label;

	ExportType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
