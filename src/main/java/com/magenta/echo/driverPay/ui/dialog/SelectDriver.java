package com.magenta.echo.driverpay.ui.dialog;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.DriverBean;
import com.magenta.echo.driverpay.core.entity.DriverDto;
import com.magenta.echo.driverpay.ui.cellfactory.DriverListCell;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 02:58
 */
public class SelectDriver extends DialogExt<DriverDto> {

	private DriverBean driverBean = Context.get().getDriverBean();

	@FXML private ListView<DriverDto> driverList;

	public SelectDriver() {
		super("/fxml/SelectDriver.fxml");
		initUI();
		loadData();
	}

	// other

	private void initUI()	{
		driverList.setCellFactory(param -> new DriverListCell());
		getDialog().setTitle("Select Driver");
		getDialog().setHeaderText(null);
		getDialog().getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
	}

	private void loadData()	{
		driverList.getItems().setAll(driverBean.loadDriverList());
	}

	// handlers

	@Override
	protected DriverDto resultConverter(ButtonType buttonType) {
		if(ButtonType.APPLY.equals(buttonType))	{
			final DriverDto selectedDriverDto = driverList.getSelectionModel().getSelectedItem();
			if(selectedDriverDto != null)	{
				return selectedDriverDto;
			}
		}
		return null;
	}
}
