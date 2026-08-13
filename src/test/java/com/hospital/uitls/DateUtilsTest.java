package com.hospital.uitls;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class DateUtilsTest {
    @Test
    public void sharedDateFormattingUsesCalendarYearAtWeekYearBoundary() {
        assertEquals("2019-12-30", DateUtils.date2String(decemberThirtieth2019()));
    }

    @Test
    public void pdfAppointmentDateFormattingUsesCalendarYearAtWeekYearBoundary() throws Exception {
        Method method = PDFUtils.class.getDeclaredMethod("date2String", Date.class);
        method.setAccessible(true);

        assertEquals("2019年12月30日", method.invoke(null, decemberThirtieth2019()));
    }

    private static Date decemberThirtieth2019() {
        Calendar calendar = new GregorianCalendar();
        calendar.clear();
        calendar.set(2019, Calendar.DECEMBER, 30, 12, 0, 0);
        return calendar.getTime();
    }
}
