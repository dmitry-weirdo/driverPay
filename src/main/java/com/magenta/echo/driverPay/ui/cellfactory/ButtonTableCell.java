package com.magenta.echo.driverpay.ui.cellfactory;

import com.magenta.echo.driverpay.core.entity.ExportHistory;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.function.Consumer;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 05-06-2016 23:45
 */
public class ButtonTableCell extends TableCell<ExportHistory, ExportHistory> {

	private final Image icon;
	private final Consumer<ExportHistory> action;

	public ButtonTableCell(Image icon, Consumer<ExportHistory> action) {
		this.icon = icon;
		this.action = action;
	}

	@Override
	protected void updateItem(ExportHistory item, boolean empty) {
		super.updateItem(item, empty);
		if(empty || item == null) {
			setText("");
			setGraphic(null);
		}else {
			final ImageView imageView = new ImageView(icon);
			final Button button = new Button();
			button.setGraphic(imageView);
			button.setOnAction(event -> action.accept(item));
			setGraphic(button);
		}
	}
}
