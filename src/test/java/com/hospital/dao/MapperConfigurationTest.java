package com.hospital.dao;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class MapperConfigurationTest {
    @Test
    public void seekDrugUpdateTargetsOnlyLatestSeekForPatient() throws Exception {
        String xml = read("src/main/resources/mapper/SeekMapper.xml")
                .replaceAll("\\s+", " ")
                .toLowerCase();

        assertTrue(xml.contains("update seek set drugs=#{drugs},price=price+#{price} where patientid=#{patientid} order by id desc limit 1"));
    }

    @Test
    public void drugStockUpdateRequiresEnoughRemainingInventory() throws Exception {
        String xml = read("src/main/resources/mapper/DrugsMapper.xml");

        assertTrue(xml.contains("number &gt;= #{number}"));
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
