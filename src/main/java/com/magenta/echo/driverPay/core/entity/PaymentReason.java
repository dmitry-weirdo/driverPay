package com.magenta.echo.driverpay.core.entity;


import com.magenta.echo.driverpay.core.enums.PaymentType;
import com.magenta.echo.driverpay.core.enums.ScheduleType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Period;

/**
 * Project: Driver Pay Enhanced
 * Author:  Lebedev
 * Created: 27-05-2016 12:55
 */
@Entity
@Table(name = "payment_reasons")
public class PaymentReason implements Identified{

    @Id
    @GeneratedValue
    private Long id;

	@NotEmpty(message = "Should not be empty")
	@Length(message = "Should be less than 255")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
	@NotNull(message = "Should not be null")
    private Driver driver;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
	@NotNull(message = "Should not be null")
    private PaymentType paymentType;

    @ManyToOne
    @JoinColumn(name = "from_id")
	@NotNull(message = "Should not be null")
    private Balance from;

    @ManyToOne
    @JoinColumn(name = "to_id")
	@NotNull(message = "Should not be null")
    private Balance to;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_type")
	@NotNull(message = "Should not be null")
    private ScheduleType scheduleType;

    @Column(name = "opening_balance")
    private Double openingBalance;

    private Double gross;

	@NotNull(message = "Should not be null")
    private Double net;

	@NotNull(message = "Should not be null")
    private Double vat;

	@NotNull(message = "Should not be null")
    private Double total;

    @Column(name = "nominal_code")
	@NotEmpty(message = "Should not be empty")
	@Length(message = "Should be less than 255")
    private String nominalCode;

    @Column(name = "tax_code")
	@NotEmpty(message = "Should not be empty")
	@Length(message = "Should be less than 255")
    private String taxCode;

    private Period frequency;

    @Column(name = "end_date")
    private LocalDate endDate;

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

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(final Driver driver) {
        this.driver = driver;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(final PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Balance getFrom() {
        return from;
    }

    public void setFrom(final Balance from) {
        this.from = from;
    }

    public Balance getTo() {
        return to;
    }

    public void setTo(final Balance to) {
        this.to = to;
    }

    public ScheduleType getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(final ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
    }

    public Double getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(final Double openingBalance) {
        this.openingBalance = openingBalance;
    }

    public Double getGross() {
        return gross;
    }

    public void setGross(final Double gross) {
        this.gross = gross;
    }

    public Double getNet() {
        return net;
    }

    public void setNet(final Double net) {
        this.net = net;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(final Double vat) {
        this.vat = vat;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(final Double total) {
        this.total = total;
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

    public Period getFrequency() {
        return frequency;
    }

    public void setFrequency(final Period frequency) {
        this.frequency = frequency;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(final LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isEndless()  {
        return endDate == null;
    }

    public void setEndless(final boolean endless)   {
        endDate = null;
    }

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		PaymentReason that = (PaymentReason)o;

		return id != null ? id.equals(that.id) : that.id == null;

	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
