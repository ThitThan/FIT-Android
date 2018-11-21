package com.kmutt.sit.apps.fit.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Freshy on 7/13/2017 AD.
 */

public class DateTimeUtil {
//    public static boolean checkIfItsTheSameDay(long dateMillis, Date date) {
//        Date date2 = new Date(dateMillis);
//        return date.compareTo(date2);
//    }
    public static Date getTodaysDateAt0AM() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
