package com.magenta.echo.driverpay.ui.dialog;

import com.magenta.echo.driverpay.core.entity.JobRateDto;
import com.magenta.echo.driverpay.ui.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 13-05-2016 01:56
 */
public class JobRateEdit extends DialogExt<JobRateDto>{

	@FXML private Label id;
	@FXML private TextField net;
	@FXML private TextField vat;
	@FXML private TextField total;
	@FXML private TextField nominalCode;
	@FXML private TextField taxCode;

	private JobRateDto jobRateDto;

	public JobRateEdit(final JobRateDto jobRateDto) {
		super("/fxml/JobRateEdit.fxml");

		this.jobRateDto = jobRateDto == null ? new JobRateDto() : jobRateDto;

		initUI();
		fillUI();
	}

	// other

	private void initUI()	{
		getDialog().setTitle("Job Rate Edit");
		getDialog().setHeaderText(null);
		getDialog().getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
	}

	@Override
	protected JobRateDto resultConverter(final ButtonType buttonType)	{
		if(ButtonType.OK.equals(buttonType))	{
			fillDto();
			return jobRateDto;
		}else {
			return null;
		}
	}

	private void fillUI()	{
		id.setText(Utils.toString(jobRateDto.getId()));
		net.setText(Utils.toString(jobRateDto.getNet()));
		vat.setText(Utils.toString(jobRateDto.getVat()));
		total.setText(Utils.toString(jobRateDto.getTotal()));
		nominalCode.setText(jobRateDto.getNominalCode());
		taxCode.setText(jobRateDto.getTaxCode());
	}

	private void fillDto()	{
		jobRateDto.setNet(Double.parseDouble(net.getText()));
		jobRateDto.setVat(Double.parseDouble(vat.getText()));
		jobRateDto.setTotal(Double.parseDouble(total.getText()));
		jobRateDto.setNominalCode(nominalCode.getText());
		jobRateDto.setTaxCode(taxCode.getText());
	}
}
