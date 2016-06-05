package com.magenta.echo.driverpay.core.entity.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 04-06-2016 19:15
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckProcessedValidator.class)
@Documented
public @interface CheckProcessed {
	String message() default "Should be unprocessed";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String expected();

	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List	{
		CheckProcessed[] value();
	}
}
