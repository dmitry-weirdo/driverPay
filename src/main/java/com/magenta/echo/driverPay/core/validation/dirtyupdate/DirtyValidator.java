package com.magenta.echo.driverpay.core.validation.dirtyupdate;

import com.magenta.echo.driverpay.core.validation.annotation.CheckUpdate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 04-06-2016 02:36
 */
public class DirtyValidator implements ConstraintValidator<Dirty,DirtyInfo> {

	@Override
	public void initialize(Dirty dirty) {}

	@Override
	public boolean isValid(DirtyInfo dirtyInfo, ConstraintValidatorContext constraintValidatorContext) {

		final Object entity = dirtyInfo.getEntity();
		final Annotation[] annotations = entity.getClass().getAnnotations();

		for(final Annotation annotation : annotations) {
			if(annotation instanceof CheckUpdate)	{
				try {
					final CheckUpdate checkUpdate = (CheckUpdate)annotation;
					final Class<? extends DirtyChecker> checkerClass = checkUpdate.checker();
					final DirtyChecker dirtyChecker = checkerClass.newInstance();
					return dirtyChecker.check(dirtyInfo);
				}catch(final Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		return true;
	}
}
