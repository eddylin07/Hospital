package com.hospital.uitls;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class PDFUtilsDateTest {
    private Locale originalLocale;
    private TimeZone originalTimeZone;

    @Before
    public void setUp() {
        originalLocale = Locale.getDefault();
        originalTimeZone = TimeZone.getDefault();
        Locale.setDefault(Locale.US);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @After
    public void tearDown() {
        Locale.setDefault(originalLocale);
        TimeZone.setDefault(originalTimeZone);
    }

    @Test
    public void dateUtilsUsesCalendarYearAtWeekYearBoundary() {
        Date date = utcDate(2019, Calendar.DECEMBER, 29);

        assertEquals("2019-12-29", DateUtils.date2String(date));
    }

    @Test
    public void pdfAppointmentDateUsesCalendarYearAtWeekYearBoundary() throws Exception {
        Date date = utcDate(2019, Calendar.DECEMBER, 29);
        Method method = PDFUtils.class.getDeclaredMethod("date2String", Date.class);
        method.setAccessible(true);

        assertEquals("2019年12月29日", method.invoke(null, date));
    }

    private Date utcDate(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
        calendar.clear();
        calendar.set(year, month, day, 12, 0, 0);
        return calendar.getTime();
    }
}
