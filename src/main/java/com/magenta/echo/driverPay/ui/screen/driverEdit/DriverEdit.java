package com.magenta.echo.driverpay.ui.screen.driverEdit;

import com.evgenltd.kwick.controls.extensions.tableview.TableViewExtension;
import com.evgenltd.kwick.ui.Screen;
import com.evgenltd.kwick.ui.UIContext;
import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.DriverBean;
import com.magenta.echo.driverpay.core.bean.PaymentDocumentProcessingBean;
import com.magenta.echo.driverpay.core.bean.PaymentLoader;
import com.magenta.echo.driverpay.core.bean.ReportBean;
import com.magenta.echo.driverpay.core.bean.dao.CommonDao;
import com.magenta.echo.driverpay.core.bean.dao.DriverDao;
import com.magenta.echo.driverpay.core.entity.*;
import com.magenta.echo.driverpay.core.entity.dto.DriverDto;
import com.magenta.echo.driverpay.core.entity.dto.PaymentReasonDto;
import com.magenta.echo.driverpay.core.enums.PaymentDocumentMethod;
import com.magenta.echo.driverpay.core.enums.PaymentType;
import com.magenta.echo.driverpay.ui.dialog.ExportHistoryBrowser;
import com.magenta.echo.driverpay.ui.dialog.PaymentEdit;
import com.magenta.echo.driverpay.ui.dialog.PaymentReasonEdit;
import com.magenta.echo.driverpay.ui.util.InstantDialogs;
import com.magenta.echo.driverpay.ui.util.Utils;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.SegmentedButton;

import javax.validation.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 28-05-2016 20:02
 */
public class DriverEdit extends Screen{

	// general
	@FXML private TextField name;
	@FXML private Label lastSalaryCalculationDate;
	@FXML private Label lastProcessingDate;
	@FXML private Label calculatedSalary;
	@FXML private Label depositState;

	// pane toggle's
	@FXML private HBox toggleButtonPane;
	private SegmentedButton toggleButtonGroup;
	@FXML private ToggleButton toggleButtonCharges;
	@FXML private ToggleButton toggleButtonSalaryCalculation;
	@FXML private ToggleButton toggleButtonProcessing;

	// charges pane
	@FXML private VBox chargesPane;
	@FXML private Button chargesEdit;
	@FXML private Button chargesRemove;
	@FXML private TableView<PaymentReasonDto> chargesTable;
	@FXML private TableColumn<PaymentReasonDto,PaymentType> chargesTypeColumn;
	@FXML private TableColumn<PaymentReasonDto,String> chargesNameColumn;
	@FXML private TableColumn<PaymentReasonDto,String> chargesScheduleRuleColumn;
	@FXML private MenuItem chargesContextMenuEdit;
	@FXML private MenuItem chargesContextMenuRemove;

	// salary calculation pane
	@FXML private VBox salaryCalculationPane;
	@FXML private Button salaryCalculationEdit;
	@FXML private Button salaryCalculationRemove;
	@FXML private DatePicker salaryCalculationUpTo;
	@FXML private Button salaryCalculationButton;
	@FXML private Button salaryCalculationImmediateButton;
	@FXML private TableView<Payment> salaryCalculationTable;
	@FXML private HBox salaryCalculationTableTotals;
	@FXML private TableColumn<Payment,PaymentType> salaryCalculationTypeColumn;
	@FXML private TableColumn<Payment,String> salaryCalculationNameColumn;
	@FXML private TableColumn<Payment,String> salaryCalculationDateColumn;
	@FXML private TableColumn<Payment,Double> salaryCalculationNetColumn;
	@FXML private TableColumn<Payment,Double> salaryCalculationVatColumn;
	@FXML private TableColumn<Payment,Double> salaryCalculationTotalColumn;
	@FXML private TableColumn<Payment,String> salaryCalculationNominalCodeColumn;
	@FXML private TableColumn<Payment,String> salaryCalculationTaxCodeColumn;
	@FXML private MenuItem salaryCalculationContextMenuEdit;
	@FXML private MenuItem salaryCalculationContextMenuRemove;
	@FXML private MenuItem salaryCalculationContextMenuImmediate;

