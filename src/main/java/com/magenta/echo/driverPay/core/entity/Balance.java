package com.magenta.echo.driverpay.core.entity;

import com.magenta.echo.driverpay.core.enums.BalanceType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Project: Driver Pay Enhanced
 * Author:  Evgeniy
 * Created: 27-05-2016 00:06
 */
@Entity
@Table(name = "balances")
public class Balance {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	@NotNull(message =  "Should not be null")
	private BalanceType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "driver_id")
	private Driver driver;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BalanceType getType() {
		return type;
	}

	public void setType(BalanceType type) {
		this.type = type;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}
}
