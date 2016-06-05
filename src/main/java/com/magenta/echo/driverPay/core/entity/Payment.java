package com.magenta.echo.driverpay.core.entity;


import com.magenta.echo.driverpay.core.entity.constraint.CheckPaymentStatus;
import com.magenta.echo.driverpay.core.enums.PaymentStatus;
import com.magenta.echo.driverpay.core.enums.PaymentType;
import com.magenta.echo.driverpay.core.validation.group.Delete;
import com.magenta.echo.driverpay.core.validation.group.DocumentProcessing;
import com.magenta.echo.driverpay.core.validation.group.SalaryCalculation;
import com.magenta.echo.driverpay.core.validation.group.SalaryRollback;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

/**
 * Project: Driver Pay Enhanced
 * Author:  Lebedev
 * Created: 27-05-2016 12:59
 */
@Entity
@Table(name = "payments")
//@CheckUpdate(checker = PaymentUpdateChecker.class)
public class Payment implements Identified {

    @Id
    @GeneratedValue
    private Long id;

	@NotEmpty(message = "Should not be empty")
	@Length(message = "Should be less than 255")
	private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_reason_id")
    private PaymentReason paymentReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
	@NotNull(message = "Should not be null")
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_document_id")
	@NotNull(groups = {SalaryRollback.class, DocumentProcessing.class})
    private PaymentDocument paymentDocument;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "payment")
	private Job job;

    @ManyToOne
    @JoinColumn(name = "from_id")
	@NotNull(message = "Should not be null")
    private Balance from;

    @ManyToOne
    @JoinColumn(name = "to_id")
	@NotNull(message = "Should not be null")
    private Balance to;

    @Enumerated(EnumType.STRING)
	@NotNull(message = "Should not be null")
    private PaymentType type;

    @Enumerated(EnumType.STRING)
	@NotNull(message = "Should not be null")
	@CheckPaymentStatus.List({
			@CheckPaymentStatus(
					expected = PaymentStatus.NONE,
					groups = {SalaryCalculation.class, Delete.class}
			),
			@CheckPaymentStatus(
					expected = PaymentStatus.CALCULATED,
					groups = {SalaryRollback.class, DocumentProcessing.class}
			)
	})
    private PaymentStatus status = PaymentStatus.NONE;

    @Column(name = "planned_date")
	@NotNull(message = "Should not be null")
    private LocalDate plannedDate;

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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "payment", cascade = CascadeType.ALL)
	private Set<Transaction> transactionSet;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PaymentReason getPaymentReason() {
        return paymentReason;
    }

    public void setPaymentReason(final PaymentReason paymentReason) {
        this.paymentReason = paymentReason;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(final Driver driver) {
        this.driver = driver;
    }

    public PaymentDocument getPaymentDocument() {
        return paymentDocument;
    }

    public void setPaymentDocument(final PaymentDocument paymentDocument) {
        this.paymentDocument = paymentDocument;
    }

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
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

    public PaymentType getType() {
        return type;
    }

    public void setType(final PaymentType type) {
        this.type = type;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(final PaymentStatus status) {
        this.status = status;
    }

    public LocalDate getPlannedDate() {
        return plannedDate;
    }

    public void setPlannedDate(final LocalDate plannedDate) {
        this.plannedDate = plannedDate;
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

	public Set<Transaction> getTransactionSet() {
		return transactionSet;
	}

	public void setTransactionSet(Set<Transaction> transactionSet) {
		this.transactionSet = transactionSet;
	}
}
