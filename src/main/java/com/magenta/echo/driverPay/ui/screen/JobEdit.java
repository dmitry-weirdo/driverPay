package com.magenta.echo.driverpay.ui.screen;

import com.evgenltd.kwickui.controls.objectpicker.ObjectPicker;
import com.evgenltd.kwickui.controls.objectpicker.SimpleObject;
import com.evgenltd.kwickui.core.Screen;
import com.evgenltd.kwickui.core.UIContext;
import com.evgenltd.kwickui.extensions.tableview.TableViewExtension;
import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.JobBean;
import com.magenta.echo.driverpay.core.entity.JobDto;
import com.magenta.echo.driverpay.core.entity.JobRateDto;
import com.magenta.echo.driverpay.core.enums.JobType;
import com.magenta.echo.driverpay.ui.PickerHelper;
import com.magenta.echo.driverpay.ui.Utils;
import com.magenta.echo.driverpay.ui.cellfactory.OptionalJobTypeListCell;
import com.magenta.echo.driverpay.ui.dialog.JobRateEdit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.*;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 13-05-2016 01:35
 */
public class JobEdit extends Screen {


	private JobBean jobBean = Context.get().getJobBean();

	@FXML private Label idField;
	@FXML private ComboBox<Optional<JobType>> typeField;
	@FXML private DatePicker jobDateField;
	@FXML private ObjectPicker<SimpleObject> driverField;

	@FXML private Button editRateButton;
	@FXML private Button removeRateButton;

	@FXML private TableView<JobRateDto> rateTable;
	@FXML private TableColumn<JobRateDto,Long> rateIdColumn;
	@FXML private TableColumn<JobRateDto,Double> rateNetColumn;
	@FXML private TableColumn<JobRateDto,String> rateNominalCodeColumn;
	@FXML private TableColumn<JobRateDto,String> rateTaxCodeColumn;
	@FXML private HBox totalPane;

	private Long jobId;
	private JobDto jobDto;
	private List<JobRateDto> jobRateDtoList;

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

		rateIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		rateNetColumn.setCellValueFactory(new PropertyValueFactory<>("net"));
		rateNominalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("nominalCode"));
		rateTaxCodeColumn.setCellValueFactory(new PropertyValueFactory<>("taxCode"));

		rateTable.getSelectionModel().selectedItemProperty().addListener(param -> {
			final boolean isNotSelected = rateTable.getSelectionModel().isEmpty();
			final boolean isNonSingleSelection = rateTable.getSelectionModel().getSelectedItems().size() != 1;
			editRateButton.setDisable(isNonSingleSelection);
			removeRateButton.setDisable(isNotSelected);
		});
		TableViewExtension.setupDoubleClickEvent(rateTable, this::handleJobRateChanging);
		TableViewExtension.setupTotalSupport(rateTable, totalPane, Collections.singletonList(rateNetColumn));
	}

	private void loadData()	{
		if(jobId == null)	{
			jobDto = new JobDto();
			jobRateDtoList = new ArrayList<>();
		}else {
			jobDto = jobBean.loadJob(jobId);
			jobRateDtoList = jobBean.loadJobRateList(jobId);
		}
		PickerHelper.setupDriverPicker(driverField);

		fillUI();
	}

	private void fillUI()	{
		idField.setText(Utils.toString(jobDto.getId()));
		typeField.getSelectionModel().select(Optional.ofNullable(jobDto.getType()));
		jobDateField.setValue(jobDto.getJobDate());
		if(jobDto.getDriverId() != null) {
			driverField.setSelectedObject(new SimpleObject(
					jobDto.getDriverId(),
					jobDto.getDriverValue()
			));
		}
		rateTable.getItems().setAll(jobRateDtoList);
	}

	private void fillDto()	{
		jobDto.setJobDate(jobDateField.getValue());
		jobDto.setType(typeField.getSelectionModel().getSelectedItem().orElse(null));
		if(driverField.getSelectedObject() == null)	{
			jobDto.setDriverId(null);
		}else {
			jobDto.setDriverId(
					driverField
							.getSelectedObject()
							.map(SimpleObject::getId)
							.orElse(null)
			);
		}
		jobRateDtoList = rateTable.getItems();
	}

	// handlers

	private void handleJobRateChanging(final JobRateDto jobRate)	{
		new JobRateEdit(jobRate)
				.showAndWait()
				.map(newJobRate -> {
					rateTable.getItems().remove(jobRate);
					rateTable.getItems().add(newJobRate);
					return null;
				});
	}

	@FXML
	private void handleAddRate(ActionEvent event) {
		handleJobRateChanging(null);
	}

	@FXML
	private void handleEditRate(ActionEvent event) {
		if(rateTable.getSelectionModel().isEmpty())	{
			return;
		}
		final JobRateDto selectedJobRate = rateTable.getSelectionModel().getSelectedItem();

		handleJobRateChanging(selectedJobRate);
	}

	@FXML
	private void handleRemoveRate(ActionEvent event) {
		if(rateTable.getSelectionModel().isEmpty())	{
			return;
		}
		final List<JobRateDto> jobRateList = rateTable.getSelectionModel().getSelectedItems();
		jobRateList.forEach(jobRate -> rateTable.getItems().remove(jobRate));
	}

	@FXML
	private void handleApply(ActionEvent event) {
		fillDto();
		jobBean.updateJob(jobDto, jobRateDtoList);
		handleClose(event);
	}

	@FXML
	private void handleClose(ActionEvent event) {
		UIContext.get().closeScreen();
	}

}
