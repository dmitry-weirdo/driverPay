package com.magenta.echo.driverpay.core.entity.dto;

import com.magenta.echo.driverpay.core.enums.PaymentType;
import com.magenta.echo.driverpay.core.enums.ScheduleType;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 28-05-2016 23:00
 */
public class PaymentReasonDto {
	private Long id;
	private String name;
	private PaymentType paymentType;
	private ScheduleType scheduleType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

//	public void setPaymentType(PaymentType paymentType) {
//		this.paymentType = paymentType;
//	}
	public void setPaymentType(String paymentType)	{
		this.paymentType = PaymentType.valueOf(paymentType);
	}

	public ScheduleType getScheduleType() {
		return scheduleType;
	}

//	public void setScheduleType(ScheduleType scheduleType) {
//		this.scheduleType = scheduleType;
//	}
	public void setScheduleType(String scheduleType) {
		this.scheduleType = ScheduleType.valueOf(scheduleType);
	}
}
