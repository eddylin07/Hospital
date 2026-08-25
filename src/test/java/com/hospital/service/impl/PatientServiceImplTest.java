package com.hospital.service.impl;

import com.hospital.dao.DrugsMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class PatientServiceImplTest {

    @Test
    public void seekRejectsOverdrawWithoutUpdatingRows() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicInteger drugUpdates = new AtomicInteger();
        AtomicInteger patientUpdates = new AtomicInteger();
        AtomicInteger seekUpdates = new AtomicInteger();

        Drugs stored = new Drugs();
        stored.setId(7);
        stored.setName("Aspirin");
        stored.setNumber(1);
        stored.setPrice(new BigDecimal("3.50"));

        ReflectionTestUtils.setField(service, "drugsMapper", drugsMapper(stored, drugUpdates));
        ReflectionTestUtils.setField(service, "patientMapper", patientMapper(patientUpdates));
        ReflectionTestUtils.setField(service, "seekMapper", seekMapper(seekUpdates));

        Patient patient = new Patient();
        patient.setId(22);
        patient.setDrugsids("7@2");

        assertEquals("对不起药品数量不足", service.seek(patient));
        assertEquals(0, drugUpdates.get());
        assertEquals(0, patientUpdates.get());
        assertEquals(0, seekUpdates.get());
    }

    private DrugsMapper drugsMapper(final Drugs stored, final AtomicInteger updates) {
        return (DrugsMapper) Proxy.newProxyInstance(
                DrugsMapper.class.getClassLoader(),
                new Class[]{DrugsMapper.class},
                (proxy, method, args) -> {
                    if ("selectByPrimaryKey".equals(method.getName())) {
                        return stored;
                    }
                    if ("updateNumber".equals(method.getName())) {
                        updates.incrementAndGet();
                        return 1;
                    }
                    return defaultValue(method.getReturnType());
                });
    }

    private PatientMapper patientMapper(final AtomicInteger updates) {
        return (PatientMapper) Proxy.newProxyInstance(
                PatientMapper.class.getClassLoader(),
                new Class[]{PatientMapper.class},
                (proxy, method, args) -> {
                    if ("updateByPrimaryKeySelective".equals(method.getName())) {
                        updates.incrementAndGet();
                        return 1;
                    }
                    return defaultValue(method.getReturnType());
                });
    }

    private SeekMapper seekMapper(final AtomicInteger updates) {
        return (SeekMapper) Proxy.newProxyInstance(
                SeekMapper.class.getClassLoader(),
                new Class[]{SeekMapper.class},
                (proxy, method, args) -> {
                    if ("updateDrugs".equals(method.getName())) {
                        updates.incrementAndGet();
                        return 1;
                    }
                    return defaultValue(method.getReturnType());
                });
    }

    private Object defaultValue(Class<?> returnType) {
        if (returnType == Integer.TYPE || returnType == Integer.class) {
            return 0;
        }
        return null;
    }
}
