package com.hospital.service.impl;

import com.hospital.dao.DrugsMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import org.junit.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PatientServiceImplTest {
    @Test
    public void rejectsOverdrawWithoutWritingPatientOrSeekData() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(5);
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.patientMapper = patientMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;

        Patient patient = new Patient();
        patient.setId(8);
        patient.setDrugsids("1@6");

        assertEquals("对不起阿司匹林数量不足", service.seek(patient));
        assertEquals(0, drugsMapper.updateCount);
        assertEquals(0, patientMapper.updateCount);
        assertEquals(0, seekMapper.updateCount);
    }

    @Test
    public void mapperSqlKeepsInventoryAtomicAndUpdatesOnlyLatestSeek() throws Exception {
        String drugsMapper = new String(Files.readAllBytes(Paths.get("src/main/resources/mapper/DrugsMapper.xml")), StandardCharsets.UTF_8);
        String seekMapper = new String(Files.readAllBytes(Paths.get("src/main/resources/mapper/SeekMapper.xml")), StandardCharsets.UTF_8);

        assertTrue(drugsMapper.contains("where id=#{id} and number &gt;= #{number}"));
        assertTrue(seekMapper.contains("where patientid=#{patientid}"));
        assertTrue(seekMapper.contains("order by id desc"));
        assertTrue(seekMapper.contains("limit 1"));
    }

    private static class FakePatientMapper implements PatientMapper {
        private int updateCount;

        @Override
        public int updateByPrimaryKeySelective(Patient record) {
            updateCount++;
            return 1;
        }

        @Override
        public List<Patient> findAll(String name, String certId) {
            return null;
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
            return null;
        }
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private final int stock;
        private int updateCount;

        private FakeDrugsMapper(int stock) {
            this.stock = stock;
        }

        @Override
        public Drugs selectByPrimaryKey(Integer id) {
            Drugs drugs = new Drugs();
            drugs.setId(id);
            drugs.setName("阿司匹林");
            drugs.setPrice(new BigDecimal("10.0"));
            drugs.setNumber(stock);
            return drugs;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateCount++;
            return 1;
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
            return null;
        }

        @Override
        public Drugs findByName(String name) {
            return null;
        }

        @Override
        public List<Drugs> getDrugsByName(String name) {
            return null;
        }
    }

    private static class FakeSeekMapper implements SeekMapper {
        private int updateCount;

        @Override
        public Integer updateDrugs(Seek seek) {
            updateCount++;
            return 1;
        }

        @Override
        public Integer insert(Seek seek) {
            return 0;
        }

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            return null;
        }
    }
}
