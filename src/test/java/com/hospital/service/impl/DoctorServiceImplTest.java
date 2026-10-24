package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.OptionMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Seek;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class DoctorServiceImplTest {
    @Test
    public void seekInfoRejectsMalformedDaysWithoutWritingSeek() {
        DoctorServiceImpl service = baseService();
        RecordingSeekMapper seekMapper = new RecordingSeekMapper();
        service.seekMapper = seekMapper.proxy();

        Map<String, String> map = validMap();
        map.put("days", "abc");

        Assert.assertEquals("诊断信息错误", service.seekInfo(map));
        Assert.assertEquals(0, seekMapper.insertCalls);
    }

    @Test
    public void seekInfoRejectsMalformedOptionsWithoutWritingSeek() {
        DoctorServiceImpl service = baseService();
        RecordingSeekMapper seekMapper = new RecordingSeekMapper();
        service.seekMapper = seekMapper.proxy();

        Map<String, String> map = validMap();
        map.put("option_bad", "abc");

        Assert.assertEquals("检查项信息错误", service.seekInfo(map));
        Assert.assertEquals(0, seekMapper.insertCalls);
    }

    @Test
    public void seekInfoAllowsTrailingBlankOptionToken() {
        DoctorServiceImpl service = baseService();
        RecordingSeekMapper seekMapper = new RecordingSeekMapper();
        service.seekMapper = seekMapper.proxy();

        Map<String, String> map = validMap();
        map.put("option_blank", "");

        Assert.assertEquals(CommonService.add_message_success, service.seekInfo(map));
        Assert.assertEquals(1, seekMapper.insertCalls);
        Assert.assertEquals(Integer.valueOf(8), seekMapper.insertedSeek.getPatientid());
    }

    private Map<String, String> validMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("option_1", "1");
        map.put("days", "0");
        map.put("describes", "cough");
        map.put("illname", "cold");
        map.put("patientid", "8");
        return map;
    }

    private DoctorServiceImpl baseService() {
        DoctorServiceImpl service = new DoctorServiceImpl();
        service.doctorMapper = noop(DoctorMapper.class);
        service.patientMapper = noop(PatientMapper.class);
        service.loginMapper = noop(LoginMapper.class);
        service.optionMapper = createProxy(OptionMapper.class, (proxy, method, args) -> {
            if ("getTotalPrice".equals(method.getName())) {
                return new BigDecimal("12.00");
            }
            return defaultValue(method.getReturnType());
        });
        return service;
    }

    private class RecordingSeekMapper {
        private int insertCalls;
        private Seek insertedSeek;

        private SeekMapper proxy() {
            return createProxy(SeekMapper.class, (proxy, method, args) -> {
                if ("insert".equals(method.getName())) {
                    insertCalls++;
                    insertedSeek = (Seek) args[0];
                    return 1;
                }
                return defaultValue(method.getReturnType());
            });
        }
    }

    private <T> T noop(Class<T> type) {
        return createProxy(type, (proxy, method, args) -> defaultValue(method.getReturnType()));
    }

    private Object defaultValue(Class<?> type) {
        if (type == Integer.TYPE || type == Integer.class) {
            return 0;
        }
        if (type == Boolean.TYPE || type == Boolean.class) {
            return false;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> T createProxy(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }
}
