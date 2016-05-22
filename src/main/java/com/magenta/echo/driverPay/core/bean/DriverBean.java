package com.magenta.echo.driverpay.core.bean;

import com.evgenltd.kwickui.controls.objectpicker.SimpleObject;
import com.magenta.echo.driverpay.core.db.ConnectionExt;
import com.magenta.echo.driverpay.core.entity.DriverDto;
import com.magenta.echo.driverpay.core.enums.BalanceType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 13-05-2016 17:43
 */
public class DriverBean extends AbstractBean {

	public List<SimpleObject> loadDriverList(final String pattern)	{

		return getDataManager().executeQuery(
				"select\n" +
					"d.id,\n" +
					"d.name\n" +
				"from drivers d\n" +
				"where d.name like ?",
				preparedStatement -> preparedStatement.setString(1,"%"+pattern+"%"),
				resultSet -> new SimpleObject(
						resultSet.getLong("id"),
						resultSet.getString("name")
				)
		);

	}

    public List<DriverDto> loadDriverList() {

        return getDataManager().executeQuery(
				"select\n" +
					"d.id,\n" +
					"d.name\n" +
				"from drivers d",
				preparedStatement -> {},
				resultSet -> new DriverDto(
						resultSet.getLong("id"),
						resultSet.getString("name")
				)
		);

    }

    public DriverDto loadDriver(@NotNull final Long driverId)    {

		return getDataManager().executeSingleQuery(
				"select\n" +
					"d.id,\n" +
					"d.name\n" +
				"from drivers d\n" +
				"where\n" +
					"d.id = ?",
				preparedStatement -> preparedStatement.setLong(1, driverId),
				resultSet -> new DriverDto(
						resultSet.getLong("id"),
						resultSet.getString("name")
				)
		);

    }

    public void updateDriver(@NotNull final DriverDto driverDto) {

		try(final ConnectionExt connection = getDataManager().getConnection()) {

			if(driverDto.getId() == null)	{

				addDriver(driverDto.getName());

			}else {

				updateDriver(driverDto.getId(), driverDto.getName());

			}

			connection.success();

		}catch(Exception e) {
			throw new RuntimeException(e);
		}

	}

	private Long addBalance(@NotNull final BalanceType type, @NotNull final Long driverId)	{
		return getDataManager().executeInsert(
				"insert into balances (id,type,driver_id) values (null,?,?)",
				preparedStatement -> {
					preparedStatement.setString(1, type.name());
					preparedStatement.setLong(2, driverId);
				}
		);
	}

	private void addDriver(@NotNull final String name)	{

		final Long driverId = getDataManager().executeInsert(
				"insert into drivers (id,name) values (null,?)",
				preparedStatement -> preparedStatement.setString(1,name)
		);

		addBalance(BalanceType.DRIVER, driverId);
		addBalance(BalanceType.DEPOSIT, driverId);

	}

	private void updateDriver(@NotNull final Long id, @NotNull final String name)	{
		getDataManager().executeUpdate(
				"update drivers set name = ? where id = ?",
				preparedStatement -> {
					preparedStatement.setString(1, name);
					preparedStatement.setLong(2, id);
				}
		);
	}

}
