package com.hospital.service.impl;

import com.hospital.dao.DoctorMapper;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.IllnessMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class PatientServiceImplTest {
    @Test
    public void rejectsDispensingMoreThanAvailableStockWithoutWriting() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicInteger stockUpdates = new AtomicInteger(0);
        AtomicInteger patientUpdates = new AtomicInteger(0);
        AtomicInteger seekUpdates = new AtomicInteger(0);
        service.drugsMapper = mapper(DrugsMapper.class, (proxy, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                Drugs drugs = new Drugs();
                drugs.setId((Integer) args[0]);
                drugs.setName("test drug");
                drugs.setNumber(5);
                drugs.setPrice(new BigDecimal("2.00"));
                return drugs;
            }
            if ("updateNumber".equals(method.getName())) {
                stockUpdates.incrementAndGet();
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.patientMapper = mapper(PatientMapper.class, (proxy, method, args) -> {
            if ("updateByPrimaryKeySelective".equals(method.getName())) {
                patientUpdates.incrementAndGet();
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.seekMapper = mapper(SeekMapper.class, (proxy, method, args) -> {
            if ("updateDrugs".equals(method.getName())) {
                seekUpdates.incrementAndGet();
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.loginMapper = mapper(LoginMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        service.doctorMapper = mapper(DoctorMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        service.illnessMapper = mapper(IllnessMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));

        Patient patient = new Patient();
        patient.setId(9);
        patient.setDrugsids("1@6");

        String message = service.seek(patient);

        assertEquals("对不起5数量不足", message);
        assertEquals(0, stockUpdates.get());
        assertEquals(0, patientUpdates.get());
        assertEquals(0, seekUpdates.get());
    }

    @SuppressWarnings("unchecked")
    private static <T> T mapper(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler);
    }

    private static Object defaultValue(Class<?> returnType) {
        if (!returnType.isPrimitive()) {
            return null;
        }
        if (boolean.class.equals(returnType)) {
            return false;
        }
        if (void.class.equals(returnType)) {
            return null;
        }
        return 0;
    }
}
