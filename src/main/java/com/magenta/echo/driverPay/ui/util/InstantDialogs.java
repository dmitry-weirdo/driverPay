package com.magenta.echo.driverpay.ui.util;

import com.magenta.echo.driverpay.core.entity.ExportHistory;
import com.magenta.echo.driverpay.core.validation.ValidationUtils;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import org.jetbrains.annotations.NotNull;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Project: driverPay-prototype
 * Author:  Evgeniy
 * Created: 05-06-2016 21:47
 */
public class InstantDialogs {

	public static <T> Dialog<Void> makeConstraintViolationDialog(final @NotNull Set<ConstraintViolation<T>> errors)	{

		final StringBuilder sb = new StringBuilder();
		sb.append("Errors count: ").append(errors.size()).append("\n");
		ValidationUtils
				.getMessagesByProperty(errors)
				.forEach(
						(key,value) -> sb
								.append(key)
								.append(":")
								.append(value)
								.append("\n")
				);

		final TextArea textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setText(sb.toString());
		textArea.setWrapText(true);

		final Dialog<Void> dialog = new Dialog<>();
		dialog.setTitle("Constraint Violation Exception");
		dialog.getDialogPane().setContent(textArea);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

		return dialog;

	}

	public static Dialog<Void> makeFileViewerDialog(@NotNull final ExportHistory exportHistory)	{

		final TextArea textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setText(exportHistory.getFileContent().getContent());
		textArea.setWrapText(true);

		final Dialog<Void> dialog = new Dialog<>();
		dialog.setTitle(exportHistory.getType().getLabel());
		dialog.getDialogPane().setContent(textArea);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

		return dialog;

	}

}
