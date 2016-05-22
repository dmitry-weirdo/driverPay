package com.magenta.echo.driverpay.core.entity;

import com.magenta.echo.driverpay.core.enums.JobType;

import java.time.LocalDate;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 13-05-2016 01:01
 */
public class JobDto {
	private Long id;
	private LocalDate jobDate;
	private JobType type;
	private Long driverId;
	private String driverValue;
	private Double total;

	public JobDto() {
	}

	public JobDto(Long id, LocalDate jobDate, JobType type, Long driverId, String driverValue, Double total) {
		this.id = id;
		this.jobDate = jobDate;
		this.type = type;
		this.driverId = driverId;
		this.driverValue = driverValue;
		this.total = total;
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

	public JobType getType() {
		return type;
	}

	public void setType(JobType type) {
		this.type = type;
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

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
}
