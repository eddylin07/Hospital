package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.*;
import com.hospital.entity.Drugs;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import com.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    PatientMapper patientMapper;
    @Autowired
    LoginMapper loginMapper;
    @Autowired
    DoctorMapper doctorMapper;
    @Autowired
    IllnessMapper illnessMapper;
    @Autowired
    DrugsMapper drugsMapper;
    @Autowired
    SeekMapper seekMapper;

    @Override
    public List<Patient> getAllPatients(String name, String certId) {
        return patientMapper.findAll(name, certId);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientMapper.findAll("", "");
    }

    @Override
    public String delPatient(Integer id) {
        return patientMapper.deleteByPrimaryKey(id) > 0 ? CommonService.del_message_success : CommonService.del_message_error;
    }

    @Override
    public Patient getPatient(Integer id) {
        Patient patient = patientMapper.selectByPrimaryKey(id);
        if (patient == null) {
            return null;
        }
        Login login = loginMapper.selectByPrimaryKey(patient.getLoginid());
        if (login != null) {
            patient.setUsername(login.getUsername());
            patient.setPassword(login.getPassword());
        }
        return patient;
    }

    @Override
    public String updatePatient(Patient patient) {
        Login login = loginMapper.selectByPrimaryKey(patient.getLoginid());
        if (login == null) {
            return patientMapper.updateByPrimaryKey(patient) > 0 ? CommonService.upd_message_success2 : CommonService.upd_message_error;
        } else {
            login.setUsername(patient.getUsername());
            login.setPassword(patient.getPassword());
            login.setId(patient.getLoginid());
            return (patientMapper.updateByPrimaryKey(patient) > 0 && loginMapper.updateByPrimaryKey(login) > 0) ? CommonService.upd_message_success : CommonService.upd_message_error;
        }

    }

    @Override
    public String addPatient(Patient patient) {
        String message = "";
        if (patientMapper.findPatientByCertId(patient.getCertId()) != null) {
            message = CommonService.add_message_error2;
        } else {
            message = patientMapper.insert(patient) > 0 ? CommonService.add_message_success : CommonService.add_message_error;
        }
        return message;
    }

    @Override
    @Transactional
    public String seek(Patient patient) {
        Seek seek = new Seek();
        String drugsids=patient.getDrugsids();
        if(patient.getId()==null||drugsids==null||drugsids.trim().isEmpty()){
            return "请选择药品";
        }
        if(seekMapper.getSeekByPatientId(patient.getId())==null){
            return "请先生成就诊信息";
        }
        seek.setPatientid(patient.getId());
        seek.setDrugs(drugsids);
        BigDecimal price=new BigDecimal("0.0");
        boolean inventoryChanged=false;
        for(String drug:drugsids.split(",")){
            String[] parts=drug.split("@");
            if(parts.length!=2){
                markRollbackIfNeeded(inventoryChanged);
                return "药品信息错误";
            }
            Integer drugId=parseInteger(parts[0]);
            Integer drugnumber=parseInteger(parts[1]);
            if(drugId==null||drugnumber==null||drugnumber<=0){
                markRollbackIfNeeded(inventoryChanged);
                return "药品信息错误";
            }
            Drugs drugs=drugsMapper.selectByPrimaryKey(drugId);
            if(drugs==null||drugs.getPrice()==null||drugs.getNumber()==null){
                markRollbackIfNeeded(inventoryChanged);
                return "药品信息错误";
            }
            Integer realnumber=drugs.getNumber();
            if(realnumber<drugnumber){
                markRollbackIfNeeded(inventoryChanged);
                return "对不起"+drugs.getName()+"数量不足";
            }
            drugs.setNumber(drugnumber);
            if(drugsMapper.updateNumber(drugs)!=1){
                markRollbackOnly();
                return "对不起"+drugs.getName()+"数量不足";
            }
            inventoryChanged=true;
            price=price.add(drugs.getPrice().multiply(BigDecimal.valueOf(drugnumber)));
        }
        seek.setPrice(price);
        if(patientMapper.updateByPrimaryKeySelective(patient) <= 0 || seekMapper.updateDrugs(seek) <= 0){
            markRollbackOnly();
            return CommonService.upd_message_error;
        }
        return CommonService.upd_message_success;
    }

    private Integer parseInteger(String value){
        if(value==null){
            return null;
        }
        try {
            return Integer.parseInt(value);
        }catch (NumberFormatException e){
            return null;
        }
    }

    private void markRollbackOnly(){
        try {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }catch (Exception ignored){
        }
    }

    private void markRollbackIfNeeded(boolean inventoryChanged){
        if(inventoryChanged){
            markRollbackOnly();
        }
    }

    @Override
    public Patient findPatientByLoginId(Integer loginid) {
        return patientMapper.findPatientByLoginId(loginid);
    }

    @Override
    public String updateAppointMent(Patient patient) {
        return patientMapper.updateByPrimaryKeySelective(patient) > 0 ? CommonService.upd_message_success : CommonService.upd_message_error;
    }

    @Override
    public Map<String, List> serrchInfo(String name, String type) {
        Map<String, List> map = new HashMap<>();
        List list = null;
        if ("doctor".equals(type)) {
            list = doctorMapper.getDoctorByName(name);
            map.put(type, list);

        } else if ("illness".equals(type)) {
            list = illnessMapper.getIllnessByName(name);
            map.clear();
            map.put(type, list);
        } else {
            list = drugsMapper.getDrugsByName(name);
            map.clear();
            map.put(type, list);
        }
        return map;
    }
}
