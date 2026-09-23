package com.hospital.service.impl;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class MapperSafetyTest {
    @Test
    public void drugStockUpdateIsGuardedAgainstOverdraw() throws Exception {
        String mapper = new String(Files.readAllBytes(Paths.get("src/main/resources/mapper/DrugsMapper.xml")), StandardCharsets.UTF_8);

        assertTrue(mapper.contains("where id=#{id} and number>=#{number}"));
    }

    @Test
    public void prescriptionUpdateTargetsOnlyLatestSeekRow() throws Exception {
        String mapper = new String(Files.readAllBytes(Paths.get("src/main/resources/mapper/SeekMapper.xml")), StandardCharsets.UTF_8);

        assertTrue(mapper.contains("order by id desc limit 1"));
        assertTrue(mapper.contains("latest_seek"));
    }
}
