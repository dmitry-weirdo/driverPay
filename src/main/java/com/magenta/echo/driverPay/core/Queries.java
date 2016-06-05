package com.magenta.echo.driverpay.core;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 28-05-2016 19:50
 */
public class Queries {

	public static final String LOAD_DRIVER_LIST_BY_PATTERN = "/sql/queries/loadDriverListByPattern.hql";
	public static final String LOAD_DRIVER_LIST_WITH_BALANCES = "/sql/queries/loadDriverListWithBalances.sql";
	public static final String LOAD_DRIVER_WITH_ADDITIONAL_INFO = "/sql/queries/loadDriverWithAdditionalInfo.sql";

	public static final String LOAD_PAYMENT_DOCUMENT_LIST_BY_DRIVER = "/sql/queries/loadPaymentDocumentListByDriver.hql";
	public static final String LOAD_PAYMENT_LIST_BY_DRIVER_AND_DATE = "/sql/queries/loadPaymentListByDriverAndDate.hql";
	public static final String LOAD_PAYMENT_LIST_BY_DRIVER_AND_PAYMENT_DOCUMENT = "/sql/queries/loadPaymentListByDriverAndPaymentDocument.hql";
	public static final String LOAD_PAYMENT_LIST_BY_PAYMENT_REASON = "/sql/queries/loadPaymentListByPaymentReason.hql";
	public static final String LOAD_PAYMENT_REASON_LIST_BY_DRIVER = "/sql/queries/loadPaymentReasonListByDriver.sql";

	public static final String LOAD_JOB_LIST = "/sql/queries/loadJobList.sql";
	public static final String LOAD_JOB_BY_ID = "/sql/queries/loadJobById.hql";

	public static final String LOAD_EXPORT_HISTORY_LIST = "/sql/queries/loadExportHistoryList.hql";
	public static final String LOAD_SAGE = "/sql/queries/loadSage.sql";
	public static final String LOAD_BARCLAYS = "/sql/queries/loadBarclays.sql";
}
