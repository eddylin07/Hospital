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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PatientServiceImplTest {

    @Test
    public void seekRejectsInsufficientStockBeforeWritingPatientOrSeek() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(drug(7, 1, "12.50"), 1);
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;

        Patient patient = new Patient();
        patient.setId(3);
        patient.setDrugsids("7@2");

        String message = service.seek(patient);

        assertTrue(message.contains("数量不足"));
        assertEquals(0, drugsMapper.updateNumberCalls);
        assertEquals(0, patientMapper.updateSelectiveCalls);
        assertEquals(0, seekMapper.updateDrugsCalls);
    }

    @Test
    public void seekRollsBackWorkflowWhenAtomicStockUpdateFails() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(drug(7, 5, "12.50"), 0);
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;

        Patient patient = new Patient();
        patient.setId(3);
        patient.setDrugsids("7@2");

        String message = service.seek(patient);

        assertTrue(message.contains("数量不足"));
        assertEquals(1, drugsMapper.updateNumberCalls);
        assertEquals(0, patientMapper.updateSelectiveCalls);
        assertEquals(0, seekMapper.updateDrugsCalls);
    }

    @Test
    public void seekUpdatesPatientAndLatestSeekAfterSuccessfulAtomicStockUpdate() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(drug(7, 5, "12.50"), 1);
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;

        Patient patient = new Patient();
        patient.setId(3);
        patient.setDrugsids("7@2");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(1, drugsMapper.updateNumberCalls);
        assertEquals(Integer.valueOf(2), drugsMapper.lastUpdatedDrug.getNumber());
        assertEquals(1, patientMapper.updateSelectiveCalls);
        assertEquals(1, seekMapper.updateDrugsCalls);
        assertNotNull(seekMapper.lastSeek);
        assertEquals(Integer.valueOf(3), seekMapper.lastSeek.getPatientid());
        assertEquals("7@2", seekMapper.lastSeek.getDrugs());
        assertEquals(0, new BigDecimal("25.00").compareTo(seekMapper.lastSeek.getPrice()));
    }

    private static Drugs drug(Integer id, Integer number, String price) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setName("drug-" + id);
        drugs.setNumber(number);
        drugs.setPrice(new BigDecimal(price));
        return drugs;
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private final Drugs drug;
        private final int updateResult;
        private int updateNumberCalls;
        private Drugs lastUpdatedDrug;

        private FakeDrugsMapper(Drugs drug, int updateResult) {
            this.drug = drug;
            this.updateResult = updateResult;
        }

        @Override
        public Drugs selectByPrimaryKey(Integer id) {
            return drug;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateNumberCalls++;
            lastUpdatedDrug = drugs;
            return updateResult;
        }

        @Override public int deleteByPrimaryKey(Integer id) { throw new UnsupportedOperationException(); }
        @Override public int insert(Drugs record) { throw new UnsupportedOperationException(); }
        @Override public int insertSelective(Drugs record) { throw new UnsupportedOperationException(); }
        @Override public int updateByPrimaryKeySelective(Drugs record) { throw new UnsupportedOperationException(); }
        @Override public int updateByPrimaryKey(Drugs record) { throw new UnsupportedOperationException(); }
        @Override public List<Drugs> findAll(Drugs drugs) { throw new UnsupportedOperationException(); }
        @Override public Drugs findByName(String name) { throw new UnsupportedOperationException(); }
        @Override public List<Drugs> getDrugsByName(String name) { throw new UnsupportedOperationException(); }
    }

    private static class FakePatientMapper implements PatientMapper {
        private int updateSelectiveCalls;

        @Override
        public int updateByPrimaryKeySelective(Patient record) {
            updateSelectiveCalls++;
            return 1;
        }

        @Override public List<Patient> findAll(String name, String certId) { throw new UnsupportedOperationException(); }
        @Override public int deleteByPrimaryKey(Integer id) { throw new UnsupportedOperationException(); }
        @Override public int insert(Patient record) { throw new UnsupportedOperationException(); }
        @Override public int insertSelective(Patient record) { throw new UnsupportedOperationException(); }
        @Override public Patient selectByPrimaryKey(Integer id) { throw new UnsupportedOperationException(); }
        @Override public int updateByPrimaryKey(Patient record) { throw new UnsupportedOperationException(); }
        @Override public Patient findPatientByCertId(String certId) { throw new UnsupportedOperationException(); }
        @Override public Patient findPatientByLoginId(Integer loginid) { throw new UnsupportedOperationException(); }
        @Override public List<Patient> getPatientByName(String name) { throw new UnsupportedOperationException(); }
    }

    private static class FakeSeekMapper implements SeekMapper {
        private int updateDrugsCalls;
        private Seek lastSeek;

        @Override
        public Integer updateDrugs(Seek seek) {
            updateDrugsCalls++;
            lastSeek = seek;
            return 1;
        }

        @Override public Integer insert(Seek seek) { throw new UnsupportedOperationException(); }
        @Override public Seek getSeekByPatientId(Integer patientid) { throw new UnsupportedOperationException(); }
    }
}
