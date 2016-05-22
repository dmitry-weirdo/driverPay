package com.magenta.echo.driverpay.core.entity;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 13-05-2016 17:43
 */
public class DriverDto {
    private Long id;
    private String name;

	public DriverDto() {

	}

	public DriverDto(String name) {
		this.name = name;
	}

	public DriverDto(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
