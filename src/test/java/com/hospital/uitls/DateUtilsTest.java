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
        Date boundaryDate = new GregorianCalendar(2018, Calendar.DECEMBER, 31).getTime();

        assertEquals("2018-12-31", DateUtils.date2String(boundaryDate));
    }

    @Test
    public void pdfDate2StringUsesCalendarYearAtWeekYearBoundary() throws Exception {
        Date boundaryDate = new GregorianCalendar(2018, Calendar.DECEMBER, 31).getTime();
        Method date2String = PDFUtils.class.getDeclaredMethod("date2String", Date.class);
        date2String.setAccessible(true);

        String formatted = (String) date2String.invoke(null, boundaryDate);

        assertEquals("2018年12月31日", formatted);
    }
}
