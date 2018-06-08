package com.datetools;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

		//获取一个月的最后一天
		public Date getMonthLastDay(Date date) {
			Calendar cal =Calendar.getInstance();
			cal.setTime(date);
			int days=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.DAY_OF_MONTH, days);
			Date newDate=cal.getTime();
			return newDate;
		}
		//获取一个月的第一天
		public Date getMonthFirstDay(Date date) {
			Calendar cal =Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			Date newDate=cal.getTime();
			return newDate;
		}
		//获取另个时间之间有多少个月份
		public int getMonthsBetweenTwoDate(Date startDate, Date endDate) {
	        Calendar startCalendar =Calendar.getInstance();
	        startCalendar.setTime(startDate);
	        Calendar endCalendar =Calendar.getInstance();
	        endCalendar.setTime(endDate);
	        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
	        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH)+1;
	        return diffMonth;
	    }
}
