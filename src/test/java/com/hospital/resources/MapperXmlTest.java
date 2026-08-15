package com.hospital.resources;

import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MapperXmlTest {
    @Test
    public void updateDrugsOnlyTargetsLatestSeekRow() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("mapper/SeekMapper.xml");
        Assert.assertNotNull(inputStream);
        String xml = new String(readAll(inputStream), StandardCharsets.UTF_8).replaceAll("\\s+", " ").toLowerCase();

        Assert.assertTrue(xml.contains("update seek set drugs=#{drugs},price=price+#{price} where patientid=#{patientid} order by id desc limit 1"));
    }

    private byte[] readAll(InputStream inputStream) throws Exception {
        byte[] buffer = new byte[4096];
        int read;
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        return outputStream.toByteArray();
    }
}
