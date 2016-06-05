package com.magenta.echo.driverpay.core.entity;


import com.magenta.echo.driverpay.core.entity.constraint.CheckDriverBalances;
import com.magenta.echo.driverpay.core.enums.BalanceType;
import com.magenta.echo.driverpay.core.validation.group.Update;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
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
// todo check for update - it should not contains changing of balances
public class Driver implements Identified{

	@Id
	@GeneratedValue
	private Long id;

	@NotEmpty(message = "Should not be empty",groups = Default.class)
	@Length(max = 255, message = "Should be less than 255")
	private String name;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "driver")
	@NotNull(message = "Should not be null")
	@CheckDriverBalances(groups = Update.class)
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

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		Driver driver = (Driver)o;

		return id != null ? id.equals(driver.id) : driver.id == null;

	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
