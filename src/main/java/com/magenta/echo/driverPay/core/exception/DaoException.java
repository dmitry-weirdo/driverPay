package com.magenta.echo.driverpay.core.exception;

import org.jetbrains.annotations.NotNull;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 24-05-2016 23:25
 */
public class DaoException extends RuntimeException	{

//	private DataCheckResult dataCheckResult;
	private Set<ConstraintViolation> errors;

	public DaoException() {
	}

	public DaoException(Set errors) {
		this.errors = errors;
	}

	public DaoException(
			@NotNull final String field,
			@NotNull final String description
	)	{
		errors = new HashSet<>();
//		errors.add(ConstraintViolationImpl.forParameterValidation()));
	}

//	public DaoException(@NotNull final DataCheckResult dataCheckResult) {
//		this.dataCheckResult = dataCheckResult;
//	}
//
//	public DataCheckResult getDataCheckResult() {
//		return dataCheckResult;
//	}

	public Set<ConstraintViolation> getErrors() {
		return errors;
	}
}
