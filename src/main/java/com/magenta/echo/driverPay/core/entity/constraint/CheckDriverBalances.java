package com.magenta.echo.driverpay.core.entity.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Project: driverPay-prototype
 * Author:  Lebedev
 * Created: 31-05-2016 20:12
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckDriverBalancesValidator.class)
@Documented
public @interface CheckDriverBalances {
    String message() default "Driver should contains DRIVER and DEPOSIT balances";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
