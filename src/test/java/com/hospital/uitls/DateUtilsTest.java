package com.hospital.uitls;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class DateUtilsTest {

    @Test
    public void date2StringUsesCalendarYearAtWeekYearBoundary() {
        assertEquals("2018-12-31", DateUtils.date2String(decemberThirtyFirst2018()));
    }

    @Test
    public void pdfAppointmentDateUsesCalendarYearAtWeekYearBoundary() throws Exception {
        Method date2String = PDFUtils.class.getDeclaredMethod("date2String", Date.class);
        date2String.setAccessible(true);

        assertEquals("2018年12月31日", date2String.invoke(null, decemberThirtyFirst2018()));
    }

    private Date decemberThirtyFirst2018() {
        Calendar calendar = new GregorianCalendar(2018, Calendar.DECEMBER, 31, 12, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
