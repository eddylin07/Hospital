package com.hospital.resources;

import com.hospital.dao.AppointmentMapper;
import com.hospital.dao.HospitalizationMapper;
import com.hospital.dao.MedicalhistoryMapper;
import org.apache.ibatis.annotations.Param;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MapperContractTest {
    @Test
    public void appointmentMapperMultiParameterQueriesExposeXmlNames() throws Exception {
        assertParamNames(AppointmentMapper.class.getMethod("findAll", List.class, List.class), "doctorids", "patientids");
        assertParamNames(AppointmentMapper.class.getMethod("selectByDoctorId", Integer.class, List.class, String.class), "doctorId", "patientids", "time");
    }

    @Test
    public void medicalhistoryAndHospitalizationMapperQueriesExposeXmlNames() throws Exception {
        assertParamNames(MedicalhistoryMapper.class.getMethod("findAll", List.class, List.class), "doctorids", "patientids");
        assertParamNames(HospitalizationMapper.class.getMethod("findAll", List.class, String.class), "patientids", "intime");
    }

    private void assertParamNames(Method method, String... expectedNames) {
        Annotation[][] annotations = method.getParameterAnnotations();
        assertEquals(expectedNames.length, annotations.length);
        for (int i = 0; i < expectedNames.length; i++) {
            assertEquals(expectedNames[i], paramName(annotations[i]));
        }
    }

    private String paramName(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Param) {
                return ((Param) annotation).value();
            }
        }
        return null;
    }
}
