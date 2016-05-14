package com.magenta.echo.driverpay.ui.screen;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.DriverBean;
import com.magenta.echo.driverpay.core.entity.DriverDto;
import com.magenta.echo.driverpay.ui.dialog.DriverEdit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    public DriverBrowser() {
        super("/fxml/DriverBrowser.fxml");
        initUI();
        loadData();
    }

    // other

    private void initUI()   {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        table.getSelectionModel().selectedItemProperty().addListener(param -> editDriverButton.setDisable(table.getSelectionModel().isEmpty()));
    }

    private void loadData() {
        table.getItems().setAll(driverBean.loadDriverList());
    }

    // handlers

    @FXML
    private void handleAddDriver(ActionEvent actionEvent) {
        Context.get().openDialogAndWait(new DriverEdit(null));
		loadData();
    }

    @FXML
    private void handleEditDriver(ActionEvent actionEvent) {
        if(table.getSelectionModel().isEmpty()) {
            return;
        }
        final Long selectedDriverId = table.getSelectionModel().getSelectedItem().getId();
        Context.get().openDialogAndWait(new DriverEdit(selectedDriverId));
		loadData();
    }
}
