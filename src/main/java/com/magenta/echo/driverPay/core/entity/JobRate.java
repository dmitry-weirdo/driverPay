package com.magenta.echo.driverpay.core.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Project: Driver Pay Enhanced
 * Author:  Lebedev
 * Created: 27-05-2016 12:56
 */
@Entity
@Table(name = "job_rates")
public class JobRate {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
	@NotNull(message = "Should not be null")
    private Job job;

	@NotNull(message = "Should not be null")
    private Double net;

    @Column(name = "nominal_code")
	@NotEmpty(message = "Should not be empty")
	@Length(message = "Should be less than 255")
    private String nominalCode;

    @Column(name = "tax_code")
	@NotEmpty(message = "Should not be empty")
	@Length(message = "Should be less than 255")
    private String taxCode;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(final Job job) {
        this.job = job;
    }

    public Double getNet() {
        return net;
    }

    public void setNet(final Double net) {
        this.net = net;
    }

    public String getNominalCode() {
        return nominalCode;
    }

    public void setNominalCode(final String nominalCode) {
        this.nominalCode = nominalCode;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(final String taxCode) {
        this.taxCode = taxCode;
    }
}
