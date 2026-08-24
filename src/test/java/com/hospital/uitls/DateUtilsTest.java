package com.hospital.uitls;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class DateUtilsTest {
    @Test
    public void date2StringUsesCalendarYearAtWeekYearBoundary() {
        Calendar calendar = new GregorianCalendar(2018, Calendar.DECEMBER, 31, 12, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        assertEquals("2018-12-31", DateUtils.date2String(calendar.getTime()));
    }
}
