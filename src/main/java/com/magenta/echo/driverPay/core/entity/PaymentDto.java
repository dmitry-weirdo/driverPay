package com.magenta.echo.driverpay.core.entity;

import com.magenta.echo.driverpay.core.enums.PaymentStatus;
import com.magenta.echo.driverpay.core.enums.PaymentType;

import java.time.LocalDate;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 02:37
 */
public class PaymentDto {
	private Long id;
	private PaymentType type;
	private PaymentStatus status = PaymentStatus.NONE;
	private LocalDate plannedDate;
	private Long driverId;
	private Double net;
	private Double vat;
	private Double total;
	private String nominalCode;
	private String taxCode;

	public PaymentDto() {
	}

	public PaymentDto(
			final Long id,
			final PaymentType type,
			final PaymentStatus status,
			final LocalDate plannedDate,
			final Long driverId,
			final Double net,
			final Double vat,
			final Double total,
			final String nominalCode,
			final String taxCode
	) {
		this.id = id;
		this.type = type;
		this.status = status;
		this.plannedDate = plannedDate;
		this.driverId = driverId;
		this.net = net;
		this.vat = vat;
		this.total = total;
		this.nominalCode = nominalCode;
		this.taxCode = taxCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PaymentType getType() {
		return type;
	}

	public void setType(PaymentType type) {
		this.type = type;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public LocalDate getPlannedDate() {
		return plannedDate;
	}

	public void setPlannedDate(LocalDate plannedDate) {
		this.plannedDate = plannedDate;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	public Double getNet() {
		return net;
	}

	public void setNet(Double net) {
		this.net = net;
	}

	public Double getVat() {
		return vat;
	}

	public void setVat(Double vat) {
		this.vat = vat;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getNominalCode() {
		return nominalCode;
	}

	public void setNominalCode(String nominalCode) {
		this.nominalCode = nominalCode;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
}
