package com.magenta.echo.driverpay.core.entity;

import com.magenta.echo.driverpay.core.enums.ExportType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 05-06-2016 20:36
 */
@Entity
@Table(name = "export_history")
public class ExportHistory {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private LocalDate date;

	@Enumerated(EnumType.STRING)
	@NotNull
	private ExportType type;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "file_id")
	@NotNull
	private FileContent fileContent;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "payment_document_exports",
			joinColumns = @JoinColumn(name = "export_history_id"),
			inverseJoinColumns = @JoinColumn(name = "payment_document_id")
	)
	private Set<PaymentDocument> paymentDocumentSet;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public ExportType getType() {
		return type;
	}

	public void setType(ExportType type) {
		this.type = type;
	}

	public FileContent getFileContent() {
		return fileContent;
	}

	public void setFileContent(FileContent fileContent) {
		this.fileContent = fileContent;
	}

	public Set<PaymentDocument> getPaymentDocumentSet() {
		return paymentDocumentSet;
	}

	public void setPaymentDocumentSet(Set<PaymentDocument> paymentDocumentSet) {
		this.paymentDocumentSet = paymentDocumentSet;
	}
}
