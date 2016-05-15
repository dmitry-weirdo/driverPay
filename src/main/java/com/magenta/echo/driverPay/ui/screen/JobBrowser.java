package com.magenta.echo.driverpay.ui.screen;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.JobBean;
import com.magenta.echo.driverpay.core.entity.DriverDto;
import com.magenta.echo.driverpay.core.entity.JobDto;
import com.magenta.echo.driverpay.ui.dialog.SelectDriver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Optional;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 12-05-2016 21:10
 */
public class JobBrowser extends Screen {

	private final JobBean jobBean = Context.get().getJobBean();

	@FXML private DatePicker dateFrom;
	@FXML private DatePicker dateTo;
	@FXML private Button selectDriver;
	@FXML private Button editJobButton;

	@FXML private TableView<JobDto> table;
	@FXML private TableColumn<JobDto,Long> idColumn;
	@FXML private TableColumn<JobDto,String> jobDateColumn;
	@FXML private TableColumn<JobDto,String> driverColumn;

	private Long selectedDriverId;

    public JobBrowser() {
        super("/fxml/JobBrowser.fxml");
		initUI();
		loadData();
    }

	// other

	private void initUI()	{
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		jobDateColumn.setCellValueFactory(new PropertyValueFactory<>("jobDate"));
		driverColumn.setCellValueFactory(new PropertyValueFactory<>("driverValue"));

		table.getSelectionModel().selectedItemProperty().addListener(param -> editJobButton.setDisable(table.getSelectionModel().isEmpty()));
	}

	private void loadData()	{
		final List<JobDto> jobList = jobBean.loadJobList(
				dateFrom.getValue(),
				dateTo.getValue(),
				selectedDriverId
		);
		table.getItems().setAll(jobList);
	}

	// handlers

	@FXML
	private void handleSearch(ActionEvent event) {
		loadData();
	}

	@FXML
	private void handleAddJob(ActionEvent event) {
		Context.get().openScreen(new JobEdit(null));
	}

	@FXML
	private void handleEditJob(ActionEvent event) {
		if(table.getSelectionModel().isEmpty())	{
			return;
		}
		final JobDto jobDto = table.getSelectionModel().getSelectedItem();
		Context.get().openScreen(new JobEdit(jobDto.getId()));
	}

	@FXML
	private void handleSelectDriver(ActionEvent event) {
		final Optional<DriverDto> result = Context.get().openDialogAndWait(new SelectDriver());
		if(!result.isPresent())	{
			return;
		}
		selectDriver.setText(result.get().getName());
		selectedDriverId = result.get().getId();
		loadData();
	}

	@FXML
	public void handleClear(ActionEvent event) {
		dateFrom.setValue(null);
		dateTo.setValue(null);
		selectedDriverId = null;
		selectDriver.setText("Select Driver");
	}
}
