package com.magenta.echo.driverpay.ui.dialog;

import com.evgenltd.kwickui.controls.objectpicker.SimpleObject;
import com.evgenltd.kwickui.controls.objectpicker.SimpleObjectListCell;
import com.evgenltd.kwickui.core.DialogScreen;
import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.DriverBean;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 02:58
 */
public class SelectDriver extends DialogScreen<SimpleObject> {

	private DriverBean driverBean = Context.get().getDriverBean();

	@FXML private ListView<SimpleObject> driverList;

	public SelectDriver() {
		super("/fxml/SelectDriver.fxml");
		initUI();
		loadData();
	}

	@Override
	protected String getTitle() {
		return "Select Driver";
	}

	// other

	private void initUI()	{
		driverList.setCellFactory(param -> new SimpleObjectListCell());
		getDialog().setTitle("Select Driver");
		getDialog().setHeaderText(null);
		getDialog().getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
	}

	private void loadData()	{
		driverList.getItems().setAll(driverBean.loadDriverList(""));
	}

	// handlers

	@Override
	protected SimpleObject resultConverter(ButtonType buttonType) {
		if(ButtonType.OK.equals(buttonType))	{
			final SimpleObject selectedDriverDto = driverList.getSelectionModel().getSelectedItem();
			if(selectedDriverDto != null)	{
				return selectedDriverDto;
			}
		}
		return null;
	}
}
