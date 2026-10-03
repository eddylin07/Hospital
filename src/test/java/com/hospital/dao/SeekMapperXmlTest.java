package com.hospital.dao;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {
    @Test
    public void updateDrugsOnlyTouchesLatestSeekAndHandlesNullPrice() throws Exception {
        Configuration configuration = new Configuration();
        try (Reader reader = Resources.getResourceAsReader("mapper/SeekMapper.xml")) {
            new XMLMapperBuilder(reader, configuration, "mapper/SeekMapper.xml", configuration.getSqlFragments()).parse();
        }

        String mapperXml = readResource("mapper/SeekMapper.xml");

        assertTrue(mapperXml.contains("price=ifnull(price,0)+#{price}"));
        assertTrue(mapperXml.contains("where patientid=#{patientid}"));
        assertTrue(mapperXml.contains("order by id desc limit 1"));
    }

    private String readResource(String resource) throws Exception {
        try (Scanner scanner = new Scanner(Resources.getResourceAsStream(resource), StandardCharsets.UTF_8.name())) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next().replaceAll("\\s+", " ") : "";
        }
    }
}
