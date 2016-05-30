package com.magenta.echo.driverpay.core.entity.dto;

import com.magenta.echo.driverpay.core.enums.PaymentType;

import java.time.LocalDate;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 28-05-2016 23:01
 */
@Deprecated
public class PaymentDto {
	public Long id;
	public String name;
	public PaymentType paymentType;
	public LocalDate date;
	public Double net;
	public Double vat;
	public Double total;
	public String nominalCode;
	public String taxCode;
}
