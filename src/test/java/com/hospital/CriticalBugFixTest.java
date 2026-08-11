package com.hospital;

import com.alibaba.fastjson.JSONObject;
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
import com.hospital.entity.Illness;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.HospitalizationService;
import com.hospital.service.LoginService;
import com.hospital.service.MedicalhistoryService;
import com.hospital.service.PatientService;
import com.hospital.service.impl.LoginServiceImpl;
import com.hospital.service.impl.PatientServiceImpl;
import com.hospital.uitls.DrugsUtils;
import com.hospital.uitls.PatientDoctorutils;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CriticalBugFixTest {

    @Test
    public void failedLoginDoesNotCreateAuthenticatedSession() throws Exception {
        LoginController controller = new LoginController();
        setField(controller, "loginService", new LoginService() {
            @Override public List<Login> findAllAdmin(String username) { return null; }
            @Override public String addAmin(Login login) { return null; }
            @Override public String updateAdmin(Login login) { return null; }
            @Override public String delAdmin(Integer id) { return null; }
            @Override public Login getAdmin(Integer id) { return null; }
            @Override public String login(Login login) { return "用户名不存在"; }
            @Override public String regist(Login login) { return null; }
        });
        MockHttpSession session = new MockHttpSession();

        JSONObject result = controller.login(new Login(), session);

        assertEquals("用户名不存在", result.get("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresResolvedLogin() throws Exception {
        LoginController controller = new LoginController();
        setField(controller, "loginService", new LoginService() {
            @Override public List<Login> findAllAdmin(String username) { return null; }
            @Override public String addAmin(Login login) { return null; }
            @Override public String updateAdmin(Login login) { return null; }
            @Override public String delAdmin(Integer id) { return null; }
            @Override public Login getAdmin(Integer id) { return null; }
            @Override public String login(Login login) {
                login.setId(7);
                login.setRole(3);
                return "登录成功3";
            }
            @Override public String regist(Login login) { return null; }
        });
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();

        JSONObject result = controller.login(login, session);

        assertEquals("登录成功3", result.get("message"));
        assertEquals(login, session.getAttribute("login"));
    }

    @Test
    public void interceptorRequiresValidSessionAndMatchingRole() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();

        MockHttpServletRequest anonymousRequest = new MockHttpServletRequest("GET", "/admin/adminManage");
        MockHttpServletResponse anonymousResponse = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(anonymousRequest, anonymousResponse, new Object()));
        assertEquals("/hospital/login", anonymousResponse.getRedirectedUrl());

        Login patient = new Login();
        patient.setId(1);
        patient.setRole(3);
        MockHttpServletRequest adminRequest = requestWithLogin("GET", "/admin/adminManage", patient);
        MockHttpServletResponse adminResponse = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(adminRequest, adminResponse, new Object()));
        assertEquals(403, adminResponse.getStatus());

        MockHttpServletRequest lookupRequest = requestWithLogin("GET", "/doctor/内科", patient);
        assertTrue(interceptor.preHandle(lookupRequest, new MockHttpServletResponse(), new Object()));

        MockHttpServletRequest doctorWorkflowRequest = requestWithLogin("PUT", "/doctor/drug", patient);
        MockHttpServletResponse doctorWorkflowResponse = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(doctorWorkflowRequest, doctorWorkflowResponse, new Object()));
        assertEquals(403, doctorWorkflowResponse.getStatus());
    }

    @Test
    public void publicRegistrationDoesNotCreateAdminForBlankCert() throws Exception {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        setField(service, "loginMapper", loginMapper);
        setField(service, "doctorMapper", new FakeDoctorMapper());
        setField(service, "patientMapper", new FakePatientMapper());
        Login login = new Login();
        login.setUsername("new-admin");
        login.setCertId(" ");

        String message = service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertEquals(0, loginMapper.insertCount);
        assertNull(login.getRole());
    }

    @Test
    public void duplicateUsernameIsRejectedBeforeLinkingMedicalIdentity() throws Exception {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        Login existing = new Login();
        existing.setId(88);
        existing.setUsername("taken");
        loginMapper.existingByUsername = existing;
        FakeDoctorMapper doctorMapper = new FakeDoctorMapper();
        Doctor doctor = new Doctor();
        doctor.setId(5);
        doctorMapper.doctorByCertId = doctor;
        setField(service, "loginMapper", loginMapper);
        setField(service, "doctorMapper", doctorMapper);
        setField(service, "patientMapper", new FakePatientMapper());
        Login login = new Login();
        login.setUsername("taken");
        login.setCertId("cert-1");

        String message = service.regist(login);

        assertEquals("该用户名已被注册", message);
        assertEquals(0, loginMapper.insertCount);
        assertNull(doctor.getLoginid());
    }

    @Test
    public void patientAppointmentUsesSessionPatientId() throws Exception {
        PatientController controller = new PatientController();
        FakePatientService patientService = new FakePatientService();
        Patient sessionPatient = new Patient();
        sessionPatient.setId(123);
        patientService.patientByLoginId = sessionPatient;
        FakeAppointmentService appointmentService = new FakeAppointmentService();
        appointmentService.lastAppointmentId = 456;
        setField(controller, "patientService", patientService);
        setField(controller, "appointmentService", appointmentService);
        setField(controller, "doctorService", null);
        setField(controller, "hospitalizationService", null);
        setField(controller, "medicalhistoryService", null);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(9);
        login.setRole(3);
        session.setAttribute("login", login);
        Appointment appointment = new Appointment();
        appointment.setPatientid(999);

        controller.appointment(appointment, session);

        assertEquals(Integer.valueOf(123), appointmentService.addedAppointment.getPatientid());
        assertEquals(Integer.valueOf(123), patientService.updatedAppointment.getId());
        assertEquals(Integer.valueOf(456), patientService.updatedAppointment.getAppointmentid());
    }

    @Test
    public void dispensingRejectsOverdrawWithoutMutatingPatientOrSeek() throws Exception {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper();
        Drugs drug = new Drugs();
        drug.setId(1);
        drug.setNumber(5);
        drug.setPrice(new BigDecimal("3.00"));
        drugsMapper.drug = drug;
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        setField(service, "drugsMapper", drugsMapper);
        setField(service, "patientMapper", patientMapper);
        setField(service, "seekMapper", seekMapper);
        setField(service, "loginMapper", new FakeLoginMapper());
        setField(service, "doctorMapper", new FakeDoctorMapper());
        setField(service, "illnessMapper", new FakeIllnessMapper());
        Patient patient = new Patient();
        patient.setId(12);
        patient.setDrugsids("1@6");

        String message = service.seek(patient);

        assertEquals("对不起药品数量不足", message);
        assertEquals(0, drugsMapper.updateNumberCount);
        assertEquals(0, patientMapper.updateSelectiveCount);
        assertEquals(0, seekMapper.updateDrugsCount);
    }

    @Test
    public void dispensingUpdatesInventoryAndLatestSeekWhenStockIsAvailable() throws Exception {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper();
        Drugs drug = new Drugs();
        drug.setId(1);
        drug.setNumber(5);
        drug.setPrice(new BigDecimal("3.00"));
        drugsMapper.drug = drug;
        drugsMapper.updateNumberResult = 1;
        FakePatientMapper patientMapper = new FakePatientMapper();
        patientMapper.updateSelectiveResult = 1;
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        seekMapper.updateDrugsResult = 1;
        setField(service, "drugsMapper", drugsMapper);
        setField(service, "patientMapper", patientMapper);
        setField(service, "seekMapper", seekMapper);
        setField(service, "loginMapper", new FakeLoginMapper());
        setField(service, "doctorMapper", new FakeDoctorMapper());
        setField(service, "illnessMapper", new FakeIllnessMapper());
        Patient patient = new Patient();
        patient.setId(12);
        patient.setDrugsids("1@2");

        String message = service.seek(patient);

        assertEquals("更新成功", message);
        assertEquals(1, drugsMapper.updateNumberCount);
        assertEquals(Integer.valueOf(1), drugsMapper.lastUpdatedDrug.getId());
        assertEquals(Integer.valueOf(2), drugsMapper.lastUpdatedDrug.getNumber());
        assertEquals(1, patientMapper.updateSelectiveCount);
        assertEquals(1, seekMapper.updateDrugsCount);
        assertEquals(Integer.valueOf(12), seekMapper.lastSeek.getPatientid());
        assertEquals("1@2", seekMapper.lastSeek.getDrugs());
        assertEquals(new BigDecimal("6.00"), seekMapper.lastSeek.getPrice());
    }

    @Test
    public void mapperXmlProtectsInventoryAndOnlyUpdatesLatestSeek() throws Exception {
        String drugsMapperXml = new String(Files.readAllBytes(Paths.get("src/main/resources/mapper/DrugsMapper.xml")), StandardCharsets.UTF_8);
        String seekMapperXml = new String(Files.readAllBytes(Paths.get("src/main/resources/mapper/SeekMapper.xml")), StandardCharsets.UTF_8);

        assertTrue(drugsMapperXml.contains("number &gt;= #{number}"));
        assertTrue(seekMapperXml.contains("order by id desc"));
        assertTrue(seekMapperXml.contains("limit 1"));
    }

    @Test
    public void emptyDrugAndOptionInputsDoNotThrow() {
        Map<String, String> empty = new HashMap<>();

        assertEquals("", DrugsUtils.vaild(empty));
        assertEquals("", DrugsUtils.vaild2(empty));
        assertTrue(PatientDoctorutils.getOptionIds("").isEmpty());
        assertTrue(PatientDoctorutils.getOptionIds(null).isEmpty());
    }

    private MockHttpServletRequest requestWithLogin(String method, String uri, Login login) {
        MockHttpServletRequest request = new MockHttpServletRequest(method, uri);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", login);
        request.setSession(session);
        return request;
    }

    private static void setField(Object target, String name, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }

    private static class FakeLoginMapper implements LoginMapper {
        Login existingByUsername;
        int insertCount;
        @Override public int deleteByPrimaryKey(Integer id) { return 0; }
        @Override public int insert(Login record) { insertCount++; return 1; }
        @Override public int insertSelective(Login record) { return 0; }
        @Override public Login selectByPrimaryKey(Integer id) { return null; }
        @Override public int updateByPrimaryKeySelective(Login record) { return 0; }
        @Override public int updateByPrimaryKey(Login record) { return 0; }
        @Override public List<Login> findAllAdmin(String username) { return Collections.emptyList(); }
        @Override public int insertAdmin(Login login) { insertCount++; return 1; }
        @Override public Login findByUsername(String username) { return existingByUsername; }
    }

    private static class FakePatientMapper implements PatientMapper {
        Patient patientByCertId;
        int updateSelectiveCount;
        int updateSelectiveResult = 1;
        @Override public List<Patient> findAll(String name, String certId) { return Collections.emptyList(); }
        @Override public int deleteByPrimaryKey(Integer id) { return 0; }
        @Override public int insert(Patient record) { return 0; }
        @Override public int insertSelective(Patient record) { return 0; }
        @Override public Patient selectByPrimaryKey(Integer id) { return null; }
        @Override public int updateByPrimaryKeySelective(Patient record) { updateSelectiveCount++; return updateSelectiveResult; }
        @Override public int updateByPrimaryKey(Patient record) { return 0; }
        @Override public Patient findPatientByCertId(String certId) { return patientByCertId; }
        @Override public Patient findPatientByLoginId(Integer loginid) { return null; }
        @Override public List<Patient> getPatientByName(String name) { return Collections.emptyList(); }
    }

    private static class FakeDoctorMapper implements DoctorMapper {
        Doctor doctorByCertId;
        @Override public List<Doctor> getAll(String name, String certId) { return Collections.emptyList(); }
        @Override public int deleteByPrimaryKey(Integer id) { return 0; }
        @Override public int insert(Doctor record) { return 0; }
        @Override public int insertSelective(Doctor record) { return 0; }
        @Override public Doctor selectByPrimaryKey(Integer id) { return null; }
        @Override public int updateByPrimaryKeySelective(Doctor record) { return 1; }
        @Override public int updateByPrimaryKey(Doctor record) { return 0; }
        @Override public Doctor getDoctorByCertId(String certId) { return doctorByCertId; }
        @Override public List<Doctor> getDoctorByDepartment(String department) { return Collections.emptyList(); }
        @Override public Doctor getDoctorByLoginId(Integer loginid) { return null; }
        @Override public List<Doctor> getDoctorByName(String name) { return Collections.emptyList(); }
    }

    private static class FakeIllnessMapper implements IllnessMapper {
        @Override public Integer insert(Illness illness) { return 0; }
        @Override public Integer deleteById(Integer id) { return 0; }
        @Override public Integer updateById(Illness illness) { return 0; }
        @Override public List<Illness> selectAll() { return Collections.emptyList(); }
        @Override public Illness getIllness(Integer id) { return null; }
        @Override public List<Illness> getIllnessByName(String name) { return Collections.emptyList(); }
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        Drugs drug;
        Drugs lastUpdatedDrug;
        int updateNumberCount;
        int updateNumberResult = 0;
        @Override public int deleteByPrimaryKey(Integer id) { return 0; }
        @Override public int updateNumber(Drugs drugs) { updateNumberCount++; lastUpdatedDrug = drugs; return updateNumberResult; }
        @Override public int insert(Drugs record) { return 0; }
        @Override public int insertSelective(Drugs record) { return 0; }
        @Override public Drugs selectByPrimaryKey(Integer id) { return drug; }
        @Override public int updateByPrimaryKeySelective(Drugs record) { return 0; }
        @Override public int updateByPrimaryKey(Drugs record) { return 0; }
        @Override public List<Drugs> findAll(Drugs drugs) { return Collections.emptyList(); }
        @Override public Drugs findByName(String name) { return null; }
        @Override public List<Drugs> getDrugsByName(String name) { return Collections.emptyList(); }
    }

    private static class FakeSeekMapper implements SeekMapper {
        Seek lastSeek;
        int updateDrugsCount;
        int updateDrugsResult = 0;
        @Override public Integer insert(Seek seek) { return 0; }
        @Override public Integer updateDrugs(Seek seek) { updateDrugsCount++; lastSeek = seek; return updateDrugsResult; }
        @Override public Seek getSeekByPatientId(Integer patientid) { return null; }
    }

    private static class FakePatientService implements PatientService {
        Patient patientByLoginId;
        Patient updatedAppointment;
        @Override public List<Patient> getAllPatients(String name, String certId) { return Collections.emptyList(); }
        @Override public List<Patient> getAllPatients() { return Collections.emptyList(); }
        @Override public String delPatient(Integer id) { return null; }
        @Override public Patient getPatient(Integer id) { return null; }
        @Override public String updatePatient(Patient patient) { return null; }
        @Override public String addPatient(Patient patient) { return null; }
        @Override public String seek(Patient patient) { return null; }
        @Override public Patient findPatientByLoginId(Integer loginid) { return patientByLoginId; }
        @Override public String updateAppointMent(Patient patient) { updatedAppointment = patient; return "更新成功"; }
        @Override public Map<String, List> serrchInfo(String name, String type) { return Collections.emptyMap(); }
    }

    private static class FakeAppointmentService implements AppointmentService {
        Appointment addedAppointment;
        Integer lastAppointmentId;
        @Override public List<Appointment> getAllAppointments() { return Collections.emptyList(); }
        @Override public List<Appointment> getAllAppointments(String doctorname, String patientname) { return Collections.emptyList(); }
        @Override public String delAppointment(Integer id) { return null; }
        @Override public Appointment getAppointment(Integer id) { return null; }
        @Override public String UpdateAppointment(Appointment appointment) { return null; }
        @Override public String addAppointment(Appointment appointment) { addedAppointment = appointment; return "添加成功"; }
        @Override public List<Appointment> getPatientMessage(Integer patientId) { return Collections.emptyList(); }
        @Override public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) { return Collections.emptyList(); }
        @Override public Integer selectTheLastAppointment(Integer patientId) { return lastAppointmentId; }
    }
}
