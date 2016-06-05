package com.magenta.echo.driverpay.core.entity;

import com.magenta.echo.driverpay.core.entity.constraint.CheckProcessed;
import com.magenta.echo.driverpay.core.enums.PaymentDocumentMethod;
import com.magenta.echo.driverpay.core.enums.PaymentDocumentType;
import com.magenta.echo.driverpay.core.validation.group.Delete;
import com.magenta.echo.driverpay.core.validation.group.DocumentProcessing;
import com.magenta.echo.driverpay.core.validation.group.SalaryCalculation;
import com.magenta.echo.driverpay.core.validation.group.SalaryRollback;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

/**
 * Project: Driver Pay Enhanced
 * Author:  Lebedev
 * Created: 27-05-2016 12:59
 */
@Entity
@Table(name = "payment_documents")
//@CheckUpdate(checker = PaymentDocumentUpdateChecker.class)
public class PaymentDocument implements Identified {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
	@NotNull
    private Driver driver;

    @Column(name = "payment_date")
	@NotNull
    private LocalDate paymentDate;

	@NotNull
	@CheckProcessed(
			message = "Should be unprocessed",
			expected = "false",
			groups = {SalaryRollback.class,DocumentProcessing.class,Delete.class}
	)
    private Boolean processed;

	@NotNull
	private PaymentDocumentType type;

	@NotNull
	private PaymentDocumentMethod method;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "paymentDocument", cascade = CascadeType.ALL)
	@NotNull
	@NotEmpty(groups = {SalaryCalculation.class, SalaryRollback.class, DocumentProcessing.class})
	@Valid
	private Set<Payment> paymentSet;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "paymentDocumentSet")
	private Set<ExportHistory> exportHistorySet;

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

	public PaymentDocumentType getType() {
		return type;
	}

	public void setType(PaymentDocumentType type) {
		this.type = type;
	}

	public PaymentDocumentMethod getMethod() {
		return method;
	}

	public void setMethod(PaymentDocumentMethod method) {
		this.method = method;
	}

	public Set<Payment> getPaymentSet() {
		return paymentSet;
	}

	public void setPaymentSet(Set<Payment> paymentSet) {
		this.paymentSet = paymentSet;
	}

	public Set<ExportHistory> getExportHistorySet() {
		return exportHistorySet;
	}

	public void setExportHistorySet(Set<ExportHistory> exportHistorySet) {
		this.exportHistorySet = exportHistorySet;
	}
}
