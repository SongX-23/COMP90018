package com.parse.unimelb.notification;

import java.util.Date;
import java.util.Random;

/**
 * Created by JOHANESG1508 on 10/13/2015.
 */
public class Converter {
    public static Date genDate(int fromDaysAgo) {
        Random r =new Random();
        long unixtime=(long) (1293861599+r.nextDouble()*60*60*24*fromDaysAgo);
        return new Date(unixtime);
    }

    public static Date longStrToDate(String timeStr) {
        return new java.util.Date(1000*Long.parseLong(timeStr));
    }

    public static String longStrToDateStr(String timeStr) {
        Date created= new java.util.Date(1000*Long.parseLong(timeStr));
        return new String(created.toString());
    }

}
