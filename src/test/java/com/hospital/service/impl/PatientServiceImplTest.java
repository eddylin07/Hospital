package com.hospital.service.impl;

import com.hospital.dao.DrugsMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PatientServiceImplTest {

    @Test
    public void rejectsDispenseWhenRequestedQuantityExceedsStock() {
        PatientServiceImpl service=new PatientServiceImpl();
        FakeDrugsMapper drugsMapper=new FakeDrugsMapper();
        drugsMapper.addDrug(drug(10,"aspirin",1,"2.50"));
        FakePatientMapper patientMapper=new FakePatientMapper();
        FakeSeekMapper seekMapper=new FakeSeekMapper(42L);
        service.drugsMapper=drugsMapper;
        service.patientMapper=patientMapper;
        service.seekMapper=seekMapper;
        Patient patient=new Patient();
        patient.setId(7);
        patient.setDrugsids("10@2");

        String message=service.seek(patient);

        assertTrue(message.contains("数量不足"));
        assertEquals(Integer.valueOf(1),drugsMapper.drugsById.get(10).getNumber());
        assertEquals(0,drugsMapper.updateNumberCalls);
        assertNull(patientMapper.updatedPatient);
        assertNull(seekMapper.updatedSeek);
    }

    @Test
    public void updatesOnlyTheLatestSeekRowWhenDispensing() {
        PatientServiceImpl service=new PatientServiceImpl();
        FakeDrugsMapper drugsMapper=new FakeDrugsMapper();
        drugsMapper.addDrug(drug(10,"aspirin",10,"2.50"));
        FakePatientMapper patientMapper=new FakePatientMapper();
        FakeSeekMapper seekMapper=new FakeSeekMapper(42L);
        service.drugsMapper=drugsMapper;
        service.patientMapper=patientMapper;
        service.seekMapper=seekMapper;
        Patient patient=new Patient();
        patient.setId(7);
        patient.setDrugsids("10@2");

        String message=service.seek(patient);

        assertEquals("更新成功",message);
        assertEquals(Integer.valueOf(8),drugsMapper.drugsById.get(10).getNumber());
        assertEquals(42L,seekMapper.updatedSeek.getId());
        assertEquals(Integer.valueOf(7),seekMapper.updatedSeek.getPatientid());
        assertEquals("10@2",seekMapper.updatedSeek.getDrugs());
        assertEquals(0,new BigDecimal("5.00").compareTo(seekMapper.updatedSeek.getPrice()));
    }

    private static Drugs drug(Integer id,String name,Integer number,String price) {
        Drugs drugs=new Drugs();
        drugs.setId(id);
        drugs.setName(name);
        drugs.setNumber(number);
        drugs.setPrice(new BigDecimal(price));
        return drugs;
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private final Map<Integer,Drugs> drugsById=new HashMap<>();
        private int updateNumberCalls;

        private void addDrug(Drugs drugs) {
            drugsById.put(drugs.getId(),drugs);
        }

        @Override
        public Drugs selectByPrimaryKey(Integer id) {
            return drugsById.get(id);
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateNumberCalls++;
            Drugs current=drugsById.get(drugs.getId());
            if(current.getNumber()<drugs.getNumber()){
                return 0;
            }
            current.setNumber(current.getNumber()-drugs.getNumber());
            return 1;
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
        private Patient updatedPatient;

        @Override
        public int updateByPrimaryKeySelective(Patient record) {
            updatedPatient=record;
            return 1;
        }

        @Override public List<Patient> findAll(String name,String certId) { throw new UnsupportedOperationException(); }
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
        private final Seek latestSeek;
        private Seek updatedSeek;

        private FakeSeekMapper(long latestSeekId) {
            latestSeek=new Seek();
            latestSeek.setId(latestSeekId);
        }

        @Override
        public Integer updateDrugs(Seek seek) {
            updatedSeek=seek;
            return 1;
        }

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            return latestSeek;
        }

        @Override public Integer insert(Seek seek) { throw new UnsupportedOperationException(); }
    }
}
