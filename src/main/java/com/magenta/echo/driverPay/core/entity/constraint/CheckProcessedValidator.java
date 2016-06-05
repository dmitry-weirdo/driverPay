package com.magenta.echo.driverpay.core.entity.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 04-06-2016 19:15
 */
public class CheckProcessedValidator implements ConstraintValidator<CheckProcessed,Boolean> {

	private Boolean expected;

	@Override
	public void initialize(CheckProcessed checkProcessed) {
		expected = Boolean.getBoolean(checkProcessed.expected());
	}

	@Override
	public boolean isValid(Boolean processed, ConstraintValidatorContext constraintValidatorContext) {
		return processed == null || Objects.equals(expected,processed);
	}
}
