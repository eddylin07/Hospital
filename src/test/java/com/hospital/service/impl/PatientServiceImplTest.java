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
import static org.junit.Assert.assertTrue;

public class PatientServiceImplTest {

    @Test
    public void seekRejectsRequestThatExceedsDrugStockBeforeWriting() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicInteger drugUpdates = new AtomicInteger(0);
        AtomicInteger patientUpdates = new AtomicInteger(0);
        AtomicInteger seekUpdates = new AtomicInteger(0);
        service.drugsMapper = mapper(DrugsMapper.class, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) {
                if ("selectByPrimaryKey".equals(method.getName())) {
                    return drug(1, "Aspirin", "2.50", 5);
                }
                if ("updateNumber".equals(method.getName())) {
                    drugUpdates.incrementAndGet();
                    return 1;
                }
                return defaultValue(method.getReturnType());
            }
        });
        service.patientMapper = mapper(PatientMapper.class, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) {
                if ("updateByPrimaryKeySelective".equals(method.getName())) {
                    patientUpdates.incrementAndGet();
                    return 1;
                }
                return defaultValue(method.getReturnType());
            }
        });
        service.seekMapper = mapper(SeekMapper.class, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) {
                if ("getSeekByPatientId".equals(method.getName())) {
                    return seek(99);
                }
                if ("updateDrugs".equals(method.getName())) {
                    seekUpdates.incrementAndGet();
                    return 1;
                }
                return defaultValue(method.getReturnType());
            }
        });

        String message = service.seek(patientWithDrugs("1@10"));

        assertEquals("对不起Aspirin数量不足", message);
        assertEquals(0, drugUpdates.get());
        assertEquals(0, patientUpdates.get());
        assertEquals(0, seekUpdates.get());
    }

    @Test
    public void seekDeductsStockAndUpdatesLatestSeekOnlyAfterValidation() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicReference<Drugs> updatedDrug = new AtomicReference<>();
        AtomicReference<Seek> updatedSeek = new AtomicReference<>();
        service.drugsMapper = mapper(DrugsMapper.class, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) {
                if ("selectByPrimaryKey".equals(method.getName())) {
                    return drug(1, "Aspirin", "3.50", 5);
                }
                if ("updateNumber".equals(method.getName())) {
                    updatedDrug.set((Drugs) args[0]);
                    return 1;
                }
                return defaultValue(method.getReturnType());
            }
        });
        service.patientMapper = mapper(PatientMapper.class, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) {
                if ("updateByPrimaryKeySelective".equals(method.getName())) {
                    return 1;
                }
                return defaultValue(method.getReturnType());
            }
        });
        service.seekMapper = mapper(SeekMapper.class, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) {
                if ("getSeekByPatientId".equals(method.getName())) {
                    return seek(99);
                }
                if ("updateDrugs".equals(method.getName())) {
                    updatedSeek.set((Seek) args[0]);
                    return 1;
                }
                return defaultValue(method.getReturnType());
            }
        });

        String message = service.seek(patientWithDrugs("1@2"));

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(Integer.valueOf(2), updatedDrug.get().getNumber());
        assertEquals(99L, updatedSeek.get().getId());
        assertTrue(new BigDecimal("7.00").compareTo(updatedSeek.get().getPrice()) == 0);
    }

    private static Patient patientWithDrugs(String drugsids) {
        Patient patient = new Patient();
        patient.setId(5);
        patient.setDrugsids(drugsids);
        return patient;
    }

    private static Drugs drug(Integer id, String name, String price, Integer number) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setName(name);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(number);
        return drugs;
    }

    private static Seek seek(long id) {
        Seek seek = new Seek();
        seek.setId(id);
        return seek;
    }

    private static <T> T mapper(Class<T> type, InvocationHandler handler) {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler));
    }

    private static Object defaultValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        }
        if (boolean.class.equals(type)) {
            return false;
        }
        if (void.class.equals(type)) {
            return null;
        }
        return 0;
    }
}
