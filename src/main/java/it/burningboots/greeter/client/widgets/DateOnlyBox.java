package it.burningboots.greeter.client.widgets;

import it.burningboots.greeter.shared.AppConstants;

import java.util.Date;

import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

public class DateOnlyBox extends DateBox {

	public DateOnlyBox() {
		super();
	}
	
	public DateOnlyBox(DatePicker picker, Date dt, Format format) {
		super(picker, moveToNoon(dt), format);
	}
	
	@Override
	public void setValue(Date date) {
		super.setValue(moveToNoon(date));
	}
	
	private static Date moveToNoon(Date dt) {
		if (dt != null) {
			Long longDt = dt.getTime();
			longDt += AppConstants.HOUR*12;
			return new Date(longDt);
		} else return null;
	}
	
}
