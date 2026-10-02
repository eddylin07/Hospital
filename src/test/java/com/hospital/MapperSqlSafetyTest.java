package com.hospital;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class MapperSqlSafetyTest {
    @Test
    public void drugsMapperUsesAtomicStockGuard() throws Exception {
        String xml = read("src/main/resources/mapper/DrugsMapper.xml");

        assertTrue(xml.contains("updateNumberIfEnough"));
        assertTrue(xml.contains("number &gt;= #{number}"));
    }

    @Test
    public void seekMapperUpdatesOnlyLatestPatientSeek() throws Exception {
        String xml = read("src/main/resources/mapper/SeekMapper.xml");

        assertTrue(xml.contains("order by id desc limit 1"));
    }

    @Test
    public void insertsPopulateGeneratedIdsUsedByControllers() throws Exception {
        assertTrue(read("src/main/resources/mapper/AppointmentMapper.xml")
                .contains("useGeneratedKeys=\"true\" keyProperty=\"id\""));
        assertTrue(read("src/main/resources/mapper/LoginMapper.xml")
                .contains("useGeneratedKeys=\"true\" keyProperty=\"id\""));
    }

    private static String read(String path) throws Exception {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }
}
