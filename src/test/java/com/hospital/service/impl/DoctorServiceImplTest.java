package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.OptionMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Seek;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DoctorServiceImplTest {

    @Mock
    private SeekMapper seekMapper;

    @Mock
    private OptionMapper optionMapper;

    private DoctorServiceImpl service;

    @Before
    public void setUp() {
        service = new DoctorServiceImpl();
        service.seekMapper = seekMapper;
        service.optionMapper = optionMapper;
    }

    @Test
    public void seekInfoBuildsSeekRecordFromSubmittedOptionsAndPatientDetails() {
        Map<String, String> request = new LinkedHashMap<>();
        request.put("option_1", "3");
        request.put("option_2", "5");
        request.put("days", "4");
        request.put("describes", "headache and fever");
        request.put("illname", "flu");
        request.put("patientid", "42");

        when(optionMapper.getTotalPrice(Arrays.asList(3, 5))).thenReturn(new BigDecimal("56.70"));
        when(seekMapper.insert(org.mockito.ArgumentMatchers.any(Seek.class))).thenReturn(1);

        String message = service.seekInfo(request);

        assertEquals(CommonService.add_message_success, message);
        verify(optionMapper).getTotalPrice(Arrays.asList(3, 5));

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).insert(seekCaptor.capture());
        Seek savedSeek = seekCaptor.getValue();
        assertEquals("3,5", savedSeek.getOptions());
        assertEquals(4, savedSeek.getDays());
        assertEquals("headache and fever", savedSeek.getDescribes());
        assertEquals("flu", savedSeek.getIllname());
        assertEquals(Integer.valueOf(42), savedSeek.getPatientid());
        assertEquals(0, new BigDecimal("56.70").compareTo(savedSeek.getPrice()));
    }
}
