package com.magenta.echo.driverpay.core.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 11:35
 */
public class ConnectionExt implements AutoCloseable{
	private static final Logger log = LogManager.getLogger(ConnectionExt.class);
	private Connection connection;
	private long reuseCount;
	private boolean success;

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

	Connection get() {
		return connection;
	}

	void reuse()	{
		reuseCount++;
	}

	public void success()	{
		success = true;
	}

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
