package com.hospital.controller;

import com.hospital.common.CommonService;
import com.hospital.entity.Appointment;
import com.hospital.entity.Hospitalization;
import com.hospital.entity.Login;
import com.hospital.entity.Medicalhistory;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.HospitalizationService;
import com.hospital.service.MedicalhistoryService;
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
        CapturingPatientService patientService = new CapturingPatientService();
        controller.patientService = patientService;
        controller.appointmentService = new GeneratedIdAppointmentService();
        controller.doctorService = new EmptyDoctorService();

        Appointment appointment = new Appointment();
        appointment.setPatientid(999);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(5);
        login.setRole(3);
        session.setAttribute("login", login);

        controller.appointment(appointment, session);

        assertEquals(Integer.valueOf(123), appointment.getPatientid());
        assertEquals(Integer.valueOf(123), patientService.updatedPatient.getId());
        assertEquals(Integer.valueOf(77), patientService.updatedPatient.getAppointmentid());
    }

    private static class CapturingPatientService implements PatientService {
        private Patient updatedPatient;

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            Patient patient = new Patient();
            patient.setId(123);
            return patient;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            updatedPatient = patient;
            return CommonService.upd_message_success;
        }

        @Override
        public List<Patient> getAllPatients(String name, String certId) { return null; }
        @Override
        public List<Patient> getAllPatients() { return null; }
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
        public Map<String, List> serrchInfo(String name, String type) { return null; }
    }

    private static class GeneratedIdAppointmentService implements AppointmentService {
        @Override
        public String addAppointment(Appointment appointment) {
            appointment.setId(77);
            return CommonService.add_message_success;
        }

        @Override
        public List<Appointment> getAllAppointments() { return null; }
        @Override
        public List<Appointment> getAllAppointments(String doctorname, String patientname) { return null; }
        @Override
        public String delAppointment(Integer id) { return null; }
        @Override
        public Appointment getAppointment(Integer id) { return null; }
        @Override
        public String UpdateAppointment(Appointment appointment) { return null; }
        @Override
        public List<Appointment> getPatientMessage(Integer patientId) { return null; }
        @Override
        public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) { return null; }
        @Override
        public Integer selectTheLastAppointment(Integer patientId) { return null; }
    }

    private static class EmptyDoctorService implements DoctorService {
        @Override
        public List<com.hospital.entity.Doctor> getAllDoctor() { return null; }
        @Override
        public List<com.hospital.entity.Doctor> getAllDoctor(String name, String certId) { return null; }
        @Override
        public String delDoctor(Integer id) { return null; }
        @Override
        public String addDoctor(com.hospital.entity.Doctor doctor) { return null; }
        @Override
        public com.hospital.entity.Doctor getDoctor(Integer id) { return null; }
        @Override
        public String upDoctor(com.hospital.entity.Doctor doctor) { return null; }
        @Override
        public com.hospital.entity.Doctor getDoctorByLoginId(Integer loginid) { return null; }
        @Override
        public List<com.hospital.entity.Doctor> getDoctorByDepartment(String department) { return null; }
        @Override
        public String seekInfo(Map map) { return null; }
    }
}
