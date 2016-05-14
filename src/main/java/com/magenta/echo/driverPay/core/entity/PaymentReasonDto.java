package com.magenta.echo.driverpay.core.entity;

import com.magenta.echo.driverpay.core.enums.PaymentType;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 13-05-2016 17:30
 */
public class PaymentReasonDto {
    private Long id;
    private String name;
	private Long driverId;
	private String driverValue;
	private PaymentType type;

	public PaymentReasonDto() {
	}

	public PaymentReasonDto(Long id, String name, Long driverId, String driverValue, PaymentType type) {
		this.id = id;
		this.name = name;
		this.driverId = driverId;
		this.driverValue = driverValue;
		this.type = type;
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

	public PaymentType getType() {
		return type;
	}

	public void setType(PaymentType type) {
		this.type = type;
	}
}
