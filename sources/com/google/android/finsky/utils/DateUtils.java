package com.google.android.finsky.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class DateUtils {
    private static final DateFormat ISO8601_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat DISPLAY_DATE_FORMAT = DateFormat.getDateInstance(1);

    public static synchronized String formatIso8601Date(String iso8601Date) throws ParseException {
        String displayDate;
        try {
            displayDate = formatDisplayDate(ISO8601_DATE_FORMAT.parse(iso8601Date));
        } catch (ParseException e) {
            if (!Pattern.matches("^\\d\\d\\d\\d$", iso8601Date)) {
                throw e;
            }
            displayDate = iso8601Date;
        }
        return displayDate;
    }

    public static synchronized String formatDisplayDate(Date date) {
        return DISPLAY_DATE_FORMAT.format(date);
    }
}
