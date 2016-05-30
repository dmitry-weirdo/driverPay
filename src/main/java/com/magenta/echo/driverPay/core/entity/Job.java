package com.magenta.echo.driverpay.core.entity;


import com.magenta.echo.driverpay.core.enums.JobType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Project: Driver Pay Enhanced
 * Author:  Lebedev
 * Created: 27-05-2016 12:53
 */
@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
	@NotNull(message = "Should not be null")
    private Driver driver;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name = "job_date")
	@NotNull(message = "Should not be null")
    private LocalDate jobDate;

    @Enumerated(EnumType.STRING)
	@NotNull(message = "Should not be null")
    private JobType type;

	@NotNull(message = "Should not be null")
	private Double pricing;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "job")
    private Set<JobRate> jobRates = new HashSet<>();

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

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(final Payment payment) {
        this.payment = payment;
    }

    public LocalDate getJobDate() {
        return jobDate;
    }

    public void setJobDate(final LocalDate jobDate) {
        this.jobDate = jobDate;
    }

    public JobType getType() {
        return type;
    }

    public void setType(final JobType type) {
        this.type = type;
    }

    public Set<JobRate> getJobRates() {
        return jobRates;
    }

    public void setJobRates(final Set<JobRate> jobRates) {
        this.jobRates = jobRates;
    }

	public Double getPricing() {
		return pricing;
	}

	public void setPricing(Double pricing) {
		this.pricing = pricing;
	}
}
