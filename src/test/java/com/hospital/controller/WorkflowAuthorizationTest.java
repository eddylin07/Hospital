package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.common.CommonService;
import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.Drugs;
import com.hospital.entity.Hospitalization;
import com.hospital.entity.Login;
import com.hospital.entity.Medicalhistory;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class WorkflowAuthorizationTest {
    @Test
    public void patientAppointmentUsesSessionPatientInsteadOfRequestPatient() {
        PatientController controller = new PatientController();
        StubPatientService patientService = new StubPatientService();
        StubAppointmentService appointmentService = new StubAppointmentService(true);
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", login(10, 3));
        Appointment appointment = new Appointment();
        appointment.setPatientid(999);

        JSONObject response = controller.appointment(appointment, session);

        assertEquals(CommonService.add_message_success, response.get("message"));
        assertEquals(Integer.valueOf(42), appointmentService.addedAppointment.getPatientid());
        assertEquals(Integer.valueOf(42), patientService.updatedAppointmentPatient.getId());
        assertEquals(Integer.valueOf(77), patientService.updatedAppointmentPatient.getAppointmentid());
    }

    @Test
    public void doctorCannotDispenseDrugForUnassignedPatient() {
        DoctorController controller = new DoctorController();
        StubPatientService patientService = new StubPatientService();
        controller.patientService = patientService;
        controller.doctorService = new StubDoctorService();
        controller.appointmentService = new StubAppointmentService(false);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", login(20, 2));
        Map<String, String> request = new HashMap<>();
        request.put("patientid", "99");

        JSONObject response = controller.drug(request, session);

        assertEquals("无权操作该患者", response.get("message"));
        assertEquals(0, patientService.seekCalls);
    }

    private static Login login(Integer id, Integer role) {
        Login login = new Login();
        login.setId(id);
        login.setRole(role);
        return login;
    }

    private static class StubPatientService implements PatientService {
        private Patient updatedAppointmentPatient;
        private int seekCalls;

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            Patient patient = new Patient();
            patient.setId(42);
            return patient;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            this.updatedAppointmentPatient = patient;
            return CommonService.upd_message_success;
        }

        @Override
        public String seek(Patient patient) {
            seekCalls++;
            throw new AssertionError("seek should not be called for unauthorized patients");
        }

        @Override public List<Patient> getAllPatients(String name, String certId) { throw new UnsupportedOperationException(); }
        @Override public List<Patient> getAllPatients() { throw new UnsupportedOperationException(); }
        @Override public String delPatient(Integer id) { throw new UnsupportedOperationException(); }
        @Override public Patient getPatient(Integer id) { throw new UnsupportedOperationException(); }
        @Override public String updatePatient(Patient patient) { throw new UnsupportedOperationException(); }
        @Override public String addPatient(Patient patient) { throw new UnsupportedOperationException(); }
        @Override public Map<String, List> serrchInfo(String name, String type) { throw new UnsupportedOperationException(); }
    }

    private static class StubAppointmentService implements AppointmentService {
        private final boolean doctorHasAccess;
        private Appointment addedAppointment;

        StubAppointmentService(boolean doctorHasAccess) {
            this.doctorHasAccess = doctorHasAccess;
        }

        @Override
        public String addAppointment(Appointment appointment) {
            this.addedAppointment = appointment;
            appointment.setId(77);
            return CommonService.add_message_success;
        }

        @Override
        public boolean doctorHasAppointmentForPatient(Integer doctorId, Integer patientId) {
            return doctorHasAccess;
        }

        @Override public List<Appointment> getAllAppointments() { throw new UnsupportedOperationException(); }
        @Override public List<Appointment> getAllAppointments(String doctorname, String patientname) { throw new UnsupportedOperationException(); }
        @Override public String delAppointment(Integer id) { throw new UnsupportedOperationException(); }
        @Override public Appointment getAppointment(Integer id) { throw new UnsupportedOperationException(); }
        @Override public String UpdateAppointment(Appointment appointment) { throw new UnsupportedOperationException(); }
        @Override public List<Appointment> getPatientMessage(Integer patientId) { throw new UnsupportedOperationException(); }
        @Override public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) { return Collections.emptyList(); }
        @Override public Integer selectTheLastAppointment(Integer patientId) { return 77; }
    }

    private static class StubDoctorService implements DoctorService {
        @Override
        public Doctor getDoctorByLoginId(Integer loginid) {
            Doctor doctor = new Doctor();
            doctor.setId(5);
            return doctor;
        }

        @Override public List<Doctor> getAllDoctor() { throw new UnsupportedOperationException(); }
        @Override public List<Doctor> getAllDoctor(String name, String certId) { throw new UnsupportedOperationException(); }
        @Override public String delDoctor(Integer id) { throw new UnsupportedOperationException(); }
        @Override public String addDoctor(Doctor doctor) { throw new UnsupportedOperationException(); }
        @Override public Doctor getDoctor(Integer id) { throw new UnsupportedOperationException(); }
        @Override public String upDoctor(Doctor doctor) { throw new UnsupportedOperationException(); }
        @Override public List<Doctor> getDoctorByDepartment(String department) { throw new UnsupportedOperationException(); }
        @Override public String seekInfo(Map map) { throw new UnsupportedOperationException(); }
    }
}
