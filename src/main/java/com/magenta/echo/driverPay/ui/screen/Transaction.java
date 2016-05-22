package com.magenta.echo.driverpay.ui.screen;

import com.evgenltd.kwickui.core.Screen;
import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.PaymentProcessBean;
import com.magenta.echo.driverpay.core.entity.TransactionDto;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 04:01
 */
public class Transaction extends Screen {

	private PaymentProcessBean paymentProcessBean = Context.get().getPaymentProcessBean();

	@FXML private TableView<TransactionDto> table;
	@FXML private TableColumn<TransactionDto,Long> idColumn;
	@FXML private TableColumn<TransactionDto,String> dateColumn;
	@FXML private TableColumn<TransactionDto,String> fromColumn;
	@FXML private TableColumn<TransactionDto,String> toColumn;
	@FXML private TableColumn<TransactionDto,Double> netColumn;
	@FXML private TableColumn<TransactionDto,Double> vatColumn;
	@FXML private TableColumn<TransactionDto,Double> totalColumn;

	private Long paymentId;
	private Long paymentDocumentId;

	private Transaction() {
		super("/fxml/Transaction.fxml");
	}

	public Transaction(Long id, boolean isPaymentDocument) {
		this();
		if(isPaymentDocument)	{
			paymentDocumentId = id;
		}else {
			paymentId = id;
		}
		initUI();
		loadData();
	}

	@Override
	public String getTitle() {
		return "Transaction Log";
	}
// other

	private void initUI()	{
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		fromColumn.setCellValueFactory(new PropertyValueFactory<>("balanceFromValue"));
		toColumn.setCellValueFactory(new PropertyValueFactory<>("balanceToValue"));
		netColumn.setCellValueFactory(new PropertyValueFactory<>("net"));
		vatColumn.setCellValueFactory(new PropertyValueFactory<>("vat"));
		totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

	}

	private void loadData()	{
		if(paymentId != null)	{
			table.getItems().setAll(paymentProcessBean.loadTransactionListByPayment(paymentId));
		}else if(paymentDocumentId != null)	{
			table.getItems().setAll(paymentProcessBean.loadTransactionListByPaymentDocument(paymentDocumentId));
		}
	}

	// handlers


}
