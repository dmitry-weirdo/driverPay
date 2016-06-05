package com.magenta.echo.driverpay.ui.dialog;

import com.evgenltd.kwick.ui.DialogScreen;
import com.magenta.echo.driverpay.core.entity.JobRate;
import com.magenta.echo.driverpay.ui.util.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 13-05-2016 01:56
 */
public class JobRateEdit extends DialogScreen<JobRate> {

	@FXML private Label id;
	@FXML private TextField net;
	@FXML private TextField nominalCode;
	@FXML private TextField taxCode;

	private JobRate jobRate;

	public JobRateEdit(final JobRate jobRate) {
		super("/fxml/JobRateEdit.fxml");

		this.jobRate = jobRate == null ? new JobRate() : jobRate;

		initUI();
		fillUI();
	}

	@Override
	protected String getTitle() {
		return "Job Rate Edit";
	}

	// other

	private void initUI()	{
		getDialog().setTitle("Job Rate Edit");
		getDialog().setHeaderText(null);
		getDialog().getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
	}

	@Override
	protected JobRate resultConverter(final ButtonType buttonType)	{
		if(ButtonType.OK.equals(buttonType))	{
			fillDto();
			return jobRate;
		}else {
			return null;
		}
	}

	private void fillUI()	{
		id.setText(Utils.toString(jobRate.getId()));
		net.setText(Utils.toString(jobRate.getNet()));
		nominalCode.setText(jobRate.getNominalCode());
		taxCode.setText(jobRate.getTaxCode());
	}

	private void fillDto()	{
		jobRate.setNet(Double.parseDouble(net.getText()));
		jobRate.setNominalCode(nominalCode.getText());
		jobRate.setTaxCode(taxCode.getText());
	}
}
