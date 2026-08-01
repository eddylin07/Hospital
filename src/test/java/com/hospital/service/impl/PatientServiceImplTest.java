package com.hospital.service.impl;

import com.hospital.TestProxy;
import com.hospital.common.CommonService;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

public class PatientServiceImplTest {
    @Test
    public void seekDoesNotDeductInventoryWhenRequestedQuantityExceedsStock() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicInteger updateNumberCalls = new AtomicInteger(0);
        service.drugsMapper = TestProxy.of(DrugsMapper.class, (proxy, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                return drug(1, 3, "10.00");
            }
            if ("updateNumber".equals(method.getName())) {
                updateNumberCalls.incrementAndGet();
                return 1;
            }
            throw new AssertionError("Unexpected method: " + method.getName());
        });
        service.patientMapper = TestProxy.of(PatientMapper.class, (proxy, method, args) -> {
            throw new AssertionError("Patient should not be updated when inventory is insufficient");
        });
        service.seekMapper = TestProxy.of(SeekMapper.class, (proxy, method, args) -> {
            throw new AssertionError("Seek should not be updated when inventory is insufficient");
        });
        Patient patient = new Patient();
        patient.setId(42);
        patient.setDrugsids("1@5");

        String message = service.seek(patient);

        assertEquals("对不起3数量不足", message);
        assertEquals(0, updateNumberCalls.get());
    }

    @Test
    public void seekDeductsInventoryAndUpdatesSeekWhenStockIsSufficient() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicReference<Drugs> deductedDrug = new AtomicReference<>();
        AtomicReference<Seek> updatedSeek = new AtomicReference<>();
        service.drugsMapper = TestProxy.of(DrugsMapper.class, (proxy, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                return drug(1, 5, "2.50");
            }
            if ("updateNumber".equals(method.getName())) {
                deductedDrug.set((Drugs) args[0]);
                return 1;
            }
            throw new AssertionError("Unexpected method: " + method.getName());
        });
        service.patientMapper = TestProxy.of(PatientMapper.class, (proxy, method, args) -> {
            if ("updateByPrimaryKeySelective".equals(method.getName())) {
                return 1;
            }
            throw new AssertionError("Unexpected method: " + method.getName());
        });
        service.seekMapper = TestProxy.of(SeekMapper.class, (proxy, method, args) -> {
            if ("updateDrugs".equals(method.getName())) {
                updatedSeek.set((Seek) args[0]);
                return 1;
            }
            throw new AssertionError("Unexpected method: " + method.getName());
        });
        Patient patient = new Patient();
        patient.setId(42);
        patient.setDrugsids("1@3");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(3, deductedDrug.get().getNumber().intValue());
        assertEquals(42, updatedSeek.get().getPatientid().intValue());
        assertEquals("1@3", updatedSeek.get().getDrugs());
        assertEquals(new BigDecimal("7.50"), updatedSeek.get().getPrice());
    }

    private Drugs drug(Integer id, Integer number, String price) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setNumber(number);
        drugs.setPrice(new BigDecimal(price));
        return drugs;
    }
}
