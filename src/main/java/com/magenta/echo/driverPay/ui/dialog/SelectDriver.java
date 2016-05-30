package com.magenta.echo.driverpay.ui.dialog;

import com.evgenltd.kwickui.core.DialogScreen;
import com.evgenltd.kwickui.extensions.listview.ListViewExtension;
import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.DriverBean;
import com.magenta.echo.driverpay.core.entity.Driver;
import com.magenta.echo.driverpay.ui.cellfactory.DriverListCell;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 02:58
 */
public class SelectDriver extends DialogScreen<Driver> {

	private DriverBean driverBean = Context.get().getDriverBean();

	@FXML private ListView<Driver> driverList;

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
		driverList.setCellFactory(param -> new DriverListCell());
		ListViewExtension.setupDoubleClickEvent(driverList,driver -> {
			getDialog().setResult(driver);
			getDialog().close();
		});
		getDialog().setTitle("Select Driver");
		getDialog().setHeaderText(null);
		getDialog().getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
	}

	private void loadData()	{
		driverList.getItems().setAll(driverBean.loadDriverList(""));
	}

	// handlers

	@Override
	protected Driver resultConverter(ButtonType buttonType) {
		if(ButtonType.OK.equals(buttonType))	{
			final Driver selectedDriver = driverList.getSelectionModel().getSelectedItem();
			if(selectedDriver != null)	{
				return selectedDriver;
			}
		}
		return null;
	}
}
