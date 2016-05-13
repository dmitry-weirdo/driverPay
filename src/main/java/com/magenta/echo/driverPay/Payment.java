/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package com.magenta.echo.driverPay;

import com.magenta.echo.driverPay.common.Dated;
import com.magenta.echo.driverPay.common.Identified;

/**
 * An atomic payment which is not split to other payments.
 *
 * Payment can be linked to any date. Any non-processed payments can be selected to form a {@link com.magenta.echo.driverPay.PaymentDocument}
 *
 * // todo: как быть с работой, бьём ли мы каждый рэйт или NominalCode на отдельный Payment?
 */
public interface Payment extends Identified, Dated
{
	PaymentStatus getStatus();
	void setStatus(PaymentStatus status);

	PaymentDocument getPaymentDocument();
	void setPaymentDocument(PaymentDocument paymentDocument);

	Transaction getTransaction();
	void setTransaction(Transaction transaction); // set after payment transaction is processed

	Double getNet();
	void setNet(Double net);

	Double getVat();
	void setVat(Double vat);

	Double getTotal();
	void setTotal(Double total);

	// todo: think whether TaxCode is required here
	// todo: think whether NominalCode is required here
}