package com.magenta.echo.driverpay.core;

import com.magenta.echo.driverpay.core.bean.*;
import com.magenta.echo.driverpay.core.bean.dao.CommonDao;
import com.magenta.echo.driverpay.core.bean.dao.DriverDao;
import com.magenta.echo.driverpay.core.bean.dao.JobDao;
import org.springframework.context.ApplicationContext;

import javax.validation.Validator;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 12-05-2016 20:34
 */
public class Context {
    private static final Context context = new Context();

	private ApplicationContext springContext;

	private Validator validator;

	private CommonDao commonDao;
	private DriverDao driverDao;
	private JobDao jobDao;

	private BalanceBean balanceBean;
	private DriverBean driverBean;
	private JobBean jobBean;
	private PaymentLoader paymentLoader;
	private PaymentDocumentProcessingBean paymentDocumentProcessingBean;
	private ReportBean reportBean;

    protected Context()    {}

    public static Context get()    {
        return context;
    }

	public void setSpringContext(ApplicationContext springContext) {
		this.springContext = springContext;
	}

	//

	public Validator getValidator() {
		if(validator == null)	{
			validator = springContext.getBean(Validator.class);
		}
		return validator;
	}

	public CommonDao getCommonDao()	{
		if(commonDao == null)	{
			commonDao = springContext.getBean(CommonDao.class);
		}
		return commonDao;
	}

	public DriverDao getDriverDao() {
		if(driverDao == null)	{
			driverDao = springContext.getBean(DriverDao.class);
		}
		return driverDao;
	}

	public JobDao getJobDao() {
		if(jobDao == null)	{
			jobDao = springContext.getBean(JobDao.class);
		}
		return jobDao;
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

	public JobBean getJobBean() {
		if(jobBean == null)	{
			jobBean = springContext.getBean(JobBean.class);
		}
		return jobBean;
	}

	public PaymentDocumentProcessingBean getPaymentDocumentProcessingBean() {
		if(paymentDocumentProcessingBean == null)	{
			paymentDocumentProcessingBean = springContext.getBean(PaymentDocumentProcessingBean.class);
		}
		return paymentDocumentProcessingBean;
	}

	public ReportBean getReportBean() {
		if(reportBean == null)	{
			reportBean = springContext.getBean(ReportBean.class);
		}
		return reportBean;
	}
}
