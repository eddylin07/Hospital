package com.hospital.mapper;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertTrue;

public class PrescriptionMapperXmlTest {
    @Test
    public void seekDrugUpdateTargetsLatestSeekOnly() throws Exception {
        String xml = readMapper("mapper/SeekMapper.xml");

        assertTrue(xml.contains("select max(id) as id from seek where patientid=#{patientid}"));
    }

    @Test
    public void drugInventoryUpdateRejectsOverdraw() throws Exception {
        String xml = readMapper("mapper/DrugsMapper.xml");

        assertTrue(xml.contains("where id=#{id} and number &gt;= #{number}"));
    }

    private static String readMapper(String path) throws Exception {
        ClassPathResource resource = new ClassPathResource(path);
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }
}
