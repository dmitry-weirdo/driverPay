package com.magenta.echo.driverpay.ui.dialog;

import com.evgenltd.kwick.ui.DialogScreen;
import com.evgenltd.kwick.ui.UIContext;
import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.ReportBean;
import com.magenta.echo.driverpay.core.entity.ExportHistory;
import com.magenta.echo.driverpay.ui.cellfactory.ButtonTableCell;
import com.magenta.echo.driverpay.ui.util.Constants;
import com.magenta.echo.driverpay.ui.util.InstantDialogs;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.controlsfx.control.Notifications;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 05-06-2016 23:11
 */
public class ExportHistoryBrowser extends DialogScreen<Void> {
	@FXML private TableView<ExportHistory> exportHistoryTable;
	@FXML private TableColumn<ExportHistory,String> dateColumn;
	@FXML private TableColumn<ExportHistory,String> typeColumn;
	@FXML private TableColumn<ExportHistory,ExportHistory> viewColumn;
	@FXML private TableColumn<ExportHistory,ExportHistory> saveColumn;

	private ReportBean reportBean = Context.get().getReportBean();

	public ExportHistoryBrowser() {
		super("/fxml/ExportHistoryBrowser.fxml");
		initUI();
		loadData();
	}

	@Override
	protected String getTitle() {
		return "Export History";
	}

	// general

	private void initUI()	{
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		typeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getLabel()));
		viewColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue()));
		viewColumn.setCellFactory(param -> new ButtonTableCell(Constants.MAGNIFIER_ZOOM, this::handleViewExportHistory));
		saveColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue()));
		saveColumn.setCellFactory(param -> new ButtonTableCell(Constants.DISK_RETURN_BLACK, this::handleSaveExportHistory));

		getDialog().getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
	}

	private void loadData()	{
		exportHistoryTable.getItems().setAll(reportBean.loadExportHistoryList());
	}

	// handlers

	private void handleViewExportHistory(@NotNull final ExportHistory exportHistory)	{
		InstantDialogs.makeFileViewerDialog(exportHistory).showAndWait();
	}

	private void handleSaveExportHistory(@NotNull final ExportHistory exportHistory)	{

		final FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV","*.csv"));
		final File selectedFile = fileChooser.showSaveDialog(UIContext.get().getStage());
		if(selectedFile == null)	{
			return;
		}

		try(FileOutputStream outputStream = new FileOutputStream(selectedFile))	{
			outputStream.write(exportHistory.getFileContent().getContent().getBytes());
			Notifications.create().title("Saving").text("Completed").showInformation();
		}catch(IOException exception)	{
			throw new RuntimeException(exception);
		}

	}

}
