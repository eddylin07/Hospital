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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PatientServiceImplTest {
    @Test
    public void overdrawDoesNotUpdateInventoryOrPatientRecords() {
        PatientServiceImpl service=newService(new FakeDrugsMapper(drug(1,5),1),new FakePatientMapper(),new FakeSeekMapper(true));
        Patient patient=new Patient();
        patient.setId(8);
        patient.setDrugsids("1@6");

        assertEquals("对不起drug-1数量不足",service.seek(patient));
        FakeDrugsMapper drugsMapper=(FakeDrugsMapper)service.drugsMapper;
        FakePatientMapper patientMapper=(FakePatientMapper)service.patientMapper;
        FakeSeekMapper seekMapper=(FakeSeekMapper)service.seekMapper;
        assertEquals(0,drugsMapper.updateNumberCount);
        assertEquals(0,patientMapper.updateCount);
        assertEquals(0,seekMapper.updateDrugsCount);
    }

    @Test
    public void failedAtomicInventoryUpdateDoesNotWritePrescription() {
        PatientServiceImpl service=newService(new FakeDrugsMapper(drug(1,5),0),new FakePatientMapper(),new FakeSeekMapper(true));
        Patient patient=new Patient();
        patient.setId(8);
        patient.setDrugsids("1@4");

        assertEquals("对不起drug-1数量不足",service.seek(patient));
        FakeDrugsMapper drugsMapper=(FakeDrugsMapper)service.drugsMapper;
        FakePatientMapper patientMapper=(FakePatientMapper)service.patientMapper;
        FakeSeekMapper seekMapper=(FakeSeekMapper)service.seekMapper;
        assertEquals(1,drugsMapper.updateNumberCount);
        assertEquals(0,patientMapper.updateCount);
        assertEquals(0,seekMapper.updateDrugsCount);
    }

    @Test
    public void laterFailureInMultiDrugDispenseDoesNotWritePrescription() {
        PatientServiceImpl service=newService(new FakeDrugsMapper(drug(1,5),1),new FakePatientMapper(),new FakeSeekMapper(true));
        Patient patient=new Patient();
        patient.setId(8);
        patient.setDrugsids("1@4,1@6");

        assertEquals("对不起drug-1数量不足",service.seek(patient));
        FakeDrugsMapper drugsMapper=(FakeDrugsMapper)service.drugsMapper;
        FakePatientMapper patientMapper=(FakePatientMapper)service.patientMapper;
        FakeSeekMapper seekMapper=(FakeSeekMapper)service.seekMapper;
        assertEquals(1,drugsMapper.updateNumberCount);
        assertEquals(0,patientMapper.updateCount);
        assertEquals(0,seekMapper.updateDrugsCount);
    }

    @Test
    public void validDispenseUpdatesPatientAndLatestSeek() {
        FakeSeekMapper seekMapper=new FakeSeekMapper(true);
        PatientServiceImpl service=newService(new FakeDrugsMapper(drug(1,5),1),new FakePatientMapper(),seekMapper);
        Patient patient=new Patient();
        patient.setId(8);
        patient.setDrugsids("1@4");

        assertEquals(CommonService.upd_message_success,service.seek(patient));
        assertEquals(1,((FakeDrugsMapper)service.drugsMapper).updateNumberCount);
        assertEquals(1,((FakePatientMapper)service.patientMapper).updateCount);
        assertEquals(1,seekMapper.updateDrugsCount);
        assertNotNull(seekMapper.updatedSeek);
        assertEquals(Integer.valueOf(8),seekMapper.updatedSeek.getPatientid());
        assertEquals("1@4",seekMapper.updatedSeek.getDrugs());
        assertEquals(new BigDecimal("40.00"),seekMapper.updatedSeek.getPrice());
    }

    private PatientServiceImpl newService(FakeDrugsMapper drugsMapper, FakePatientMapper patientMapper, FakeSeekMapper seekMapper) {
        PatientServiceImpl service=new PatientServiceImpl();
        service.drugsMapper=drugsMapper;
        service.patientMapper=patientMapper;
        service.seekMapper=seekMapper;
        service.loginMapper=new FakeLoginMapper();
        service.doctorMapper=new FakeDoctorMapper();
        service.illnessMapper=new FakeIllnessMapper();
        return service;
    }

    private static Drugs drug(Integer id, Integer number) {
        Drugs drugs=new Drugs();
        drugs.setId(id);
        drugs.setName("drug-"+id);
        drugs.setNumber(number);
        drugs.setPrice(new BigDecimal("10.00"));
        return drugs;
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private final Drugs drug;
        private final int updateResult;
        private int updateNumberCount;

        private FakeDrugsMapper(Drugs drug, int updateResult) {
            this.drug=drug;
            this.updateResult=updateResult;
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateNumberCount++;
            return updateResult;
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
            return drug;
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

    private static class FakePatientMapper implements PatientMapper {
        private int updateCount;

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
        public int updateByPrimaryKeySelective(Patient record) {
            updateCount++;
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
            return null;
        }
    }

    private static class FakeSeekMapper implements SeekMapper {
        private final boolean hasSeek;
        private int updateDrugsCount;
        private Seek updatedSeek;

        private FakeSeekMapper(boolean hasSeek) {
            this.hasSeek=hasSeek;
        }

        @Override
        public Integer insert(Seek seek) {
            return 0;
        }

        @Override
        public Integer updateDrugs(Seek seek) {
            updateDrugsCount++;
            updatedSeek=seek;
            return 1;
        }

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            return hasSeek?new Seek():null;
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
            return null;
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
            return null;
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
            return null;
        }

        @Override
        public Doctor getDoctorByLoginId(Integer loginid) {
            return null;
        }

        @Override
        public List<Doctor> getDoctorByName(String name) {
            return null;
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
            return null;
        }

        @Override
        public Illness getIllness(Integer id) {
            return null;
        }

        @Override
        public List<Illness> getIllnessByName(String name) {
            return null;
        }
    }
}
