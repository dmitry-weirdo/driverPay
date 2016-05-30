package com.magenta.echo.driverpay.core.bean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

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
			final URI uri = getClass().getResource(path).toURI();
			final byte[] content = Files.readAllBytes(Paths.get(uri));
			return new String(content);
		}catch(final Exception e) {
			getLogger().error(e);
			throw new RuntimeException(e);
		}
	}
}
