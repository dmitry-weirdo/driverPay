/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package com.magenta.echo.driverPay;

public class BalanceImpl implements Balance
{
	private Long id;
	private BalanceType type;
	private Double total;

	@Override
	public Long getId() {
		return id;
	}
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	@Override
	public BalanceType getType() {
		return type;
	}
	@Override
	public void setType(final BalanceType type) {
		this.type = type;
	}

	@Override
	public Double getTotal() {
		return total;
	}
	@Override
	public void setTotal(final Double total) {
		this.total = total;
	}
}