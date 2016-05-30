package com.magenta.echo.driverpay.core.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Project: Driver Pay Enhanced
 * Author:  Lebedev
 * Created: 27-05-2016 12:59
 */
@Entity
@Table(name = "payment_documents")
public class PaymentDocument {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
	@NotNull(message = "Should not be null")
    private Driver driver;

    @Column(name = "payment_date")
	@NotNull(message = "Should not be null")
    private LocalDate paymentDate;

	@NotNull(message = "Should not be null")
    private Boolean processed;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(final Driver driver) {
        this.driver = driver;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(final LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

	public Boolean getProcessed() {
		return processed;
	}

	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}
}
