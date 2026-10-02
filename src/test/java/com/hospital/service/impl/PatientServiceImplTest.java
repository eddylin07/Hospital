package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PatientServiceImplTest {
    @Test
    public void insufficientStockDoesNotDecrementInventory() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicBoolean decremented = new AtomicBoolean(false);
        service.drugsMapper = proxy(DrugsMapper.class, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                if ("selectByPrimaryKey".equals(method.getName())) {
                    return drugWithStock(1);
                }
                if ("updateNumberIfEnough".equals(method.getName())) {
                    decremented.set(true);
                    return 1;
                }
                return defaultValue(method.getReturnType());
            }
        });

        Patient patient = new Patient();
        patient.setId(8);
        patient.setDrugsids("3@2");

        String message = service.seek(patient);

        assertEquals("对不起库存数量不足", message);
        assertFalse(decremented.get());
    }

    @Test
    public void concurrentStockMissDoesNotWritePatientOrSeek() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicBoolean patientUpdated = new AtomicBoolean(false);
        AtomicBoolean seekUpdated = new AtomicBoolean(false);
        service.drugsMapper = proxy(DrugsMapper.class, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                if ("selectByPrimaryKey".equals(method.getName())) {
                    return drugWithStock(5);
                }
                if ("updateNumberIfEnough".equals(method.getName())) {
                    return 0;
                }
                return defaultValue(method.getReturnType());
            }
        });
        service.patientMapper = proxy(PatientMapper.class, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                if ("updateByPrimaryKeySelective".equals(method.getName())) {
                    patientUpdated.set(true);
                    return 1;
                }
                return defaultValue(method.getReturnType());
            }
        });
        service.seekMapper = proxy(SeekMapper.class, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                if ("updateDrugs".equals(method.getName())) {
                    seekUpdated.set(true);
                    return 1;
                }
                return defaultValue(method.getReturnType());
            }
        });

        Patient patient = new Patient();
        patient.setId(8);
        patient.setDrugsids("3@2");

        String message = service.seek(patient);

        assertEquals("对不起库存数量不足", message);
        assertFalse(patientUpdated.get());
        assertFalse(seekUpdated.get());
    }

    @Test
    public void validPrescriptionUsesAtomicDecrementAndWritesSeek() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicBoolean decremented = new AtomicBoolean(false);
        AtomicBoolean seekUpdated = new AtomicBoolean(false);
        service.drugsMapper = proxy(DrugsMapper.class, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                if ("selectByPrimaryKey".equals(method.getName())) {
                    return drugWithStock(5);
                }
                if ("updateNumberIfEnough".equals(method.getName())) {
                    assertEquals(Integer.valueOf(3), args[0]);
                    assertEquals(Integer.valueOf(2), args[1]);
                    decremented.set(true);
                    return 1;
                }
                return defaultValue(method.getReturnType());
            }
        });
        service.patientMapper = proxy(PatientMapper.class, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                if ("updateByPrimaryKeySelective".equals(method.getName())) {
                    return 1;
                }
                return defaultValue(method.getReturnType());
            }
        });
        service.seekMapper = proxy(SeekMapper.class, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                if ("updateDrugs".equals(method.getName())) {
                    seekUpdated.set(true);
                    return 1;
                }
                return defaultValue(method.getReturnType());
            }
        });

        Patient patient = new Patient();
        patient.setId(8);
        patient.setDrugsids("3@2");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertTrue(decremented.get());
        assertTrue(seekUpdated.get());
    }

    private static Drugs drugWithStock(int stock) {
        Drugs drugs = new Drugs();
        drugs.setId(3);
        drugs.setNumber(stock);
        drugs.setPrice(new BigDecimal("2.50"));
        return drugs;
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }

    private static Object defaultValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        }
        if (type == Boolean.TYPE) {
            return false;
        }
        if (type == Character.TYPE) {
            return '\0';
        }
        return 0;
    }
}
