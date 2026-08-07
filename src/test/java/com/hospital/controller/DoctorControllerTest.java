package com.hospital.controller;

import com.hospital.entity.Doctor;
import com.hospital.entity.Login;
import com.hospital.entity.Option;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import com.hospital.service.DoctorService;
import com.hospital.service.OptionService;
import com.hospital.service.PatientService;
import com.hospital.service.SeekService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DoctorControllerTest {
    @Test
    public void printSeekWithoutSeekRecordReturnsBusinessMessage() {
        DoctorController controller = new DoctorController();
        controller.doctorService = new FakeDoctorService();
        controller.seekService = new FakeSeekService();
        controller.patientService = new FakePatientService();
        controller.optionService = new FakeOptionService();
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(2);
        login.setRole(2);
        session.setAttribute("login", login);

        assertEquals("暂无就诊单", controller.printseek(1, session).get("message"));
    }

    private static class FakeDoctorService implements DoctorService {
        @Override
        public List<Doctor> getAllDoctor() {
            return Collections.emptyList();
        }

        @Override
        public List<Doctor> getAllDoctor(String name, String certId) {
            return Collections.emptyList();
        }

        @Override
        public String delDoctor(Integer id) {
            return "";
        }

        @Override
        public String addDoctor(Doctor doctor) {
            return "";
        }

        @Override
        public Doctor getDoctor(Integer id) {
            return null;
        }

        @Override
        public String upDoctor(Doctor doctor) {
            return "";
        }

        @Override
        public Doctor getDoctorByLoginId(Integer loginid) {
            Doctor doctor = new Doctor();
            doctor.setId(7);
            doctor.setName("doctor");
            return doctor;
        }

        @Override
        public List<Doctor> getDoctorByDepartment(String department) {
            return Collections.emptyList();
        }

        @Override
        public String seekInfo(Map map) {
            return "";
        }
    }

    private static class FakeSeekService implements SeekService {
        @Override
        public String addSeek(Seek seek) {
            return "";
        }

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            return null;
        }
    }

    private static class FakePatientService implements PatientService {
        @Override
        public List<Patient> getAllPatients(String name, String certId) {
            return Collections.emptyList();
        }

        @Override
        public List<Patient> getAllPatients() {
            return Collections.emptyList();
        }

        @Override
        public String delPatient(Integer id) {
            return "";
        }

        @Override
        public Patient getPatient(Integer id) {
            Patient patient = new Patient();
            patient.setId(id);
            patient.setName("patient");
            return patient;
        }

        @Override
        public String updatePatient(Patient patient) {
            return "";
        }

        @Override
        public String addPatient(Patient patient) {
            return "";
        }

        @Override
        public String seek(Patient patient) {
            return "";
        }

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            return null;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            return "";
        }

        @Override
        public Map<String, List> serrchInfo(String name, String type) {
            return Collections.emptyMap();
        }
    }

    private static class FakeOptionService implements OptionService {
        @Override
        public List<Option> getAll() {
            return Collections.emptyList();
        }

        @Override
        public Option getOption(Integer id) {
            return null;
        }

        @Override
        public String insert(Option option) {
            return "";
        }

        @Override
        public String deleteOption(Integer id) {
            return "";
        }

        @Override
        public String updateBOption(Option option) {
            return "";
        }
    }
}
