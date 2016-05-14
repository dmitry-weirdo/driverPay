package com.magenta.echo.driverpay.ui.dialog;

import com.magenta.echo.driverpay.ui.screen.Screen;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.util.Optional;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 13-05-2016 02:08
 */
public abstract class DialogExt<T> extends Screen{

	private Dialog<T> dialog;

	public DialogExt(String path) {
		super(path);
		dialog = new Dialog<>();
		dialog.setHeaderText(null);
		dialog.getDialogPane().setContent(getRoot());
		dialog.setResultConverter(this::resultConverter);
	}

	public DialogExt() {
		dialog = new Dialog<>();
		dialog.setHeaderText(null);
		dialog.getDialogPane().setContent(getRoot());
		dialog.setResultConverter(this::resultConverter);
	}

	public Dialog getDialog() {
		return dialog;
	}

	protected abstract T resultConverter(final ButtonType buttonType);

	public Optional<T> showAndWait()	{
		return dialog.showAndWait();
	}
}