	// processing pane
	@FXML private VBox processingPane;
	@FXML private Button processingRollbackCalculation;
	@FXML private Button processingButton;
	@FXML private Button processingOpenStatementButton;
	@FXML private Button processingMakeSageButton;
	@FXML private Button processingMakeBarclaysButton;
	@FXML private Button processingViewHistoryButton;
	@FXML private ListView<PaymentDocument> processingOperationsList;
	@FXML private TableView<Payment> processingPaymentsTable;
	@FXML private HBox processingPaymentsTableTotals;
	@FXML private TableColumn<Payment,PaymentType> processingTypeColumn;
	@FXML private TableColumn<Payment,String> processingNameColumn;
	@FXML private TableColumn<Payment,String> processingPlannedDateColumn;
	@FXML private TableColumn<Payment,Double> processingNetColumn;
	@FXML private TableColumn<Payment,Double> processingVatColumn;
	@FXML private TableColumn<Payment,Double> processingTotalColumn;
	@FXML private TableColumn<Payment,String> processingNominalCodeColumn;
	@FXML private TableColumn<Payment,String> processingTaxCodeColumn;

	// data model
	private Long driverId;
	private DriverDto driver = new DriverDto();
	private List<PaymentReasonDto> paymentReasonList = new ArrayList<>();
	private List<Payment> paymentList = new ArrayList<>();
	private List<PaymentDocument> paymentDocumentList = new ArrayList<>();
	private List<Payment> processingPaymentList = new ArrayList<>();

	// beans
	private final CommonDao commonDao = Context.get().getCommonDao();
	private final DriverDao driverDao = Context.get().getDriverDao();
	private final DriverBean driverBean = Context.get().getDriverBean();
	private final PaymentLoader paymentLoader = Context.get().getPaymentLoader();
	private final PaymentDocumentProcessingBean paymentDocumentProcessingBean = Context.get().getPaymentDocumentProcessingBean();
	private final ReportBean reportBean = Context.get().getReportBean();

	private final Validator validator = Context.get().getValidator();

	public DriverEdit(final Long driverId) {
		super("/fxml/DriverEdit.fxml");
		this.driverId = driverId;
		initUI();
		loadDriver();
		loadPaymentReasonList();
		loadPaymentList();
		loadPaymentDocumentList();
	}

	@Override
	public String getTitle() {
		return "Edit Driver";
	}

	// ##############################################################
	// #                                                            #
	// #                   Initializations                          #
	// #                                                            #
	// ##############################################################

