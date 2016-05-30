package com.magenta.echo.driverpay.core.bean;

import com.magenta.echo.driverpay.core.entity.Balance;
import com.magenta.echo.driverpay.core.entity.Driver;
import com.magenta.echo.driverpay.core.enums.BalanceType;
import com.magenta.echo.driverpay.core.enums.PaymentType;
import com.magenta.echo.driverpay.core.rule.PaymentTypeToTransactionRules;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Project: driverPay-prototype
 * Author:  Lebedev
 * Created: 26-05-2016 15:53
 */
@Component
@Transactional
public class BalanceBean extends AbstractBean {

    public Long getBalanceId(
            @NotNull final Long driverId,
            @NotNull final BalanceType balanceType
    )	{

        if(!Objects.equals(balanceType,BalanceType.COMPANY)) {
			return (Long)getEntityManager()
					.createNativeQuery(
							"select id from balances where driver_id = :driver and type = :type"
					)
					.setParameter("driver",driverId)
					.setParameter("type",BalanceType.COMPANY)
					.getSingleResult();
        }else {
            return (Long)getEntityManager()
					.createNativeQuery("select id from balances where type = :type")
					.setParameter("type",balanceType)
					.getSingleResult();
        }

    }

	public Balance getBalance(
			@NotNull Driver driver,
			@NotNull final BalanceType balanceType
	) {
		driver = getEntityManager().find(Driver.class,driver.getId());

		switch(balanceType) {
			case COMPANY:
				return getEntityManager()
						.createQuery("select b from Balance b where b.type = :type", Balance.class)
						.setParameter("type", BalanceType.COMPANY)
						.getSingleResult();
			case DRIVER:
				return driver.getDriverBalance();
			case DEPOSIT:
				return driver.getDepositBalance();
			default:
				throw new IllegalArgumentException(String.format("Unknown balance type, '%s'", balanceType));
		}
	}

	@Deprecated
    public Double getBalanceAmount(
            @NotNull final Long driverId,
            @NotNull final BalanceType balanceType
    )  {

        final Long driverBalanceId = getBalanceId(driverId, balanceType);

		return (Double)getEntityManager()
				.createNativeQuery(
						"select sum(" +
						"case " +
						"when from_id = :balance then -total " +
						"when to_id = :balance then total " +
						"else 0 end" +
						") total " +
						"from payments " +
						"where " +
						"driver_id = :driver " +
						"and (from_id = :balance or to_id = :balance)"
				)
				.setParameter("balance",driverBalanceId)
				.setParameter("driver",driverId)
				.getSingleResult();

    }

	public Balance getBalanceFrom(
			@NotNull final Driver driver,
			@NotNull final PaymentType paymentType
	)  {

		final BalanceType balanceType = PaymentTypeToTransactionRules.getFromBalanceType(paymentType);

		return getBalance(driver, balanceType);

	}

	public Balance getBalanceTo(
			@NotNull final Driver driver,
			@NotNull final PaymentType paymentType
	)  {

		final BalanceType balanceType = PaymentTypeToTransactionRules.getToBalanceType(paymentType);

		return getBalance(driver, balanceType);

	}

}
