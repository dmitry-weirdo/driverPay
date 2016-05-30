package com.magenta.echo.driverpay.ui.cellfactory;

import com.magenta.echo.driverpay.core.enums.ScheduleType;
import javafx.scene.control.ListCell;

/**
 * Project: driverPay-prototype
 * Author:  Lebedev
 * Created: 26-05-2016 18:37
 */
public class ScheduleTypeListCell extends ListCell<ScheduleType> {
    @Override
    protected void updateItem(final ScheduleType item, final boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null)   {
            setText("");
        }else {
            setText(item.getLabel());
        }
    }
}
