package com.magenta.echo.driverpay.core.entity;

import java.time.LocalDate;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 03:47
 */
public class PaymentDocumentDto {
	private Long id;
	private Long driverId;
	private String driverValue;
	private LocalDate paymentDate;
	private Boolean processed;

	public PaymentDocumentDto() {
	}

	public PaymentDocumentDto(Long id, Long driverId, String driverValue, LocalDate paymentDate, Boolean processed) {
		this.id = id;
		this.driverId = driverId;
		this.driverValue = driverValue;
		this.paymentDate = paymentDate;
		this.processed = processed;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	public String getDriverValue() {
		return driverValue;
	}

	public void setDriverValue(String driverValue) {
		this.driverValue = driverValue;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Boolean getProcessed() {
		return processed;
	}

	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}
}
