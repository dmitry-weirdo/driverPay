package com.magenta.echo.driverpay.core;

import com.magenta.echo.driverpay.core.db.DataManager;
import com.magenta.echo.driverpay.core.enums.BalanceType;

import java.util.List;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 13-05-2016 02:34
 */
public class Scheme {
	public static void initDatabase()	{
		final DataManager dataManager = Context.get().getDataManager();
		final Integer version = getVersion(dataManager);

		switch(version)	{
			case 0:
				initVersion1(dataManager);
				break;
			case 1:
				initVersion2(dataManager);
				break;
		}
	}

	private static Integer getVersion(final DataManager dataManager)	{
		List<Integer> result = dataManager.executeQuery("pragma user_version", param -> {}, resultSet -> resultSet.getInt(1));

		if(result.isEmpty())	{
			return 0;
		}else {
			return result.get(0);
		}
	}

	private static void initVersion1(final DataManager dataManager)	{
		dataManager.executeUpdate(
				"CREATE TABLE balances (\n" +
				"\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"\ttype TEXT\n" +
				");"
		);

		dataManager.executeInsert(
				"insert into balances (id,type) values (null,?)",
				preparedStatement -> preparedStatement.setString(1, BalanceType.COMPANY.name())
		);

		dataManager.executeUpdate(
				"CREATE TABLE drivers (\n" +
				"\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"\tname TEXT,\n" +
				"\tsalary_balance_id INTEGER,\n" +
				"\tdeposit_balance_id INTEGER,\n" +
				"\tFOREIGN KEY (salary_balance_id) REFERENCES balances (id)\n" +
				"\tFOREIGN KEY (deposit_balance_id) REFERENCES balances (id)\n" +
				");"
		);

		dataManager.executeUpdate(
				"CREATE TABLE payment_reasons (\n" +
				"\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"\tname TEXT,\n" +
				"\tdriver_id INTEGER,\n" +
				"\ttype TEXT,\n" +
				"\tFOREIGN KEY (driver_id) REFERENCES drivers (id)\n" +
				");"
		);

		dataManager.executeUpdate(
				"CREATE TABLE payments (\n" +
				"\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"\tpayment_reason_id INTEGER,\n" +
				"\tjob_id INTEGER,\n" +
				"\tdriver_id INTEGER,\n" +
				"\ttype TEXT,\n" +
				"\tstatus TEXT,\n" +
				"\tpayment_document_id INTEGER,\n" +
				"\tplanned_date INTEGER,\n" +
				"\tnet REAL,\n" +
				"\tvat REAL,\n" +
				"\ttotal REAL,\n" +
				"\tnominal_code TEXT,\n" +
				"\ttax_code TEXT,\n" +
				"\tFOREIGN KEY (payment_reason_id) REFERENCES payment_reasons (id)\n" +
				"\tFOREIGN KEY (job_id) REFERENCES jobs (id)\n" +
				"\tFOREIGN KEY (driver_id) REFERENCES drivers (id)\n" +
				"\tFOREIGN KEY (payment_document_id) REFERENCES payment_documents (id)\n" +
				");"
		);

		dataManager.executeUpdate(
				"CREATE TABLE jobs (\n" +
				"\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"\tjob_date INTEGER,\n" +
				"\tdriver_id INTEGER,\n" +
				"\tFOREIGN KEY (driver_id) REFERENCES drivers (id)\n" +
				");"
		);
		dataManager.executeUpdate(
				"CREATE TABLE job_rates (\n" +
				"\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"\tjob_id INTEGER,\n" +
				"\tnet REAL,\n" +
				"\tvat REAL,\n" +
				"\ttotal REAL,\n" +
				"\tnominal_code TEXT,\n" +
				"\ttax_code TEXT,\n" +
				"\tFOREIGN KEY (job_id) REFERENCES jobs (id)\n" +
				");"

		);

		dataManager.executeUpdate(
				"CREATE TABLE payment_documents (\n" +
				"\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"\tpayment_date INTEGER,\n" +
				"\tdriver_id INTEGER,\n" +
				"\tstatus INTEGER,\n" +
				"\tFOREIGN KEY (driver_id) REFERENCES drivers (id)\n" +
				");"
		);

		dataManager.executeUpdate(
				"CREATE TABLE transactions (\n" +
				"\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"\tpayment_date INTEGER,\n" +
				"\tpayment_id INTEGER,\n" +
				"\tfrom_id INTEGER,\n" +
				"\tto_id INTEGER,\n" +
				"\tnet REAL,\n" +
				"\tvat REAL,\n" +
				"\ttotal REAL,\n" +
				"\tFOREIGN KEY (payment_id) REFERENCES payments (id),\n" +
				"\tFOREIGN KEY (from_id) REFERENCES balances (id),\n" +
				"\tFOREIGN KEY (to_id) REFERENCES balances (id)\n" +
				");"
		);

		dataManager.executeUpdate("pragma user_version = 1");
	}

	private static void initVersion2(final DataManager dataManager)	{

//		dataManager.executeUpdate("pragma user_version = 2");
	}
}
