package com.magenta.echo.driverpay.ui.screen;

import com.evgenltd.kwickui.controls.objectpicker.ObjectPicker;
import com.evgenltd.kwickui.controls.objectpicker.SimpleObject;
import com.evgenltd.kwickui.core.Screen;
import com.evgenltd.kwickui.core.UIContext;
import com.evgenltd.kwickui.extensions.tableview.TableViewExtension;
import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.JobBean;
import com.magenta.echo.driverpay.core.entity.JobDto;
import com.magenta.echo.driverpay.core.enums.JobType;
import com.magenta.echo.driverpay.ui.PickerHelper;
import com.magenta.echo.driverpay.ui.cellfactory.OptionalJobTypeListCell;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Arrays;
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
	@FXML private ObjectPicker<SimpleObject> driver;
	@FXML private ComboBox<Optional<JobType>> type;
	@FXML private Button editJobButton;

	@FXML private TableView<JobDto> table;
	@FXML private TableColumn<JobDto,Long> idColumn;
	@FXML private TableColumn<JobDto,String> jobDateColumn;
	@FXML private TableColumn<JobType,String> typeColumn;
	@FXML private TableColumn<JobDto,String> driverColumn;
	@FXML private TableColumn<JobDto,Double> totalColumn;

    public JobBrowser() {
        super("/fxml/JobBrowser.fxml");
		initUI();
		loadData();
    }

	@Override
	public String getTitle() {
		return "Job Browser";
	}

	// other

	private void initUI()	{
		type.setButtonCell(new OptionalJobTypeListCell(true));
		type.setCellFactory(param -> new OptionalJobTypeListCell(false));
		type.getItems().setAll(Arrays.asList(
				Optional.empty(),
				Optional.of(JobType.REGULAR_JOB),
				Optional.of(JobType.CASH_JOB)
		));

		TableViewExtension.setupDoubleClickEvent(table, this::handleEditJob);

		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		jobDateColumn.setCellValueFactory(new PropertyValueFactory<>("jobDate"));
		typeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLabel()));
		driverColumn.setCellValueFactory(new PropertyValueFactory<>("driverValue"));
		totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

		table.getSelectionModel().selectedItemProperty().addListener(param -> editJobButton.setDisable(table.getSelectionModel().isEmpty()));
	}

	private void loadData()	{
		PickerHelper.setupDriverPicker(driver);

		final List<JobDto> jobList = jobBean.loadJobList(
				dateFrom.getValue(),
				dateTo.getValue(),
				driver.getSelectedObject().map(SimpleObject::getId).orElse(null),
				type.getSelectionModel().getSelectedItem().orElse(null)
		);
		table.getItems().setAll(jobList);
	}

	// handlers

	@FXML
	private void handleSearch(ActionEvent event) {
		loadData();
	}

	@FXML
	public void handleClear(ActionEvent event) {
		dateFrom.setValue(null);
		dateTo.setValue(null);
		driver.clear();
		type.getSelectionModel().select(Optional.empty());
	}

	private void handleEditJob(final JobDto jobDto)	{
		if(jobDto == null)	{
			return;
		}

		UIContext.get().openScreen(new JobEdit(jobDto.getId()));
	}

	@FXML
	private void handleAddJob(ActionEvent event) {
		UIContext.get().openScreen(new JobEdit(null));
	}

	@FXML
	private void handleEditJob(ActionEvent event) {
		if(table.getSelectionModel().isEmpty())	{
			return;
		}
		final JobDto jobDto = table.getSelectionModel().getSelectedItem();
		UIContext.get().openScreen(new JobEdit(jobDto.getId()));
	}


}
