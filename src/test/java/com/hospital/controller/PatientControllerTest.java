package com.hospital.controller;

import com.hospital.entity.Appointment;
import com.hospital.entity.Patient;
import com.hospital.entity.Login;
import com.hospital.service.AppointmentService;
import com.hospital.service.PatientService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class PatientControllerTest {
    @Test
    public void appointmentPatientIdComesFromSessionPatient() {
        PatientController controller = new PatientController();
        FakePatientService patientService = new FakePatientService();
        FakeAppointmentService appointmentService = new FakeAppointmentService();
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(5);
        login.setRole(3);
        session.setAttribute("login", login);

        Appointment appointment = new Appointment();
        appointment.setPatientid(999);
        appointment.setDoctorid(4);

        controller.appointment(appointment, session);

        assertEquals(Integer.valueOf(11), appointment.getPatientid());
        assertSame(appointment, appointmentService.addedAppointment);
        assertEquals(Integer.valueOf(11), patientService.updatedPatient.getId());
        assertEquals(Integer.valueOf(77), patientService.updatedPatient.getAppointmentid());
    }

    private static class FakePatientService implements PatientService {
        private Patient updatedPatient;

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            Patient patient = new Patient();
            patient.setId(11);
            patient.setLoginid(loginid);
            return patient;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            updatedPatient = patient;
            return "更新成功";
        }

        @Override
        public List<Patient> getAllPatients(String name, String certId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Patient> getAllPatients() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String delPatient(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Patient getPatient(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String updatePatient(Patient patient) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String addPatient(Patient patient) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String seek(Patient patient) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, List> serrchInfo(String name, String type) {
            throw new UnsupportedOperationException();
        }
    }

    private static class FakeAppointmentService implements AppointmentService {
        private Appointment addedAppointment;

        @Override
        public String addAppointment(Appointment appointment) {
            addedAppointment = appointment;
            return "添加成功";
        }

        @Override
        public Integer selectTheLastAppointment(Integer patientId) {
            return 77;
        }

        @Override
        public List<Appointment> getAllAppointments() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Appointment> getAllAppointments(String doctorname, String patientname) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String delAppointment(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Appointment getAppointment(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String UpdateAppointment(Appointment appointment) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Appointment> getPatientMessage(Integer patientId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) {
            throw new UnsupportedOperationException();
        }
    }
}
