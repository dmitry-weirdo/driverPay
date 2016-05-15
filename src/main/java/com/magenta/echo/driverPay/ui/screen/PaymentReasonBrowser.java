package com.magenta.echo.driverpay.ui.screen;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.PaymentBean;
import com.magenta.echo.driverpay.core.entity.DriverDto;
import com.magenta.echo.driverpay.core.entity.PaymentReasonDto;
import com.magenta.echo.driverpay.core.enums.PaymentType;
import com.magenta.echo.driverpay.ui.cellfactory.OptionalPaymentTypeListCell;
import com.magenta.echo.driverpay.ui.dialog.SelectDriver;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Arrays;
import java.util.Optional;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 12-05-2016 21:30
 */
public class PaymentReasonBrowser extends Screen {

	private PaymentBean paymentBean = Context.get().getPaymentBean();

	@FXML private Button editPaymentReasonButton;
    @FXML private TableView<PaymentReasonDto> table;
    @FXML private TableColumn<PaymentReasonDto,Long> idColumn;
    @FXML private TableColumn<PaymentReasonDto,String> nameColumn;
	@FXML private TableColumn<PaymentReasonDto,String> driverColumn;
	@FXML private TableColumn<PaymentReasonDto,String> typeColumn;

	@FXML private Button selectDriver;
	@FXML private ComboBox<Optional<PaymentType>> selectType;

	private Long selectedDriverId;

    public PaymentReasonBrowser() {
        super("/fxml/PaymentReasonBrowser.fxml");
        initUI();
        loadData();
    }

    private void initUI()   {

		selectType.setCellFactory(param -> new OptionalPaymentTypeListCell(false));
		selectType.setButtonCell(new OptionalPaymentTypeListCell(true));
		selectType.getItems().setAll(Arrays.asList(
				Optional.empty(),
				Optional.of(PaymentType.CREDIT),
				Optional.of(PaymentType.DEDUCTION),
				Optional.of(PaymentType.DEPOSIT),
				Optional.of(PaymentType.RELEASE_DEPOSIT),
				Optional.of(PaymentType.TAKE_DEPOSIT),
				Optional.of(PaymentType.REFILL_DEPOSIT)
		));
		selectType.getSelectionModel().select(Optional.empty());

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		driverColumn.setCellValueFactory(new PropertyValueFactory<>("driverValue"));
		typeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getLabel()));

		table.getSelectionModel().selectedItemProperty().addListener(param -> editPaymentReasonButton.setDisable(table.getSelectionModel().isEmpty()));
    }

    private void loadData() {
		final Optional<PaymentType> selectedType = selectType.getValue();
        table.getItems().setAll(paymentBean.loadPaymentReasonList(
				selectedDriverId,
				selectedType.orElse(null)
		));
    }

    // handlers

    @FXML
    private void handleAddPaymentReason(ActionEvent actionEvent) {
		Context.get().openScreen(new PaymentReasonEdit(null));
    }

    @FXML
    public void handleEditPaymentReason(ActionEvent actionEvent) {
		if(table.getSelectionModel().isEmpty()) {
			return;
		}
		Context.get().openScreen(new PaymentReasonEdit(table.getSelectionModel().getSelectedItem().getId()));
    }

	@FXML
	private void handleSelectDriver(ActionEvent event) {
		final Optional<DriverDto> result = Context.get().openDialogAndWait(new SelectDriver());
		if(!result.isPresent())	{
			return;
		}
		selectDriver.setText(result.get().getName());
		selectedDriverId = result.get().getId();
	}

	@FXML
	private void handleClear(ActionEvent event) {
		selectDriver.setText("Select Driver...");
		selectedDriverId = null;
		selectType.setValue(Optional.empty());
	}

	@FXML
	private void handleSearch(ActionEvent event) {
		loadData();
	}
}
