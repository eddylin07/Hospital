package com.hospital.service.impl;

import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Login;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LoginServiceImplTest {
    @Test
    public void registDoesNotCreateAdminForBlankCertId() {
        LoginServiceImpl service=new LoginServiceImpl();
        final int[] insertCalls={0};
        service.loginMapper=mapper(LoginMapper.class,(proxy,method,args)->{
            if(method.getName().equals("insert")){
                insertCalls[0]++;
                return 1;
            }
            return defaultValue(method);
        });
        service.doctorMapper=mapper(DoctorMapper.class,(proxy,method,args)->defaultValue(method));
        service.patientMapper=mapper(PatientMapper.class,(proxy,method,args)->defaultValue(method));
        Login login=new Login();
        login.setUsername("attacker");
        login.setPassword("secret");
        login.setCertId(" ");

        String message=service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者",message);
        assertEquals(0,insertCalls[0]);
        assertNull(login.getRole());
    }

    private static <T> T mapper(Class<T> type,InvocationHandler handler){
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(),new Class[]{type},handler));
    }

    private static Object defaultValue(Method method){
        Class<?> type=method.getReturnType();
        if(type.equals(Integer.TYPE)||type.equals(Integer.class)){
            return 0;
        }
        if(type.equals(Boolean.TYPE)||type.equals(Boolean.class)){
            return false;
        }
        return null;
    }
}
