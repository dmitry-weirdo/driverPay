package com.magenta.echo.driverpay.core.entity;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 13-05-2016 01:06
 */
public class JobRateDto {
	private Long id;
	private Double net;
	private String nominalCode;
	private String taxCode;

	public JobRateDto() {
	}

	public JobRateDto(Long id, Double net, String nominalCode, String taxCode) {
		this.id = id;
		this.net = net;
		this.nominalCode = nominalCode;
		this.taxCode = taxCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getNet() {
		return net;
	}

	public void setNet(Double net) {
		this.net = net;
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
