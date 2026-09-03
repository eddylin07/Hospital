package com.hospital.mapper;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MapperXmlSafetyTest {

    @Test
    public void drugInventoryUpdateUsesAtomicStockGuard() throws Exception {
        String xml = resource("mapper/DrugsMapper.xml");

        Assert.assertTrue(xml.contains("where id=#{id} and number &gt;= #{number}"));
    }

    @Test
    public void drugPrescriptionUpdatesOnlyLatestSeekRow() throws Exception {
        String xml = resource("mapper/SeekMapper.xml");

        Assert.assertTrue(xml.contains("select max(id) as id from seek where patientid=#{patientid}"));
        Assert.assertTrue(xml.contains("latest_seek"));
    }

    @Test
    public void appointmentInsertReturnsGeneratedId() throws Exception {
        String xml = resource("mapper/AppointmentMapper.xml");

        Assert.assertTrue(xml.contains("useGeneratedKeys=\"true\""));
        Assert.assertTrue(xml.contains("keyProperty=\"id\""));
    }

    @Test
    public void drugResultMapIncludesDispensingFields() throws Exception {
        String xml = resource("mapper/DrugsMapper.xml");

        Assert.assertTrue(xml.contains("property=\"price\""));
        Assert.assertTrue(xml.contains("property=\"number\""));
        Assert.assertTrue(xml.contains("property=\"text\""));
    }

    private String resource(String path) throws Exception {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            Assert.assertNotNull("Missing resource " + path, inputStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        }
    }
}
