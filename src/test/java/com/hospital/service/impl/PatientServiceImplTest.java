package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PatientServiceImplTest {

    @Test
    public void rejectsPrescriptionThatExceedsStockWithoutWriting() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicInteger stockUpdates = new AtomicInteger();
        AtomicBoolean patientUpdated = new AtomicBoolean(false);
        AtomicBoolean seekUpdated = new AtomicBoolean(false);

        service.drugsMapper = proxy(DrugsMapper.class, (ignored, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                return drug(1, "Aspirin", "2.50", 5);
            }
            if ("updateNumber".equals(method.getName())) {
                stockUpdates.incrementAndGet();
            }
            return defaultValue(method.getReturnType());
        });
        service.patientMapper = proxy(PatientMapper.class, (ignored, method, args) -> {
            if ("updateByPrimaryKeySelective".equals(method.getName())) {
                patientUpdated.set(true);
            }
            return defaultValue(method.getReturnType());
        });
        service.seekMapper = proxy(SeekMapper.class, (ignored, method, args) -> {
            if ("updateDrugs".equals(method.getName())) {
                seekUpdated.set(true);
            }
            return defaultValue(method.getReturnType());
        });

        Patient patient = new Patient();
        patient.setId(42);
        patient.setDrugsids("1@6");

        String message = service.seek(patient);

        assertEquals("对不起Aspirin数量不足", message);
        assertEquals(0, stockUpdates.get());
        assertFalse(patientUpdated.get());
        assertFalse(seekUpdated.get());
    }

    @Test
    public void deductsStockAndUpdatesSeekWhenPrescriptionIsValid() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicReference<Drugs> deducted = new AtomicReference<>();
        AtomicReference<Seek> updatedSeek = new AtomicReference<>();

        service.drugsMapper = proxy(DrugsMapper.class, (ignored, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                return drug(1, "Aspirin", "2.50", 5);
            }
            if ("updateNumber".equals(method.getName())) {
                deducted.set((Drugs) args[0]);
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.patientMapper = proxy(PatientMapper.class, (ignored, method, args) -> {
            if ("updateByPrimaryKeySelective".equals(method.getName())) {
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.seekMapper = proxy(SeekMapper.class, (ignored, method, args) -> {
            if ("updateDrugs".equals(method.getName())) {
                updatedSeek.set((Seek) args[0]);
                return 1;
            }
            return defaultValue(method.getReturnType());
        });

        Patient patient = new Patient();
        patient.setId(42);
        patient.setDrugsids("1@3");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(Integer.valueOf(1), deducted.get().getId());
        assertEquals(Integer.valueOf(3), deducted.get().getNumber());
        assertEquals(0, new BigDecimal("7.50").compareTo(updatedSeek.get().getPrice()));
        assertEquals(Integer.valueOf(42), updatedSeek.get().getPatientid());
    }

    @Test
    public void mapperSqlGuardsStockAndUpdatesOnlyLatestSeek() throws Exception {
        String drugsMapperXml = resource("/mapper/DrugsMapper.xml");
        String seekMapperXml = resource("/mapper/SeekMapper.xml");

        assertTrue(drugsMapperXml.contains("number &gt;= #{number}"));
        assertTrue(seekMapperXml.contains("where id ="));
        assertTrue(seekMapperXml.contains("select max(id) as id from seek where patientid=#{patientid}"));
    }

    private static Drugs drug(Integer id, String name, String price, Integer number) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setName(name);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(number);
        return drugs;
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler);
    }

    private static Object defaultValue(Class<?> returnType) {
        if (!returnType.isPrimitive()) {
            return null;
        }
        if (returnType.equals(boolean.class)) {
            return false;
        }
        return 0;
    }

    private static String resource(String name) throws Exception {
        InputStream stream = PatientServiceImplTest.class.getResourceAsStream(name);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
        }
        return builder.toString();
    }
}
