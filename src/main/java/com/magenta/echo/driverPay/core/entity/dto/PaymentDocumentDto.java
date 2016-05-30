package com.magenta.echo.driverpay.core.entity.dto;

import java.time.LocalDate;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 28-05-2016 23:01
 */
@Deprecated
public class PaymentDocumentDto {
	public Long id;
	public LocalDate date;
	public Boolean processed;
}
