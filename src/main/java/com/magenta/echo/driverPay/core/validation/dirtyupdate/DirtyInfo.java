package com.magenta.echo.driverpay.core.validation.dirtyupdate;

import com.magenta.echo.driverpay.core.entity.Identified;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.Type;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 04-06-2016 15:33
 */
@Dirty
public class DirtyInfo {
	private Object entity;
	private Serializable id;
	private Object[] currentState;
	private Object[] previousState;
	private List<String> propertyNames;
	private Type[] types;

	public DirtyInfo(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		this.entity = entity;
		this.id = id;
		this.currentState = currentState;
		this.previousState = previousState;
		this.propertyNames = Arrays.asList(propertyNames);
		this.types = types;
	}

	public Object getEntity() {
		return entity;
	}

	public Serializable getId() {
		return id;
	}

	public Object[] getCurrentState() {
		return currentState;
	}

	public Object[] getPreviousState() {
		return previousState;
	}

	public List<String> getPropertyNames() {
		return propertyNames;
	}

	public Type[] getTypes() {
		return types;
	}

	@SuppressWarnings("unchecked")
	public <T> T getCurrentValue(@NotNull final String property)	{
		final int index = propertyNames.indexOf(property);
		if(index < 0)	{
			throw new IllegalArgumentException(String.format("Unknown property [%s]",property));
		}
		return (T)currentState[index];
	}

	@SuppressWarnings("unchecked")
	public <T> T getPreviousValue(@NotNull final String property)	{
		final int index = propertyNames.indexOf(property);
		if(index < 0)	{
			throw new IllegalArgumentException(String.format("Unknown property [%s]",property));
		}
		return (T)previousState[index];
	}

	public boolean checkPropertyEquality(@NotNull final String property) {
		final Object previous = getPreviousValue(property);
		final Object current = getCurrentValue(property);

		if(previous == null && current == null)	{
			return true;
		}

		if(previous == null || current == null) {
			return false;
		}

		if(previous instanceof Identified && current instanceof Identified)	{
			return Objects.equals(getId((Identified)previous), getId((Identified)current));
		}

		return Objects.equals(previous,current);
	}

	private Long getId(@NotNull final Identified object)	{

		if(object instanceof HibernateProxy)	{

			final HibernateProxy objectProxy = (HibernateProxy)object;
			return (Long)objectProxy.getHibernateLazyInitializer().getIdentifier();

		}else {

			return object.getId();

		}
	}
}
