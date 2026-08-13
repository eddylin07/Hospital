package com.hospital.resources;

import org.junit.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.Assert.assertTrue;

public class MapperSqlRegressionTest {

    @Test
    public void drugInventoryUpdateRequiresEnoughStockAtomically() {
        String xml = readResource("/mapper/DrugsMapper.xml");

        assertTrue(xml.contains("where id=#{id} and number &gt;= #{number}"));
    }

    @Test
    public void prescriptionUpdateTargetsOnlyLatestSeekForPatient() {
        String xml = readResource("/mapper/SeekMapper.xml");

        assertTrue(xml.contains("select id from seek where patientid=#{patientid} order by id desc limit 1"));
    }

    private String readResource(String path) {
        InputStream stream = MapperSqlRegressionTest.class.getResourceAsStream(path);
        Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8.name()).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
}

