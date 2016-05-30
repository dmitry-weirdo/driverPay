package com.magenta.echo.driverpay.core.entity.dto;

import java.time.LocalDate;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 13-05-2016 17:43
 */
public class DriverDto {
    private Long id;
    private String name;
	private Double calculatedSalary;
	private Double currentDeposit;
	private Double totalDeposit;
	private LocalDate lastSalaryCalculationDate;
	private LocalDate lastProcessingDate;

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

	public Double getCalculatedSalary() {
		return calculatedSalary;
	}

	public void setCalculatedSalary(Double calculatedSalary) {
		this.calculatedSalary = calculatedSalary;
	}

	public Double getCurrentDeposit() {
		return currentDeposit;
	}

	public void setCurrentDeposit(Double currentDeposit) {
		this.currentDeposit = currentDeposit;
	}

	public Double getTotalDeposit() {
		return totalDeposit;
	}

	public void setTotalDeposit(Double totalDeposit) {
		this.totalDeposit = totalDeposit;
	}

	public LocalDate getLastSalaryCalculationDate() {
		return lastSalaryCalculationDate;
	}

	public void setLastSalaryCalculationDate(Long lastSalaryCalculationDate) {
		if(lastSalaryCalculationDate == null)	{
			return;
		}
		this.lastSalaryCalculationDate = LocalDate.ofEpochDay(lastSalaryCalculationDate);
	}

	public LocalDate getLastProcessingDate() {
		return lastProcessingDate;
	}

	public void setLastProcessingDate(Long lastProcessingDate) {
		if(lastProcessingDate == null)	{
			return;
		}
		this.lastProcessingDate = LocalDate.ofEpochDay(lastProcessingDate);
	}
}
