package com.magenta.echo.driverpay.core;

import com.magenta.echo.driverpay.core.bean.*;
import com.magenta.echo.driverpay.core.db.DataManager;
import com.magenta.echo.driverpay.ui.dialog.DialogExt;
import com.magenta.echo.driverpay.ui.screen.Screen;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 12-05-2016 20:34
 */
public class Context {
    private static final Context context = new Context();

    private Stage stage;
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

    public Stage getStage() {
        return stage;
    }

    public void setStage(final Stage stage) {
        this.stage = stage;
    }

    //

    public void openScreen(final Screen screen)    {
        stage.getScene().setRoot(screen.getRoot());
    }

	public <T> Optional<T> openDialogAndWait(final DialogExt<T> dialogExt)	{
		return dialogExt.showAndWait();
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