	private void initUI()	{

		toggleButtonPane.getChildren().clear();
		toggleButtonGroup = new SegmentedButton(
				toggleButtonCharges,
				toggleButtonSalaryCalculation,
				toggleButtonProcessing
		);
		toggleButtonGroup.getToggleGroup().selectedToggleProperty().addListener(param -> updateToggleButtonState());
		toggleButtonPane.getChildren().add(toggleButtonGroup);
		// ugly hack
		toggleButtonGroup.getToggleGroup().getToggles().addListener((InvalidationListener)observable -> {
			if(toggleButtonGroup.getToggleGroup().getToggles().size() == 3 && driverId != null)	{
				updateToggleButtonState();
			}
		});
		// end

		chargesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		chargesTable.getSelectionModel().selectedItemProperty().addListener(param -> updateChargesCommandsState());
		TableViewExtension.setupDoubleClickEvent(chargesTable, pr -> handleChargesEdit(null));

		chargesTypeColumn.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
		chargesTypeColumn.setCellFactory(param -> new PaymentTypeTableCell());
		chargesNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		chargesScheduleRuleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getScheduleType().getLabel()));

		salaryCalculationUpTo.valueProperty().addListener(param -> loadPaymentList());
		salaryCalculationUpTo.setValue(LocalDate.now());

		salaryCalculationTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		salaryCalculationTable.getSelectionModel().selectedItemProperty().addListener(param -> updateSalaryCalculationCommandsState());
		TableViewExtension.setupTotalSupport(
				salaryCalculationTable,
				salaryCalculationTableTotals,
				Arrays.asList(
						salaryCalculationNetColumn,
						salaryCalculationVatColumn,
						salaryCalculationTotalColumn
				)

		);
		TableViewExtension.setupDoubleClickEvent(salaryCalculationTable, payment -> handleSalaryCalculationEdit(null));

		salaryCalculationTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
		salaryCalculationTypeColumn.setCellFactory(param -> new PaymentTypeTableCell());
		salaryCalculationNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		salaryCalculationDateColumn.setCellValueFactory(new PropertyValueFactory<>("plannedDate"));
		salaryCalculationNetColumn.setCellValueFactory(Utils.getAmountCellValueFactory(Payment::getNet));
		salaryCalculationVatColumn.setCellValueFactory(Utils.getAmountCellValueFactory(Payment::getVat));
		salaryCalculationTotalColumn.setCellValueFactory(Utils.getAmountCellValueFactory(Payment::getTotal));
		salaryCalculationNominalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("nominalCode"));
		salaryCalculationTaxCodeColumn.setCellValueFactory(new PropertyValueFactory<>("taxCode"));

		processingOperationsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		processingOperationsList.setCellFactory(param -> new PaymentDocumentListCell());
		processingOperationsList.getSelectionModel().selectedItemProperty().addListener(param -> loadProcessedPaymentList());
		processingOperationsList.getSelectionModel().selectedItemProperty().addListener(param -> updateProcessingCommandsState());
		processingPaymentsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		TableViewExtension.setupTotalSupport(
				processingPaymentsTable,
				processingPaymentsTableTotals,
				Arrays.asList(
						processingNetColumn,
						processingVatColumn,
						processingTotalColumn
				)
		);

		processingTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
		processingTypeColumn.setCellFactory(param -> new PaymentTypeTableCell());
		processingNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		processingPlannedDateColumn.setCellValueFactory(new PropertyValueFactory<>("plannedDate"));
		processingNetColumn.setCellValueFactory(Utils.getAmountCellValueFactory(Payment::getNet));
		processingVatColumn.setCellValueFactory(Utils.getAmountCellValueFactory(Payment::getVat));
		processingTotalColumn.setCellValueFactory(Utils.getAmountCellValueFactory(Payment::getTotal));
		processingNominalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("nominalCode"));
		processingTaxCodeColumn.setCellValueFactory(new PropertyValueFactory<>("taxCode"));

		updateToggleButtonState();
		updateChargesCommandsState();
		updateSalaryCalculationCommandsState();
		updateProcessingCommandsState();
	}

	private void fillUI()	{
		if(driverId == null)	{
			toggleButtonGroup.getToggleGroup().selectToggle(null);
			toggleButtonGroup.setDisable(true);
		}
		name.setText(driver.getName());
		lastSalaryCalculationDate.setText(Utils.toString(driver.getLastSalaryCalculationDate(),"—"));
		lastProcessingDate.setText(Utils.toString(driver.getLastProcessingDate(),"—"));
		depositState.setText(String.format(
				"%s / %s",
				Utils.toString(driver.getCurrentDeposit(),"—"),
				Utils.toString(driver.getTotalDeposit(),"—")
		));
		calculatedSalary.setText(Utils.toString(driver.getCalculatedSalary(),"—"));
	}

	private void fillChargesPane()	{
		chargesTable.getItems().setAll(paymentReasonList);
	}

	private void fillSalaryCalculationPane()	{
		salaryCalculationTable.getItems().setAll(paymentList);
	}

	private void fillProcessingPane()	{
		processingOperationsList.getItems().setAll(paymentDocumentList);
	}

	private void fillProcessedPayments()	{
		processingPaymentsTable.getItems().setAll(processingPaymentList);
	}

	// ##############################################################
	// #                                                            #
	// #                       Load data                            #
	// #                                                            #
	// ##############################################################

	private void loadDriver()	{
		if(driverId != null)	{
			driver = driverBean.loadDriver(driverId);
		}
		fillUI();
	}

	private void loadPaymentReasonList()	{
		if(driverId == null)	{
			return;
		}
		paymentReasonList = paymentLoader.loadPaymentReasonList(driverId);
		fillChargesPane();
	}

	private void loadPaymentList()	{
		if(driverId == null)	{
			return;
		}
		LocalDate upToDate = salaryCalculationUpTo.getValue();
		if(upToDate == null)	{
			upToDate = LocalDate.now();
		}
		paymentList = paymentLoader.loadPaymentList(driverId, upToDate);
		fillSalaryCalculationPane();
	}

	private void loadPaymentDocumentList()	{
		if(driverId == null)	{
			return;
		}
		paymentDocumentList = paymentLoader.loadPaymentDocumentList(driverId);
		fillProcessingPane();
	}

	private void loadProcessedPaymentList()	{
		if(driverId == null)	{
			return;
		}

		if(processingOperationsList.getSelectionModel().getSelectedItems().size() != 1)	{
			processingPaymentList = new ArrayList<>();
		}else {
			final PaymentDocument selectedPaymentDocument = processingOperationsList.getSelectionModel().getSelectedItem();
			processingPaymentList = paymentLoader.loadPaymentList(driverId, selectedPaymentDocument.getId());
		}

		fillProcessedPayments();
	}

	// ##############################################################
	// #                                                            #
	// #                     Persist data                           #
	// #                                                            #
	// ##############################################################

	private Driver fillDriver()	{
		final Driver driver = new Driver();
		driver.setId(driverId);
		driver.setName(name.getText());
		return driver;
	}

	// ##############################################################
	// #                                                            #
	// #                    Service methods                         #
	// #                                                            #
	// ##############################################################

	// screen actions

	private void openChargesPane()	{
		toggleButtonGroup.getToggleGroup().selectToggle(toggleButtonCharges);
	}

	private void openSalaryCalculationPane()	{
		toggleButtonGroup.getToggleGroup().selectToggle(toggleButtonSalaryCalculation);
	}

	private void openProcessingPane()	{
		toggleButtonGroup.getToggleGroup().selectToggle(toggleButtonProcessing);
	}

	private Optional<PaymentDocumentMethod> askPaymentDocumentMethod()	{
		final ButtonType cash = new ButtonType("Cash");
		final ButtonType account = new ButtonType("Account");
		final Dialog<PaymentDocumentMethod> dialog = new Dialog<>();
		dialog.setTitle("Select");
		dialog.setContentText("Select payment document type");
		dialog.getDialogPane().getButtonTypes().addAll(account, cash, ButtonType.CANCEL);
		dialog.setResultConverter(bt -> {
			if(bt.equals(cash))	{
				return PaymentDocumentMethod.CASH;
			}else if(bt.equals(account))	{
				return PaymentDocumentMethod.ACCOUNT;
			}else {
				return null;
			}
		});
		return dialog.showAndWait();
	}

	private Optional<LocalDate> askDate()	{
		final DatePicker datePicker = new DatePicker();
		final Dialog<LocalDate> dialog = new Dialog<>();
		dialog.setTitle("Select");
		dialog.getDialogPane().setContent(datePicker);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		dialog.setResultConverter(bt -> datePicker.getValue());
		return dialog.showAndWait();
	}

	// update state

	private void updateToggleButtonState() {
		final boolean isChargesPaneVisible = toggleButtonCharges.isSelected();
		chargesPane.setVisible(isChargesPaneVisible);
		chargesPane.setDisable(!isChargesPaneVisible);

		final boolean isSalaryCalculationPaneVisible = toggleButtonSalaryCalculation.isSelected();
		salaryCalculationPane.setVisible(isSalaryCalculationPaneVisible);
		salaryCalculationPane.setDisable(!isSalaryCalculationPaneVisible);

		final boolean isProcessingPaneVisible = toggleButtonProcessing.isSelected();
		processingPane.setVisible(isProcessingPaneVisible);
		processingPane.setDisable(!isProcessingPaneVisible);

		if(toggleButtonGroup.getToggleGroup().getSelectedToggle() == null)	{
			openChargesPane();
		}
	}

	private void updateChargesCommandsState()	{
		final int selectedItemCount = chargesTable.getSelectionModel().getSelectedItems().size();
		chargesEdit.setDisable(selectedItemCount != 1);
		chargesContextMenuEdit.setDisable(selectedItemCount != 1);
		chargesRemove.setDisable(selectedItemCount < 1);
		chargesContextMenuRemove.setDisable(selectedItemCount < 1);
	}

	private void updateSalaryCalculationCommandsState()	{
		final int selectedItemCount = salaryCalculationTable.getSelectionModel().getSelectedItems().size();
		salaryCalculationEdit.setDisable(selectedItemCount != 1);
		salaryCalculationContextMenuEdit.setDisable(selectedItemCount != 1);
		salaryCalculationRemove.setDisable(selectedItemCount < 1);
		salaryCalculationContextMenuRemove.setDisable(selectedItemCount < 1);
		salaryCalculationImmediateButton.setDisable(selectedItemCount != 1);
	}

	private void updateProcessingCommandsState()	{
		final int selectedItemCount = processingOperationsList.getSelectionModel().getSelectedItems().size();
		final boolean containsProcessed = processingOperationsList
				.getSelectionModel()
				.getSelectedItems()
				.stream()
				.filter(PaymentDocument::getProcessed)
				.findAny()
				.isPresent();
		final boolean containsUnprocessed = processingOperationsList
				.getSelectionModel()
				.getSelectedItems()
				.stream()
				.filter(paymentDocument -> !paymentDocument.getProcessed())
				.findAny()
				.isPresent();
		processingButton.setDisable(selectedItemCount < 1 || containsProcessed);
		processingRollbackCalculation.setDisable(selectedItemCount < 1 || containsProcessed);
		processingOpenStatementButton.setDisable(selectedItemCount != 1);
		processingMakeSageButton.setDisable(selectedItemCount < 1 || containsUnprocessed);
		processingMakeBarclaysButton.setDisable(selectedItemCount < 1 || containsUnprocessed);
	}

	// ##############################################################
	// #                                                            #
	// #                     FXML Handlers                          #
	// #                                                            #
	// ##############################################################

	// general

	@FXML
	private void handleApply(ActionEvent event) {
		if(driverId == null)	{
			driverDao.insert(fillDriver());
		}else {
			commonDao.update(fillDriver());
		}
		UIContext.get().closeScreen();
	}

	@FXML
	private void handleClose(ActionEvent event) {
		UIContext.get().closeScreen();
	}

	// charges pane

	@FXML
	private void handleChargesAddCredit(ActionEvent event) {
		new PaymentReasonEdit(driverId,PaymentType.CREDIT)
				.showAndWait();
		loadPaymentReasonList();
	}

	@FXML
	private void handleChargesAddDeduction(ActionEvent event) {
		new PaymentReasonEdit(driverId,PaymentType.DEDUCTION)
				.showAndWait();
		loadPaymentReasonList();
	}

	@FXML
	private void handleChargesEdit(ActionEvent event) {
		final PaymentReasonDto paymentReason = chargesTable.getSelectionModel().getSelectedItem();
		if(paymentReason == null)	{
			return;
		}

		new PaymentReasonEdit(driverId,paymentReason.getId())
				.showAndWait();
		loadPaymentReasonList();
	}

	@FXML
	private void handleChargesRemove(ActionEvent event) {
		final PaymentReasonDto paymentReason = chargesTable.getSelectionModel().getSelectedItem();
		if(paymentReason == null)	{
			return;
		}

		commonDao.delete(PaymentReason.class, paymentReason.getId());
		loadPaymentReasonList();
	}

	// salary calculation pane

	@FXML
	private void handleSalaryCalculationAddCredit(ActionEvent event) {
		new PaymentEdit(
				driverId,
				null,
				null,
				PaymentType.CREDIT,
				false
		).showAndWait();
		loadPaymentList();
	}

	@FXML
	private void handleSalaryCalculationAddDeduction(ActionEvent event) {
		new PaymentEdit(
				driverId,
				null,
				null,
				PaymentType.DEDUCTION,
				false
		).showAndWait();
		loadPaymentList();
	}

	@FXML
	private void handleSalaryCalculationAddDeposit(ActionEvent event) {
		new PaymentEdit(
				driverId,
				null,
				null,
				PaymentType.DEPOSIT,
				false
		).showAndWait();
		loadPaymentList();
	}

	@FXML
	private void handleSalaryCalculationAddCashPaymentBack(ActionEvent actionEvent) {
		new PaymentEdit(
				driverId,
				null,
				null,
				PaymentType.CASH_PAYMENT,
				true
		).showAndWait();
		loadPaymentList();
	}

	@FXML
	private void handleSalaryCalculationAddReleaseDeduction(ActionEvent actionEvent) {
		new PaymentEdit(
				driverId,
				null,
				null,
				PaymentType.RELEASE_DEPOSIT,
				true
		).showAndWait();
		loadPaymentList();
	}

	@FXML
	private void handleSalaryCalculationEdit(ActionEvent event) {
		if(salaryCalculationTable.getSelectionModel().isEmpty())	{
			return;
		}

		final Payment selectedPayment = salaryCalculationTable.getSelectionModel().getSelectedItem();

		new PaymentEdit(
				driverId,
				Optional
						.ofNullable(selectedPayment.getPaymentReason())
						.map(PaymentReason::getId)
						.orElse(null),
				selectedPayment.getId(),
				null,
				false
		).showAndWait();
		loadPaymentList();
	}

	@FXML
	private void handleSalaryCalculationRemove(ActionEvent event) {
		if(salaryCalculationTable.getSelectionModel().isEmpty())	{
			return;
		}

		final Payment selectedPayment = salaryCalculationTable.getSelectionModel().getSelectedItem();
		commonDao.delete(Payment.class, selectedPayment.getId());
		loadPaymentList();
	}

	@FXML
	private void handleCalculateSalary(ActionEvent event) {
		askPaymentDocumentMethod().map(pdm -> {
			final LocalDate upTo = salaryCalculationUpTo.getValue() == null
					? LocalDate.now()
					: salaryCalculationUpTo.getValue();
			paymentDocumentProcessingBean.calculateSalary(driverId,upTo,pdm);
			Notifications
					.create()
					.title("Salary calculation")
					.text("Completed")
					.showInformation();
			loadDriver();
			loadPaymentList();
			loadPaymentDocumentList();
			loadProcessedPaymentList();
			return null;
		});

	}

	@FXML
	private void handleSalaryCalculationImmediate(ActionEvent event) {
		if(salaryCalculationTable.getSelectionModel().isEmpty())	{
			return;
		}

		final Payment selectedPayment = salaryCalculationTable.getSelectionModel().getSelectedItem();
		askPaymentDocumentMethod().map(pdm -> {

			askDate().map(date -> {

				paymentDocumentProcessingBean.processImmediatePayment(date, selectedPayment.getId(), pdm);
				Notifications.create().title("Process payment immediately").text("Completed").showInformation();
				loadDriver();
				loadPaymentList();
				loadPaymentDocumentList();
				loadProcessedPaymentList();

				return null;

			});

			return null;

		});
	}

	// processing pane

	@FXML
	private void handleProcessingButton(ActionEvent event) {
		final List<PaymentDocument> selectedPaymentDocumentList = processingOperationsList.getSelectionModel().getSelectedItems();
		if(selectedPaymentDocumentList.isEmpty())	{
			return;
		}

		askDate().map(date -> {
			paymentDocumentProcessingBean.processPaymentDocument(
					date,
					selectedPaymentDocumentList
							.stream()
							.map(PaymentDocument::getId)
							.collect(Collectors.toList())
			);
			Notifications.create().title("Document processing").text("Completed").showInformation();
			loadDriver();
			loadPaymentList();
			loadPaymentDocumentList();
			loadProcessedPaymentList();
			return null;
		});
	}

	@FXML
	private void handleRollbackCalculation(ActionEvent event) {
		final List<Long> paymentDocumentIdList = processingOperationsList
				.getSelectionModel()
				.getSelectedItems()
				.stream()
				.map(PaymentDocument::getId)
				.collect(Collectors.toList());
		paymentDocumentProcessingBean.rollbackSalaryCalculation(paymentDocumentIdList);
		Notifications.create().title("Salary Rollback").text("Completed").showInformation();
		loadDriver();
		loadPaymentList();
		loadPaymentDocumentList();
		loadProcessedPaymentList();
	}

	@FXML
	private void handleOpenStatement(ActionEvent event) {
		if(processingOperationsList.getSelectionModel().isEmpty())	{
			return;
		}

		final PaymentDocument paymentDocument = processingOperationsList
				.getSelectionModel()
				.getSelectedItem();
		final ExportHistory exportHistory = reportBean.loadStatement(paymentDocument.getId());

		if(exportHistory == null)	{
			Notifications
					.create()
					.title("Warning")
					.text("Statement does not exists")
					.showWarning();
			return;
		}

		InstantDialogs
				.makeFileViewerDialog(exportHistory)
				.showAndWait();
	}

	@FXML
	private void handleMakeSage(ActionEvent event) {
		if(processingOperationsList.getSelectionModel().isEmpty())	{
			return;
		}
		final List<Long> paymentDocumentIdList = processingOperationsList
				.getSelectionModel()
				.getSelectedItems()
				.stream()
				.map(PaymentDocument::getId)
				.collect(Collectors.toList());

		final ExportHistory exportHistory = reportBean.makeSage(paymentDocumentIdList);
		InstantDialogs.makeFileViewerDialog(exportHistory).showAndWait();
	}

	@FXML
	private void handleMakeBarclays(ActionEvent event) {
		if(processingOperationsList.getSelectionModel().isEmpty())	{
			return;
		}
		final List<Long> paymentDocumentIdList = processingOperationsList
				.getSelectionModel()
				.getSelectedItems()
				.stream()
				.map(PaymentDocument::getId)
				.collect(Collectors.toList());

		final ExportHistory exportHistory = reportBean.makeBarclays(paymentDocumentIdList);
		InstantDialogs.makeFileViewerDialog(exportHistory).showAndWait();
	}

	@FXML
	private void handleViewHistory(ActionEvent event) {
		new ExportHistoryBrowser().showAndWait();
	}

}
