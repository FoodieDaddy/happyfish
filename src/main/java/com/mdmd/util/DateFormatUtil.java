package com.mdmd.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatUtil {

    private static final String DATEFORMAT__yyMMdd = "yyMMdd";
    private static final DateFormat dateFormat_HHmmss = new SimpleDateFormat(DATEFORMAT__yyMMdd);

    private static final String DATEFORMAT__yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    private static final DateFormat dateFormat_yyyyMMddHHmmss = new SimpleDateFormat(DATEFORMAT__yyyyMMddHHmmss);

    private static final String DATEFORMAT_ORDERTIME = "yyyyMMddHHmmss";
    private static final DateFormat dateFormat_orderTime = new SimpleDateFormat(DATEFORMAT_ORDERTIME);


    public static Integer now_yyMMdd_intVal(){
        return Integer.valueOf(now_yyMMdd());
    }

    public static String now_yyMMdd(){
        Date date = new Date();
        return dateFormat_HHmmss.format(date);

    }

    public static String now_yyyyMMddHHmmss(){
        Date date = new Date();
        return dateFormat_yyyyMMddHHmmss.format(date);
    }
    public static  Date parse_yyyyMMddHHmmss(String time) throws ParseException {
        return dateFormat_yyyyMMddHHmmss.parse(time);
    }

    public static int now_HH(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static String now_orderTime(){
        Date date = new Date();
        return dateFormat_orderTime.format(date);
    }
}
