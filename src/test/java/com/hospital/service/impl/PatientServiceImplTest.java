package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;

public class PatientServiceImplTest {
    @Test
    public void seekRejectsRequestedQuantityGreaterThanStockWithoutUpdatingInventory() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicInteger updateCalls = new AtomicInteger();
        service.drugsMapper = mapper(DrugsMapper.class, (proxy, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                return drug(1, "A", 1, "2.00");
            }
            if ("updateNumber".equals(method.getName())) {
                updateCalls.incrementAndGet();
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        Patient patient = new Patient();
        patient.setId(5);
        patient.setDrugsids("1@2");

        String message = service.seek(patient);

        assertEquals("对不起A数量不足", message);
        assertEquals(0, updateCalls.get());
    }

    @Test
    public void seekUpdatesOnlyTheLatestSeekRowAfterAtomicInventoryUpdate() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicInteger requestedQuantity = new AtomicInteger();
        AtomicLong updatedSeekId = new AtomicLong();
        service.drugsMapper = mapper(DrugsMapper.class, (proxy, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                return drug(1, "A", 10, "2.00");
            }
            if ("updateNumber".equals(method.getName())) {
                requestedQuantity.set(((Drugs) args[0]).getNumber());
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.patientMapper = mapper(PatientMapper.class, (proxy, method, args) -> {
            if ("updateByPrimaryKeySelective".equals(method.getName())) {
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.seekMapper = mapper(SeekMapper.class, (proxy, method, args) -> {
            if ("getSeekByPatientId".equals(method.getName())) {
                Seek seek = new Seek();
                seek.setId(99);
                return seek;
            }
            if ("updateDrugs".equals(method.getName())) {
                updatedSeekId.set(((Seek) args[0]).getId());
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        Patient patient = new Patient();
        patient.setId(5);
        patient.setDrugsids("1@3");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(3, requestedQuantity.get());
        assertEquals(99, updatedSeekId.get());
    }

    private Drugs drug(Integer id, String name, Integer stock, String price) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setName(name);
        drugs.setNumber(stock);
        drugs.setPrice(new BigDecimal(price));
        return drugs;
    }

    @SuppressWarnings("unchecked")
    private <T> T mapper(Class<T> type, java.lang.reflect.InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }

    private Object defaultValue(Class<?> type) {
        if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
            return 0;
        }
        return null;
    }
}
