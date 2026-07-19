package com.hospital.service.impl;

import com.hospital.dao.DrugsMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class PatientServiceImplTest {
    @Test
    public void seekRejectsQuantityGreaterThanStockBeforeAnyWrite() {
        PatientServiceImpl service=new PatientServiceImpl();
        final int[] drugUpdates={0};
        final int[] patientUpdates={0};
        final int[] seekUpdates={0};
        service.drugsMapper=mapper(DrugsMapper.class,(proxy,method,args)->{
            if(method.getName().equals("selectByPrimaryKey")){
                return drug(1,"Aspirin",5,"2.50");
            }
            if(method.getName().equals("updateNumber")){
                drugUpdates[0]++;
                return 1;
            }
            return defaultValue(method);
        });
        service.patientMapper=mapper(PatientMapper.class,(proxy,method,args)->{
            if(method.getName().equals("updateByPrimaryKeySelective")){
                patientUpdates[0]++;
                return 1;
            }
            return defaultValue(method);
        });
        service.seekMapper=mapper(SeekMapper.class,(proxy,method,args)->{
            if(method.getName().equals("updateDrugs")){
                seekUpdates[0]++;
                return 1;
            }
            return defaultValue(method);
        });
        Patient patient=new Patient();
        patient.setId(11);
        patient.setDrugsids("1@6");

        String message=service.seek(patient);

        assertEquals("对不起Aspirin数量不足",message);
        assertEquals(0,drugUpdates[0]);
        assertEquals(0,patientUpdates[0]);
        assertEquals(0,seekUpdates[0]);
    }

    @Test
    public void seekDeductsStockAndUpdatesSeekAfterValidation() {
        PatientServiceImpl service=new PatientServiceImpl();
        final int[] drugUpdates={0};
        final int[] deductedNumber={0};
        final int[] patientUpdates={0};
        final Seek[] updatedSeek={null};
        service.drugsMapper=mapper(DrugsMapper.class,(proxy,method,args)->{
            if(method.getName().equals("selectByPrimaryKey")){
                return drug(1,"Aspirin",5,"2.50");
            }
            if(method.getName().equals("updateNumber")){
                drugUpdates[0]++;
                deductedNumber[0]=((Drugs)args[0]).getNumber();
                return 1;
            }
            return defaultValue(method);
        });
        service.patientMapper=mapper(PatientMapper.class,(proxy,method,args)->{
            if(method.getName().equals("updateByPrimaryKeySelective")){
                patientUpdates[0]++;
                return 1;
            }
            return defaultValue(method);
        });
        service.seekMapper=mapper(SeekMapper.class,(proxy,method,args)->{
            if(method.getName().equals("updateDrugs")){
                updatedSeek[0]=(Seek)args[0];
                return 1;
            }
            return defaultValue(method);
        });
        Patient patient=new Patient();
        patient.setId(11);
        patient.setDrugsids("1@2");

        String message=service.seek(patient);

        assertEquals("更新成功",message);
        assertEquals(1,drugUpdates[0]);
        assertEquals(2,deductedNumber[0]);
        assertEquals(1,patientUpdates[0]);
        assertEquals(new BigDecimal("5.00"),updatedSeek[0].getPrice());
    }

    private static Drugs drug(Integer id,String name,Integer number,String price) {
        Drugs drugs=new Drugs();
        drugs.setId(id);
        drugs.setName(name);
        drugs.setNumber(number);
        drugs.setPrice(new BigDecimal(price));
        return drugs;
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
