package com.mdmd.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatUtil {

    private static final DateFormat dateFormat_yyMMdd = new SimpleDateFormat( "yyMMdd");
    private static final DateFormat dateFormat_yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat dateFormat_yyyyMMdd_int = new SimpleDateFormat("yyyyMMdd");

    private static final DateFormat dateFormat_yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat dateFormat_orderTime = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final String HHmmss_0 = " 00:00:00";

    public static Integer now_yyMMdd_intVal(){
        return Integer.valueOf(now_yyMMdd());
    }

    public static String now_yyMMdd(){
        Date date = new Date();
        return dateFormat_yyMMdd.format(date);

    }
    public static Integer now_yyyyMMdd_intVal(){
        return Integer.valueOf(dateFormat_yyyyMMdd_int.format(new Date()));
    }

    public static String today_yyyyMMddHHmmss(){
        Date date = new Date();
        return dateFormat_yyyyMMdd.format(date) + HHmmss_0;
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

    /**
     * 获取日期
     * @param index 0 为今天
     * @return
     */
    public static String getDayByCalendar(int index){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,index);
        Date time = calendar.getTime();
        return dateFormat_yyyyMMdd.format(time);
    }

}
