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
        if (patient == null || patient.getId() == null || patient.getDrugsids() == null || patient.getDrugsids().trim().equals("")) {
            return CommonService.upd_message_error;
        }
        List<DrugOrder> orders = parseDrugOrders(patient.getDrugsids());
        if (orders.isEmpty()) {
            return CommonService.upd_message_error;
        }
        Seek seek = new Seek();
        String drugsids=patient.getDrugsids();
        seek.setPatientid(patient.getId());
        seek.setDrugs(drugsids);
        BigDecimal price=new BigDecimal("0.0");
        for(DrugOrder order:orders){
          Drugs drugs=drugsMapper.selectByPrimaryKey(order.drugId);
          if(drugs==null||drugs.getPrice()==null||drugs.getNumber()==null||drugs.getNumber()<order.quantity){
              return "对不起药品数量不足";
          }
          order.drug = drugs;
          price=price.add(drugs.getPrice().multiply(BigDecimal.valueOf(order.quantity)));
        }
        seek.setPrice(price);
        for(DrugOrder order:orders){
            Drugs updateDrug = new Drugs();
            updateDrug.setId(order.drugId);
            updateDrug.setNumber(order.quantity);
            if(drugsMapper.updateNumber(updateDrug)!=1){
                rollbackIfActive();
                return "对不起药品数量不足";
            }
        }
        if(patientMapper.updateByPrimaryKeySelective(patient) <= 0 || seekMapper.updateDrugs(seek) <= 0){
            rollbackIfActive();
            return CommonService.upd_message_error;
        }
        return CommonService.upd_message_success;
    }

    private List<DrugOrder> parseDrugOrders(String drugsids) {
        List<DrugOrder> orders = new ArrayList<>();
        for(String drug:drugsids.split(",")){
            String[] parts = drug.split("@");
            if(parts.length!=2){
                return new ArrayList<>();
            }
            try{
                Integer drugId = Integer.parseInt(parts[0]);
                Integer quantity = Integer.parseInt(parts[1]);
                if(quantity==null||quantity<=0){
                    return new ArrayList<>();
                }
                orders.add(new DrugOrder(drugId, quantity));
            }catch (NumberFormatException e){
                return new ArrayList<>();
            }
        }
        return orders;
    }

    private void rollbackIfActive() {
        try{
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }catch (NoTransactionException ignored){
        }
    }

    private static class DrugOrder {
        private final Integer drugId;
        private final Integer quantity;
        private Drugs drug;

        private DrugOrder(Integer drugId, Integer quantity) {
            this.drugId = drugId;
            this.quantity = quantity;
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
