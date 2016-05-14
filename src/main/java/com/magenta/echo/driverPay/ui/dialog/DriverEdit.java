package com.magenta.echo.driverpay.ui.dialog;

import com.magenta.echo.driverpay.core.Context;
import com.magenta.echo.driverpay.core.bean.DriverBean;
import com.magenta.echo.driverpay.core.entity.DriverDto;
import com.magenta.echo.driverpay.ui.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Project: Driver Pay
 * Author:  Lebedev
 * Created: 13-05-2016 19:10
 */
public class DriverEdit extends DialogExt<DriverDto> {

    private DriverBean driverBean = Context.get().getDriverBean();

    @FXML private Label id;
    @FXML private TextField name;

    private Long driverId;
    private DriverDto driverDto;

    public DriverEdit(final Long driverId) {
        super("/fxml/DriverEdit.fxml");
        this.driverId = driverId;
        initUI();
        loadData();
    }

    // other

    private void initUI()   {
        getDialog().setTitle("Driver Edit");
        getDialog().setHeaderText(null);
        getDialog().getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
    }

    private void loadData() {
        if(driverId == null)    {
            driverDto = new DriverDto();
        }else {
            driverDto = driverBean.loadDriver(driverId);
        }
        fillUI();

    }

    private void fillUI()   {
        id.setText(Utils.toString(driverDto.getId()));
        name.setText(Utils.toString(driverDto.getName()));
    }

    private void fillDto()  {
        driverDto.setName(name.getText());
    }

    // handlers

    @Override
    protected DriverDto resultConverter(final ButtonType buttonType) {
        if(ButtonType.APPLY.equals(buttonType)) {
            fillDto();
            driverBean.updateDriver(driverDto);
            return driverDto;
        }
        return null;
    }
}
