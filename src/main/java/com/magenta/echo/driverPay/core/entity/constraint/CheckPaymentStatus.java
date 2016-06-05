package com.magenta.echo.driverpay.core.entity.constraint;

import com.magenta.echo.driverpay.core.enums.PaymentStatus;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 01-06-2016 01:53
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckPaymentStatusValidator.class)
@Documented
public @interface CheckPaymentStatus {
	String message() default "Should be NONE";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	PaymentStatus[] expected() default {};

	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List	{
		CheckPaymentStatus[] value();
	}
}
