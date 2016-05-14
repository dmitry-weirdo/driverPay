package com.magenta.echo.driverpay.core.entity;

import java.time.LocalDate;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 03:49
 */
public class TransactionDto {
	private Long id;
	private LocalDate date;
	private String balanceFromValue;
	private String balanceToValue;
	private Double net;
	private Double vat;
	private Double total;

	public TransactionDto() {
	}

	public TransactionDto(Long id, LocalDate date, String balanceFromValue, String balanceToValue, Double net, Double vat, Double total) {
		this.id = id;
		this.date = date;
		this.balanceFromValue = balanceFromValue;
		this.balanceToValue = balanceToValue;
		this.net = net;
		this.vat = vat;
		this.total = total;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getBalanceFromValue() {
		return balanceFromValue;
	}

	public void setBalanceFromValue(String balanceFromValue) {
		this.balanceFromValue = balanceFromValue;
	}

	public String getBalanceToValue() {
		return balanceToValue;
	}

	public void setBalanceToValue(String balanceToValue) {
		this.balanceToValue = balanceToValue;
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
}
