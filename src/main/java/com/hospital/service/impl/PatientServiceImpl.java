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
import java.util.ArrayList;
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
        if (patient.getId() == null || drugsids == null || drugsids.trim().equals("")) {
            return "请选择药品";
        }
        seek.setPatientid(patient.getId());
        seek.setDrugs(drugsids);
        BigDecimal price=new BigDecimal("0.0");
        List<DispenseDrug> dispenseDrugs = new ArrayList<>();
        for(String drug:drugsids.split(",")){
          String[] parts = drug.split("@");
          if (parts.length != 2) {
              return "药品信息错误";
          }
          Integer drugId;
          Integer drugnumber;
          try {
              drugId = Integer.parseInt(parts[0]);
              drugnumber = Integer.parseInt(parts[1]);
          } catch (NumberFormatException e) {
              return "药品信息错误";
          }
          if (drugnumber <= 0) {
              return "药品数量错误";
          }
          Drugs drugs=drugsMapper.selectByPrimaryKey(drugId);
          if (drugs == null || drugs.getPrice() == null || drugs.getNumber() == null) {
              return "药品信息不存在";
          }
          if(drugs.getNumber() < drugnumber){
              return "对不起"+drugs.getName()+"数量不足";
          }
          dispenseDrugs.add(new DispenseDrug(drugId, drugnumber, drugs.getName()));
          price=price.add(drugs.getPrice().multiply(BigDecimal.valueOf(drugnumber)));
        }
        for (DispenseDrug dispenseDrug : dispenseDrugs) {
            Drugs drugs = new Drugs();
            drugs.setId(dispenseDrug.id);
            drugs.setNumber(dispenseDrug.number);
            if (drugsMapper.updateNumber(drugs) <= 0) {
                rollback();
                return "对不起"+dispenseDrug.name+"数量不足";
            }
        }
        seek.setPrice(price);
        if (patientMapper.updateByPrimaryKeySelective(patient) <= 0 || seekMapper.updateDrugs(seek) <= 0) {
            rollback();
            return CommonService.upd_message_error;
        }
        return CommonService.upd_message_success;
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

    private void rollback() {
        try {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } catch (NoTransactionException ignored) {
        }
    }

    private static class DispenseDrug {
        private final Integer id;
        private final Integer number;
        private final String name;

        private DispenseDrug(Integer id, Integer number, String name) {
            this.id = id;
            this.number = number;
            this.name = name;
        }
    }
}
