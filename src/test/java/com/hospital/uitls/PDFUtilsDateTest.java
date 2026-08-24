package com.hospital.uitls;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class PDFUtilsDateTest {
    @Test
    public void pdfDateFormattingUsesCalendarYearAtWeekYearBoundary() throws Exception {
        Calendar calendar = new GregorianCalendar(2018, Calendar.DECEMBER, 31, 12, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Method date2String = PDFUtils.class.getDeclaredMethod("date2String", java.util.Date.class);
        date2String.setAccessible(true);

        assertEquals("2018年12月31日", date2String.invoke(null, calendar.getTime()));
    }
}
