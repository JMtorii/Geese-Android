package com.teamawesome.geese.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by MichaelQ on 2016-03-03.
 */
public class DateUtil {

    /** Converts an integer array from the server into a date string */
    public static String getCreatedDateFromDateArray(List<Integer> dateArray) {
        if (dateArray == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, dateArray.get(0));
        cal.set(Calendar.MONTH, dateArray.get(1) - 1);
        cal.set(Calendar.DAY_OF_MONTH, dateArray.get(2));
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
        String date = format.format(Date.parse(cal.getTime().toString()));
        return date;
    }

}
