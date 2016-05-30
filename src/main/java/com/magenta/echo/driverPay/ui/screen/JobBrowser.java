package com.magenta.echo.driverpay.ui.screen;

import com.evgenltd.kwickui.controls.objectbrowser.ObjectBrowser;
import com.evgenltd.kwickui.controls.objectpicker.ObjectPicker;
import com.evgenltd.kwickui.core.Screen;
import com.evgenltd.kwickui.core.UIContext;
import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.JobBean;
import com.magenta.echo.driverpay.core.bean.dao.JobDao;
import com.magenta.echo.driverpay.core.entity.Driver;
import com.magenta.echo.driverpay.core.entity.dto.JobDto;
import com.magenta.echo.driverpay.core.enums.JobType;
import com.magenta.echo.driverpay.ui.cellfactory.OptionalJobTypeListCell;
import com.magenta.echo.driverpay.ui.util.ObjectBrowserHelper;
import com.magenta.echo.driverpay.ui.util.PickerHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 12-05-2016 21:10
 */
public class JobBrowser extends Screen {

	@FXML private DatePicker dateFrom;
	@FXML private DatePicker dateTo;
	@FXML private ObjectPicker<Driver> driver;
	@FXML private ComboBox<Optional<JobType>> type;
	@FXML private ObjectBrowser<JobDto> jobBrowser;

	private final JobBean jobBean = Context.get().getJobBean();
	private final JobDao jobDao = Context.get().getJobDao();

    public JobBrowser() {
        super("/fxml/JobBrowser.fxml");
		initUI();
		loadData();
    }

	@Override
	public String getTitle() {
		return "Job Browser";
	}

	@Override
	public void refresh() {
		loadData();
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
		type.getSelectionModel().select(Optional.empty());

		ObjectBrowserHelper.setupJobDtoTable(jobBrowser);
		jobBrowser.setupDoubleClickEvent(job -> handleEditJob(null));
		jobBrowser.addButton("Add", ObjectBrowser.NodeType.ADD, this::handleAddJob);
		jobBrowser.addButton("Edit", ObjectBrowser.NodeType.EDIT, this::handleEditJob);
		jobBrowser.addButton("Remove", ObjectBrowser.NodeType.REMOVE, this::handleRemoveJob);
	}

	private void loadData()	{
		PickerHelper.setupDriverPicker(driver);

		final List<JobDto> jobList = jobBean.loadJobList(
				dateFrom.getValue(),
				dateTo.getValue(),
				driver.getSelectedObject().map(Driver::getId).orElse(null),
				type.getSelectionModel().getSelectedItem().orElse(null)
		);
		jobBrowser.getItems().setAll(jobList);
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

	private void handleAddJob(ActionEvent event) {
		UIContext.get().openScreen(new JobEdit(null));
	}

	private void handleEditJob(ActionEvent event) {
		if(jobBrowser.isSelectionEmpty())	{
			return;
		}

		final JobDto jobDto = jobBrowser.getSelectedItem();
		UIContext.get().openScreen(new JobEdit(jobDto.getId()));
	}

	private void handleRemoveJob(ActionEvent event)	{
		if(jobBrowser.isSelectionEmpty())	{
			return;
		}

		final List<JobDto> jobList = jobBrowser.getSelectedItems();
		final List<Long> jobIdList = jobList
				.stream()
				.map(JobDto::getId)
				.collect(Collectors.toList());
		jobBean.butchDelete(jobIdList);
		loadData();
	}

}
