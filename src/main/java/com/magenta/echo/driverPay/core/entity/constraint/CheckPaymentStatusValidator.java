package com.magenta.echo.driverpay.core.entity.constraint;

import com.magenta.echo.driverpay.core.enums.PaymentStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 01-06-2016 01:53
 */
public class CheckPaymentStatusValidator implements ConstraintValidator<CheckPaymentStatus,PaymentStatus> {

	private List<PaymentStatus> expected;

	@Override
	public void initialize(CheckPaymentStatus checkPaymentStatus) {
		expected = Arrays.asList(checkPaymentStatus.expected());
	}

	@Override
	public boolean isValid(PaymentStatus paymentStatus, ConstraintValidatorContext constraintValidatorContext) {

		return paymentStatus == null || expected.contains(paymentStatus);

	}
}
