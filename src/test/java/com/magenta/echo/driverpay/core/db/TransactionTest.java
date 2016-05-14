package com.magenta.echo.driverpay.core.db;

import com.magenta.echo.driverpay.core.Scheme;
import com.magenta.echo.driverpay.core.TestContext;
import com.magenta.echo.driverpay.core.TestUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 11:48
 */
public class TransactionTest {

	private DataManager dataManager = TestContext.get().getDataManager();
	{
		Scheme.initDatabase();
		TestUtils.cleanup(dataManager);
	}

	@After
	public void afterMethod()	{
		TestUtils.cleanup(dataManager);
	}

	@Test
	public void testSingleTransaction()	{
		try(final ConnectionExt connection = dataManager.getConnection()) {

			dataManager.executeInsert(
					"insert into balances (id,type) values (null,'TEST')",
					preparedStatement -> {}
			);

			dataManager.executeInsert(
					"insert into balances (id,type) values (null,'TEST2')",
					preparedStatement -> {}
			);

			connection.success();

		}catch(Exception e)	{
			throw new RuntimeException(e);
		}

		final List<String> result = dataManager.executeQuery(
				"select id,type from balances",
				preparedStatement -> {},
				resultSet -> resultSet.getString(2)
		);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals("TEST", result.get(0));
		Assert.assertEquals("TEST2", result.get(1));

	}

	@Test
	public void testTransactionFail()	{
		try(final ConnectionExt connection = dataManager.getConnection())	{

			dataManager.executeInsert(
					"insert into balances (id,type) values (null,'TEST')",
					preparedStatement -> {}
			);

			dataManager.executeInsert(
					"insert into balances (id,type) values (null,'TEST2')",
					preparedStatement -> {}
			);

			throw new Exception("Managed exception");

			// connection.success();

		}catch(Exception e)	{
			// catch managed exception
		}

		final List<String> result = dataManager.executeQuery(
				"select id,type from balances",
				preparedStatement -> {},
				resultSet -> resultSet.getString(2)
		);

		Assert.assertEquals(0, result.size());
	}
}
