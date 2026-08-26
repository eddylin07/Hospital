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
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class PatientServiceImplTest {

    @Test
    public void seekRejectsOverstockRequestWithoutAnyWrites() {
        PatientServiceImpl service = serviceWithDrugStock(5, 1);
        Patient patient = patient("8@10");

        assertEquals("对不起药品数量不足", service.seek(patient));
        assertEquals(0, ((DrugsMapperHandler) Proxy.getInvocationHandler(service.drugsMapper)).updateNumberCalls);
        assertEquals(0, ((PatientMapperHandler) Proxy.getInvocationHandler(service.patientMapper)).updateCalls);
        assertEquals(0, ((SeekMapperHandler) Proxy.getInvocationHandler(service.seekMapper)).updateCalls);
    }

    @Test
    public void seekStopsWhenAtomicStockUpdateFails() {
        PatientServiceImpl service = serviceWithDrugStock(5, 0);
        Patient patient = patient("8@3");

        assertEquals("对不起药品数量不足", service.seek(patient));
        assertEquals(1, ((DrugsMapperHandler) Proxy.getInvocationHandler(service.drugsMapper)).updateNumberCalls);
        assertEquals(0, ((PatientMapperHandler) Proxy.getInvocationHandler(service.patientMapper)).updateCalls);
        assertEquals(0, ((SeekMapperHandler) Proxy.getInvocationHandler(service.seekMapper)).updateCalls);
    }

    @Test
    public void seekUpdatesPatientAndLatestSeekWhenStockUpdateSucceeds() {
        PatientServiceImpl service = serviceWithDrugStock(5, 1);
        Patient patient = patient("8@3");

        assertEquals(CommonService.upd_message_success, service.seek(patient));
        assertEquals(1, ((DrugsMapperHandler) Proxy.getInvocationHandler(service.drugsMapper)).updateNumberCalls);
        assertEquals(1, ((PatientMapperHandler) Proxy.getInvocationHandler(service.patientMapper)).updateCalls);
        assertEquals(1, ((SeekMapperHandler) Proxy.getInvocationHandler(service.seekMapper)).updateCalls);
    }

    private PatientServiceImpl serviceWithDrugStock(int stock, int updateNumberResult) {
        PatientServiceImpl service = new PatientServiceImpl();
        service.drugsMapper = proxy(DrugsMapper.class, new DrugsMapperHandler(stock, updateNumberResult));
        service.patientMapper = proxy(PatientMapper.class, new PatientMapperHandler());
        service.seekMapper = proxy(SeekMapper.class, new SeekMapperHandler());
        service.loginMapper = proxy(LoginMapper.class, defaultHandler());
        service.doctorMapper = proxy(DoctorMapper.class, defaultHandler());
        service.illnessMapper = proxy(IllnessMapper.class, defaultHandler());
        return service;
    }

    private Patient patient(String drugsids) {
        Patient patient = new Patient();
        patient.setId(6);
        patient.setDrugsids(drugsids);
        return patient;
    }

    private static class DrugsMapperHandler implements InvocationHandler {
        private final int stock;
        private final int updateNumberResult;
        private int updateNumberCalls;

        private DrugsMapperHandler(int stock, int updateNumberResult) {
            this.stock = stock;
            this.updateNumberResult = updateNumberResult;
        }

        @Override
        public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) {
            if ("selectByPrimaryKey".equals(method.getName())) {
                Drugs drugs = new Drugs();
                drugs.setId((Integer) args[0]);
                drugs.setNumber(stock);
                drugs.setPrice(new BigDecimal("2.50"));
                return drugs;
            }
            if ("updateNumber".equals(method.getName())) {
                updateNumberCalls++;
                return updateNumberResult;
            }
            return defaultValue(method.getReturnType());
        }
    }

    private static class PatientMapperHandler implements InvocationHandler {
        private int updateCalls;

        @Override
        public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) {
            if ("updateByPrimaryKeySelective".equals(method.getName())) {
                updateCalls++;
                return 1;
            }
            return defaultValue(method.getReturnType());
        }
    }

    private static class SeekMapperHandler implements InvocationHandler {
        private int updateCalls;

        @Override
        public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) {
            if ("updateDrugs".equals(method.getName())) {
                updateCalls++;
                return 1;
            }
            return defaultValue(method.getReturnType());
        }
    }

    private static InvocationHandler defaultHandler() {
        return (proxy, method, args) -> defaultValue(method.getReturnType());
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }

    private static Object defaultValue(Class<?> type) {
        if (type.equals(Boolean.TYPE)) {
            return false;
        }
        if (type.equals(Integer.TYPE)) {
            return 0;
        }
        return null;
    }
}
