package com.hospital.service.impl;

import com.hospital.TestProxies;
import com.hospital.common.CommonService;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class PatientServiceImplSeekTest {
    @Test
    public void requestedQuantityGreaterThanStockDoesNotWriteAnything() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicInteger drugUpdates = new AtomicInteger();
        AtomicInteger patientUpdates = new AtomicInteger();
        AtomicInteger seekUpdates = new AtomicInteger();
        service.drugsMapper = drugsMapper(5, drugUpdates, -1);
        service.patientMapper = patientMapper(patientUpdates);
        service.seekMapper = seekMapper(seekUpdates);
        Patient patient = new Patient();
        patient.setId(9);
        patient.setDrugsids("1@6");

        String message = service.seek(patient);

        assertEquals("对不起药品数量不足", message);
        assertEquals(0, drugUpdates.get());
        assertEquals(0, patientUpdates.get());
        assertEquals(0, seekUpdates.get());
    }

    @Test
    public void failedAtomicStockUpdateDoesNotWritePatientOrSeek() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicInteger drugUpdates = new AtomicInteger();
        AtomicInteger patientUpdates = new AtomicInteger();
        AtomicInteger seekUpdates = new AtomicInteger();
        service.drugsMapper = drugsMapper(5, drugUpdates, 0);
        service.patientMapper = patientMapper(patientUpdates);
        service.seekMapper = seekMapper(seekUpdates);
        Patient patient = new Patient();
        patient.setId(9);
        patient.setDrugsids("1@5");

        String message = service.seek(patient);

        assertEquals("对不起药品数量不足", message);
        assertEquals(1, drugUpdates.get());
        assertEquals(0, patientUpdates.get());
        assertEquals(0, seekUpdates.get());
    }

    @Test
    public void successfulDispenseUpdatesPatientAndLatestSeek() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicInteger drugUpdates = new AtomicInteger();
        AtomicInteger patientUpdates = new AtomicInteger();
        AtomicInteger seekUpdates = new AtomicInteger();
        service.drugsMapper = drugsMapper(5, drugUpdates, 1);
        service.patientMapper = patientMapper(patientUpdates);
        service.seekMapper = seekMapper(seekUpdates);
        Patient patient = new Patient();
        patient.setId(9);
        patient.setDrugsids("1@5");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(1, drugUpdates.get());
        assertEquals(1, patientUpdates.get());
        assertEquals(1, seekUpdates.get());
    }

    private DrugsMapper drugsMapper(Integer stock, AtomicInteger updates, int updateResult) {
        return TestProxies.proxy(DrugsMapper.class, (proxy, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                Drugs drugs = new Drugs();
                drugs.setId((Integer) args[0]);
                drugs.setNumber(stock);
                drugs.setPrice(new BigDecimal("2.00"));
                return drugs;
            }
            if ("updateNumber".equals(method.getName())) {
                updates.incrementAndGet();
                return updateResult;
            }
            return null;
        });
    }

    private PatientMapper patientMapper(AtomicInteger updates) {
        return TestProxies.proxy(PatientMapper.class, (proxy, method, args) -> {
            if ("updateByPrimaryKeySelective".equals(method.getName())) {
                updates.incrementAndGet();
                return 1;
            }
            return null;
        });
    }

    private SeekMapper seekMapper(AtomicInteger updates) {
        return TestProxies.proxy(SeekMapper.class, (proxy, method, args) -> {
            if ("updateDrugs".equals(method.getName())) {
                updates.incrementAndGet();
                return 1;
            }
            return null;
        });
    }
}
