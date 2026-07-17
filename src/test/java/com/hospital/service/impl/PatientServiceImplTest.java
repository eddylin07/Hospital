package com.hospital.service.impl;

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

public class PatientServiceImplTest {
    @Test
    public void seekRejectsQuantityGreaterThanStockBeforeAnyWrite() {
        PatientServiceImpl service=new PatientServiceImpl();
        RecordingDrugsMapper drugsMapper=new RecordingDrugsMapper(drug(1,"Aspirin",5,"2.50"));
        RecordingPatientMapper patientMapper=new RecordingPatientMapper();
        RecordingSeekMapper seekMapper=new RecordingSeekMapper();
        service.drugsMapper=drugsMapper;
        service.patientMapper=patientMapper;
        service.seekMapper=seekMapper;
        Patient patient=new Patient();
        patient.setId(11);
        patient.setDrugsids("1@6");

        String message=service.seek(patient);

        assertEquals("对不起Aspirin数量不足",message);
        assertEquals(0,drugsMapper.updateNumberCalls);
        assertEquals(0,patientMapper.updateCalls);
        assertEquals(0,seekMapper.updateCalls);
    }

    @Test
    public void seekDeductsStockAndUpdatesSeekAfterValidation() {
        PatientServiceImpl service=new PatientServiceImpl();
        RecordingDrugsMapper drugsMapper=new RecordingDrugsMapper(drug(1,"Aspirin",5,"2.50"));
        RecordingPatientMapper patientMapper=new RecordingPatientMapper();
        RecordingSeekMapper seekMapper=new RecordingSeekMapper();
        service.drugsMapper=drugsMapper;
        service.patientMapper=patientMapper;
        service.seekMapper=seekMapper;
        Patient patient=new Patient();
        patient.setId(11);
        patient.setDrugsids("1@2");

        String message=service.seek(patient);

        assertEquals("更新成功",message);
        assertEquals(1,drugsMapper.updateNumberCalls);
        assertEquals(2,drugsMapper.lastDeductedNumber);
        assertEquals(1,patientMapper.updateCalls);
        assertEquals(1,seekMapper.updateCalls);
        assertEquals(new BigDecimal("5.00"),seekMapper.lastSeek.getPrice());
    }

    private static Drugs drug(Integer id,String name,Integer number,String price) {
        Drugs drugs=new Drugs();
        drugs.setId(id);
        drugs.setName(name);
        drugs.setNumber(number);
        drugs.setPrice(new BigDecimal(price));
        return drugs;
    }

    private static class RecordingDrugsMapper implements DrugsMapper {
        private final Drugs drugs;
        private int updateNumberCalls;
        private int lastDeductedNumber;

        private RecordingDrugsMapper(Drugs drugs) {
            this.drugs=drugs;
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateNumberCalls++;
            lastDeductedNumber=drugs.getNumber();
            return 1;
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
        public Drugs selectByPrimaryKey(Integer id) {
            return drugs;
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

    private static class RecordingPatientMapper implements PatientMapper {
        private int updateCalls;

        @Override
        public List<Patient> findAll(String name,String certId) {
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
        public int updateByPrimaryKeySelective(Patient record) {
            updateCalls++;
            return 1;
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

    private static class RecordingSeekMapper implements SeekMapper {
        private int updateCalls;
        private Seek lastSeek;

        @Override
        public Integer insert(Seek seek) {
            return 0;
        }

        @Override
        public Integer updateDrugs(Seek seek) {
            updateCalls++;
            lastSeek=seek;
            return 1;
        }

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            return null;
        }
    }
}
