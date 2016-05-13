/**
 * Copyright 2016 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package com.magenta.echo.driverPay.common;

import java.util.Date;

public interface Dated
{
	// todo: use org.joda.DateTime for Echo
	Date getDate();
	void setDate(Date date);
}