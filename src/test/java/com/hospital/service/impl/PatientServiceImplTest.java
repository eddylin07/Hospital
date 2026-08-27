package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DoctorMapper;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.IllnessMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Doctor;
import com.hospital.entity.Drugs;
import com.hospital.entity.Illness;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import org.junit.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PatientServiceImplTest {

    @Test
    public void seekRejectsRequestsThatExceedStockBeforeWritingAnything() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper();
        drugsMapper.stock = 1;
        drugsMapper.price = new BigDecimal("2.50");
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
        service.drugsMapper = drugsMapper;

        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("3@2");

        String message = service.seek(patient);

        assertEquals("对不起药品数量不足", message);
        assertEquals(0, drugsMapper.updateNumberCalls);
        assertEquals(0, patientMapper.updateSelectiveCalls);
        assertEquals(0, seekMapper.updateDrugsCalls);
    }

    @Test
    public void seekStopsBeforePatientAndSeekUpdatesWhenAtomicStockDeductionFails() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper();
        drugsMapper.stock = 5;
        drugsMapper.price = new BigDecimal("2.50");
        drugsMapper.updateNumberResult = 0;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
        service.drugsMapper = drugsMapper;

        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("3@2");

        String message = service.seek(patient);

        assertEquals("对不起药品数量不足", message);
        assertEquals(1, drugsMapper.updateNumberCalls);
        assertEquals(0, patientMapper.updateSelectiveCalls);
        assertEquals(0, seekMapper.updateDrugsCalls);
    }

    @Test
    public void seekUpdatesOnlyTheLatestSeekRowForThePatient() throws Exception {
        String mapperXml = new String(
                Files.readAllBytes(Paths.get("src/main/resources/mapper/SeekMapper.xml")),
                StandardCharsets.UTF_8);

        assertTrue(mapperXml.contains("where id = ("));
        assertTrue(mapperXml.contains("select id from seek where patientid=#{patientid} order by id desc limit 1"));
        assertFalse(mapperXml.contains("set drugs=#{drugs},price=price+#{price}\n     where patientid=#{patientid}"));
    }

    @Test
    public void drugDeductionSqlUsesStockGuard() throws Exception {
        String mapperXml = new String(
                Files.readAllBytes(Paths.get("src/main/resources/mapper/DrugsMapper.xml")),
                StandardCharsets.UTF_8);

        assertTrue(mapperXml.contains("where id=#{id} and number &gt;= #{number}"));
    }

    private static class FakePatientMapper implements PatientMapper {
        int updateSelectiveCalls;

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
        public int updateByPrimaryKeySelective(Patient record) {
            updateSelectiveCalls++;
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

    private static class FakeDrugsMapper implements DrugsMapper {
        int stock;
        BigDecimal price = BigDecimal.ONE;
        int updateNumberResult = 1;
        int updateNumberCalls;

        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateNumberCalls++;
            return updateNumberResult;
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
            Drugs drugs = new Drugs();
            drugs.setId(id);
            drugs.setNumber(stock);
            drugs.setPrice(price);
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

    private static class FakeSeekMapper implements SeekMapper {
        int updateDrugsCalls;

        @Override
        public Integer insert(Seek seek) {
            return 0;
        }

        @Override
        public Integer updateDrugs(Seek seek) {
            updateDrugsCalls++;
            return 1;
        }

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            return null;
        }
    }
}
