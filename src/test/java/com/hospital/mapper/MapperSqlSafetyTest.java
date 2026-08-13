package com.hospital.mapper;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class MapperSqlSafetyTest {

    @Test
    public void drugStockUpdateRequiresEnoughInventory() throws Exception {
        String xml = read("src/main/resources/mapper/DrugsMapper.xml");

        assertTrue(xml.contains("where id=#{id} and number &gt;= #{number}"));
    }

    @Test
    public void drugPrimaryKeyResultMapIncludesDispensingFields() throws Exception {
        String xml = read("src/main/resources/mapper/DrugsMapper.xml");

        assertTrue(xml.contains("property=\"price\""));
        assertTrue(xml.contains("property=\"number\""));
    }

    @Test
    public void appointmentInsertReturnsGeneratedId() throws Exception {
        String xml = read("src/main/resources/mapper/AppointmentMapper.xml");

        assertTrue(xml.contains("useGeneratedKeys=\"true\""));
        assertTrue(xml.contains("keyProperty=\"id\""));
    }

    @Test
    public void prescriptionUpdateTargetsLatestSeekOnly() throws Exception {
        String xml = read("src/main/resources/mapper/SeekMapper.xml");

        assertTrue(xml.contains("select id from seek where patientid=#{patientid} order by id desc limit 1"));
    }

    private String read(String path) throws Exception {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }
}
