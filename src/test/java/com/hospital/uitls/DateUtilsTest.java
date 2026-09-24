package com.hospital.uitls;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class DateUtilsTest {
    @Test
    public void dateUtilsUsesCalendarYearAtWeekYearBoundary() {
        assertEquals("2018-12-31", DateUtils.date2String(december31Of2018()));
    }

    @Test
    public void pdfUtilsUsesCalendarYearAtWeekYearBoundary() throws Exception {
        Method date2String = PDFUtils.class.getDeclaredMethod("date2String", java.util.Date.class);
        date2String.setAccessible(true);

        assertEquals("2018年12月31日", date2String.invoke(null, december31Of2018()));
    }

    private static java.util.Date december31Of2018() {
        return new GregorianCalendar(2018, Calendar.DECEMBER, 31).getTime();
    }
}
