package com.magenta.echo.driverpay.ui.dialog;

import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Project: Driver Pay
 * Author:  Evgeniy
 * Created: 14-05-2016 15:06
 */
public class ExceptionView extends DialogExt<Void> {

	private TextArea stackTraceArea;

	private Throwable throwable;

	public ExceptionView(final Throwable throwable) {
		super();
		this.throwable = throwable;
		initUI();
		loadData();
	}

	// other

	private void initUI()	{
		getDialog().setTitle("Exception");
		getDialog().setHeaderText(throwable.getMessage());
		stackTraceArea = new TextArea();
		stackTraceArea.setPrefHeight(300);
		stackTraceArea.setEditable(false);
		getDialog().getDialogPane().setExpandableContent(stackTraceArea);
		getDialog().getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
		getDialog().getDialogPane().setPrefWidth(600);
	}

	private void loadData()	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		stackTraceArea.setText(sw.toString());
	}

	// handlers

	@Override
	protected Void resultConverter(ButtonType buttonType) {
		return null;
	}
}
