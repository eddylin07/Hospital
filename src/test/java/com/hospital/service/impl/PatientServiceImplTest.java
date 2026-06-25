package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PatientServiceImplTest {
    @Test
    public void rejectsDispensingMoreThanAvailableStockBeforeWriting() {
        PatientServiceImpl service = serviceWithDrug(5, 1, 1, true);
        Patient patient = patientWithDrugs("1@6");

        String message = service.seek(patient);

        assertEquals("\u5bf9\u4e0d\u8d77\u836f\u54c11\u6570\u91cf\u4e0d\u8db3", message);
        assertEquals(0, ((StubDrugsMapper) service.drugsMapper).updateNumberCalls);
        assertEquals(0, ((StubPatientMapper) service.patientMapper).updateCalls);
        assertNull(((StubSeekMapper) service.seekMapper).updatedSeek);
    }

    @Test
    public void failedConditionalInventoryUpdateStopsPrescriptionWrite() {
        PatientServiceImpl service = serviceWithDrug(5, 1, 1, false);
        Patient patient = patientWithDrugs("1@3");

        String message = service.seek(patient);

        assertEquals("\u5bf9\u4e0d\u8d77\u836f\u54c11\u6570\u91cf\u4e0d\u8db3", message);
        assertEquals(1, ((StubDrugsMapper) service.drugsMapper).updateNumberCalls);
        assertEquals(0, ((StubPatientMapper) service.patientMapper).updateCalls);
        assertNull(((StubSeekMapper) service.seekMapper).updatedSeek);
    }

    @Test
    public void validDispenseUpdatesInventoryAndLatestSeek() {
        PatientServiceImpl service = serviceWithDrug(5, 1, 1, true);
        Patient patient = patientWithDrugs("1@3");

        String message = service.seek(patient);

        StubDrugsMapper drugsMapper = (StubDrugsMapper) service.drugsMapper;
        StubPatientMapper patientMapper = (StubPatientMapper) service.patientMapper;
        StubSeekMapper seekMapper = (StubSeekMapper) service.seekMapper;
        assertEquals(CommonService.upd_message_success, message);
        assertEquals(1, drugsMapper.updateNumberCalls);
        assertEquals(Integer.valueOf(3), drugsMapper.lastUpdatedDrug.getNumber());
        assertEquals(1, patientMapper.updateCalls);
        assertEquals("1@3", seekMapper.updatedSeek.getDrugs());
        assertEquals(new BigDecimal("4.50"), seekMapper.updatedSeek.getPrice());
    }

    private PatientServiceImpl serviceWithDrug(Integer stock, int patientUpdateRows, int seekUpdateRows, boolean inventoryUpdateSucceeds) {
        PatientServiceImpl service = new PatientServiceImpl();
        service.drugsMapper = new StubDrugsMapper(stock, inventoryUpdateSucceeds);
        service.patientMapper = new StubPatientMapper(patientUpdateRows);
        service.seekMapper = new StubSeekMapper(seekUpdateRows);
        return service;
    }

    private Patient patientWithDrugs(String drugsids) {
        Patient patient = new Patient();
        patient.setId(10);
        patient.setDrugsids(drugsids);
        return patient;
    }

    private static class StubDrugsMapper implements DrugsMapper {
        private final Integer stock;
        private final boolean inventoryUpdateSucceeds;
        private int updateNumberCalls;
        private Drugs lastUpdatedDrug;

        private StubDrugsMapper(Integer stock, boolean inventoryUpdateSucceeds) {
            this.stock = stock;
            this.inventoryUpdateSucceeds = inventoryUpdateSucceeds;
        }

        @Override
        public Drugs selectByPrimaryKey(Integer id) {
            Drugs drugs = new Drugs();
            drugs.setId(id);
            drugs.setName("\u836f\u54c1" + id);
            drugs.setNumber(stock);
            drugs.setPrice(new BigDecimal("1.50"));
            return drugs;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateNumberCalls++;
            lastUpdatedDrug = drugs;
            return inventoryUpdateSucceeds ? 1 : 0;
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int insert(Drugs record) {
            return 0;
        }

        @Override
        public int insertSelective(Drugs record) {
            return 0;
        }

        @Override
        public int updateByPrimaryKeySelective(Drugs record) {
            return 0;
        }

        @Override
        public int updateByPrimaryKey(Drugs record) {
            return 0;
        }

        @Override
        public List<Drugs> findAll(Drugs drugs) {
            return Collections.emptyList();
        }

        @Override
        public Drugs findByName(String name) {
            return null;
        }

        @Override
        public List<Drugs> getDrugsByName(String name) {
            return Collections.emptyList();
        }
    }

    private static class StubPatientMapper implements PatientMapper {
        private final int updateRows;
        private int updateCalls;

        private StubPatientMapper(int updateRows) {
            this.updateRows = updateRows;
        }

        @Override
        public int updateByPrimaryKeySelective(Patient record) {
            updateCalls++;
            return updateRows;
        }

        @Override
        public List<Patient> findAll(String name, String certId) {
            return Collections.emptyList();
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int insert(Patient record) {
            return 0;
        }

        @Override
        public int insertSelective(Patient record) {
            return 0;
        }

        @Override
        public Patient selectByPrimaryKey(Integer id) {
            return null;
        }

        @Override
        public int updateByPrimaryKey(Patient record) {
            return 0;
        }

        @Override
        public Patient findPatientByCertId(String certId) {
            return null;
        }

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            return null;
        }

        @Override
        public List<Patient> getPatientByName(String name) {
            return Collections.emptyList();
        }
    }

    private static class StubSeekMapper implements SeekMapper {
        private final int updateRows;
        private Seek updatedSeek;

        private StubSeekMapper(int updateRows) {
            this.updateRows = updateRows;
        }

        @Override
        public Integer updateDrugs(Seek seek) {
            updatedSeek = seek;
            return updateRows;
        }

        @Override
        public Integer insert(Seek seek) {
            return 0;
        }

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            Seek seek = new Seek();
            seek.setPatientid(patientid);
            return seek;
        }
    }
}
