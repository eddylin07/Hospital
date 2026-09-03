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
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class PatientServiceImplTest {

    @Test
    public void emptyDrugSelectionDoesNotUpdateInventory() {
        AtomicInteger inventoryUpdates = new AtomicInteger();
        PatientServiceImpl service = service(drugsMapper(null, inventoryUpdates), patientMapper(1), seekMapper(1));
        Patient patient = new Patient();
        patient.setId(1);
        patient.setDrugsids("");

        String message = service.seek(patient);

        Assert.assertEquals("请选择药品", message);
        Assert.assertEquals(0, inventoryUpdates.get());
    }

    @Test
    public void rejectsOverdrawBeforeDeductingInventory() {
        Drugs drug = drug(1, "阿莫西林", "25.00", 2);
        AtomicInteger inventoryUpdates = new AtomicInteger();
        PatientServiceImpl service = service(drugsMapper(drug, inventoryUpdates), patientMapper(1), seekMapper(1));
        Patient patient = new Patient();
        patient.setId(1);
        patient.setDrugsids("1@3");

        String message = service.seek(patient);

        Assert.assertEquals("对不起阿莫西林数量不足", message);
        Assert.assertEquals(0, inventoryUpdates.get());
    }

    @Test
    public void validDispenseUpdatesInventoryAndLatestSeek() {
        Drugs drug = drug(1, "阿莫西林", "25.00", 5);
        AtomicInteger inventoryUpdates = new AtomicInteger();
        AtomicInteger patientUpdates = new AtomicInteger();
        AtomicInteger seekUpdates = new AtomicInteger();
        PatientServiceImpl service = service(
                drugsMapper(drug, inventoryUpdates),
                patientMapper(patientUpdates),
                seekMapper(seekUpdates)
        );
        Patient patient = new Patient();
        patient.setId(1);
        patient.setDrugsids("1@3");

        String message = service.seek(patient);

        Assert.assertEquals(CommonService.upd_message_success, message);
        Assert.assertEquals(1, inventoryUpdates.get());
        Assert.assertEquals(1, patientUpdates.get());
        Assert.assertEquals(1, seekUpdates.get());
    }

    private PatientServiceImpl service(DrugsMapper drugsMapper, PatientMapper patientMapper, SeekMapper seekMapper) {
        PatientServiceImpl service = new PatientServiceImpl();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
        service.loginMapper = mapper(LoginMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        service.doctorMapper = mapper(DoctorMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        service.illnessMapper = mapper(IllnessMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        return service;
    }

    private DrugsMapper drugsMapper(Drugs drug, AtomicInteger inventoryUpdates) {
        return mapper(DrugsMapper.class, (proxy, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                return drug;
            }
            if ("updateNumber".equals(method.getName())) {
                inventoryUpdates.incrementAndGet();
                Drugs update = (Drugs) args[0];
                Assert.assertEquals(Integer.valueOf(3), update.getNumber());
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
    }

    private PatientMapper patientMapper(AtomicInteger updates) {
        return mapper(PatientMapper.class, (proxy, method, args) -> {
            if ("updateByPrimaryKeySelective".equals(method.getName())) {
                updates.incrementAndGet();
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
    }

    private PatientMapper patientMapper(int updateResult) {
        return mapper(PatientMapper.class, (proxy, method, args) -> {
            if ("updateByPrimaryKeySelective".equals(method.getName())) {
                return updateResult;
            }
            return defaultValue(method.getReturnType());
        });
    }

    private SeekMapper seekMapper(AtomicInteger updates) {
        return mapper(SeekMapper.class, (proxy, method, args) -> {
            if ("updateDrugs".equals(method.getName())) {
                updates.incrementAndGet();
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
    }

    private SeekMapper seekMapper(int updateResult) {
        return mapper(SeekMapper.class, (proxy, method, args) -> {
            if ("updateDrugs".equals(method.getName())) {
                return updateResult;
            }
            return defaultValue(method.getReturnType());
        });
    }

    private Drugs drug(Integer id, String name, String price, Integer number) {
        Drugs drug = new Drugs();
        drug.setId(id);
        drug.setName(name);
        drug.setPrice(new BigDecimal(price));
        drug.setNumber(number);
        return drug;
    }

    @SuppressWarnings("unchecked")
    private <T> T mapper(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }

    private Object defaultValue(Class<?> returnType) {
        if (!returnType.isPrimitive()) {
            return null;
        }
        if (returnType == boolean.class) {
            return false;
        }
        return 0;
    }
}
