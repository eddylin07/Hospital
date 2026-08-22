package com.hospital.controller;

import com.hospital.common.CommonService;
import com.hospital.entity.Appointment;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.PatientService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PatientControllerTest {
    @Test
    public void appointmentUsesSessionPatientInsteadOfRequestBodyPatient() {
        PatientController controller = new PatientController();
        FakePatientService patientService = new FakePatientService(7);
        FakeAppointmentService appointmentService = new FakeAppointmentService(123);
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;
        MockHttpSession session = new MockHttpSession();
        com.hospital.entity.Login login = new com.hospital.entity.Login();
        login.setId(55);
        login.setRole(3);
        session.setAttribute("login", login);
        Appointment request = new Appointment();
        request.setPatientid(99);
        request.setDoctorid(3);

        controller.appointment(request, session);

        assertEquals(7, appointmentService.lastAppointment.getPatientid().intValue());
        assertEquals(7, patientService.lastUpdatedPatient.getId().intValue());
        assertEquals(123, patientService.lastUpdatedPatient.getAppointmentid().intValue());
    }

    private static class FakeAppointmentService implements AppointmentService {
        private final Integer generatedId;
        private Appointment lastAppointment;

        private FakeAppointmentService(Integer generatedId) {
            this.generatedId = generatedId;
        }

        @Override
        public String addAppointment(Appointment appointment) {
            lastAppointment = appointment;
            appointment.setId(generatedId);
            return CommonService.add_message_success;
        }

        @Override
        public List<Appointment> getAllAppointments() { return Collections.emptyList(); }
        @Override
        public List<Appointment> getAllAppointments(String doctorname, String patientname) { return Collections.emptyList(); }
        @Override
        public String delAppointment(Integer id) { return null; }
        @Override
        public Appointment getAppointment(Integer id) { return null; }
        @Override
        public String UpdateAppointment(Appointment appointment) { return null; }
        @Override
        public List<Appointment> getPatientMessage(Integer patientId) { return Collections.emptyList(); }
        @Override
        public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) { return Collections.emptyList(); }
        @Override
        public Integer selectTheLastAppointment(Integer patientId) { return null; }
        @Override
        public boolean hasDoctorPatientAppointment(Integer doctorId, Integer patientId) { return false; }
    }

    private static class FakePatientService implements PatientService {
        private final Integer sessionPatientId;
        private Patient lastUpdatedPatient;

        private FakePatientService(Integer sessionPatientId) {
            this.sessionPatientId = sessionPatientId;
        }

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            Patient patient = new Patient();
            patient.setId(sessionPatientId);
            return patient;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            lastUpdatedPatient = patient;
            return CommonService.upd_message_success;
        }

        @Override
        public List<Patient> getAllPatients(String name, String certId) { return Collections.emptyList(); }
        @Override
        public List<Patient> getAllPatients() { return Collections.emptyList(); }
        @Override
        public String delPatient(Integer id) { return null; }
        @Override
        public Patient getPatient(Integer id) { return null; }
        @Override
        public String updatePatient(Patient patient) { return null; }
        @Override
        public String addPatient(Patient patient) { return null; }
        @Override
        public String seek(Patient patient) { return null; }
        @Override
        public Map<String, List> serrchInfo(String name, String type) { return Collections.emptyMap(); }
    }
}
