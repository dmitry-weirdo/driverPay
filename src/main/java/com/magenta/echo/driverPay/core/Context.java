package com.magenta.echo.driverpay.core;

import com.magenta.echo.driverpay.core.bean.*;
import com.magenta.echo.driverpay.core.bean.dao.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 12-05-2016 20:34
 */
public class Context {
    private static final Context context = new Context();

	private AnnotationConfigApplicationContext springContext;

	private BalanceDao balanceDao;
	private DriverDao driverDao;
	private PaymentReasonDao paymentReasonDao;
	private PaymentDao paymentDao;
	private JobDao jobDao;
	private JobRateDao jobRateDao;
	private PaymentDocumentDao paymentDocumentDao;
	private TransactionDao transactionDao;

	private DriverBean driverBean;
	private PaymentLoader paymentLoader;
	private BalanceBean balanceBean;
	private SalaryCalculationBean salaryCalculationBean;
	private JobBean jobBean;

    protected Context()    {}

    public static Context get()    {
        return context;
    }

	public void setSpringContext(AnnotationConfigApplicationContext springContext) {
		this.springContext = springContext;
	}

	//

	public BalanceDao getBalanceDao() {
		if(balanceDao == null)	{
			balanceDao = springContext.getBean(BalanceDao.class);
		}
		return balanceDao;
	}

	public DriverDao getDriverDao() {
		if(driverDao == null)	{
			driverDao = springContext.getBean(DriverDao.class);
		}
		return driverDao;
	}

	public PaymentReasonDao getPaymentReasonDao() {
		if(paymentReasonDao == null)	{
			paymentReasonDao = springContext.getBean(PaymentReasonDao.class);
		}
		return paymentReasonDao;
	}

	public PaymentDao getPaymentDao() {
		if(paymentDao == null)	{
			paymentDao = springContext.getBean(PaymentDao.class);
		}
		return paymentDao;
	}

	public JobDao getJobDao() {
		if(jobDao == null)	{
			jobDao = springContext.getBean(JobDao.class);
		}
		return jobDao;
	}

	public JobRateDao getJobRateDao() {
		if(jobRateDao == null)	{
			jobRateDao = springContext.getBean(JobRateDao.class);
		}
		return jobRateDao;
	}

	public PaymentDocumentDao getPaymentDocumentDao() {
		if(paymentDocumentDao == null)	{
			paymentDocumentDao = springContext.getBean(PaymentDocumentDao.class);
		}
		return paymentDocumentDao;
	}

	public TransactionDao getTransactionDao() {
		if(transactionDao == null)	{
			transactionDao = springContext.getBean(TransactionDao.class);
		}
		return transactionDao;
	}

	public DriverBean getDriverBean() {
		if(driverBean == null)	{
			driverBean = springContext.getBean(DriverBean.class);
		}
		return driverBean;
	}

	public PaymentLoader getPaymentLoader()	{
		if(paymentLoader == null)	{
			paymentLoader = springContext.getBean(PaymentLoader.class);
		}
		return paymentLoader;
	}

	public BalanceBean getBalanceBean() {
		if(balanceBean == null)	{
			balanceBean = springContext.getBean(BalanceBean.class);
		}
		return balanceBean;
	}

	public SalaryCalculationBean getSalaryCalculationBean() {
		if(salaryCalculationBean == null)	{
			salaryCalculationBean = springContext.getBean(SalaryCalculationBean.class);
		}
		return salaryCalculationBean;
	}

	public JobBean getJobBean() {
		if(jobBean == null)	{
			jobBean = springContext.getBean(JobBean.class);
		}
		return jobBean;
	}
}
