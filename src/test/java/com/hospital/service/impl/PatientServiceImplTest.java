package com.hospital.service.impl;

import com.hospital.TestSupport;
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
    public void rejectsOverdrawWithoutUpdatingInventoryOrSeek() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicInteger inventoryUpdates = new AtomicInteger();
        AtomicInteger seekUpdates = new AtomicInteger();
        TestSupport.setField(service, "drugsMapper", TestSupport.proxy(DrugsMapper.class, (proxy, method, args) -> {
            if (method.getName().equals("selectByPrimaryKey")) {
                return drug(1, "medicine", "10.00", 3);
            }
            if (method.getName().equals("updateNumber")) {
                inventoryUpdates.incrementAndGet();
                return 1;
            }
            return null;
        }));
        TestSupport.setField(service, "patientMapper", TestSupport.proxy(PatientMapper.class, (proxy, method, args) -> 1));
        TestSupport.setField(service, "seekMapper", TestSupport.proxy(SeekMapper.class, (proxy, method, args) -> {
            if (method.getName().equals("updateDrugs")) {
                seekUpdates.incrementAndGet();
            }
            return 1;
        }));

        Patient patient = new Patient();
        patient.setId(5);
        patient.setDrugsids("1@4");

        assertEquals("对不起medicine数量不足", service.seek(patient));
        assertEquals(0, inventoryUpdates.get());
        assertEquals(0, seekUpdates.get());
    }

    @Test
    public void successfulDispenseUpdatesInventoryAndSeekPrice() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicInteger inventoryUpdates = new AtomicInteger();
        AtomicReference<Seek> updatedSeek = new AtomicReference<>();
        TestSupport.setField(service, "drugsMapper", TestSupport.proxy(DrugsMapper.class, (proxy, method, args) -> {
            if (method.getName().equals("selectByPrimaryKey")) {
                return drug(1, "medicine", "12.50", 10);
            }
            if (method.getName().equals("updateNumber")) {
                Drugs drugs = (Drugs) args[0];
                assertEquals(Integer.valueOf(2), drugs.getNumber());
                inventoryUpdates.incrementAndGet();
                return 1;
            }
            return null;
        }));
        TestSupport.setField(service, "patientMapper", TestSupport.proxy(PatientMapper.class, (proxy, method, args) -> 1));
        TestSupport.setField(service, "seekMapper", TestSupport.proxy(SeekMapper.class, (proxy, method, args) -> {
            if (method.getName().equals("updateDrugs")) {
                updatedSeek.set((Seek) args[0]);
            }
            return 1;
        }));

        Patient patient = new Patient();
        patient.setId(5);
        patient.setDrugsids("1@2");

        assertEquals(CommonService.upd_message_success, service.seek(patient));
        assertEquals(1, inventoryUpdates.get());
        assertEquals(new BigDecimal("25.00"), updatedSeek.get().getPrice());
        assertEquals(Integer.valueOf(5), updatedSeek.get().getPatientid());
    }

    private Drugs drug(Integer id, String name, String price, Integer number) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setName(name);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(number);
        return drugs;
    }
}
