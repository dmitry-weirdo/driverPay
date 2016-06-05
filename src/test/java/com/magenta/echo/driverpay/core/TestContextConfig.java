package com.magenta.echo.driverpay.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;

/**
 * Project: driverPay-prototype
 * Author:  Lebedev
 * Created: 31-05-2016 18:17
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.magenta.echo.driverpay.core.bean")
public class TestContextConfig extends ContextConfig {

    private static final String DATABASE_NAME = "test-driver-pay.db";

    public TestContextConfig() {
        final File db = new File(DATABASE_NAME);
        if(db.exists()) {
			System.err.println("delete "+DATABASE_NAME);
			db.delete();
        }
    }

    @Override
    protected String connectionString() {
        return "jdbc:sqlite:"+DATABASE_NAME;
    }

}
