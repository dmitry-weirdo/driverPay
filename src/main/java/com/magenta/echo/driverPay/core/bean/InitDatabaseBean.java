package com.magenta.echo.driverpay.core.bean;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 28-05-2016 13:31
 */
@Component
@Transactional
public class InitDatabaseBean extends AbstractBean {

	public void init()	{
		final Long version = getVersion();
		getLogger().info(String.format("Current database scheme version is [%s]",version));
		if(version < 10L)	{
			executeUpdate("/sql/scheme/init.sql",11L);
		}else {
			if(version == 10L)	{
				executeUpdate("/sql/scheme/version11.sql",11L);
			}
		}
	}

	private Long getVersion()	{
		return (Long)getEntityManager()
				.createNativeQuery("pragma user_version")
				.getSingleResult();
	}

	private void updateVersion(@NotNull final Long version)	{
		getEntityManager()
				.createNativeQuery(String.format("pragma user_version = %s",version))
				.executeUpdate();
	}

	private void executeUpdate(@NotNull final String path, @NotNull final Long newVersion)	{

		getLogger().info(String.format("Update database to version %s", newVersion));

		final String initQuery = getQuery(path);
		final String[] queries = initQuery.split(";");

		for(final String query : queries) {
			final String trimmedQuery = query.trim();
			if(trimmedQuery.length() == 0)	{
				continue;
			}
			getEntityManager().createNativeQuery(trimmedQuery).executeUpdate();
		}

		updateVersion(newVersion);
	}
}
