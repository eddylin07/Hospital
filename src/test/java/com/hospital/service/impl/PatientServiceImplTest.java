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
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;

public class PatientServiceImplTest {
    @Test
    public void seekRejectsInsufficientStockBeforeUpdatingAnything() {
        PatientServiceImpl service = serviceWithDrug(stockedDrug(1, "Aspirin", 1, "2.50"), 1);
        RecordingPatientMapper patientMapper = new RecordingPatientMapper();
        RecordingSeekMapper seekMapper = new RecordingSeekMapper();
        service.patientMapper = patientMapper.proxy();
        service.seekMapper = seekMapper.proxy();
        Patient patient = patient(8, "1@2");

        String message = service.seek(patient);

        Assert.assertEquals("对不起Aspirin数量不足", message);
        Assert.assertEquals(0, patientMapper.updateCalls);
        Assert.assertEquals(0, seekMapper.updateCalls);
    }

    @Test
    public void seekStopsWhenAtomicStockUpdateFails() {
        RecordingDrugsMapper drugsMapper = new RecordingDrugsMapper(stockedDrug(1, "Aspirin", 5, "2.50"), 0);
        PatientServiceImpl service = baseService();
        service.drugsMapper = drugsMapper.proxy();
        RecordingPatientMapper patientMapper = new RecordingPatientMapper();
        RecordingSeekMapper seekMapper = new RecordingSeekMapper();
        service.patientMapper = patientMapper.proxy();
        service.seekMapper = seekMapper.proxy();

        String message = service.seek(patient(8, "1@2"));

        Assert.assertEquals("对不起Aspirin数量不足", message);
        Assert.assertEquals(1, drugsMapper.updateCalls);
        Assert.assertEquals(0, patientMapper.updateCalls);
        Assert.assertEquals(0, seekMapper.updateCalls);
    }

    @Test
    public void seekUpdatesPatientAndLatestSeekWhenStockUpdateSucceeds() {
        RecordingDrugsMapper drugsMapper = new RecordingDrugsMapper(stockedDrug(1, "Aspirin", 5, "2.50"), 1);
        PatientServiceImpl service = baseService();
        service.drugsMapper = drugsMapper.proxy();
        RecordingPatientMapper patientMapper = new RecordingPatientMapper();
        RecordingSeekMapper seekMapper = new RecordingSeekMapper();
        service.patientMapper = patientMapper.proxy();
        service.seekMapper = seekMapper.proxy();

        String message = service.seek(patient(8, "1@2"));

        Assert.assertEquals(CommonService.upd_message_success, message);
        Assert.assertEquals(1, drugsMapper.updateCalls);
        Assert.assertEquals(1, patientMapper.updateCalls);
        Assert.assertEquals(1, seekMapper.updateCalls);
        Assert.assertEquals(Integer.valueOf(8), seekMapper.updatedSeek.getPatientid());
        Assert.assertEquals(new BigDecimal("5.00"), seekMapper.updatedSeek.getPrice());
    }

    private PatientServiceImpl serviceWithDrug(Drugs drug, int updateResult) {
        PatientServiceImpl service = baseService();
        service.drugsMapper = new RecordingDrugsMapper(drug, updateResult).proxy();
        return service;
    }

    private PatientServiceImpl baseService() {
        PatientServiceImpl service = new PatientServiceImpl();
        service.loginMapper = noop(LoginMapper.class);
        service.doctorMapper = noop(DoctorMapper.class);
        service.illnessMapper = noop(IllnessMapper.class);
        return service;
    }

    private Patient patient(Integer id, String drugsids) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setDrugsids(drugsids);
        return patient;
    }

    private Drugs stockedDrug(Integer id, String name, Integer number, String price) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setName(name);
        drugs.setNumber(number);
        drugs.setPrice(new BigDecimal(price));
        return drugs;
    }

    @SuppressWarnings("unchecked")
    private <T> T noop(Class<T> type) {
        return createProxy(type, (proxy, method, args) -> defaultValue(method.getReturnType()));
    }

    private Object defaultValue(Class<?> type) {
        if (type == Integer.TYPE || type == Integer.class) {
            return 0;
        }
        if (type == Boolean.TYPE || type == Boolean.class) {
            return false;
        }
        return null;
    }

    private class RecordingDrugsMapper {
        private final Drugs drug;
        private final int updateResult;
        private int updateCalls;

        private RecordingDrugsMapper(Drugs drug, int updateResult) {
            this.drug = drug;
            this.updateResult = updateResult;
        }

        private DrugsMapper proxy() {
            return createProxy(DrugsMapper.class, (proxy, method, args) -> {
                if ("selectByPrimaryKey".equals(method.getName())) {
                    return drug;
                }
                if ("updateNumber".equals(method.getName())) {
                    updateCalls++;
                    return updateResult;
                }
                return defaultValue(method.getReturnType());
            });
        }
    }

    private class RecordingPatientMapper {
        private int updateCalls;

        private PatientMapper proxy() {
            return createProxy(PatientMapper.class, (proxy, method, args) -> {
                if ("updateByPrimaryKeySelective".equals(method.getName())) {
                    updateCalls++;
                    return 1;
                }
                return defaultValue(method.getReturnType());
            });
        }
    }

    private class RecordingSeekMapper {
        private int updateCalls;
        private Seek updatedSeek;

        private SeekMapper proxy() {
            return createProxy(SeekMapper.class, (proxy, method, args) -> {
                if ("updateDrugs".equals(method.getName())) {
                    updateCalls++;
                    updatedSeek = (Seek) args[0];
                    return 1;
                }
                return defaultValue(method.getReturnType());
            });
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T createProxy(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }
}
