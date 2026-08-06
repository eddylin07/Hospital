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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.hospital.TestProxies.defaultValue;
import static com.hospital.TestProxies.proxy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PatientServiceImplTest {
    @Test
    public void dispensingRejectsRequestedQuantityGreaterThanStockBeforeWriting() {
        AtomicBoolean stockUpdated = new AtomicBoolean(false);
        AtomicBoolean patientUpdated = new AtomicBoolean(false);
        AtomicBoolean seekUpdated = new AtomicBoolean(false);
        PatientServiceImpl service = serviceWithStock(5, stockUpdated, patientUpdated, seekUpdated, new AtomicReference<>());
        Patient patient = new Patient();
        patient.setId(3);
        patient.setDrugsids("11@6");

        String message = service.seek(patient);

        assertEquals("对不起药品数量不足", message);
        assertFalse(stockUpdated.get());
        assertFalse(patientUpdated.get());
        assertFalse(seekUpdated.get());
    }

    @Test
    public void successfulDispensingAtomicallyDeductsStockAndUpdatesSeek() {
        AtomicBoolean stockUpdated = new AtomicBoolean(false);
        AtomicBoolean patientUpdated = new AtomicBoolean(false);
        AtomicBoolean seekUpdated = new AtomicBoolean(false);
        AtomicReference<Seek> updatedSeek = new AtomicReference<>();
        PatientServiceImpl service = serviceWithStock(5, stockUpdated, patientUpdated, seekUpdated, updatedSeek);
        Patient patient = new Patient();
        patient.setId(3);
        patient.setDrugsids("11@2");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertTrue(stockUpdated.get());
        assertTrue(patientUpdated.get());
        assertTrue(seekUpdated.get());
        assertEquals(Integer.valueOf(3), updatedSeek.get().getPatientid());
        assertEquals("11@2", updatedSeek.get().getDrugs());
        assertEquals(0, new BigDecimal("7.00").compareTo(updatedSeek.get().getPrice()));
    }

    @Test
    public void mapperSqlKeepsInventoryAndSeekUpdatesBounded() throws Exception {
        String drugsMapper = resource("mapper/DrugsMapper.xml");
        String seekMapper = resource("mapper/SeekMapper.xml");

        assertTrue(drugsMapper.contains("where id=#{id} and number >= #{number}"));
        assertTrue(seekMapper.contains("order by id desc limit 1"));
        assertTrue(seekMapper.contains("join"));
    }

    private PatientServiceImpl serviceWithStock(final int stock,
                                                final AtomicBoolean stockUpdated,
                                                final AtomicBoolean patientUpdated,
                                                final AtomicBoolean seekUpdated,
                                                final AtomicReference<Seek> updatedSeek) {
        PatientServiceImpl service = new PatientServiceImpl();
        service.drugsMapper = proxy(DrugsMapper.class, (proxy, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                Drugs drugs = new Drugs();
                drugs.setId((Integer) args[0]);
                drugs.setNumber(stock);
                drugs.setPrice(new BigDecimal("3.50"));
                return drugs;
            }
            if ("updateNumber".equals(method.getName())) {
                stockUpdated.set(true);
                Drugs drugs = (Drugs) args[0];
                return drugs.getNumber() <= stock ? 1 : 0;
            }
            return defaultValue(method.getReturnType());
        });
        service.patientMapper = proxy(PatientMapper.class, (proxy, method, args) -> {
            if ("updateByPrimaryKeySelective".equals(method.getName())) {
                patientUpdated.set(true);
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.seekMapper = proxy(SeekMapper.class, (proxy, method, args) -> {
            if ("updateDrugs".equals(method.getName())) {
                seekUpdated.set(true);
                updatedSeek.set((Seek) args[0]);
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.loginMapper = proxy(LoginMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        service.doctorMapper = proxy(DoctorMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        service.illnessMapper = proxy(IllnessMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        return service;
    }

    private String resource(String name) throws Exception {
        InputStream input = getClass().getClassLoader().getResourceAsStream(name);
        assertNotNull(input);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;
        while ((read = input.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }
        return output.toString("UTF-8");
    }
}
