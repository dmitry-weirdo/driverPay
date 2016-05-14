package com.magenta.echo.driverpay.core.entity;

import com.magenta.echo.driverpay.core.enums.BalanceType;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 13-05-2016 17:43
 */
public class DriverDto {
    private Long id;
    private String name;
    private Long salaryBalanceId;
    private BalanceType salaryBalanceType;
    private Long depositBalanceId;
    private BalanceType depositBalanceType;

	public DriverDto() {

	}

	public DriverDto(String name) {
		this.name = name;
	}

	public DriverDto(Long id, String name, Long salaryBalanceId, BalanceType salaryBalanceType, Long depositBalanceId, BalanceType depositBalanceType) {
		this.id = id;
		this.name = name;
		this.salaryBalanceId = salaryBalanceId;
		this.salaryBalanceType = salaryBalanceType;
		this.depositBalanceId = depositBalanceId;
		this.depositBalanceType = depositBalanceType;
	}

	public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getSalaryBalanceId() {
        return salaryBalanceId;
    }

    public void setSalaryBalanceId(final Long salaryBalanceId) {
        this.salaryBalanceId = salaryBalanceId;
    }

    public BalanceType getSalaryBalanceType() {
        return salaryBalanceType;
    }

    public void setSalaryBalanceType(final BalanceType salaryBalanceType) {
        this.salaryBalanceType = salaryBalanceType;
    }

    public Long getDepositBalanceId() {
        return depositBalanceId;
    }

    public void setDepositBalanceId(final Long depositBalanceId) {
        this.depositBalanceId = depositBalanceId;
    }

    public BalanceType getDepositBalanceType() {
        return depositBalanceType;
    }

    public void setDepositBalanceType(final BalanceType depositBalanceType) {
        this.depositBalanceType = depositBalanceType;
    }
}
