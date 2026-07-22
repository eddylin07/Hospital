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
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
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
        seek.setPatientid(patient.getId());
        seek.setDrugs(drugsids);
        BigDecimal price=new BigDecimal("0.0");
        if (drugsids == null || drugsids.trim().equals("")) {
            return "请选择药品";
        }
        Map<Integer, Integer> requestedDrugs = new LinkedHashMap<>();
        try {
            for(String drug:drugsids.split(",")){
                String[] drugInfo = drug.split("@");
                if (drugInfo.length != 2) {
                    return "药品信息错误";
                }
                Integer drugId = Integer.parseInt(drugInfo[0]);
                Integer drugnumber = Integer.parseInt(drugInfo[1]);
                if (drugnumber <= 0) {
                    return "药品数量错误";
                }
                Integer total = requestedDrugs.get(drugId);
                requestedDrugs.put(drugId, total == null ? drugnumber : total + drugnumber);
            }
        } catch (NumberFormatException e) {
            return "药品信息错误";
        }
        Map<Integer, Drugs> drugsById = new HashMap<>();
        for(Map.Entry<Integer, Integer> entry:requestedDrugs.entrySet()){
          Drugs drugs=drugsMapper.selectByPrimaryKey(entry.getKey());
          if(drugs == null){
              return "药品信息错误";
          }
          Integer realnumber=drugs.getNumber();
          Integer drugnumber=entry.getValue();
          if(realnumber == null || realnumber < drugnumber){
              return "对不起"+drugs.getNumber()+"数量不足";
          }
          price=price.add(drugs.getPrice().multiply(BigDecimal.valueOf(drugnumber)));
          drugsById.put(entry.getKey(), drugs);
        }
        for(Map.Entry<Integer, Integer> entry:requestedDrugs.entrySet()){
          if(drugsMapper.updateNumber(entry.getKey(), entry.getValue()) <= 0){
              markRollbackOnly();
              return "对不起"+drugsById.get(entry.getKey()).getNumber()+"数量不足";
          }
        }
        seek.setPrice(price);
        if(patientMapper.updateByPrimaryKeySelective(patient) > 0 && seekMapper.updateDrugs(seek) > 0){
            return CommonService.upd_message_success;
        }
        markRollbackOnly();
        return CommonService.upd_message_error;
    }

    private void markRollbackOnly() {
        try {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } catch (NoTransactionException ignored) {
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
