/**
 * 
 */
package com.aartitest.selenium.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Aarti Arya
 *
 */
public class DateTimeStamp {

	public String findDateAndTimeStamp() {
		String date = "dd-MM-yyyy_HH:mm:ss";
		
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(date);
		return simpleDateFormat.format(calendar.getTime());
	}
	
	public String findDateTimeInFormat(String format) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(calendar.getTime());
	}
	
	public String findNextDaysDateTimeInFormat(String format) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		calendar.add(Calendar.DATE, 1);
		return simpleDateFormat.format(calendar.getTime());
	}	
		
		public String findnext2DaysDateTimeInFormat(String format) {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			calendar.add(Calendar.DATE, 2);
			return simpleDateFormat.format(calendar.getTime());
		}
}
