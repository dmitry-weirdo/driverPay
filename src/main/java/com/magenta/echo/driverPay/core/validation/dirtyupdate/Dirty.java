package com.magenta.echo.driverpay.core.validation.dirtyupdate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 04-06-2016 01:36
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DirtyValidator.class)
public @interface Dirty {
	String message() default "Unable to update";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
