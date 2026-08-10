package com.hospital;

import com.alibaba.fastjson.JSONObject;
import com.hospital.common.CommonService;
import com.hospital.config.intercepors.LoginInterceptor;
import com.hospital.controller.LoginController;
import com.hospital.controller.PatientController;
import com.hospital.dao.DoctorMapper;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.IllnessMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.Drugs;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.HospitalizationService;
import com.hospital.service.MedicalhistoryService;
import com.hospital.service.PatientService;
import com.hospital.service.impl.LoginServiceImpl;
import com.hospital.service.impl.PatientServiceImpl;
import com.hospital.uitls.DrugsUtils;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class CriticalBugFixTest {

    @Test
    public void failedLoginDoesNotCreateAuthenticatedSession() {
        LoginController controller = new LoginController();
        ReflectionTestUtils.setField(controller, "loginService", new StubLoginService() {
            @Override
            public String login(Login login) {
                return "密码错误";
            }
        });

        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setUsername("alice");
        JSONObject response = controller.login(login, session);

        assertEquals("密码错误", response.get("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void interceptorRejectsUnhydratedLoginAndWrongRole() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/patient/appointment");
        request.getSession().setAttribute("login", new Login());

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals("/hospital/login", response.getRedirectedUrl());

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setRequestURI("/admin/patientManage");
        Login patientLogin = new Login();
        patientLogin.setId(3);
        patientLogin.setRole(3);
        request.getSession().setAttribute("login", patientLogin);

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(403, response.getStatus());
    }

    @Test
    public void publicRegistrationDoesNotCreateAdminForBlankCertificate() {
        LoginServiceImpl service = loginService(new FakeLoginMapper(), new FakePatientMapper(), new FakeDoctorMapper());
        Login login = new Login();
        login.setUsername("new-admin");
        login.setCertId(" ");

        String message = service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertNull(login.getRole());
    }

    @Test
    public void duplicateUsernameIsRejectedBeforeBindingDoctorCertificate() {
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        Login existing = new Login();
        existing.setId(99);
        loginMapper.existingLogin = existing;
        FakeDoctorMapper doctorMapper = new FakeDoctorMapper();
        doctorMapper.doctorByCert = new Doctor();

        LoginServiceImpl service = loginService(loginMapper, new FakePatientMapper(), doctorMapper);
        Login login = new Login();
        login.setUsername("taken");
        login.setCertId("doctor-cert");

        String message = service.regist(login);

        assertEquals("该用户名已被注册", message);
        assertEquals(0, loginMapper.insertCalls);
        assertEquals(0, doctorMapper.updateCalls);
    }

    @Test
    public void patientAppointmentUsesCurrentSessionPatientId() {
        PatientController controller = new PatientController();
        FakePatientService patientService = new FakePatientService();
        Patient sessionPatient = new Patient();
        sessionPatient.setId(123);
        patientService.patientByLoginId = sessionPatient;
        FakeAppointmentService appointmentService = new FakeAppointmentService();
        ReflectionTestUtils.setField(controller, "patientService", patientService);
        ReflectionTestUtils.setField(controller, "appointmentService", appointmentService);

        Login login = new Login();
        login.setId(7);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", login);
        Appointment appointment = new Appointment();
        appointment.setPatientid(999);

        JSONObject response = controller.appointment(appointment, session);

        assertEquals(CommonService.add_message_success, response.get("message"));
        assertEquals(Integer.valueOf(123), appointmentService.insertedAppointment.getPatientid());
        assertEquals(Integer.valueOf(77), patientService.updatedAppointment.getAppointmentid());
        assertEquals(Integer.valueOf(123), patientService.updatedAppointment.getId());
    }

    @Test
    public void dispensingRejectsOverdrawBeforeWritingAnything() {
        PatientServiceImpl service = patientServiceWithDrug(5, 0);
        FakeDrugsMapper drugsMapper = (FakeDrugsMapper) ReflectionTestUtils.getField(service, "drugsMapper");
        FakePatientMapper patientMapper = (FakePatientMapper) ReflectionTestUtils.getField(service, "patientMapper");
        FakeSeekMapper seekMapper = (FakeSeekMapper) ReflectionTestUtils.getField(service, "seekMapper");
        Patient patient = patientWithDrugRequest("1@10");

        String message = service.seek(patient);

        assertEquals("对不起Aspirin数量不足", message);
        assertEquals(0, drugsMapper.updateNumberCalls);
        assertEquals(0, patientMapper.updateSelectiveCalls);
        assertEquals(0, seekMapper.updateDrugsCalls);
    }

    @Test
    public void dispensingRollsBackBusinessUpdatesWhenAtomicStockGuardFails() {
        PatientServiceImpl service = patientServiceWithDrug(10, 0);
        FakePatientMapper patientMapper = (FakePatientMapper) ReflectionTestUtils.getField(service, "patientMapper");
        FakeSeekMapper seekMapper = (FakeSeekMapper) ReflectionTestUtils.getField(service, "seekMapper");
        Patient patient = patientWithDrugRequest("1@3");

        String message = service.seek(patient);

        assertEquals("对不起Aspirin数量不足", message);
        assertEquals(0, patientMapper.updateSelectiveCalls);
        assertEquals(0, seekMapper.updateDrugsCalls);
    }

    @Test
    public void emptyDrugAndOptionSelectionsDoNotThrow() {
        assertEquals("", DrugsUtils.vaild(Collections.singletonMap("patientid", "1")));
        assertEquals("", DrugsUtils.vaild2(Collections.singletonMap("patientid", "1")));
    }

    private LoginServiceImpl loginService(LoginMapper loginMapper, PatientMapper patientMapper, DoctorMapper doctorMapper) {
        LoginServiceImpl service = new LoginServiceImpl();
        ReflectionTestUtils.setField(service, "loginMapper", loginMapper);
        ReflectionTestUtils.setField(service, "patientMapper", patientMapper);
        ReflectionTestUtils.setField(service, "doctorMapper", doctorMapper);
        return service;
    }

    private PatientServiceImpl patientServiceWithDrug(Integer stock, int updateNumberResult) {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper();
        drugsMapper.stock = stock;
        drugsMapper.updateNumberResult = updateNumberResult;
        ReflectionTestUtils.setField(service, "drugsMapper", drugsMapper);
        ReflectionTestUtils.setField(service, "patientMapper", new FakePatientMapper());
        ReflectionTestUtils.setField(service, "seekMapper", new FakeSeekMapper());
        ReflectionTestUtils.setField(service, "loginMapper", new FakeLoginMapper());
        ReflectionTestUtils.setField(service, "doctorMapper", new FakeDoctorMapper());
        ReflectionTestUtils.setField(service, "illnessMapper", new StubIllnessMapper());
        return service;
    }

    private Patient patientWithDrugRequest(String drugsids) {
        Patient patient = new Patient();
        patient.setId(42);
        patient.setDrugsids(drugsids);
        return patient;
    }

    private static class StubLoginService implements com.hospital.service.LoginService {
        public List<Login> findAllAdmin(String username) { return null; }
        public String addAmin(Login login) { return null; }
        public String updateAdmin(Login login) { return null; }
        public String delAdmin(Integer id) { return null; }
        public Login getAdmin(Integer id) { return null; }
        public String login(Login login) { return null; }
        public String regist(Login login) { return null; }
    }

    private static class FakeAppointmentService implements AppointmentService {
        private Appointment insertedAppointment;

        public List<Appointment> getAllAppointments() { return null; }
        public List<Appointment> getAllAppointments(String doctorname, String patientname) { return null; }
        public String delAppointment(Integer id) { return null; }
        public Appointment getAppointment(Integer id) { return null; }
        public String UpdateAppointment(Appointment appointment) { return null; }
        public String addAppointment(Appointment appointment) {
            appointment.setId(77);
            insertedAppointment = appointment;
            return CommonService.add_message_success;
        }
        public List<Appointment> getPatientMessage(Integer patientId) { return null; }
        public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) { return null; }
        public Integer selectTheLastAppointment(Integer patientId) { return 77; }
    }

    private static class FakePatientService implements PatientService {
        private Patient patientByLoginId;
        private Patient updatedAppointment;

        public List<Patient> getAllPatients(String name, String certId) { return null; }
        public List<Patient> getAllPatients() { return null; }
        public String delPatient(Integer id) { return null; }
        public Patient getPatient(Integer id) { return null; }
        public String updatePatient(Patient patient) { return null; }
        public String addPatient(Patient patient) { return null; }
        public String seek(Patient patient) { return null; }
        public Patient findPatientByLoginId(Integer loginid) { return patientByLoginId; }
        public String updateAppointMent(Patient patient) {
            updatedAppointment = patient;
            return CommonService.upd_message_success;
        }
        public Map<String, List> serrchInfo(String name, String type) { return null; }
    }

    private static class FakeLoginMapper implements LoginMapper {
        private Login existingLogin;
        private int insertCalls;

        public int deleteByPrimaryKey(Integer id) { return 0; }
        public int insert(Login record) {
            insertCalls++;
            record.setId(100);
            return 1;
        }
        public int insertSelective(Login record) { return 0; }
        public Login selectByPrimaryKey(Integer id) { return null; }
        public int updateByPrimaryKeySelective(Login record) { return 0; }
        public int updateByPrimaryKey(Login record) { return 0; }
        public List<Login> findAllAdmin(String username) { return null; }
        public int insertAdmin(Login login) { return 0; }
        public Login findByUsername(String username) { return existingLogin; }
    }

    private static class FakePatientMapper implements PatientMapper {
        private int updateSelectiveCalls;

        public List<Patient> findAll(String name, String certId) { return null; }
        public int deleteByPrimaryKey(Integer id) { return 0; }
        public int insert(Patient record) { return 0; }
        public int insertSelective(Patient record) { return 0; }
        public Patient selectByPrimaryKey(Integer id) { return null; }
        public int updateByPrimaryKeySelective(Patient record) {
            updateSelectiveCalls++;
            return 1;
        }
        public int updateByPrimaryKey(Patient record) { return 0; }
        public Patient findPatientByCertId(String certId) { return null; }
        public Patient findPatientByLoginId(Integer loginid) { return null; }
        public List<Patient> getPatientByName(String name) { return null; }
    }

    private static class FakeDoctorMapper implements DoctorMapper {
        private Doctor doctorByCert;
        private int updateCalls;

        public List<Doctor> getAll(String name, String certId) { return null; }
        public int deleteByPrimaryKey(Integer id) { return 0; }
        public int insert(Doctor record) { return 0; }
        public int insertSelective(Doctor record) { return 0; }
        public Doctor selectByPrimaryKey(Integer id) { return null; }
        public int updateByPrimaryKeySelective(Doctor record) {
            updateCalls++;
            return 1;
        }
        public int updateByPrimaryKey(Doctor record) { return 0; }
        public Doctor getDoctorByCertId(String certId) { return doctorByCert; }
        public List<Doctor> getDoctorByDepartment(String department) { return null; }
        public Doctor getDoctorByLoginId(Integer loginid) { return null; }
        public List<Doctor> getDoctorByName(String name) { return null; }
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private Integer stock;
        private int updateNumberResult;
        private int updateNumberCalls;

        public int deleteByPrimaryKey(Integer id) { return 0; }
        public int updateNumber(Drugs drugs) {
            updateNumberCalls++;
            return updateNumberResult;
        }
        public int insert(Drugs record) { return 0; }
        public int insertSelective(Drugs record) { return 0; }
        public Drugs selectByPrimaryKey(Integer id) {
            Drugs drugs = new Drugs();
            drugs.setId(id);
            drugs.setName("Aspirin");
            drugs.setNumber(stock);
            drugs.setPrice(new BigDecimal("2.50"));
            return drugs;
        }
        public int updateByPrimaryKeySelective(Drugs record) { return 0; }
        public int updateByPrimaryKey(Drugs record) { return 0; }
        public List<Drugs> findAll(Drugs drugs) { return null; }
        public Drugs findByName(String name) { return null; }
        public List<Drugs> getDrugsByName(String name) { return null; }
    }

    private static class FakeSeekMapper implements SeekMapper {
        private int updateDrugsCalls;

        public Integer insert(Seek seek) { return 0; }
        public Integer updateDrugs(Seek seek) {
            updateDrugsCalls++;
            return 1;
        }
        public Seek getSeekByPatientId(Integer patientid) { return null; }
    }

    private static class StubIllnessMapper implements IllnessMapper {
        public Integer insert(com.hospital.entity.Illness illness) { return 0; }
        public Integer deleteById(Integer id) { return 0; }
        public Integer updateById(com.hospital.entity.Illness illness) { return 0; }
        public List<com.hospital.entity.Illness> selectAll() { return null; }
        public com.hospital.entity.Illness getIllness(Integer id) { return null; }
        public List<com.hospital.entity.Illness> getIllnessByName(String name) { return null; }
    }
}
