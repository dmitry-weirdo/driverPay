package com.magenta.echo.driverpay.core.exception;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 14:20
 */
public class EntryNotExist extends RuntimeException {

	public EntryNotExist(Object entry, Throwable cause) {
		super(String.format("Entry [%s] does not exist", entry), cause);
	}

	public EntryNotExist(Object entry) {
		super(String.format("Entry [%s] does not exist", entry));
	}

}
