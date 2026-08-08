package com.hospital.mapper;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertTrue;

public class CriticalMapperSqlTest {

    @Test
    public void drugStockUpdateUsesAtomicLowerBoundGuard() throws Exception {
        String sql = resource("mapper/DrugsMapper.xml");

        assertTrue(sql.contains("update drugs set number=number-#{number} where id=#{id} and number&gt;=#{number}"));
    }

    @Test
    public void prescriptionUpdateTargetsLatestSeekOnly() throws Exception {
        String sql = resource("mapper/SeekMapper.xml");

        assertTrue(sql.contains("where id = ("));
        assertTrue(sql.contains("select latest.id from ("));
        assertTrue(sql.contains("select id from seek where patientid=#{patientid} order by id desc limit 1"));
    }

    private static String resource(String path) throws Exception {
        ClassPathResource resource = new ClassPathResource(path);
        try (InputStream inputStream = resource.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            return outputStream.toString(StandardCharsets.UTF_8.name());
        }
    }
}
