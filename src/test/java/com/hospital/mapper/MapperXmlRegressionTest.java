package com.hospital.mapper;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class MapperXmlRegressionTest {
    @Test
    public void prescriptionUpdateTargetsLatestSeekRowOnly() throws Exception {
        String xml = new String(Files.readAllBytes(Paths.get("src/main/resources/mapper/SeekMapper.xml")),
                StandardCharsets.UTF_8);

        assertTrue(xml.contains("order by id desc limit 1"));
        assertTrue(xml.contains("latest_seek"));
    }

    @Test
    public void drugStockDecrementRequiresSufficientInventory() throws Exception {
        String xml = new String(Files.readAllBytes(Paths.get("src/main/resources/mapper/DrugsMapper.xml")),
                StandardCharsets.UTF_8);

        assertTrue(xml.contains("number &gt;= #{number}"));
    }
}
