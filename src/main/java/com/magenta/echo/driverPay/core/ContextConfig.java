package com.magenta.echo.driverpay.core;

import com.magenta.echo.driverpay.core.bean.InitDatabaseBean;
import com.magenta.echo.driverpay.core.bean.PersistenceInterceptor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: driverPay-prototype
 * Author:  Lebedev
 * Created: 27-05-2016 12:47
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.magenta.echo.driverpay.core.bean")
public class ContextConfig {

    public static final String DATABASE_NAME = "driver-pay.db";

    protected String connectionString()	{
        return "jdbc:sqlite:"+DATABASE_NAME;
    }

	@Bean
	public ApplicationListener<ContextRefreshedEvent> applicationStartedListener(final InitDatabaseBean initDatabaseBean)	{
		return contextStartedEvent -> {
			Context
					.get()
					.setSpringContext(contextStartedEvent.getApplicationContext());
			initDatabaseBean.init();
		};

	}

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory()	{

        final Map<String,Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto","none");
        properties.put("hibernate.dialect","org.hibernate.dialect.SQLiteDialect");
        properties.put("hibernate.show_sql","true");
        properties.put("hibernate.connection.foreign_keys","true");
		properties.put("hibernate.ejb.interceptor",new PersistenceInterceptor());

        final JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

        final LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPackagesToScan("com.magenta.echo.driverpay.core.entity");
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setJpaPropertyMap(properties);

        return entityManagerFactory;
    }

    @Bean
    public SingleConnectionDataSource dataSource()	{
        try {
			// ugly hack for pragma foreign_keys = on
			// which should be specified on first connection
            Class.forName("org.sqlite.JDBC");
            final Connection connection = DriverManager.getConnection(connectionString());
            connection.setAutoCommit(true);
            final Statement statement = connection.createStatement();
            statement.executeUpdate("pragma foreign_keys = on");
            statement.close();
            return new SingleConnectionDataSource(connection, true);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory)	{
        final JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }

	@Bean
	public LocalValidatorFactoryBean localValidatorFactoryBean()	{
		return new LocalValidatorFactoryBean();
	}
}
