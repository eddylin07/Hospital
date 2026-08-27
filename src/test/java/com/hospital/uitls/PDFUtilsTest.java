package com.hospital.uitls;

import com.hospital.entity.Option;
import com.hospital.entity.Seek;
import com.hospital.service.OptionService;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PDFUtilsTest {

    @Test
    public void createAppointmentReturnsControlledMessageForMissingAppointment() {
        assertEquals("暂无预约单", PDFUtils.createAppointMent(null, ""));
    }

    @Test
    public void createSeekInfoReturnsControlledMessageForMissingSeek() {
        assertEquals("暂无就诊单", PDFUtils.createSeekInfo(null, new FakeOptionService(), ""));
    }

    @Test
    public void createSeekInfoReturnsControlledMessageForDeletedOptionReference() {
        Seek seek = new Seek();
        seek.setOptions("999");

        assertEquals("系统内部错误，生成失败", PDFUtils.createSeekInfo(seek, new FakeOptionService(), ""));
    }

    private static class FakeOptionService implements OptionService {
        @Override
        public List<Option> getAll() {
            return Collections.emptyList();
        }

        @Override
        public Option getOption(Integer id) {
            return null;
        }

        @Override
        public String insert(Option option) {
            return "";
        }

        @Override
        public String deleteOption(Integer id) {
            return "";
        }

        @Override
        public String updateBOption(Option option) {
            return "";
        }
    }
}
