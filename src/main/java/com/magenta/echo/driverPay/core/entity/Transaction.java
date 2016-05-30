package com.magenta.echo.driverpay.core.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Project: Driver Pay Enhanced
 * Author:  Lebedev
 * Created: 27-05-2016 12:59
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
	@NotNull(message = "Should not be null")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "from_id")
	@NotNull(message = "Should not be null")
    private Balance from;

    @ManyToOne
    @JoinColumn(name = "to_id")
	@NotNull(message = "Should not be null")
    private Balance to;

    @Column(name = "payment_date")
	@NotNull(message = "Should not be null")
    private LocalDate paymentDate;

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

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(final Payment payment) {
        this.payment = payment;
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

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(final LocalDate paymentDate) {
        this.paymentDate = paymentDate;
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
}
