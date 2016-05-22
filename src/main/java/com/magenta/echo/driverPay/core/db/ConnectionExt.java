package com.magenta.echo.driverpay.core.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Implementation of transactional context.
 * Designed for execution many SQL queries in one transaction.
 * Using:
 * <code>
 *     try(final ConnectionExt connection = dataManager.getConnection())	{
 *         // your code should be executed in transaction
 *         connection.success();
 *     }catch(Exception e)	{
 *         throw new RuntimeException(e);
 *     }
 * </code>
 *	</br>
 * dataManager.getConnection() establishes connection to the database.</br>
 * connection.success() performs commit of all changes which occurred
 * in the transaction body (try-catch block)</br>
 * try-with-resource guarantees what if transaction fails the connection will be closed properly</br>
 * </br>
 * In the transaction body you can start another transaction
 * or you can execute methods which may start transactions itself.
 * In this case all nested transactions will be stacked in one.
 * If any transaction fails than all linked transactions will be rolled back.
 *
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 11:35
 */
public class ConnectionExt implements AutoCloseable	{
	private static final Logger log = LogManager.getLogger(ConnectionExt.class);
	private Connection connection;
	private long reuseCount;
	private boolean success;

	/**
	 * Opens new connection to the database and starts transaction at the same time
	 */
	ConnectionExt() {
		try {
			connection = DriverManager.getConnection(DataManager.CONNECTION_STRING);
			connection.setAutoCommit(false);
			reuseCount = 1;
			success = false;
		}catch(SQLException e) {
			throw new RuntimeException("Unable to open connection", e);
		}
	}

	/**
	 * @return currently opened connection
	 */
	Connection get() {
		return connection;
	}

	/**
	 * Increase count of reusing. Calls when there is already opened connections.
	 */
	void reuse()	{
		reuseCount++;
	}

	/**
	 * Mark transaction as completed successful
	 */
	public void success()	{
		success = true;
	}

	/**
	 * Performs closing of opened connection.
	 * If transaction is not marked as successful then starts rollback</br>
	 * If was called in nested transaction than just decrease reuse counter</br>
	 * If it is an upper level of opened connections then starts commit process.
	 * If commit fails then starts rollback
	 *
	 * @throws SQLException on any SQL exceptions
	 */
	@Override
	public void close() throws SQLException {

		if(success) {

			if(reuseCount > 1)	{
				reuseCount--;
				success = false;
				return;
			}

			try {
				connection.commit();
			}catch(SQLException e) {
				log.error("Commit transaction failed", e);
				connection.rollback();
			}

		}else {

			if(!connection.isClosed()) {
				connection.rollback();
			}

		}

		connection.close();
	}
}
