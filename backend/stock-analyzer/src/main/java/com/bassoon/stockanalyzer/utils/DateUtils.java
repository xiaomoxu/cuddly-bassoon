package com.bassoon.stockanalyzer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static int dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        int[] weekDays = {0, 1, 2, 3, 4, 5, 6};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static void main(String argz[]) {
//        System.out.println(Math.round((double) ((3.0 - 5.1) / 5.0) * 100) / 100.0);
//        System.out.println(compareDataWith20070119("2007-01-20"));
        System.out.println(DateUtils.dateToWeek("2007-02-09"));
    }

    public static int compareDataWith20070119(String compared) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date comparedDate = simpleDateFormat.parse(compared);
            Date fixedDate = simpleDateFormat.parse("2007-01-19");
            return org.apache.commons.lang3.time.DateUtils.truncatedCompareTo(comparedDate, fixedDate, Calendar.DATE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
