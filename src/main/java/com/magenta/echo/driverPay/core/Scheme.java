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
			case 1:
				initVersion2(dataManager);
			case 2:
				initVersion3(dataManager);
			case 3:
				initVersion4(dataManager);
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
				"\tvat REAL,\n" +	// deprecated
				"\ttotal REAL,\n" +	// deorecated
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

		dataManager.executeUpdate("ALTER TABLE jobs ADD COLUMN payment_reason_id INTEGER");

		dataManager.executeUpdate(
				"insert into payment_reasons (name,driver_id,type) \n" +
				"select id,driver_id,'REGULAR_JOB' \n" +
				"from payments \n" +
				"where job_id is not null"
		);

		dataManager.executeUpdate(
				"replace into payments\n" +
				"(id,payment_reason_id,job_id,driver_id,type,status,payment_document_id,planned_date,net,vat,total,nominal_code,tax_code)\n" +
				"select\n" +
					"p.id,\n" +
					"pr.id,\n" +
					"p.job_id,p.driver_id,p.type,p.status,p.payment_document_id,p.planned_date,p.net,p.vat,p.total,p.nominal_code,p.tax_code\n" +
				"from payment_reasons pr\n" +
				"join payments p on pr.name = p.id\n" +
				"where\n" +
					"p.payment_reason_id is null"
		);

		dataManager.executeUpdate(
				"replace into jobs (id,job_date,driver_id,payment_reason_id)\n" +
				"select\n" +
					"j.id, j.job_date, j.driver_id, p.payment_reason_id\n" +
				"from payments p\n" +
				"join jobs j on p.job_id = j.id\n" +
				"where\n" +
					"p.job_id is not null"
		);

		dataManager.executeUpdate("update payment_reasons set name = 'Regular Job' where type = 'REGULAR_JOB'");

		dataManager.executeUpdate("update payments set job_id = null where job_id is not null");

		dataManager.executeUpdate("pragma user_version = 2");
	}

	private static void initVersion3(final DataManager dataManager)	{

		dataManager.executeUpdate("alter table jobs add column type text");

		dataManager.executeUpdate("update jobs set type = 'REGULAR_JOB'");

		dataManager.executeUpdate("pragma user_version = 3");
	}

	private static void initVersion4(final DataManager dataManager)	{

		dataManager.executeUpdate("alter table balances add column driver_id integer");

		dataManager.executeUpdate(
				"replace into balances\n" +
				"select \n" +
					"b.id,\n" +
					"b.type,\n" +
					"ifnull(sb.id,db.id) driver_id\n" +
				"from balances b\n" +
				"left join drivers sb on b.id = sb.salary_balance_id\n" +
				"left join drivers db on b.id = db.deposit_balance_id"
		);

		dataManager.executeUpdate("update drivers set salary_balance_id = null, deposit_balance_id = null");

		dataManager.executeUpdate("pragma user_version = 4");
	}
}
