package com.hospital.service.impl;

import com.hospital.common.CommonService;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class DoctorServiceImplTest {

    @Test
    public void seekInfoWithNoOptionsReturnsBusinessError() {
        DoctorServiceImpl service = new DoctorServiceImpl();
        HashMap<String,String> request = new HashMap<>();
        request.put("patientid", "3");
        request.put("days", "0");
        request.put("describes", "desc");
        request.put("illname", "ill");

        String message = service.seekInfo(request);

        assertEquals(CommonService.add_message_error, message);
    }
}
