package com.magenta.echo.driverpay.core.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 05-06-2016 20:36
 */
@Entity
@Table(name = "files")
public class FileContent {

	@Id
	@GeneratedValue
	private Long id;

	private byte[] content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent()	{
		return new String(content);
	}

	public void setContent(String content)	{
		this.content = content.getBytes();
	}
}
