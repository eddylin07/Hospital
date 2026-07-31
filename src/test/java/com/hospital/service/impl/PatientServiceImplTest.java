package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PatientServiceImplTest {
    @Test
    public void overstockRequestDoesNotDeductInventoryOrUpdateSeek() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicInteger inventoryUpdates = new AtomicInteger();
        AtomicInteger patientUpdates = new AtomicInteger();
        AtomicInteger seekUpdates = new AtomicInteger();
        service.drugsMapper = mapper(DrugsMapper.class, (proxy, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                return drug(14, "TestDrug", 50, "2.00");
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

        String message = service.seek(patient("14@75"));

        assertEquals("对不起TestDrug数量不足", message);
        assertEquals(0, inventoryUpdates.get());
        assertEquals(0, patientUpdates.get());
        assertEquals(0, seekUpdates.get());
    }

    @Test
    public void failedAtomicDeductionDoesNotUpdatePatientOrSeek() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicInteger patientUpdates = new AtomicInteger();
        AtomicInteger seekUpdates = new AtomicInteger();
        service.drugsMapper = mapper(DrugsMapper.class, (proxy, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                return drug(14, "TestDrug", 50, "2.00");
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

        String message = service.seek(patient("14@5"));

        assertEquals("对不起TestDrug数量不足", message);
        assertEquals(0, patientUpdates.get());
        assertEquals(0, seekUpdates.get());
    }

    @Test
    public void successfulDispensingUpdatesInventoryAndSeek() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicInteger inventoryUpdates = new AtomicInteger();
        service.drugsMapper = mapper(DrugsMapper.class, (proxy, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                return drug(14, "TestDrug", 50, "2.00");
            }
            if ("updateNumber".equals(method.getName())) {
                Drugs drugs = (Drugs) args[0];
                assertEquals(Integer.valueOf(5), drugs.getNumber());
                inventoryUpdates.incrementAndGet();
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.patientMapper = mapper(PatientMapper.class, (proxy, method, args) -> 1);
        service.seekMapper = mapper(SeekMapper.class, (proxy, method, args) -> 1);

        String message = service.seek(patient("14@5"));

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(1, inventoryUpdates.get());
    }

    @Test
    public void mapperSqlKeepsInventoryAndLatestSeekGuards() throws Exception {
        String drugsMapperXml = resource("/mapper/DrugsMapper.xml");
        String seekMapperXml = resource("/mapper/SeekMapper.xml");

        assertTrue(drugsMapperXml.contains("where id=#{id} and number >= #{number}"));
        assertTrue(seekMapperXml.contains("order by id desc limit 1"));
    }

    private static Patient patient(String drugsids) {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setDrugsids(drugsids);
        return patient;
    }

    private static Drugs drug(Integer id, String name, Integer number, String price) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setName(name);
        drugs.setNumber(number);
        drugs.setPrice(new BigDecimal(price));
        return drugs;
    }

    private static String resource(String path) throws Exception {
        InputStream input = PatientServiceImplTest.class.getResourceAsStream(path);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) != -1) {
            output.write(buffer, 0, length);
        }
        return new String(output.toByteArray(), StandardCharsets.UTF_8);
    }

    private static <T> T mapper(Class<T> type, InvocationHandler handler) {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler));
    }

    private static Object defaultValue(Class<?> returnType) {
        if (returnType == Integer.TYPE) {
            return 0;
        }
        return null;
    }
}

