package com.hospital.mapper;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class MapperContractTest {

    @Test
    public void drugInventoryDeductionRequiresEnoughStock() throws Exception {
        String xml = read("src/main/resources/mapper/DrugsMapper.xml");

        assertTrue(xml.contains("number=number-#{number}"));
        assertTrue(xml.contains("number &gt;= #{number}"));
    }

    @Test
    public void drugDispensingUpdatesLatestSeekOnly() throws Exception {
        String xml = read("src/main/resources/mapper/SeekMapper.xml");

        assertTrue(xml.contains("select max(id) as id from seek where patientid=#{patientid}"));
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
