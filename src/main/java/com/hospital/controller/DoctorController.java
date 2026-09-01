package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.*;
import com.hospital.service.*;
import com.hospital.uitls.DrugsUtils;
import com.hospital.uitls.PDFUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class DoctorController {
    @Autowired
    DoctorService doctorService;
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    PatientService patientService;
    @Autowired
    DrugsService drugsService;
    @Autowired
    HospitalizationService hospitalizationService;
    @Autowired
    MedicalhistoryService medicalhistoryService;
    @Autowired
    OptionService optionService;
    @Autowired
    SeekService seekService;
    @Value("${filepath.seekpdfpath}")
    private String path;
    @RequestMapping("/admin/doctorManage")
    public String doctorManage(HttpServletRequest request,@RequestParam(value="name",required = false) String name,@RequestParam(value="certId",required = false) String certId){
        request.setAttribute("name",name);
        request.setAttribute("certId",certId);
        request.setAttribute("doctors",doctorService.getAllDoctor(name,certId));
        return "admin/doctorManage";
    }
    @RequestMapping(value = "/admin/doctor/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject delDoctor(@PathVariable Integer id){
        JSONObject json=new JSONObject();
        json.put("message",doctorService.delDoctor(id));
        return json;
    }
    @RequestMapping(value = "/admin/doctor/{id}",method = RequestMethod.GET)
    public String doctorInfo(@PathVariable Integer id,HttpServletRequest request){
        request.setAttribute("doctor",doctorService.getDoctor(id));
        return "admin/info/doctorinfo";
    }
    @RequestMapping(value = "/admin/doctor",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject AddDoctor(@RequestBody Doctor doctor){
        JSONObject json=new JSONObject();
        json.put("message",doctorService.addDoctor(doctor));
        return json;
    }
    @RequestMapping(value = "/admin/doctor",method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject updateDoctor(@RequestBody Doctor doctor){
        JSONObject json=new JSONObject();
        json.put("message",doctorService.upDoctor(doctor));
        return json;
    }
    @RequestMapping("/admin/doctorAdd")
    public String doctorAddPage(){
        return "admin/add/doctoradd";
    }

    @RequestMapping("/doctor/seekMedicalAdvice")
    public String seekMedicalAdvice(HttpServletRequest request, HttpSession session,@RequestParam(value = "patientname",required = false)String patientname,@RequestParam(value = "time",required = false)String time){
        Login login=(Login)session.getAttribute("login");
        Doctor doctor=doctorService.getDoctorByLoginId(login.getId());
        request.setAttribute("appointments" ,appointmentService.selectByDoctorId(doctor.getId(),patientname,time));
        return "doctor/seekMedicalAdvice";
    }
    @RequestMapping("/doctor/seek/{id}")
    public String seek(@PathVariable Integer id,HttpServletRequest request,HttpSession session){
        if(!canCurrentDoctorAccessPatient(session,id)){
            return "redirect:/doctor/seekMedicalAdvice";
        }
        request.setAttribute("options",optionService.getAll());
        request.setAttribute("patient",patientService.getPatient(id));
        request.setAttribute("drugs",drugsService.getAllDrugs());
        return "doctor/seek";
    }
    @RequestMapping(value = "/doctor/drug",method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject drug(@RequestBody Map map,HttpSession session){
        JSONObject json=new JSONObject();
        Integer patientId=getPatientId(map);
        if(!canCurrentDoctorAccessPatient(session,patientId)){
            json.put("message","无权操作该患者");
            return json;
        }
        String drugsids=DrugsUtils.vaild(map);
        if(drugsids.equals("")){
            json.put("message","请选择药品");
            return json;
        }
        Patient patient=new Patient();
        patient.setDrugsids(drugsids);
        patient.setId(patientId);
        json.put("message",patientService.seek(patient));
        return json;
    }
    @RequestMapping(value = "/doctor/zation",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject zation(@RequestBody Hospitalization hospitalization,HttpSession session){
        JSONObject json=new JSONObject();
        if(!canCurrentDoctorAccessPatient(session,hospitalization.getPatientid())){
            json.put("message","无权操作该患者");
            return json;
        }
        json.put("message",hospitalizationService.AddHospitalization(hospitalization));
        return json;
    }
    @RequestMapping(value = "/doctor/medicalhistory/{id}")
    public String medicalhistory(@PathVariable Integer id,HttpServletRequest request,HttpSession session){
        if(!canCurrentDoctorAccessPatient(session,id)){
            return "redirect:/doctor/seekMedicalAdvice";
        }
        request.setAttribute("medicalhistorys",medicalhistoryService.getMedicalhistoryByPatientId(id));
        return "doctor/medicalhistory";
    }

    @RequestMapping( value = "/doctor/{department}",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getDoctorByDepartment(@PathVariable String department){
        JSONObject json=new JSONObject();
        json.put("doctors",doctorService.getDoctorByDepartment(department));
        return json;
    }
    @RequestMapping( value = "/doctor/seekinfo",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject seekinfo(@RequestBody Map map,HttpSession session){
        JSONObject json=new JSONObject();
        if(!canCurrentDoctorAccessPatient(session,getPatientId(map))){
            json.put("message","无权操作该患者");
            return json;
        }
        String message=doctorService.seekInfo(map);
        json.put("message",message);
        return json;
    }
    @RequestMapping( value = "/doctor/printseek/{id}",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject printseek(@PathVariable Integer id,HttpSession session){
        Login login=(Login)session.getAttribute("login");
        Doctor doctor=doctorService.getDoctorByLoginId(login.getId());
        JSONObject json=new JSONObject();
        if(!canCurrentDoctorAccessPatient(session,id)){
            json.put("message","无权操作该患者");
            return json;
        }
        Seek seek=seekService.getSeekByPatientId(id);
        if(seek==null){
            json.put("message","暂无就诊记录，无法生成就诊单");
            return json;
        }
        seek.setPatientname(patientService.getPatient(id).getName());
        seek.setDoctorname(doctor.getName());
        //createSeekInfo，第三个参数填空字符串就是生成在项目根目录里面，要是想生成在别的路径，例：D:\\ 就是生成在D盘根目录
        String message= PDFUtils.createSeekInfo(seek,optionService,path);
        json.put("message",message);
        return json;
    }

    private Integer getPatientId(Map map){
        Object value=map.get("patientid");
        return value==null?null:Integer.parseInt(String.valueOf(value));
    }

    private boolean canCurrentDoctorAccessPatient(HttpSession session,Integer patientId){
        if(patientId==null){
            return false;
        }
        Login login=(Login)session.getAttribute("login");
        if(login==null||login.getId()==null){
            return false;
        }
        Doctor doctor=doctorService.getDoctorByLoginId(login.getId());
        if(doctor==null){
            return false;
        }
        List<Appointment> appointments=appointmentService.selectByDoctorId(doctor.getId(),null,null);
        if(appointments==null){
            return false;
        }
        for(Appointment appointment:appointments){
            if(patientId.equals(appointment.getPatientid())){
                return true;
            }
        }
        return false;
    }

}
