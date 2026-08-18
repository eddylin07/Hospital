package com.hospital.uitls;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

public class DateUtilsTest {

    private TimeZone originalTimeZone;

    @Before
    public void setUp() {
        originalTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @After
    public void tearDown() {
        TimeZone.setDefault(originalTimeZone);
    }

    @Test
    public void dateUtilsUsesCalendarYearAtEndOfDecember() throws Exception {
        Date date = parseUtcDate("2019-12-31");

        assertThat(DateUtils.date2String(date)).isEqualTo("2019-12-31");
    }

    @Test
    public void pdfAppointmentDateUsesCalendarYearAtEndOfDecember() throws Exception {
        Method date2String = PDFUtils.class.getDeclaredMethod("date2String", Date.class);
        date2String.setAccessible(true);

        String formatted = (String) date2String.invoke(null, parseUtcDate("2019-12-31"));

        assertThat(formatted).isEqualTo("2019年12月31日");
    }

    private Date parseUtcDate(String value) throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        parser.setTimeZone(TimeZone.getTimeZone("UTC"));
        return parser.parse(value);
    }
}
