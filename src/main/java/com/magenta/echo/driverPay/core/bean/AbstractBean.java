package com.magenta.echo.driverpay.core.bean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 12:20
 */
public abstract class AbstractBean {

	private final Logger log = LogManager.getLogger(getClass());

	@PersistenceContext
	private EntityManager entityManager;

	protected Logger getLogger()	{
		return log;
	}

	protected EntityManager getEntityManager()	{
		return entityManager;
	}

	protected String getQuery(@NotNull final String path)	{
		try {
			getLogger().info("Load query "+path);
			final InputStream stream = getClass().getResourceAsStream(path);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			final StringBuilder sb = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			return sb.toString();
		}catch(final Exception e) {
			getLogger().error(e);
			throw new RuntimeException(e);
		}
	}
}
