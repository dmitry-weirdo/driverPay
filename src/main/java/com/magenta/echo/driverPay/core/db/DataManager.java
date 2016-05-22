package com.magenta.echo.driverpay.core.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class contains suitable methods for executions different SQL queries on the SQLite database.
 * All methods are invoked in transaction context. For more information see {@link ConnectionExt}
 *
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

	/**
	 * @return new {@link ConnectionExt} if connection not established or old connection if it already exists
	 * @throws RuntimeException when checking connection fails
	 */
	public ConnectionExt getConnection() throws RuntimeException	{
		if(checkConnection())	{
			connection = new ConnectionExt();
		}else {
			connection.reuse();
		}
		return connection;
	}

	/**
	 * @return true if connection established, false otherwise
	 * @throws RuntimeException suppress SQLException on checking connection
	 */
	private boolean checkConnection() throws RuntimeException	{
		try {
			return connection == null || connection.get().isClosed();
		}catch(SQLException e) {
			throw new RuntimeException("Error on checking connection",e);
		}
	}

	/**
	 * Executes any SQL query which may returns list of objects
	 * @param query SQL query
	 * @param parameterProvider consumer which allows to fill up query parameters
	 * @param resultConverter function which describes how to build row item or query result
	 * @param <T> type of query result row
	 * @return result list
	 * @throws RuntimeException on any exceptions
	 */
	public <T> List<T> executeQuery(
			final String query,
			final ParameterProvider parameterProvider,
			final ResultSetReader<T> resultConverter
	) throws RuntimeException	{

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

	/**
	 * The same as the {@link this#executeQuery(String, ParameterProvider, ResultSetReader)} but returns only one row
	 * @param query SQL query
	 * @param parameterProvider consumer which allows to fill up query parameters
	 * @param resultConverter function which describes how to build row item or query result
	 * @param <T> type of query result row
	 * @return result list
	 * @throws RuntimeException if result contains zero or more than two rows or on any other exceptions
	 */
	public <T> T executeSingleQuery(
			final String query,
			final ParameterProvider parameterProvider,
			final ResultSetReader<T> resultConverter
	)	{
		final List<T> result = executeQuery(query, parameterProvider, resultConverter);
		if(result.size() != 1)	{
			throw new RuntimeException("Non single result");
		}
		return result.get(0);
	}

	/**
	 * Performs any update SQL query (update, create table, alter table, etc)
	 * @param query SQL update query
	 * @param parameterProvider consumer which allows to fill up query parameters
	 * @return count of affected records
	 * @throws RuntimeException on any exceptions
	 */
	public int executeUpdate(
			final String query,
			final ParameterProvider parameterProvider
	) throws RuntimeException	{

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

	/**
	 * The same as {@link this#executeUpdate(String, ParameterProvider)} but takes only SQL query without specifying parameters
	 * @param query SQL update query
	 * @return count of affected rows
	 * @throws RuntimeException on any exceptions
	 */
	public int executeUpdate(final String query) throws RuntimeException	{
		return executeUpdate(query, param -> {});
	}

	/**
	 * Performs insert query
	 * @param query SQL insert query
	 * @param parameterProvider consumer which allows to fill up query parameters
	 * @return id of last added record
	 * @throws RuntimeException on any exceptions
	 */
	public Long executeInsert(
			final String query,
			final ParameterProvider parameterProvider
	) throws RuntimeException	{

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

	/**
	 * The same as the {@link this#executeInsert(String, ParameterProvider)} but
	 * takes only SQL query without specifying parameters
	 * @param query SQL insert query
	 * @return id of last added record
	 * @throws RuntimeException on any exceptions
	 */
	public Long executeInsert(final String query) throws RuntimeException	{
		return executeInsert(query, preparedStatement -> {});
	}

	/**
	 * Functional interface. Allows to fill up parameters of SQL queries
	 */
	public interface ParameterProvider	{
		void fillUp(final PreparedStatement preparedStatement) throws Exception;
	}

	/**
	 * Function interface. Describes how to build row item of SQL query execution result.
	 * Accepts {@link ResultSet} which contains all query results.
	 * Function is called for every row in ResultSet
	 * @param <T> type of result row
	 */
	public interface ResultSetReader<T>	{
		T read(final ResultSet resultSet) throws Exception;
	}

}
