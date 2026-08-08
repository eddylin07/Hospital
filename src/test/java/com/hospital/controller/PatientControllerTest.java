package com.hospital.controller;

import com.hospital.common.CommonService;
import com.hospital.entity.Appointment;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.service.PatientService;
import com.hospital.service.AppointmentService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PatientControllerTest {

    @Test
    public void patientAppointmentUsesSessionPatientIdInsteadOfRequestBody() {
        PatientController controller = new PatientController();
        FakePatientService patientService = new FakePatientService(12);
        FakeAppointmentService appointmentService = new FakeAppointmentService();
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(5);
        login.setRole(3);
        session.setAttribute("login", login);
        Appointment request = new Appointment();
        request.setPatientid(99);

        controller.appointment(request, session);

        assertEquals(Integer.valueOf(12), appointmentService.lastAppointment.getPatientid());
        assertEquals(Integer.valueOf(12), patientService.lastAppointmentUpdate.getId());
        assertEquals(Integer.valueOf(123), patientService.lastAppointmentUpdate.getAppointmentid());
    }

    private static class FakeAppointmentService implements AppointmentService {
        private Appointment lastAppointment;

        @Override
        public String addAppointment(Appointment appointment) {
            lastAppointment = appointment;
            return CommonService.add_message_success;
        }

        @Override
        public Integer selectTheLastAppointment(Integer patientId) {
            return 123;
        }

        @Override public List<Appointment> getAllAppointments() { throw new UnsupportedOperationException(); }
        @Override public List<Appointment> getAllAppointments(String doctorname, String patientname) { throw new UnsupportedOperationException(); }
        @Override public String delAppointment(Integer id) { throw new UnsupportedOperationException(); }
        @Override public Appointment getAppointment(Integer id) { throw new UnsupportedOperationException(); }
        @Override public String UpdateAppointment(Appointment appointment) { throw new UnsupportedOperationException(); }
        @Override public List<Appointment> getPatientMessage(Integer patientId) { throw new UnsupportedOperationException(); }
        @Override public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) { throw new UnsupportedOperationException(); }
    }

    private static class FakePatientService implements PatientService {
        private final Integer patientId;
        private Patient lastAppointmentUpdate;

        private FakePatientService(Integer patientId) {
            this.patientId = patientId;
        }

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            Patient patient = new Patient();
            patient.setId(patientId);
            return patient;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            lastAppointmentUpdate = patient;
            return CommonService.upd_message_success;
        }

        @Override public List<Patient> getAllPatients(String name, String certId) { throw new UnsupportedOperationException(); }
        @Override public List<Patient> getAllPatients() { throw new UnsupportedOperationException(); }
        @Override public String delPatient(Integer id) { throw new UnsupportedOperationException(); }
        @Override public Patient getPatient(Integer id) { throw new UnsupportedOperationException(); }
        @Override public String updatePatient(Patient patient) { throw new UnsupportedOperationException(); }
        @Override public String addPatient(Patient patient) { throw new UnsupportedOperationException(); }
        @Override public String seek(Patient patient) { throw new UnsupportedOperationException(); }
        @Override public Map<String, List> serrchInfo(String name, String type) { throw new UnsupportedOperationException(); }
    }
}
