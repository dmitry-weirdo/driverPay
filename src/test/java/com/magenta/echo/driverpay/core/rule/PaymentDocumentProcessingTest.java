package com.magenta.echo.driverpay.core.rule;

import com.magenta.echo.driverpay.core.TestContextConfig;
import com.magenta.echo.driverpay.core.TestUtils;
import com.magenta.echo.driverpay.core.bean.dao.DriverDao;
import com.magenta.echo.driverpay.core.entity.Driver;
import com.magenta.echo.driverpay.core.entity.Payment;
import com.magenta.echo.driverpay.core.entity.PaymentDocument;
import com.magenta.echo.driverpay.core.enums.PaymentDocumentMethod;
import com.magenta.echo.driverpay.core.enums.PaymentType;
import com.magenta.echo.driverpay.core.validation.ValidationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.time.LocalDate;
import java.util.Collections;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 05-06-2016 18:04
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestContextConfig.class, loader = AnnotationConfigContextLoader.class)
public class PaymentDocumentProcessingTest {

	@Autowired
	private DriverDao driverDao;

	@Test
	public void testProcessPayments()	{

		final Driver driver = new Driver();
		driver.setName("Test");
		driverDao.insert(driver);
		final Payment payment = TestUtils.makePayment(driver, PaymentType.CREDIT);

		try {
			final PaymentDocument paymentDocument = PaymentDocumentProcessing.calculateSalary(
					driver,
					LocalDate.now(),
					PaymentDocumentMethod.ACCOUNT,
					Collections.singletonList(payment)
			);
		}catch(Exception e) {
			Exception cve = ValidationUtils.getConstraintViolationException(e);
		}

	}

}
