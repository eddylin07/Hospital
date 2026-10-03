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
        Date lastDayOf2019 = new GregorianCalendar(2019, Calendar.DECEMBER, 31).getTime();

        assertEquals("2019-12-31", DateUtils.date2String(lastDayOf2019));
    }

    @Test
    public void pdfAppointmentDateFormattingUsesCalendarYearAtWeekYearBoundary() throws Exception {
        Date lastDayOf2019 = new GregorianCalendar(2019, Calendar.DECEMBER, 31).getTime();
        Method method = PDFUtils.class.getDeclaredMethod("date2String", Date.class);
        method.setAccessible(true);

        assertEquals("2019年12月31日", method.invoke(null, lastDayOf2019));
    }
}
