package com.magenta.echo.driverpay.core;

import com.magenta.echo.driverpay.core.bean.*;
import com.magenta.echo.driverpay.core.db.DataManager;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 12-05-2016 20:34
 */
public class Context {
    private static final Context context = new Context();

    private final DataManager dataManager = new DataManager();
	private final DriverBean driverBean = new DriverBean();
	private final JobBean jobBean = new JobBean();
	private final PaymentBean paymentBean = new PaymentBean();
	private final SalaryCalculationBean salaryCalculationBean = new SalaryCalculationBean();
	private final PaymentProcessBean paymentProcessBean = new PaymentProcessBean();
	private final PaymentGenerationBean paymentGenerationBean = new PaymentGenerationBean();

    protected Context()    {}

    public static Context get()    {
        return context;
    }

	public String getDatabaseName()	{
		return "driver-pay.db";
	}

    //

	public DataManager getDataManager() {
		return dataManager;
	}

	public DriverBean getDriverBean() {
		return driverBean;
	}

	public JobBean getJobBean() {
		return jobBean;
	}

	public PaymentBean getPaymentBean() {
		return paymentBean;
	}

	public SalaryCalculationBean getSalaryCalculationBean() {
		return salaryCalculationBean;
	}

	public PaymentProcessBean getPaymentProcessBean() {
		return paymentProcessBean;
	}

	public PaymentGenerationBean getPaymentGenerationBean() {
		return paymentGenerationBean;
	}
}
