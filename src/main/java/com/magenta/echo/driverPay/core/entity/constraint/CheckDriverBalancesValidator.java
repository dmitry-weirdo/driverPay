package com.magenta.echo.driverpay.core.entity.constraint;

import com.magenta.echo.driverpay.core.entity.Balance;
import com.magenta.echo.driverpay.core.enums.BalanceType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Project: driverPay-prototype
 * Author:  Lebedev
 * Created: 31-05-2016 20:14
 */
public class CheckDriverBalancesValidator implements ConstraintValidator<CheckDriverBalances,Set<Balance>> {
    @Override
    public void initialize(final CheckDriverBalances checkDriverBalances) {}

    @Override
    public boolean isValid(final Set<Balance> balanceSet, final ConstraintValidatorContext constraintValidatorContext) {

        if(balanceSet == null)  {
            return true;
        }

        if(balanceSet.size() != 2)    {
            return false;
        }

        final List<BalanceType> balanceTypeList = balanceSet
                .stream()
                .map(Balance::getType)
                .collect(Collectors.toList());

        return balanceTypeList.containsAll(Arrays.asList(BalanceType.DRIVER,BalanceType.DEPOSIT));

    }
}
