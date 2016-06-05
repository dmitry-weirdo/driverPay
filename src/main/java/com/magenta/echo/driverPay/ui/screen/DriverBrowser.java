package com.magenta.echo.driverpay.ui.screen;

import com.evgenltd.kwick.controls.extensions.tableview.TableViewExtension;
import com.evgenltd.kwick.ui.Screen;
import com.evgenltd.kwick.ui.UIContext;
import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.DriverBean;
import com.magenta.echo.driverpay.core.entity.dto.DriverDto;
import com.magenta.echo.driverpay.ui.screen.driverEdit.DriverEdit;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 13-05-2016 19:10
 */
public class DriverBrowser extends Screen {

	private DriverBean driverBean = Context.get().getDriverBean();

    @FXML private Button editDriverButton;

    @FXML private TableView<DriverDto> table;
    @FXML private TableColumn<DriverDto,Long> idColumn;
    @FXML private TableColumn<DriverDto,String> nameColumn;
	@FXML private TableColumn<DriverDto,Double> calculatedSalaryColumn;
	@FXML private TableColumn<DriverDto,Double> currentDepositColumn;

    public DriverBrowser() {
        super("/fxml/DriverBrowser.fxml");
        initUI();
        loadData();
    }

    @Override
    public String getTitle() {
        return "Driver browser";
    }

	@Override
	public void refresh() {
		loadData();
	}

	// other

    private void initUI()   {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		calculatedSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("calculatedSalary"));
		currentDepositColumn.setCellValueFactory(new PropertyValueFactory<>("currentDeposit"));

        table.getSelectionModel().selectedItemProperty().addListener(this::handleTableSelectedItemChanged);
        TableViewExtension.setupDoubleClickEvent(table, param -> handleEditDriver(null));
    }

    private void loadData() {
		final List<DriverDto> driverList = driverBean.loadDriverList();
		table.getItems().setAll(driverList);
    }

    // handlers

    @FXML
    private void handleAddDriver(ActionEvent actionEvent) {
		UIContext.get().openScreen(new DriverEdit(null));
		loadData();
    }

    @FXML
    private void handleEditDriver(ActionEvent actionEvent) {
        if(table.getSelectionModel().isEmpty()) {
            return;
        }
        final Long selectedDriverId = table.getSelectionModel().getSelectedItem().getId();
		UIContext.get().openScreen(new DriverEdit(selectedDriverId));
    }

	private void handleTableSelectedItemChanged(Observable observable) {
		final boolean isSelectionEmpty = table.getSelectionModel().isEmpty();
		editDriverButton.setDisable(isSelectionEmpty);
	}
}
