package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PatientServiceImplTest {
    @Test
    public void rejectsPrescriptionWhenRequestedQuantityExceedsStock() {
        PatientServiceImpl service = serviceWithDrug(3, 1, new AtomicInteger(), new AtomicInteger(), new AtomicInteger());
        Patient patient = new Patient();
        patient.setId(8);
        patient.setDrugsids("1@5");

        String message = service.seek(patient);

        assertTrue(message.contains("数量不足"));
    }

    @Test
    public void stopsBeforePatientAndSeekUpdatesWhenAtomicStockUpdateFails() {
        AtomicInteger stockUpdates = new AtomicInteger();
        AtomicInteger patientUpdates = new AtomicInteger();
        AtomicInteger seekUpdates = new AtomicInteger();
        PatientServiceImpl service = serviceWithDrug(5, 0, stockUpdates, patientUpdates, seekUpdates);
        Patient patient = new Patient();
        patient.setId(8);
        patient.setDrugsids("1@4");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_error, message);
        assertEquals(1, stockUpdates.get());
        assertEquals(0, patientUpdates.get());
        assertEquals(0, seekUpdates.get());
    }

    @Test
    public void updatesPatientAndLatestSeekAfterSuccessfulStockDeduction() {
        AtomicInteger stockUpdates = new AtomicInteger();
        AtomicInteger patientUpdates = new AtomicInteger();
        AtomicInteger seekUpdates = new AtomicInteger();
        PatientServiceImpl service = serviceWithDrug(5, 1, stockUpdates, patientUpdates, seekUpdates);
        Patient patient = new Patient();
        patient.setId(8);
        patient.setDrugsids("1@4");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(1, stockUpdates.get());
        assertEquals(1, patientUpdates.get());
        assertEquals(1, seekUpdates.get());
    }

    private PatientServiceImpl serviceWithDrug(int stock, int stockUpdateResult, AtomicInteger stockUpdates,
                                               AtomicInteger patientUpdates, AtomicInteger seekUpdates) {
        PatientServiceImpl service = new PatientServiceImpl();
        service.drugsMapper = proxy(DrugsMapper.class, (proxy, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                Drugs drugs = new Drugs();
                drugs.setId((Integer) args[0]);
                drugs.setName("阿莫西林");
                drugs.setNumber(stock);
                drugs.setPrice(new BigDecimal("2.50"));
                return drugs;
            }
            if ("updateNumber".equals(method.getName())) {
                stockUpdates.incrementAndGet();
                return stockUpdateResult;
            }
            return defaultValue(method.getReturnType());
        });
        service.patientMapper = proxy(PatientMapper.class, (proxy, method, args) -> {
            if ("updateByPrimaryKeySelective".equals(method.getName())) {
                patientUpdates.incrementAndGet();
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.seekMapper = proxy(SeekMapper.class, (proxy, method, args) -> {
            if ("updateDrugs".equals(method.getName())) {
                seekUpdates.incrementAndGet();
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        return service;
    }

    @SuppressWarnings("unchecked")
    private <T> T proxy(Class<T> type, java.lang.reflect.InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }

    private static Object defaultValue(Class<?> returnType) {
        if (returnType == Integer.TYPE) {
            return 0;
        }
        if (returnType == Boolean.TYPE) {
            return false;
        }
        return null;
    }
}
