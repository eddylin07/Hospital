package com.hospital.service.impl;

import com.hospital.TestSupport;
import com.hospital.dao.OptionMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Seek;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class DoctorServiceImplTest {
    @Test
    public void seekInfoRejectsEmptyOptionsInsteadOfThrowing() {
        DoctorServiceImpl service = new DoctorServiceImpl();
        AtomicInteger inserts = new AtomicInteger();
        TestSupport.setField(service, "seekMapper", TestSupport.proxy(SeekMapper.class, (proxy, method, args) -> {
            inserts.incrementAndGet();
            return 1;
        }));

        Map<String, String> body = new HashMap<>();
        body.put("patientid", "7");
        body.put("days", "3");

        assertEquals("请选择检查项目", service.seekInfo(body));
        assertEquals(0, inserts.get());
    }

    @Test
    public void seekInfoRejectsMalformedDaysInsteadOfThrowing() {
        DoctorServiceImpl service = new DoctorServiceImpl();
        TestSupport.setField(service, "optionMapper", TestSupport.proxy(OptionMapper.class, (proxy, method, args) -> BigDecimal.TEN));
        TestSupport.setField(service, "seekMapper", TestSupport.proxy(SeekMapper.class, (proxy, method, args) -> 1));

        Map<String, String> body = new HashMap<>();
        body.put("patientid", "7");
        body.put("days", "not-a-number");
        body.put("option_1", "1");

        assertEquals("就诊信息错误", service.seekInfo(body));
    }

    @Test
    public void seekInfoInsertsWhenInputIsValid() {
        DoctorServiceImpl service = new DoctorServiceImpl();
        AtomicInteger inserts = new AtomicInteger();
        TestSupport.setField(service, "optionMapper", TestSupport.proxy(OptionMapper.class, (proxy, method, args) -> new BigDecimal("30.00")));
        TestSupport.setField(service, "seekMapper", TestSupport.proxy(SeekMapper.class, (proxy, method, args) -> {
            if (method.getName().equals("insert")) {
                Seek seek = (Seek) args[0];
                assertEquals(Integer.valueOf(7), seek.getPatientid());
                assertEquals(3L, seek.getDays());
                assertEquals("1", seek.getOptions());
                inserts.incrementAndGet();
            }
            return 1;
        }));

        Map<String, String> body = new HashMap<>();
        body.put("patientid", "7");
        body.put("days", "3");
        body.put("option_1", "1");

        assertEquals("添加成功", service.seekInfo(body));
        assertEquals(1, inserts.get());
    }
}
