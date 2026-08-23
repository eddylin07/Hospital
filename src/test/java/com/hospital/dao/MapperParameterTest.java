package com.hospital.dao;

import org.apache.ibatis.annotations.Param;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;

public class MapperParameterTest {
    @Test
    public void multiParameterMapperMethodsUseExplicitParamNames() {
        Class<?>[] mapperTypes = new Class<?>[]{
                AppointmentMapper.class,
                HospitalizationMapper.class,
                MedicalhistoryMapper.class
        };
        for (Class<?> mapperType : mapperTypes) {
            for (Method method : mapperType.getDeclaredMethods()) {
                if (method.getParameterTypes().length > 1) {
                    Annotation[][] annotations = method.getParameterAnnotations();
                    for (int i = 0; i < annotations.length; i++) {
                        assertTrue(mapperType.getSimpleName() + "." + method.getName() + " parameter " + i + " is missing @Param",
                                hasParamAnnotation(annotations[i]));
                    }
                }
            }
        }
    }

    private boolean hasParamAnnotation(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(Param.class)) {
                return true;
            }
        }
        return false;
    }
}
