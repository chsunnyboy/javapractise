package com.thread;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Test {

	public static int getMonths(Date startDate, Date endDate) {
        Calendar startCalendar =Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar =Calendar.getInstance();
        endCalendar.setTime(endDate);
        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH)+1;
        return diffMonth;
    }
	
	public static void main(String[] args) {
		int a=getMonths(Calendar.getInstance().getTime(),Calendar.getInstance().getTime());
		System.out.println(a);
	}

}
