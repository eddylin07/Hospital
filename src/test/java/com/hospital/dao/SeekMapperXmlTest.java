package com.hospital.dao;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyTargetsLatestSeekRowForPatient() throws Exception {
        String updateDrugs = extractUpdateDrugsStatement();

        assertTrue(updateDrugs.contains("where patientid=#{patientid}"));
        assertTrue(updateDrugs.contains("order by id desc"));
        assertTrue(updateDrugs.contains("limit 1"));
        assertFalse(updateDrugs.matches("(?s).*where patientid=\\#\\{patientid\\}\\s*</update>.*"));
    }

    private String extractUpdateDrugsStatement() throws Exception {
        String mapperXml = readResource("mapper/SeekMapper.xml")
                .replaceAll("\\s+", " ")
                .toLowerCase(Locale.ROOT);
        int start = mapperXml.indexOf("<update id=\"updatedrugs\"");
        assertTrue("updateDrugs mapper statement should exist", start >= 0);
        int end = mapperXml.indexOf("</update>", start);
        assertTrue("updateDrugs mapper statement should be closed", end > start);
        return mapperXml.substring(start, end + "</update>".length());
    }

    private String readResource(String path) throws Exception {
        InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path);
        assertTrue("resource should exist: " + path, inputStream != null);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        } finally {
            inputStream.close();
        }
    }
}
