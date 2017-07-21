package com.example.djordje.seeds;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Djordje on 03-Jul-17.
 */

public class Helper {

    public static String formatDate(Date value, String format) {
        if(format == null)
            format = "dd-MM-YYYY HH:mm:ss";

        DateFormat df = new SimpleDateFormat(format);
        return df.format(value);
    }

    public static String formatCalendar(Calendar value, String format) {
        if(format == null)
            format = "dd-MM-YYYY HH:mm:ss";

        DateFormat df = new SimpleDateFormat(format);
        return df.format(value.getTime());
    }
}
