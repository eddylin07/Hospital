package com.hospital.service;

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
import com.hospital.service.impl.PatientServiceImpl;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PatientServiceSeekTest {
    @Test
    public void overStockPrescriptionDoesNotWriteAnything() {
        PatientServiceImpl service = newService();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(drug(7, "Aspirin", "10.00", 1));
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        ReflectionTestUtils.setField(service, "drugsMapper", drugsMapper);
        ReflectionTestUtils.setField(service, "patientMapper", patientMapper);
        ReflectionTestUtils.setField(service, "seekMapper", seekMapper);
        Patient patient = new Patient();
        patient.setId(3);
        patient.setDrugsids("7@2");

        String message = service.seek(patient);

        assertEquals("对不起Aspirin数量不足", message);
        assertEquals(0, drugsMapper.updateNumberCalls);
        assertEquals(0, patientMapper.updateCalls);
        assertEquals(0, seekMapper.updateDrugsCalls);
    }

    @Test
    public void validPrescriptionUpdatesInventoryPatientAndLatestSeek() {
        PatientServiceImpl service = newService();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(drug(7, "Aspirin", "10.00", 5));
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        ReflectionTestUtils.setField(service, "drugsMapper", drugsMapper);
        ReflectionTestUtils.setField(service, "patientMapper", patientMapper);
        ReflectionTestUtils.setField(service, "seekMapper", seekMapper);
        Patient patient = new Patient();
        patient.setId(3);
        patient.setDrugsids("7@2");

        String message = service.seek(patient);

        assertEquals("更新成功", message);
        assertEquals(1, drugsMapper.updateNumberCalls);
        assertEquals(Integer.valueOf(2), drugsMapper.lastUpdated.getNumber());
        assertEquals(1, patientMapper.updateCalls);
        assertEquals(1, seekMapper.updateDrugsCalls);
        assertEquals(Integer.valueOf(3), seekMapper.lastSeek.getPatientid());
        assertEquals(new BigDecimal("20.00"), seekMapper.lastSeek.getPrice());
    }

    private static PatientServiceImpl newService() {
        PatientServiceImpl service = new PatientServiceImpl();
        ReflectionTestUtils.setField(service, "loginMapper", new FakeLoginMapper());
        ReflectionTestUtils.setField(service, "doctorMapper", new FakeDoctorMapper());
        ReflectionTestUtils.setField(service, "illnessMapper", new FakeIllnessMapper());
        return service;
    }

    private static Drugs drug(Integer id, String name, String price, Integer number) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setName(name);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(number);
        return drugs;
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private final Drugs drug;
        private int updateNumberCalls;
        private Drugs lastUpdated;

        private FakeDrugsMapper(Drugs drug) {
            this.drug = drug;
        }

        @Override
        public Drugs selectByPrimaryKey(Integer id) {
            return drug != null && drug.getId().equals(id) ? drug : null;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateNumberCalls++;
            lastUpdated = drugs;
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
            return new ArrayList<>();
        }

        @Override
        public Drugs findByName(String name) {
            return null;
        }

        @Override
        public List<Drugs> getDrugsByName(String name) {
            return new ArrayList<>();
        }
    }

    private static class FakePatientMapper implements PatientMapper {
        private int updateCalls;

        @Override
        public int updateByPrimaryKeySelective(Patient record) {
            updateCalls++;
            return 1;
        }

        @Override
        public List<Patient> findAll(String name, String certId) {
            return new ArrayList<>();
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
            return new ArrayList<>();
        }
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

        @Override
        public Integer insert(Seek seek) {
            return 0;
        }

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            return null;
        }
    }

    private static class FakeLoginMapper implements LoginMapper {
        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int insert(Login record) {
            return 0;
        }

        @Override
        public int insertSelective(Login record) {
            return 0;
        }

        @Override
        public Login selectByPrimaryKey(Integer id) {
            return null;
        }

        @Override
        public int updateByPrimaryKeySelective(Login record) {
            return 0;
        }

        @Override
        public int updateByPrimaryKey(Login record) {
            return 0;
        }

        @Override
        public List<Login> findAllAdmin(String username) {
            return new ArrayList<>();
        }

        @Override
        public int insertAdmin(Login login) {
            return 0;
        }

        @Override
        public Login findByUsername(String username) {
            return null;
        }
    }

    private static class FakeDoctorMapper implements DoctorMapper {
        @Override
        public List<Doctor> getAll(String name, String certId) {
            return new ArrayList<>();
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int insert(Doctor record) {
            return 0;
        }

        @Override
        public int insertSelective(Doctor record) {
            return 0;
        }

        @Override
        public Doctor selectByPrimaryKey(Integer id) {
            return null;
        }

        @Override
        public int updateByPrimaryKeySelective(Doctor record) {
            return 0;
        }

        @Override
        public int updateByPrimaryKey(Doctor record) {
            return 0;
        }

        @Override
        public Doctor getDoctorByCertId(String certId) {
            return null;
        }

        @Override
        public List<Doctor> getDoctorByDepartment(String department) {
            return new ArrayList<>();
        }

        @Override
        public Doctor getDoctorByLoginId(Integer loginid) {
            return null;
        }

        @Override
        public List<Doctor> getDoctorByName(String name) {
            return new ArrayList<>();
        }
    }

    private static class FakeIllnessMapper implements IllnessMapper {
        @Override
        public Integer insert(Illness illness) {
            return 0;
        }

        @Override
        public Integer deleteById(Integer id) {
            return 0;
        }

        @Override
        public Integer updateById(Illness illness) {
            return 0;
        }

        @Override
        public List<Illness> selectAll() {
            return new ArrayList<>();
        }

        @Override
        public Illness getIllness(Integer id) {
            return null;
        }

        @Override
        public List<Illness> getIllnessByName(String name) {
            return new ArrayList<>();
        }
    }
}
