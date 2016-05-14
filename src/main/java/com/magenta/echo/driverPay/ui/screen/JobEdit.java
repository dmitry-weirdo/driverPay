package com.magenta.echo.driverpay.ui.screen;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.JobBean;
import com.magenta.echo.driverpay.core.entity.DriverDto;
import com.magenta.echo.driverpay.core.entity.JobDto;
import com.magenta.echo.driverpay.core.entity.JobRateDto;
import com.magenta.echo.driverpay.ui.dialog.JobRateEdit;
import com.magenta.echo.driverpay.ui.dialog.SelectDriver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 13-05-2016 01:35
 */
public class JobEdit extends Screen {

	private JobBean jobBean = Context.get().getJobBean();

	@FXML private Label idField;
	@FXML private DatePicker jobDateField;
	@FXML private Button driverField;

	@FXML private Button editRateButton;
	@FXML private Button removeRateButton;

	@FXML private TableView<JobRateDto> rateTable;
	@FXML private TableColumn<JobRateDto,Long> rateIdColumn;
	@FXML private TableColumn<JobRateDto,Double> rateNetColumn;
	@FXML private TableColumn<JobRateDto,Double> rateVatColumn;
	@FXML private TableColumn<JobRateDto,Double> rateTotalColumn;
	@FXML private TableColumn<JobRateDto,String> rateNominalCodeColumn;
	@FXML private TableColumn<JobRateDto,String> rateTaxCodeColumn;

	private Long jobId;
	private JobDto jobDto;
	private List<JobRateDto> jobRateDtoList;
	private Long selectedDriverId;

	public JobEdit(final Long jobId) {
		super("/fxml/JobEdit.fxml");
		this.jobId = jobId;
		initUI();
		loadData();
	}

	// other

	private void initUI()	{
		rateIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		rateNetColumn.setCellValueFactory(new PropertyValueFactory<>("net"));
		rateVatColumn.setCellValueFactory(new PropertyValueFactory<>("vat"));
		rateTotalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
		rateNominalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("nominalCode"));
		rateTaxCodeColumn.setCellValueFactory(new PropertyValueFactory<>("taxCode"));

		rateTable.getSelectionModel().selectedItemProperty().addListener(param -> {
			final boolean isEmpty = rateTable.getSelectionModel().isEmpty();
			editRateButton.setDisable(isEmpty);
			removeRateButton.setDisable(isEmpty);
		});
	}

	private void loadData()	{
		if(jobId == null)	{
			jobDto = new JobDto();
			jobRateDtoList = new ArrayList<>();
			return;
		}

		jobDto = jobBean.loadJob(jobId);
		jobRateDtoList = jobBean.loadJobRateList(jobId);

		fillUI();
	}

	private void fillUI()	{
		idField.setText(String.valueOf(jobDto.getId()));
		jobDateField.setValue(jobDto.getJobDate());
		driverField.setText(jobDto.getDriverValue());
		selectedDriverId = jobDto.getDriverId();
		rateTable.getItems().setAll(jobRateDtoList);
	}

	private void fillDto()	{
		jobDto.setJobDate(jobDateField.getValue());
		jobDto.setDriverId(selectedDriverId);
		jobRateDtoList = rateTable.getItems();
	}

	// handlers

	@FXML
	private void handleAddRate(ActionEvent event) {
		final Optional<JobRateDto> resultHolder = Context.get().openDialogAndWait(new JobRateEdit(null));
		if(resultHolder.isPresent())	{
			rateTable.getItems().add(resultHolder.get());
		}
	}

	@FXML
	private void handleEditRate(ActionEvent event) {
		if(rateTable.getSelectionModel().isEmpty())	{
			return;
		}
		final JobRateDto jobRateDto = rateTable.getSelectionModel().getSelectedItem();

		final Optional<JobRateDto> resultHolder = Context.get().openDialogAndWait(new JobRateEdit(jobRateDto));
		if(resultHolder.isPresent())	{
			rateTable.getItems().remove(jobRateDto);
			rateTable.getItems().add(resultHolder.get());
		}
	}

	@FXML
	private void handleRemoveRate(ActionEvent event) {
		if(rateTable.getSelectionModel().isEmpty())	{
			return;
		}
		final JobRateDto jobRateDto = rateTable.getSelectionModel().getSelectedItem();
		rateTable.getItems().remove(jobRateDto);
	}

	@FXML
	private void handleApply(ActionEvent event) {
		fillDto();
		jobBean.updateJob(jobDto, jobRateDtoList);
		Context.get().openScreen(new JobBrowser());
	}

	@FXML
	private void handleClose(ActionEvent event) {
		Context.get().openScreen(new JobBrowser());
	}

	@FXML
	private void handleSelectDriver(ActionEvent event) {
		final Optional<DriverDto> result = Context.get().openDialogAndWait(new SelectDriver());
		if(!result.isPresent())	{
			return;
		}
		driverField.setText(result.get().getName());
		selectedDriverId = result.get().getId();
	}
}
