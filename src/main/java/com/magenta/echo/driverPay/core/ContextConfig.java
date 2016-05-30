package com.magenta.echo.driverpay.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.persistence.EntityManagerFactory;
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

    protected String connectionString()	{
        return "jdbc:sqlite:driver-pay.db";
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory()	{

        final Map<String,String> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto","none");
        properties.put("hibernate.dialect","org.hibernate.dialect.SQLiteDialect");
        properties.put("hibernate.show_sql","true");

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
        final SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl(connectionString());
		dataSource.setSuppressClose(true);
        return dataSource;
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
