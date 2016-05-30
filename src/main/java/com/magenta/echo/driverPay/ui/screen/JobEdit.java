package com.magenta.echo.driverpay.ui.screen;

import com.evgenltd.kwickui.controls.objectbrowser.ObjectBrowser;
import com.evgenltd.kwickui.controls.objectpicker.ObjectPicker;
import com.evgenltd.kwickui.core.Screen;
import com.evgenltd.kwickui.core.UIContext;
import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.JobBean;
import com.magenta.echo.driverpay.core.bean.dao.JobDao;
import com.magenta.echo.driverpay.core.entity.Driver;
import com.magenta.echo.driverpay.core.entity.Job;
import com.magenta.echo.driverpay.core.entity.JobRate;
import com.magenta.echo.driverpay.core.enums.JobType;
import com.magenta.echo.driverpay.ui.cellfactory.OptionalJobTypeListCell;
import com.magenta.echo.driverpay.ui.dialog.JobRateEdit;
import com.magenta.echo.driverpay.ui.util.ObjectBrowserHelper;
import com.magenta.echo.driverpay.ui.util.PickerHelper;
import com.magenta.echo.driverpay.ui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 13-05-2016 01:35
 */
public class JobEdit extends Screen {

	@FXML private Label idField;
	@FXML private ComboBox<Optional<JobType>> typeField;
	@FXML private DatePicker jobDateField;
	@FXML private ObjectPicker<Driver> driverField;
	@FXML private TextField pricingField;
	@FXML private ObjectBrowser<JobRate> jobRateBrowser;

	private Long jobId;
	private Job job = new Job();

	private JobDao jobDao = Context.get().getJobDao();
	private JobBean jobBean = Context.get().getJobBean();

	public JobEdit(final Long jobId) {
		super("/fxml/JobEdit.fxml");
		this.jobId = jobId;
		initUI();
		loadData();
	}

	@Override
	public String getTitle() {
		return "Job Edit";
	}

	// other

	private void initUI()	{

		typeField.setButtonCell(new OptionalJobTypeListCell(true));
		typeField.setCellFactory(param -> new OptionalJobTypeListCell(false));
		typeField.getItems().setAll(Arrays.asList(
				Optional.of(JobType.REGULAR_JOB),
				Optional.of(JobType.CASH_JOB)
		));
		typeField.getSelectionModel().select(Optional.of(JobType.REGULAR_JOB));

		ObjectBrowserHelper.setupJobRateTable(jobRateBrowser);
		jobRateBrowser.setupDoubleClickEvent(jobRate -> handleEditRate(null));
		jobRateBrowser.addButton("Add", ObjectBrowser.NodeType.ADD, this::handleAddRate);
		jobRateBrowser.addButton("Edit", ObjectBrowser.NodeType.EDIT, this::handleEditRate);
		jobRateBrowser.addButton("Remove", ObjectBrowser.NodeType.REMOVE, this::handleRemoveRate);

	}

	private void loadData()	{
		if(jobId != null)	{
			job = jobBean.loadJob(jobId);
		}
		PickerHelper.setupDriverPicker(driverField);

		fillUI();
	}

	private void fillUI()	{
		idField.setText(Utils.toString(job.getId()));
		typeField.getSelectionModel().select(job.getType() == null
				? Optional.of(JobType.REGULAR_JOB)
				: Optional.of(job.getType())
		);
		jobDateField.setValue(job.getJobDate());
		driverField.setSelectedObject(job.getDriver());
		pricingField.setText(Utils.toString(job.getPricing()));

		fillRateList();
	}

	private void fillRateList()	{
		jobRateBrowser.getItems().setAll(job.getJobRates());
	}

	private void fillDto()	{
		job.setJobDate(jobDateField.getValue());
		job.setType(typeField.getSelectionModel().getSelectedItem().orElse(null));
		job.setDriver(driverField.getSelectedObject().orElse(null));
		job.setPricing(Double.valueOf(pricingField.getText()));
	}

	// handlers

	private void handleAddRate(ActionEvent event) {
		new JobRateEdit(null)
				.showAndWait()
				.map(jobRate -> {
					job.getJobRates().add(jobRate);
					fillRateList();
					return null;
				});
	}

	private void handleEditRate(ActionEvent event) {
		if(jobRateBrowser.isSelectionEmpty())	{
			return;
		}

		final JobRate selectedJobRate = jobRateBrowser.getSelectedItem();
		new JobRateEdit(selectedJobRate).showAndWait().map(jobRate -> {
			fillRateList();
			return null;
		});
	}

	private void handleRemoveRate(ActionEvent event) {
		if(jobRateBrowser.isSelectionEmpty())	{
			return;
		}
		final List<JobRate> jobRateList = jobRateBrowser.getSelectedItems();
		jobRateList.forEach(jobRate -> job.getJobRates().remove(jobRate));
		fillRateList();
	}

	@FXML
	private void handleApply(ActionEvent event) {
		fillDto();
		if(jobId == null)	{
			jobDao.insert(job);
		}else {
			jobDao.update(job);
		}
		UIContext.get().closeScreen();
	}

	@FXML
	private void handleClose(ActionEvent event) {
		UIContext.get().closeScreen();
	}

}
