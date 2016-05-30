package com.magenta.echo.driverpay.core.entity;


import com.magenta.echo.driverpay.core.enums.BalanceType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Project: Driver Pay Enhanced
 * Author:  Evgeniy
 * Created: 26-05-2016 23:24
 */
@Entity
@Table(name = "drivers")
public class Driver {

	@Id
	@GeneratedValue
	private Long id;

	@NotEmpty(message = "Should not be empty")
	@Length(max = 255, message = "Should be less than 255")
	private String name;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "driver")
	private Set<Balance> balances;

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

	public Set<Balance> getBalances() {
		return balances;
	}

	public void setBalances(final Set<Balance> balances) {
		this.balances = balances;
	}

	public Balance getDriverBalance()	{
		final Optional<Balance> balanceHolder = getBalances()
				.stream()
				.filter(balance -> Objects.equals(BalanceType.DRIVER,balance.getType()))
				.findFirst();
		if(!balanceHolder.isPresent())	{
			throw new IllegalStateException("Drive does not have Driver balance");
		}
		return balanceHolder.get();
	}

	public Balance getDepositBalance()	{
		final Optional<Balance> balanceHolder = getBalances()
				.stream()
				.filter(balance -> Objects.equals(BalanceType.DEPOSIT,balance.getType()))
				.findFirst();
		if(!balanceHolder.isPresent())	{
			throw new IllegalStateException("Drive does not have Driver balance");
		}
		return balanceHolder.get();
	}
}
