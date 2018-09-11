package com.du.frameapplication.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by dubaojiao on 2017/12/5.
 */
public class TimeUtil {
    private static LocalDate nowDate;
    private static LocalDateTime nowTime;


    public static String getNowDate(){
        return nowDate.toString();
    }

    public static String getNowTime(){
        return nowTime.toString();
    }

    public static String getCompleteDate(){
        StringBuffer stringBuffer = new StringBuffer(nowDate.toString());
        stringBuffer.append(" ").append(nowTime.toString());
        return stringBuffer.toString();
    }

    public static Date stringToDate(String format, String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.parse(date);
    }

    public static String getDateTimeFromTimeStamp13(String str, String format) {
        if (str == null || str.length() != 13) {
            return "";
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            long ts   = Long.parseLong(str);
            Date date = new Date(ts);
            return simpleDateFormat.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /*
     * 格式化输出时间戳（yyyy-MM-dd HH:mm:ss）
     */
    public static String getTimeFromTimeStamp13(String str) {
        return getDateTimeFromTimeStamp13(str, "yyyy-MM-dd HH:mm:ss");
    }


    public static Integer getWeek(Integer day){
        if(day<8){
            return 1;
        }else if(day<15){
            return 2;
        } else  if(day < 22){
            return 3;
        }else if(day < 29){
            return 4;
        }else {
            return 5;
        }
    }

    public static void main(String[] arg){
        System.out.println(getWeek(3));
    }
}
