package com.magenta.echo.driverpay.ui.screen;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.PaymentBean;
import com.magenta.echo.driverpay.core.bean.PaymentProcessBean;
import com.magenta.echo.driverpay.core.entity.PaymentDocumentDto;
import com.magenta.echo.driverpay.core.entity.PaymentDto;
import com.magenta.echo.driverpay.core.entity.TransactionDto;
import com.magenta.echo.driverpay.ui.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 15-05-2016 23:18
 */
public class PaymentDocumentView extends Screen {

	private PaymentBean paymentBean = Context.get().getPaymentBean();
	private PaymentProcessBean paymentProcessBean = Context.get().getPaymentProcessBean();

	@FXML private Label id;
	@FXML private Label paymentDate;
	@FXML private Label driver;
	@FXML private CheckBox processed;

	@FXML private TableView<PaymentDto> paymentTable;
	@FXML private TableColumn<PaymentDto,Long> paymentIdColumn;
	@FXML private TableColumn<PaymentDto,String> paymentTypeColumn;
	@FXML private TableColumn<PaymentDto,String> paymentPlannedDateColumn;
	@FXML private TableColumn<PaymentDto,Double> paymentNetColumn;
	@FXML private TableColumn<PaymentDto,Double> paymentVatColumn;
	@FXML private TableColumn<PaymentDto,Double> paymentTotalColumn;
	@FXML private TableColumn<PaymentDto,String> paymentNominalCodeColumn;
	@FXML private TableColumn<PaymentDto,String> paymentTaxCodeColumn;

	@FXML private TableView<TransactionDto> transactionTable;
	@FXML private TableColumn<TransactionDto,Long> transactionIdColumn;
	@FXML private TableColumn<TransactionDto,String> transactionBalanceFromColumn;
	@FXML private TableColumn<TransactionDto,String> transactionBalanceToColumn;
	@FXML private TableColumn<TransactionDto,Double> transactionNetColumn;
	@FXML private TableColumn<TransactionDto,Double> transactionVatColumn;
	@FXML private TableColumn<TransactionDto,Double> transactionTotalColumn;
	@FXML private TableColumn<TransactionDto,String> transactionNominalCodeColumn;
	@FXML private TableColumn<TransactionDto,String> transactionTaxCodeColumn;

	private Long paymentDocumentId;
	private PaymentDocumentDto paymentDocument;
	private List<PaymentDto> paymentList;
	private List<TransactionDto> transactionList;

	public PaymentDocumentView(final Long paymentDocumentId) {
		super("/fxml/PaymentDocumentView.fxml");
		this.paymentDocumentId = paymentDocumentId;
		initUI();
		loadData();
	}

	// other

	private void initUI()	{
		paymentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		paymentTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getLabel()));
		paymentPlannedDateColumn.setCellValueFactory(new PropertyValueFactory<>("plannedDate"));
		paymentNetColumn.setCellValueFactory(new PropertyValueFactory<>("net"));
		paymentVatColumn.setCellValueFactory(new PropertyValueFactory<>("vat"));
		paymentTotalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
		paymentNominalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("nominalCode"));
		paymentTaxCodeColumn.setCellValueFactory(new PropertyValueFactory<>("taxCode"));

		transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		transactionBalanceFromColumn.setCellValueFactory(new PropertyValueFactory<>("balanceFromValue"));
		transactionBalanceToColumn.setCellValueFactory(new PropertyValueFactory<>("balanceToValue"));
		transactionNetColumn.setCellValueFactory(new PropertyValueFactory<>("net"));
		transactionVatColumn.setCellValueFactory(new PropertyValueFactory<>("vat"));
		transactionTotalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
		transactionNominalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("nominalCode"));
		transactionTaxCodeColumn.setCellValueFactory(new PropertyValueFactory<>("taxCode"));
	}

	private void loadData()	{
		paymentDocument = paymentProcessBean.loadPaymentDocument(paymentDocumentId);
		paymentList = paymentBean.loadPaymentListByPaymentDocument(paymentDocumentId);
		transactionList = paymentProcessBean.loadTransactionListByPaymentDocument(paymentDocumentId);
		fillUI();
	}

	private void fillUI()	{
		id.setText(Utils.toString(paymentDocument.getId()));
		paymentDate.setText(Utils.toString(paymentDocument.getPaymentDate()));
		driver.setText(Utils.toString(paymentDocument.getDriverValue()));
		processed.setSelected(paymentDocument.getProcessed());

		paymentTable.getItems().setAll(paymentList);
		transactionTable.getItems().setAll(transactionList);
	}

	// handlers

	@FXML
	private void handleClose(ActionEvent event) {
		Context.get().openScreen(new PaymentDocumentBrowser());
	}
}
