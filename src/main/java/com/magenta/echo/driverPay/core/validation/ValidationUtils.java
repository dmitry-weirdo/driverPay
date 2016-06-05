package com.magenta.echo.driverpay.core.validation;

import com.magenta.echo.driverpay.core.Context;
import org.jetbrains.annotations.NotNull;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 01-06-2016 01:32
 */
public class ValidationUtils {

	public static <T> void validate(@NotNull final T entity, @NotNull final Class<?>... groups)	{
		final Validator validator = Context.get().getValidator();
		final Set<ConstraintViolation<T>> errors = validator.validate(entity, groups);
		if(!errors.isEmpty())	{
			throw new ConstraintViolationException(errors);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Map<String,String> getMessagesByProperty(@NotNull final Set<ConstraintViolation<T>> errors)	{

		return errors
				.stream()
				.collect(Collectors.groupingBy(
						ValidationUtils::getFieldName,
						Collectors.mapping(
								ConstraintViolation::getMessage,
								Collectors.joining()
						)
				));

	}

	private static String getFieldName(@NotNull final ConstraintViolation<?> violation)	{
		final Iterator<Path.Node> iterator = violation.getPropertyPath().iterator();
		if(iterator.hasNext())	{
			final Path.Node node = iterator.next();
			return node.getName() == null ? "" : node.getName();
		}
		return "";
	}

	public static ConstraintViolationException getConstraintViolationException(@NotNull final Throwable e)	{
		if(e instanceof ConstraintViolationException)	{
			return (ConstraintViolationException)e;
		}

		if(e.getCause() != null)	{
			return getConstraintViolationException(e.getCause());
		}

		return null;
	}
}
