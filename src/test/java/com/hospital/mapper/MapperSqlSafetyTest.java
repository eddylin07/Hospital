package com.hospital.mapper;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class MapperSqlSafetyTest {

    @Test
    public void dispensingUpdatesOnlyLatestSeekRecord() throws Exception {
        String sql = read("src/main/resources/mapper/SeekMapper.xml")
                .replaceAll("\\s+", " ")
                .toLowerCase();

        assertTrue(sql.contains("where patientid=#{patientid} order by id desc limit 1"));
    }

    @Test
    public void inventoryDecreaseHasStockGuard() throws Exception {
        String sql = read("src/main/resources/mapper/DrugsMapper.xml")
                .replaceAll("\\s+", " ")
                .toLowerCase();

        assertTrue(sql.contains("update drugs set number=number-#{number} where id=#{id} and number &gt;= #{number}"));
    }

    private String read(String path) throws Exception {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }
}
