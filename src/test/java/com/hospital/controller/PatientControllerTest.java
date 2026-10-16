package com.hospital.controller;

import com.hospital.entity.Appointment;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.PatientService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PatientControllerTest {

    @Test
    public void appointmentUsesPatientFromSessionInsteadOfRequestBody() {
        PatientController controller = new PatientController();
        PatientServiceStub patientService = new PatientServiceStub();
        AppointmentServiceStub appointmentService = new AppointmentServiceStub();
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;
        Login login = new Login();
        login.setId(5);
        login.setRole(3);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", login);
        Appointment appointment = new Appointment();
        appointment.setPatientid(777);

        controller.appointment(appointment, session);

        assertEquals(Integer.valueOf(42), appointmentService.addedAppointment.getPatientid());
        assertEquals(Integer.valueOf(42), patientService.updatedPatient.getId());
        assertEquals(Integer.valueOf(123), patientService.updatedPatient.getAppointmentid());
    }

    private static class PatientServiceStub implements PatientService {
        private Patient updatedPatient;

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            Patient patient = new Patient();
            patient.setId(42);
            return patient;
        }

        @Override public String updateAppointMent(Patient patient) { updatedPatient = patient; return "更新成功"; }
        @Override public List<Patient> getAllPatients(String name, String certId) { return null; }
        @Override public List<Patient> getAllPatients() { return null; }
        @Override public String delPatient(Integer id) { return null; }
        @Override public Patient getPatient(Integer id) { return null; }
        @Override public String updatePatient(Patient patient) { return null; }
        @Override public String addPatient(Patient patient) { return null; }
        @Override public String seek(Patient patient) { return null; }
        @Override public Map<String, List> serrchInfo(String name, String type) { return null; }
    }

    private static class AppointmentServiceStub implements AppointmentService {
        private Appointment addedAppointment;

        @Override public String addAppointment(Appointment appointment) { addedAppointment = appointment; return "添加成功"; }
        @Override public Integer selectTheLastAppointment(Integer patientId) { return 123; }
        @Override public List<Appointment> getAllAppointments() { return null; }
        @Override public List<Appointment> getAllAppointments(String doctorname, String patientname) { return null; }
        @Override public String delAppointment(Integer id) { return null; }
        @Override public Appointment getAppointment(Integer id) { return null; }
        @Override public String UpdateAppointment(Appointment appointment) { return null; }
        @Override public List<Appointment> getPatientMessage(Integer patientId) { return null; }
        @Override public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) { return null; }
    }
}
