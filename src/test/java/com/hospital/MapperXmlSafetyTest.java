package com.hospital;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertTrue;

public class MapperXmlSafetyTest {
    @Test
    public void appointmentInsertUsesGeneratedKeys() throws IOException {
        String xml = resource("mapper/AppointmentMapper.xml");

        assertTrue(xml.contains("useGeneratedKeys=\"true\""));
        assertTrue(xml.contains("keyProperty=\"id\""));
    }

    @Test
    public void drugInventoryUpdateHasAtomicStockGuard() throws IOException {
        String xml = resource("mapper/DrugsMapper.xml");

        assertTrue(xml.contains("number&gt;=#{number}"));
    }

    @Test
    public void dispensingUpdatesOnlyLatestSeekRecord() throws IOException {
        String xml = resource("mapper/SeekMapper.xml");

        assertTrue(xml.contains("latest_seek"));
        assertTrue(xml.contains("order by id desc"));
        assertTrue(xml.contains("limit 1"));
    }

    private String resource(String path) throws IOException {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IOException("Missing resource: " + path);
            }
            byte[] bytes = new byte[inputStream.available()];
            int read = inputStream.read(bytes);
            return new String(bytes, 0, read, StandardCharsets.UTF_8);
        }
    }
}
