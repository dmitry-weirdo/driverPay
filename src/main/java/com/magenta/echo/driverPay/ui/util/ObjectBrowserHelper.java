package com.magenta.echo.driverpay.ui.util;

import com.evgenltd.kwick.controls.objectbrowser.ObjectBrowser;
import com.magenta.echo.driverpay.core.entity.JobRate;
import com.magenta.echo.driverpay.core.entity.Payment;
import com.magenta.echo.driverpay.core.entity.dto.JobDto;
import com.magenta.echo.driverpay.core.enums.PaymentType;
import com.magenta.echo.driverpay.ui.screen.driverEdit.PaymentTypeTableCell;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 29-05-2016 19:46
 */
public class ObjectBrowserHelper {

	public static final String PAYMENT_ID = "id";
	public static final String PAYMENT_NAME = "name";
	public static final String PAYMENT_PLANNED_DATE = "plannedDate";
	public static final String PAYMENT_TYPE = "type";
	public static final String PAYMENT_STATUS = "status";
	public static final String PAYMENT_NET = "net";
	public static final String PAYMENT_VAT = "vat";
	public static final String PAYMENT_TOTAL = "total";
	public static final String PAYMENT_NOMINAL_CODE = "nominalCode";
	public static final String PAYMENT_TAX_CODE = "taxCode";

	public static void setupPaymentTable(final @NotNull ObjectBrowser<Payment> paymentTable)	{

		paymentTable.addColumn(PAYMENT_TYPE, "Type");
		paymentTable.addColumn(PAYMENT_NAME, "Name");
		paymentTable.addColumn(PAYMENT_PLANNED_DATE, "Date");
		paymentTable.addColumn(PAYMENT_NET, "Net");
		paymentTable.addColumn(PAYMENT_VAT, "Vat");
		paymentTable.addColumn(PAYMENT_TOTAL, "Total");
		paymentTable.addColumn(PAYMENT_NOMINAL_CODE, "Nominal Code");
		paymentTable.addColumn(PAYMENT_TAX_CODE, "Tax Code");

		final TableColumn<Payment,PaymentType> paymentTypeColumn = paymentTable.getColumn(PAYMENT_TYPE);
		paymentTypeColumn.setCellFactory(param -> new PaymentTypeTableCell());

		paymentTable.setupTotalSupport(Arrays.asList(
				PAYMENT_NET,
				PAYMENT_VAT,
				PAYMENT_TOTAL
		));
	}

	public static final String JOB_DTO_DRIVER = "driverValue";
	public static final String JOB_DTO_TYPE = "jobType";
	public static final String JOB_DTO_DATE = "jobDate";
	public static final String JOB_DTO_PRICING = "pricing";
	public static final String JOB_DTO_CHARGING = "charging";

	public static void setupJobDtoTable(@NotNull final ObjectBrowser<JobDto> jobTable)	{

		jobTable.addColumn(JOB_DTO_DRIVER, "Driver");
		jobTable.addColumn(JOB_DTO_TYPE, "Type");
		jobTable.addColumn(JOB_DTO_DATE, "Date");
		jobTable.addColumn(JOB_DTO_PRICING, "Pricing");
		jobTable.addColumn(JOB_DTO_CHARGING, "Charging");

		final TableColumn<JobDto,String> jobTypeColumn = jobTable.getColumn(JOB_DTO_TYPE);
		jobTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getLabel()));

		jobTable.setSelectionMode(SelectionMode.MULTIPLE);

	}

	public static final String JOB_RATE_NET = "net";
	public static final String JOB_RATE_NOMINAL_CODE = "nominalCode";
	public static final String JOB_RATE_TAX_CODE = "taxCode";

	public static void setupJobRateTable(@NotNull final ObjectBrowser<JobRate> jobRateTable)	{

		jobRateTable.addColumn(JOB_RATE_NET, "Net");
		jobRateTable.addColumn(JOB_RATE_NOMINAL_CODE, "Nominal Code");
		jobRateTable.addColumn(JOB_RATE_TAX_CODE, "Tax Code");

		jobRateTable.setupTotalSupport(Collections.singletonList(JOB_RATE_NET));
		jobRateTable.setSelectionMode(SelectionMode.MULTIPLE);

	}
}
