package com.hospital.uitls;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class DateUtilsTest {

    @Test
    public void dateUtilsUsesCalendarYearAtWeekYearBoundary() {
        TimeZone original = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        try {
            assertEquals("2018-12-31", DateUtils.date2String(date(2018, Calendar.DECEMBER, 31)));
        } finally {
            TimeZone.setDefault(original);
        }
    }

    @Test
    public void pdfUtilsUsesCalendarYearAtWeekYearBoundary() throws Exception {
        TimeZone original = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        try {
            Method date2String = PDFUtils.class.getDeclaredMethod("date2String", Date.class);
            date2String.setAccessible(true);

            assertEquals("2018年12月31日", date2String.invoke(null, date(2018, Calendar.DECEMBER, 31)));
        } finally {
            TimeZone.setDefault(original);
        }
    }

    private Date date(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        calendar.set(year, month, day);
        return calendar.getTime();
    }
}
