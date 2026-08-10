package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

public class PatientServiceImplSeekTest {

    @Test
    public void rejectsRequestedQuantityGreaterThanStockWithoutUpdating() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicInteger stockUpdates = new AtomicInteger();
        service.drugsMapper = mapper(DrugsMapper.class, (proxy, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                return drug(5, "Aspirin", 1, "3.50");
            }
            if ("updateNumber".equals(method.getName())) {
                stockUpdates.incrementAndGet();
            }
            return defaultValue(method.getReturnType());
        });
        service.patientMapper = mapper(PatientMapper.class, (proxy, method, args) -> {
            if ("updateByPrimaryKeySelective".equals(method.getName())) {
                throw new AssertionError("patient row must not be updated when stock is insufficient");
            }
            return defaultValue(method.getReturnType());
        });
        service.seekMapper = mapper(SeekMapper.class, (proxy, method, args) -> {
            if ("updateDrugs".equals(method.getName())) {
                throw new AssertionError("seek row must not be updated when stock is insufficient");
            }
            return defaultValue(method.getReturnType());
        });
        Patient patient = new Patient();
        patient.setId(12);
        patient.setDrugsids("5@2");

        String message = service.seek(patient);

        assertEquals("对不起Aspirin数量不足", message);
        assertEquals(0, stockUpdates.get());
    }

    @Test
    public void successfulDispenseUpdatesStockAndSeekPrice() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicReference<Drugs> stockUpdate = new AtomicReference<>();
        AtomicReference<Seek> seekUpdate = new AtomicReference<>();
        service.drugsMapper = mapper(DrugsMapper.class, (proxy, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                return drug(5, "Aspirin", 10, "3.50");
            }
            if ("updateNumber".equals(method.getName())) {
                stockUpdate.set((Drugs) args[0]);
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
            if ("updateDrugs".equals(method.getName())) {
                seekUpdate.set((Seek) args[0]);
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        Patient patient = new Patient();
        patient.setId(12);
        patient.setDrugsids("5@2");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(Integer.valueOf(2), stockUpdate.get().getNumber());
        assertEquals(Integer.valueOf(12), seekUpdate.get().getPatientid());
        assertEquals("5@2", seekUpdate.get().getDrugs());
        assertEquals(new BigDecimal("7.00"), seekUpdate.get().getPrice());
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
    private <T> T mapper(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }

    private Object defaultValue(Class<?> returnType) {
        if (returnType.equals(Integer.TYPE)) {
            return 0;
        }
        if (returnType.equals(Boolean.TYPE)) {
            return false;
        }
        return null;
    }
}
