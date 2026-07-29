package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.common.CommonService;
import com.hospital.entity.Appointment;
import com.hospital.entity.Hospitalization;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.service.*;
import com.hospital.uitls.PDFUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class PatientController {
    @Autowired
    PatientService patientService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    HospitalizationService hospitalizationService;
    @Autowired
    MedicalhistoryService medicalhistoryService;
    @Value("${filepath.appointpdf}")
    private String path;
    @RequestMapping("/admin/patientManage")
    public String patientlist(HttpServletRequest request,@RequestParam(value="name",required = false) String name,@RequestParam(value="certId",required = false) String certId){
        request.setAttribute("name",name);
        request.setAttribute("certId",certId);
        request.setAttribute("patients",patientService.getAllPatients(name,certId));
        return "admin/patientManage";
    }
    @RequestMapping(value = "/admin/patient/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject delPatient(@PathVariable Integer id){
        JSONObject json=new JSONObject();
        json.put("message",patientService.delPatient(id));
        return json;
    }
    @RequestMapping(value = "/admin/patient/{id}",method = RequestMethod.GET)
    public String patientInfo(@PathVariable Integer id,HttpServletRequest request){
        request.setAttribute("patient",patientService.getPatient(id));
        request.setAttribute("appointments",appointmentService.getPatientMessage(id));
        request.setAttribute("hospitalizations",hospitalizationService.getPatientMessage(id));
        request.setAttribute("doctors",doctorService.getAllDoctor());
        return "admin/info/patientinfo";
    }
    @RequestMapping(value = "/admin/patientAdd",method = RequestMethod.GET)
    public String patientAddPage(){
        return "admin/add/patientadd";
    }
    @RequestMapping(value = "/admin/patient",method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject patientInfo(@RequestBody Patient patient){
        JSONObject json=new JSONObject();
        json.put("message",patientService.updatePatient(patient));
        return json;
    }
    @RequestMapping(value = "/admin/patient",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject delPatient(@RequestBody Patient patient){
        JSONObject json=new JSONObject();
        json.put("message",patientService.addPatient(patient));
        return json;
    }
    @RequestMapping(value = "/patient/medicalhistory")
    public String medicalhistory(HttpSession session,HttpServletRequest request){
        Login login=(Login)session.getAttribute("login");
        Patient patient=patientService.findPatientByLoginId(login.getId());
        request.setAttribute("medicalhistorys",medicalhistoryService.getMedicalhistoryByPatientId(patient.getId()));
        return "patient/medicalhistory";
    }
    @RequestMapping(value = "/patient/hospitalization")
    public String hospitalization(HttpSession session,HttpServletRequest request){
        Login login=(Login)session.getAttribute("login");
        Patient patient=patientService.findPatientByLoginId(login.getId());
        request.setAttribute("theLast",hospitalizationService.findTheLastHospitalization(patient.getHospitalizationid()));
        Hospitalization hospitalization=new Hospitalization();
        hospitalization.setPatientid(patient.getId());
        hospitalization.setId(patient.getHospitalizationid());
        request.setAttribute("others",hospitalizationService.findOtherHospitalization(hospitalization));
        return "patient/hospitalization";
    }
    @RequestMapping(value = "/patient/appointment")
    public String appointmentInfo(HttpServletRequest request,HttpSession session){
        Login login=(Login)session.getAttribute("login");
        Patient patient=patientService.findPatientByLoginId(login.getId());
        request.setAttribute("patientid",patient.getId());
        request.setAttribute("doctors",doctorService.getAllDoctor());
        return "patient/appointment";
    }
    @RequestMapping(value = "/patient/appointment",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject appointment(@RequestBody Appointment appointment,HttpSession session){
        JSONObject json=new JSONObject();
        Patient currentPatient=getCurrentPatient(session);
        if(currentPatient==null){
            json.put("message",CommonService.add_message_error);
            return json;
        }
        Patient patient=new Patient();
        appointment.setPatientid(currentPatient.getId());
        String message=appointmentService.addAppointment(appointment);
        if(CommonService.add_message_success.equals(message)){
            patient.setAppointmentid(appointmentService.selectTheLastAppointment(currentPatient.getId()));
            patient.setId(currentPatient.getId());
            patientService.updateAppointMent(patient);
        }
        json.put("message",message);
        return json;
    }
    @RequestMapping(value="/patient/search",method=RequestMethod.GET)
    public String search(){
        return "/patient/search";
    }
    @RequestMapping(value="/patient/searchinfo",method=RequestMethod.GET)
    @ResponseBody
    public JSONObject searchinfo(@RequestParam("name")String name,@RequestParam("type")String type){
        JSONObject json=new JSONObject();
        json.put("map",patientService.serrchInfo(name,type));
        return json;
    }
    @RequestMapping(value = "/hospital/{view}")
    public String test(@PathVariable String view){
        return "patient/"+view;
    }
    @RequestMapping(value = "/patient/downloadpdf",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject downloadpdf(HttpSession session){
        JSONObject json=new JSONObject();
        Patient patient=getCurrentPatient(session);
        if(patient==null){
            json.put("message","未找到患者信息，生成失败");
            return json;
        }
        Integer idlast=appointmentService.selectTheLastAppointment(patient.getId());
        if(idlast==null){
            json.put("message","未找到预约信息，生成失败");
            return json;
        }
        Appointment appointment=appointmentService.getAppointment(idlast);
        if(appointment==null){
            json.put("message","未找到预约信息，生成失败");
            return json;
        }
        //createAppointMent，第三个参数填空字符串就是生成在项目根目录里面，要是想生成在别的路径，例：D:\\ 就是生成在D盘根目录
        json.put("message",PDFUtils.createAppointMent(appointment,path));
        return json;
    }

    private Patient getCurrentPatient(HttpSession session){
        Login login=(Login)session.getAttribute("login");
        if(login==null||login.getId()==null){
            return null;
        }
        return patientService.findPatientByLoginId(login.getId());
    }
}
