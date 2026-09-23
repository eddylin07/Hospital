package com.hospital.service.impl;

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

public class PatientServiceImplTest {
    @Test
    public void overdrawnPrescriptionDoesNotUpdatePatientOrSeekRows() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(5);
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;

        Patient patient = new Patient();
        patient.setId(10);
        patient.setDrugsids("1@6");

        assertEquals("对不起阿司匹林数量不足", service.seek(patient));
        assertEquals(1, drugsMapper.updateCalls);
        assertEquals(0, patientMapper.updateCalls);
        assertEquals(0, seekMapper.updateCalls);
    }

    @Test
    public void validPrescriptionUsesAtomicStockUpdateThenUpdatesLatestSeekData() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(5);
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;

        Patient patient = new Patient();
        patient.setId(10);
        patient.setDrugsids("1@2");

        assertEquals("更新成功", service.seek(patient));
        assertEquals(1, drugsMapper.updateCalls);
        assertEquals(1, patientMapper.updateCalls);
        assertEquals(1, seekMapper.updateCalls);
        assertEquals(Integer.valueOf(10), seekMapper.updatedSeek.getPatientid());
        assertEquals("1@2", seekMapper.updatedSeek.getDrugs());
        assertEquals(new BigDecimal("6.00"), seekMapper.updatedSeek.getPrice());
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private final int stock;
        int updateCalls;

        FakeDrugsMapper(int stock) {
            this.stock = stock;
        }

        @Override
        public Drugs selectByPrimaryKey(Integer id) {
            Drugs drugs = new Drugs();
            drugs.setId(id);
            drugs.setName("阿司匹林");
            drugs.setNumber(stock);
            drugs.setPrice(new BigDecimal("3.00"));
            return drugs;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateCalls++;
            return stock >= drugs.getNumber() ? 1 : 0;
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int insert(Drugs record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int insertSelective(Drugs record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int updateByPrimaryKeySelective(Drugs record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int updateByPrimaryKey(Drugs record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Drugs> findAll(Drugs drugs) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Drugs findByName(String name) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Drugs> getDrugsByName(String name) {
            throw new UnsupportedOperationException();
        }
    }

    private static class FakePatientMapper implements PatientMapper {
        int updateCalls;

        @Override
        public int updateByPrimaryKeySelective(Patient record) {
            updateCalls++;
            return 1;
        }

        @Override
        public List<Patient> findAll(String name, String certId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int insert(Patient record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int insertSelective(Patient record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Patient selectByPrimaryKey(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int updateByPrimaryKey(Patient record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Patient findPatientByCertId(String certId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Patient> getPatientByName(String name) {
            throw new UnsupportedOperationException();
        }
    }

    private static class FakeSeekMapper implements SeekMapper {
        int updateCalls;
        Seek updatedSeek;

        @Override
        public Integer updateDrugs(Seek seek) {
            updateCalls++;
            updatedSeek = seek;
            return 1;
        }

        @Override
        public Integer insert(Seek seek) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            throw new UnsupportedOperationException();
        }
    }
}
