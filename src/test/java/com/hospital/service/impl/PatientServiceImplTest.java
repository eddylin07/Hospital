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
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PatientServiceImplTest {

    @Test
    public void insufficientStockDoesNotDeductInventoryOrUpdateSeek() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicBoolean inventoryUpdated = new AtomicBoolean(false);
        AtomicBoolean seekUpdated = new AtomicBoolean(false);
        service.drugsMapper = mapper(DrugsMapper.class, (proxy, method, args) -> {
            if (method.getName().equals("selectByPrimaryKey")) {
                return drug(1, 1, "8.00");
            }
            if (method.getName().equals("updateNumber")) {
                inventoryUpdated.set(true);
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.patientMapper = mapper(PatientMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        service.seekMapper = mapper(SeekMapper.class, (proxy, method, args) -> {
            if (method.getName().equals("updateDrugs")) {
                seekUpdated.set(true);
                return 1;
            }
            return defaultValue(method.getReturnType());
        });

        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@2");

        assertEquals("对不起药品数量不足", service.seek(patient));
        assertFalse(inventoryUpdated.get());
        assertFalse(seekUpdated.get());
    }

    @Test
    public void atomicInventoryFailureStopsSeekUpdate() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicBoolean seekUpdated = new AtomicBoolean(false);
        service.drugsMapper = mapper(DrugsMapper.class, (proxy, method, args) -> {
            if (method.getName().equals("selectByPrimaryKey")) {
                return drug(1, 3, "8.00");
            }
            if (method.getName().equals("updateNumber")) {
                return 0;
            }
            return defaultValue(method.getReturnType());
        });
        service.patientMapper = mapper(PatientMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        service.seekMapper = mapper(SeekMapper.class, (proxy, method, args) -> {
            if (method.getName().equals("updateDrugs")) {
                seekUpdated.set(true);
                return 1;
            }
            return defaultValue(method.getReturnType());
        });

        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@2");

        assertEquals("对不起药品数量不足", service.seek(patient));
        assertFalse(seekUpdated.get());
    }

    @Test
    public void malformedDrugInputFailsWithoutUpdating() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicBoolean inventoryUpdated = new AtomicBoolean(false);
        service.drugsMapper = mapper(DrugsMapper.class, (proxy, method, args) -> {
            if (method.getName().equals("updateNumber")) {
                inventoryUpdated.set(true);
            }
            return defaultValue(method.getReturnType());
        });
        service.patientMapper = mapper(PatientMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        service.seekMapper = mapper(SeekMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));

        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("bad@2");

        assertEquals(CommonService.upd_message_error, service.seek(patient));
        assertFalse(inventoryUpdated.get());
    }

    @Test
    public void successfulPrescriptionUpdatesLatestSeekWithCalculatedPrice() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicBoolean patientUpdated = new AtomicBoolean(false);
        AtomicBoolean seekUpdated = new AtomicBoolean(false);
        service.drugsMapper = mapper(DrugsMapper.class, (proxy, method, args) -> {
            if (method.getName().equals("selectByPrimaryKey")) {
                return drug(1, 3, "8.00");
            }
            if (method.getName().equals("updateNumber")) {
                Drugs requestedDrug = (Drugs) args[0];
                assertEquals(Integer.valueOf(2), requestedDrug.getNumber());
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.patientMapper = mapper(PatientMapper.class, (proxy, method, args) -> {
            if (method.getName().equals("updateByPrimaryKeySelective")) {
                patientUpdated.set(true);
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.seekMapper = mapper(SeekMapper.class, (proxy, method, args) -> {
            if (method.getName().equals("updateDrugs")) {
                Seek seek = (Seek) args[0];
                assertEquals(Integer.valueOf(7), seek.getPatientid());
                assertEquals("1@2", seek.getDrugs());
                assertEquals(new BigDecimal("16.00"), seek.getPrice());
                seekUpdated.set(true);
                return 1;
            }
            return defaultValue(method.getReturnType());
        });

        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@2");

        assertEquals(CommonService.upd_message_success, service.seek(patient));
        assertTrue(patientUpdated.get());
        assertTrue(seekUpdated.get());
    }

    private static Drugs drug(Integer id, Integer number, String price) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setNumber(number);
        drugs.setPrice(new BigDecimal(price));
        return drugs;
    }

    private static <T> T mapper(Class<T> type, InvocationHandler handler) {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler));
    }

    private static Object defaultValue(Class<?> returnType) {
        if (returnType.equals(boolean.class)) {
            return false;
        }
        if (returnType.equals(int.class) || returnType.equals(Integer.class)) {
            return 0;
        }
        return null;
    }
}
