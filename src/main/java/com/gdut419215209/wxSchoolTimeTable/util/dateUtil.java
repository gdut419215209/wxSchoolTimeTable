package com.example.demo.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class dateUtil {
    public static String getDateYear(){
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(date);
        String[] split = format.split("-");
        return split[0];
    }

    public static String getDateMonth(){
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(date);
        String[] split = format.split("-");
        return split[1];
    }

}
