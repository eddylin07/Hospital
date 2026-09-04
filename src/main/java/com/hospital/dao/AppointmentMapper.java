package com.hospital.dao;

import com.hospital.entity.Appointment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppointmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Appointment record);

    int insertSelective(Appointment record);

    Appointment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Appointment record);

    int updateByPrimaryKey(Appointment record);
    List<Appointment> findAll(@Param("doctorids")List<Integer> doctorids,@Param("patientids")List<Integer> patientids);
    List<Appointment> selectByPatientId(@Param("patientid")Integer patientId);
    List<Appointment> selectByDoctorId(@Param("doctorId")Integer doctorId,@Param("patientids")List<Integer> patientids,@Param("time")String time);
    Integer selectTheLast(@Param("patientid")Integer patientId);
    Integer countByDoctorAndPatient(@Param("doctorid")Integer doctorid,@Param("patientid")Integer patientid);
}