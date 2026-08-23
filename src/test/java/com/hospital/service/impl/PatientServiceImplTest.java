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
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PatientServiceImplTest {
    @Test
    public void seekRejectsRequestedQuantityGreaterThanStockWithoutWriting() {
        PatientServiceImpl service = new PatientServiceImpl();
        CountingDrugsMapper drugsMapper = new CountingDrugsMapper(drug(1, "Aspirin", 5, "2.50"));
        CountingPatientMapper patientMapper = new CountingPatientMapper();
        CountingSeekMapper seekMapper = new CountingSeekMapper();
        service.drugsMapper = proxy(DrugsMapper.class, drugsMapper);
        service.patientMapper = proxy(PatientMapper.class, patientMapper);
        service.seekMapper = proxy(SeekMapper.class, seekMapper);
        service.loginMapper = proxy(LoginMapper.class, emptyHandler());
        service.doctorMapper = proxy(DoctorMapper.class, emptyHandler());
        service.illnessMapper = proxy(IllnessMapper.class, emptyHandler());
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@6");

        String message = service.seek(patient);

        assertTrue(message.contains("数量不足"));
        assertEquals(0, drugsMapper.updateNumberCalls);
        assertEquals(0, patientMapper.updateCalls);
        assertEquals(0, seekMapper.updateCalls);
    }

    @Test
    public void seekUpdatesInventoryAndLatestSeekWhenStockIsAvailable() {
        PatientServiceImpl service = new PatientServiceImpl();
        CountingDrugsMapper drugsMapper = new CountingDrugsMapper(drug(1, "Aspirin", 5, "2.50"));
        CountingPatientMapper patientMapper = new CountingPatientMapper();
        CountingSeekMapper seekMapper = new CountingSeekMapper();
        service.drugsMapper = proxy(DrugsMapper.class, drugsMapper);
        service.patientMapper = proxy(PatientMapper.class, patientMapper);
        service.seekMapper = proxy(SeekMapper.class, seekMapper);
        service.loginMapper = proxy(LoginMapper.class, emptyHandler());
        service.doctorMapper = proxy(DoctorMapper.class, emptyHandler());
        service.illnessMapper = proxy(IllnessMapper.class, emptyHandler());
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@3");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(1, drugsMapper.updateNumberCalls);
        assertEquals(Integer.valueOf(3), drugsMapper.updatedNumber);
        assertEquals(1, patientMapper.updateCalls);
        assertEquals(1, seekMapper.updateCalls);
        assertEquals(Integer.valueOf(7), seekMapper.updatedSeek.getPatientid());
        assertEquals(new BigDecimal("7.50"), seekMapper.updatedSeek.getPrice());
    }

    private static Drugs drug(Integer id, String name, Integer number, String price) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setName(name);
        drugs.setNumber(number);
        drugs.setPrice(new BigDecimal(price));
        return drugs;
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }

    private static InvocationHandler emptyHandler() {
        return new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getDeclaringClass().equals(Object.class)) {
                    return method.invoke(this, args);
                }
                return null;
            }
        };
    }

    private static class CountingDrugsMapper implements InvocationHandler {
        private final Drugs drugs;
        int updateNumberCalls;
        Integer updatedNumber;

        CountingDrugsMapper(Drugs drugs) {
            this.drugs = drugs;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getDeclaringClass().equals(Object.class)) {
                return method.invoke(this, args);
            }
            if ("selectByPrimaryKey".equals(method.getName())) {
                return drugs;
            }
            if ("updateNumber".equals(method.getName())) {
                updateNumberCalls++;
                Drugs requested = (Drugs) args[0];
                updatedNumber = requested.getNumber();
                return drugs.getNumber() >= requested.getNumber() ? 1 : 0;
            }
            return null;
        }
    }

    private static class CountingPatientMapper implements InvocationHandler {
        int updateCalls;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getDeclaringClass().equals(Object.class)) {
                return method.invoke(this, args);
            }
            if ("updateByPrimaryKeySelective".equals(method.getName())) {
                updateCalls++;
                return 1;
            }
            return null;
        }
    }

    private static class CountingSeekMapper implements InvocationHandler {
        int updateCalls;
        Seek updatedSeek;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getDeclaringClass().equals(Object.class)) {
                return method.invoke(this, args);
            }
            if ("updateDrugs".equals(method.getName())) {
                updateCalls++;
                updatedSeek = (Seek) args[0];
                return 1;
            }
            return null;
        }
    }
}
