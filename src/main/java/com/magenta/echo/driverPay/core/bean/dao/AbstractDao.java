package com.magenta.echo.driverpay.core.bean.dao;

import com.magenta.echo.driverpay.core.bean.AbstractBean;
import org.jetbrains.annotations.NotNull;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 24-05-2016 23:38
 */
public abstract class AbstractDao<T> extends AbstractBean {

	public abstract Long insert(@NotNull final T entity);

	public abstract void update(@NotNull final T entity);

	public abstract void delete(@NotNull final Long id);

	public abstract T find(@NotNull final Long id);

}
