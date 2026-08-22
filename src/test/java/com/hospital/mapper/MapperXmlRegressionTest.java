package com.hospital.mapper;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MapperXmlRegressionTest {
    @Test
    public void seekDrugUpdateTargetsLatestSeekOnly() throws Exception {
        String xml = resource("/mapper/SeekMapper.xml");

        assertTrue(xml.contains("select max(id) as id from seek where patientid=#{patientid}"));
        assertFalse(xml.contains("set drugs=#{drugs},price=price+#{price}\n     where patientid=#{patientid}"));
    }

    @Test
    public void drugStockUpdateUsesAtomicStockGuard() throws Exception {
        String xml = resource("/mapper/DrugsMapper.xml");

        assertTrue(xml.contains("where id=#{id} and number &gt;= #{number}"));
        assertTrue(xml.contains("property=\"price\""));
        assertTrue(xml.contains("property=\"number\""));
    }

    private String resource(String name) throws Exception {
        InputStream input = getClass().getResourceAsStream(name);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;
        while ((read = input.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }
        return new String(output.toByteArray(), StandardCharsets.UTF_8);
    }
}
