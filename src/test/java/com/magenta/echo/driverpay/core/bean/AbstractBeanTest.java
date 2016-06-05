package com.magenta.echo.driverpay.core.bean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Project: driverPay-prototype
 * Author:  Lebedev
 * Created: 31-05-2016 18:32
 */
public class AbstractBeanTest {

    private Logger log;

    @PersistenceContext
    private EntityManager entityManager;
	@Autowired
	private TransactionRunner transactionRunner;

    protected Logger getLogger()    {
        if(log == null) {
            log = LogManager.getLogger(getClass());
        }
        return log;
    }

    protected EntityManager getEntityManager()  {
        return entityManager;
    }

	protected void runInTransaction(@NotNull final Runnable runnable)	{
		transactionRunner.runInTransaction(runnable);
	}

	@Before
	public void before()	{}

	@After
	public void after()	{
		runInTransaction(() -> {
			entityManager.createNativeQuery("delete from transactions").executeUpdate();
			entityManager.createNativeQuery("delete from payments").executeUpdate();
			entityManager.createNativeQuery("delete from payment_documents").executeUpdate();
			entityManager.createNativeQuery("delete from payment_reasons").executeUpdate();
			entityManager.createNativeQuery("delete from jobs").executeUpdate();
			entityManager.createNativeQuery("delete from job_rates").executeUpdate();
			entityManager.createNativeQuery("delete from balances where driver_id is not null").executeUpdate();
			entityManager.createNativeQuery("delete from drivers").executeUpdate();
		});
	}

}
