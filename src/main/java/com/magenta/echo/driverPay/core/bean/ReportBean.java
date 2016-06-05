package com.magenta.echo.driverpay.core.bean;

import com.magenta.echo.driverpay.core.Queries;
import com.magenta.echo.driverpay.core.bean.dao.CommonDao;
import com.magenta.echo.driverpay.core.entity.ExportHistory;
import com.magenta.echo.driverpay.core.entity.FileContent;
import com.magenta.echo.driverpay.core.entity.PaymentDocument;
import com.magenta.echo.driverpay.core.enums.ExportType;
import org.hibernate.Hibernate;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 05-06-2016 21:04
 */
@Component
@Transactional
public class ReportBean extends AbstractBean {

	@Autowired
	private CommonDao commonDao;

	public ExportHistory loadStatement(@NotNull final Long paymentDocumentId)	{
		final PaymentDocument paymentDocument = getEntityManager().find(PaymentDocument.class, paymentDocumentId);
		final ExportHistory exportHistory = paymentDocument
				.getExportHistorySet()
				.stream()
				.filter(eh -> Objects.equals(eh.getType(), ExportType.STATEMENT))
				.findFirst()
				.orElse(null);
		Hibernate.initialize(exportHistory.getFileContent());
		return exportHistory;
	}

	public List<ExportHistory> loadExportHistoryList()	{
		return getEntityManager()
				.createQuery(getQuery(Queries.LOAD_EXPORT_HISTORY_LIST),ExportHistory.class)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public ExportHistory makeBarclays(@NotNull final List<Long> paymentDocumentId)	{

		final List<Object[]> result = getEntityManager()
				.createNativeQuery(getQuery(Queries.LOAD_BARCLAYS))
				.setParameter("paymentDocumentIdList",paymentDocumentId)
				.getResultList();

		final StringBuilder sb = new StringBuilder();

		sb.append("driver,net,vat,total\n");

		result.forEach(row -> {
			final String driver = (String)row[0];
			final Double net = (Double)row[1];
			final Double vat = (Double)row[2];
			final Double total = (Double)row[3];
			sb
					.append(driver).append(",")
					.append(net).append(",")
					.append(vat).append(",")
					.append(total).append("\n");
		});

		final Set<PaymentDocument> paymentDocumentSet = paymentDocumentId
				.stream()
				.map(id -> {
					final PaymentDocument paymentDocument = new PaymentDocument();
					paymentDocument.setId(id);
					return paymentDocument;
				})
				.collect(Collectors.toSet());

		final FileContent fileContent = new FileContent();
		fileContent.setContent(sb.toString());

		final ExportHistory exportHistory = new ExportHistory();
		exportHistory.setDate(LocalDate.now());
		exportHistory.setType(ExportType.BARCLAYS);
		exportHistory.setFileContent(fileContent);
		exportHistory.setPaymentDocumentSet(paymentDocumentSet);

		commonDao.insert(exportHistory);

		return exportHistory;
	}

	@SuppressWarnings("unchecked")
	public ExportHistory makeSage(@NotNull final List<Long> paymentDocumentId)	{

		final List<Object[]> result = getEntityManager()
				.createNativeQuery(getQuery(Queries.LOAD_SAGE))
				.setParameter("paymentDocumentIdList",paymentDocumentId)
				.getResultList();

		final StringBuilder sb = new StringBuilder();

		sb.append("driver,nominalCode,taxCode,net,vat,total\n");

		result.forEach(row -> {
			final String driver = (String)row[0];
			final String nominalCode = (String)row[1];
			final String taxCode = (String)row[2];
			final Double net = (Double)row[3];
			final Double vat = (Double)row[4];
			final Double total = (Double)row[5];
			sb
					.append(driver).append(",")
					.append(nominalCode).append(",")
					.append(taxCode).append(",")
					.append(net).append(",")
					.append(vat).append(",")
					.append(total).append("\n");
		});

		final Set<PaymentDocument> paymentDocumentSet = paymentDocumentId
				.stream()
				.map(id -> {
					final PaymentDocument paymentDocument = new PaymentDocument();
					paymentDocument.setId(id);
					return paymentDocument;
				})
				.collect(Collectors.toSet());

		final FileContent fileContent = new FileContent();
		fileContent.setContent(sb.toString());

		final ExportHistory exportHistory = new ExportHistory();
		exportHistory.setDate(LocalDate.now());
		exportHistory.setType(ExportType.SAGE);
		exportHistory.setFileContent(fileContent);
		exportHistory.setPaymentDocumentSet(paymentDocumentSet);

		commonDao.insert(exportHistory);

		return exportHistory;

	}
}
