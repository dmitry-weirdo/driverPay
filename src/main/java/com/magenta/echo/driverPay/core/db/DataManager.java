package com.magenta.echo.driverpay.core.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 13-05-2016 02:19
 */
public class DataManager {
	private static final Logger log = LogManager.getLogger(DataManager.class);
	public static final String CONNECTION_STRING = "jdbc:sqlite:driver-pay.db";

	private ConnectionExt connection;

	public DataManager() {
		try {
			Class.forName("org.sqlite.JDBC");
		}catch(ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public ConnectionExt getConnection()	{
		if(checkConnection())	{
			connection = new ConnectionExt();
		}else {
			connection.reuse();
		}
		return connection;
	}

	private boolean checkConnection()	{
		try {
			return connection == null || connection.get().isClosed();
		}catch(SQLException e) {
			throw new RuntimeException("Unable to open connection",e);
		}
	}

	public <T> List<T> executeQuery(
			final String query,
			final ParameterProvider parameterProvider,
			final ResultSetReader<T> resultConverter
	)	{

		try(final ConnectionExt connection = getConnection())	{

			final PreparedStatement preparedStatement = connection.get().prepareStatement(query);
			parameterProvider.fillUp(preparedStatement);

			final ResultSet resultSet = preparedStatement.executeQuery();
			final List<T> result = new ArrayList<>();

			while(resultSet.next())	{
				final T t = resultConverter.read(resultSet);
				result.add(t);
			}

			connection.success();

			return result;

		}catch(Exception e)	{
			throw new RuntimeException(e);
		}

	}

	public <T> T executeSingleQuery(
			final String query,
			final ParameterProvider parameterProvider,
			final ResultSetReader<T> resultConverter
	)	{
		final List<T> result = executeQuery(query, parameterProvider, resultConverter);
		if(result.size() != 1)	{
			throw new RuntimeException("non single result");
		}
		return result.get(0);
	}

	public int executeUpdate(
			final String query,
			final ParameterProvider parameterProvider
	)	{

		try(final ConnectionExt connection = getConnection())	{

			final PreparedStatement preparedStatement = connection.get().prepareStatement(query);
			parameterProvider.fillUp(preparedStatement);

			final int result = preparedStatement.executeUpdate();
			connection.success();
			return result;

		}catch(Exception e)	{
			throw new RuntimeException(e);
		}

	}

	public int executeUpdate(final String query)	{
		return executeUpdate(query, param -> {});
	}

	public Long executeInsert(
			final String query,
			final ParameterProvider parameterProvider
	)	{

		try(final ConnectionExt connection = getConnection())	{

			final PreparedStatement preparedStatement = connection.get().prepareStatement(query);
			parameterProvider.fillUp(preparedStatement);

			preparedStatement.executeUpdate();

			try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys())	{

				if(generatedKeys.next())	{

					final Long id = generatedKeys.getLong(1);
					connection.success();
					return id;

				}else {

					throw new SQLException("ID not returned");

				}

			}

		}catch(Exception e)	{
			throw new RuntimeException(e);
		}

	}

	public Long executeInsert(final String query)	{
		return executeInsert(query, preparedStatement -> {});
	}

	public interface ParameterProvider	{
		void fillUp(final PreparedStatement preparedStatement) throws Exception;
	}

	public interface ResultSetReader<T>	{
		T read(final ResultSet resultSet) throws Exception;
	}

}
