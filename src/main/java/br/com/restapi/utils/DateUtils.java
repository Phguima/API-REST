package br.com.restapi.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static String getData(Integer d){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, d);
		return getDataFormat(cal.getTime());
	}
	
	public static String getDataFormat(Date date) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(date);	
	}
}