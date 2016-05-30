package com.magenta.echo.driverpay.core.entity.dto;

import com.magenta.echo.driverpay.core.enums.JobType;

import java.time.LocalDate;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 29-05-2016 23:59
 */
public class JobDto {
	private Long id;
	private Long driverId;
	private String driverValue;
	private JobType type;
	private LocalDate jobDate;
	private Double pricing;
	private Double charging;

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

	public JobType getType() {
		return type;
	}

	public void setType(String type) {
		this.type = JobType.valueOf(type);
	}

	public LocalDate getJobDate() {
		return jobDate;
	}

	public void setJobDate(Long jobDate) {
		this.jobDate = LocalDate.ofEpochDay(jobDate);
	}

	public Double getPricing() {
		return pricing;
	}

	public void setPricing(Float pricing) {
		this.pricing = pricing.doubleValue();
	}

	public Double getCharging() {
		return charging;
	}

	public void setCharging(Double charging) {
		this.charging = charging;
	}
}
