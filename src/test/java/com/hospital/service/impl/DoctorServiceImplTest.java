package com.hospital.service.impl;

import com.hospital.common.CommonService;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class DoctorServiceImplTest {

    @Test
    public void seekInfoRejectsEmptyOptionSelection() {
        DoctorServiceImpl service = new DoctorServiceImpl();

        assertEquals(CommonService.add_message_error, service.seekInfo(Collections.singletonMap("patientid", "7")));
    }
}
