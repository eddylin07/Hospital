package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DoctorMapper;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.IllnessMapper;
import com.hospital.dao.LoginMapper;
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
import static org.junit.Assert.assertNull;

public class PatientServiceImplTest {
    @Test
    public void seekRejectsRequestedQuantityGreaterThanStockWithoutWrites() {
        PatientServiceImpl service = serviceWithUnusedDependencies();
        AtomicInteger inventoryUpdates = new AtomicInteger();
        AtomicInteger patientUpdates = new AtomicInteger();
        AtomicInteger seekUpdates = new AtomicInteger();
        service.drugsMapper = mapper(DrugsMapper.class, (proxy, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                return drug(3, "aspirin", 5, "1.20");
            }
            if ("updateNumber".equals(method.getName())) {
                inventoryUpdates.incrementAndGet();
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
        Patient patient = new Patient();
        patient.setId(9);
        patient.setDrugsids("3@6");

        String message = service.seek(patient);

        assertEquals("对不起aspirin数量不足", message);
        assertEquals(0, inventoryUpdates.get());
        assertEquals(0, patientUpdates.get());
        assertEquals(0, seekUpdates.get());
    }

    @Test
    public void seekDeductsRequestedQuantityAndUpdatesSeekPrice() {
        PatientServiceImpl service = serviceWithUnusedDependencies();
        AtomicReference<Drugs> inventoryUpdate = new AtomicReference<>();
        AtomicReference<Seek> seekUpdate = new AtomicReference<>();
        service.drugsMapper = mapper(DrugsMapper.class, (proxy, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                return drug(3, "aspirin", 5, "1.20");
            }
            if ("updateNumber".equals(method.getName())) {
                inventoryUpdate.set((Drugs) args[0]);
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
        patient.setId(9);
        patient.setDrugsids("3@2");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(Integer.valueOf(3), inventoryUpdate.get().getId());
        assertEquals(Integer.valueOf(2), inventoryUpdate.get().getNumber());
        assertEquals(Integer.valueOf(9), seekUpdate.get().getPatientid());
        assertEquals("3@2", seekUpdate.get().getDrugs());
        assertEquals(new BigDecimal("2.40"), seekUpdate.get().getPrice());
    }

    @Test
    public void seekDoesNotWritePatientOrSeekWhenAtomicInventoryUpdateFails() {
        PatientServiceImpl service = serviceWithUnusedDependencies();
        AtomicInteger patientUpdates = new AtomicInteger();
        AtomicInteger seekUpdates = new AtomicInteger();
        service.drugsMapper = mapper(DrugsMapper.class, (proxy, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                return drug(3, "aspirin", 5, "1.20");
            }
            if ("updateNumber".equals(method.getName())) {
                return 0;
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
        Patient patient = new Patient();
        patient.setId(9);
        patient.setDrugsids("3@2");

        String message = service.seek(patient);

        assertEquals("对不起aspirin数量不足", message);
        assertEquals(0, patientUpdates.get());
        assertEquals(0, seekUpdates.get());
    }

    private PatientServiceImpl serviceWithUnusedDependencies() {
        PatientServiceImpl service = new PatientServiceImpl();
        service.loginMapper = mapper(LoginMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        service.doctorMapper = mapper(DoctorMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        service.illnessMapper = mapper(IllnessMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        return service;
    }

    private Drugs drug(Integer id, String name, Integer number, String price) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setName(name);
        drugs.setNumber(number);
        drugs.setPrice(new BigDecimal(price));
        return drugs;
    }

    @SuppressWarnings("unchecked")
    private <T> T mapper(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }

    private Object defaultValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        }
        if (boolean.class.equals(type)) {
            return false;
        }
        return 0;
    }
}
