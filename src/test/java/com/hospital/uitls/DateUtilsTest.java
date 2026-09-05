package com.hospital.uitls;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class DateUtilsTest {

    @Test
    public void date2StringUsesCalendarYearAtWeekYearBoundary() {
        Date date = date(2018, Calendar.DECEMBER, 31);

        assertEquals("2018-12-31", DateUtils.date2String(date));
    }

    @Test
    public void pdfAppointmentDateUsesCalendarYearAtWeekYearBoundary() throws Exception {
        Method date2String = PDFUtils.class.getDeclaredMethod("date2String", Date.class);
        date2String.setAccessible(true);

        assertEquals("2018年12月31日", date2String.invoke(null, date(2018, Calendar.DECEMBER, 31)));
    }

    private Date date(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        calendar.set(year, month, day);
        return calendar.getTime();
    }
}
