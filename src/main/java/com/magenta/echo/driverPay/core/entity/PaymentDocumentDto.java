package com.magenta.echo.driverpay.core.entity;

import java.time.LocalDate;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 03:47
 */
public class PaymentDocumentDto {
	private Long id;
	private LocalDate paymentDate;
	private Boolean processed;

	public PaymentDocumentDto() {
	}

	public PaymentDocumentDto(Long id, LocalDate paymentDate, Boolean processed) {
		this.id = id;
		this.paymentDate = paymentDate;
		this.processed = processed;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
