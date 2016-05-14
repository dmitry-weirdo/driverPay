package com.magenta.echo.driverpay.core.bean;

import com.magenta.echo.driverpay.core.db.ConnectionExt;
import com.magenta.echo.driverpay.core.entity.DriverDto;
import com.magenta.echo.driverpay.core.enums.BalanceType;
import com.magenta.echo.driverpay.core.exception.EntryNotExist;
import com.magenta.echo.driverpay.ui.Utils;

import java.util.List;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 13-05-2016 17:43
 */
public class DriverBean extends AbstractBean {

    public List<DriverDto> loadDriverList() {

        return getDataManager().executeQuery(
				"select\n" +
					"d.id,\n" +
					"d.name,\n" +
					"d.salary_balance_id,\n" +
					"s.type salary_type,\n" +
					"d.deposit_balance_id,\n" +
					"dep.type deposit_type\n" +
				"from drivers d\n" +
				"join balances s on d.salary_balance_id = s.id\n" +
				"join balances dep on d.deposit_balance_id = dep.id",
				preparedStatement -> {},
				resultSet -> new DriverDto(
						resultSet.getLong("id"),
						resultSet.getString("name"),
						resultSet.getLong("salary_balance_id"),
						BalanceType.valueOf(resultSet.getString("salary_type")),
						resultSet.getLong("deposit_balance_id"),
						BalanceType.valueOf(resultSet.getString("deposit_type"))
				)
		);

    }

    public DriverDto loadDriver(final Long driverId)    {

		final List<DriverDto> result = getDataManager().executeQuery(
				"select\n" +
					"d.id,\n" +
					"d.name,\n" +
					"d.salary_balance_id,\n" +
					"s.type salary_type,\n" +
					"d.deposit_balance_id,\n" +
					"dep.type deposit_type\n" +
				"from drivers d\n" +
				"join balances s on d.salary_balance_id = s.id\n" +
				"join balances dep on d.deposit_balance_id = dep.id\n" +
				"where\n" +
					"d.id = ?",
				preparedStatement -> preparedStatement.setLong(1, driverId),
				resultSet -> new DriverDto(
						resultSet.getLong("id"),
						resultSet.getString("name"),
						resultSet.getLong("salary_balance_id"),
						BalanceType.valueOf(resultSet.getString("salary_type")),
						resultSet.getLong("deposit_balance_id"),
						BalanceType.valueOf(resultSet.getString("deposit_type"))
				)
		);

		if(result.size() != 1)	{
			throw new EntryNotExist(Utils.toString(driverId));
		}

		return result.get(0);

    }

    public void updateDriver(final DriverDto driverDto) {

		try(final ConnectionExt connection = getDataManager().getConnection()) {

			if(driverDto.getId() == null)	{

				buildNewDriver(driverDto.getName());

			}else {

				updateDriver(
						driverDto.getId(),
						driverDto.getName()
				);

			}

			connection.success();

		}catch(Exception e) {
			throw new RuntimeException(e);
		}

	}

	private Long buildSalaryBalance()	{
		return getDataManager().executeInsert(
				"insert into balances (id,type) values (null,?)",
				preparedStatement -> preparedStatement.setString(1, BalanceType.DRIVER.name())
		);
	}

	private Long buildDepositBalance()	{
		return getDataManager().executeInsert(
				"insert into balances (id,type) values (null,?)",
				preparedStatement -> preparedStatement.setString(1, BalanceType.DEPOSIT.name())
		);
	}

	private void buildNewDriver(final String name)	{

		final Long salaryBalanceId = buildSalaryBalance();
		final Long depositBalanceId = buildDepositBalance();

		getDataManager().executeInsert(
				"insert into drivers (id,name,salary_balance_id,deposit_balance_id) values (null,?,?,?)",
				preparedStatement -> {
					preparedStatement.setString(1,name);
					preparedStatement.setLong(2,salaryBalanceId);
					preparedStatement.setLong(3,depositBalanceId);
				}
		);

	}

	private void updateDriver(final Long id, final String name)	{
		getDataManager().executeUpdate(
				"update drivers set name = ? where id = ?",
				preparedStatement -> {
					preparedStatement.setString(1, name);
					preparedStatement.setLong(2, id);
				}
		);
	}

}
