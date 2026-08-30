package com.hospital.mapper;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class MapperXmlTest {

    @Test
    public void drugInventoryDeductionRequiresAvailableStock() throws Exception {
        String xml = read("src/main/resources/mapper/DrugsMapper.xml");

        assertTrue(xml.contains("where id=#{id} and number &gt;= #{number}"));
    }

    @Test
    public void prescriptionOnlyUpdatesLatestSeekForPatient() throws Exception {
        String xml = read("src/main/resources/mapper/SeekMapper.xml");

        assertTrue(xml.contains("where patientid=#{patientid}"));
        assertTrue(xml.contains("order by id desc"));
        assertTrue(xml.contains("limit 1"));
    }

    @Test
    public void appointmentInsertReturnsGeneratedId() throws Exception {
        String xml = read("src/main/resources/mapper/AppointmentMapper.xml");

        assertTrue(xml.contains("useGeneratedKeys=\"true\""));
        assertTrue(xml.contains("keyProperty=\"id\""));
    }

    private String read(String path) throws Exception {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }
}
