package com.magenta.echo.driverpay.core.entity;

import java.time.LocalDate;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 13-05-2016 01:01
 */
public class JobDto {
	private Long id;
	private LocalDate jobDate;
	private Long driverId;
	private String driverValue;

	public JobDto() {
	}

	public JobDto(Long id, LocalDate jobDate, Long driverId, String driverValue) {
		this.id = id;
		this.jobDate = jobDate;
		this.driverId = driverId;
		this.driverValue = driverValue;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getJobDate() {
		return jobDate;
	}

	public void setJobDate(LocalDate jobDate) {
		this.jobDate = jobDate;
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
}
